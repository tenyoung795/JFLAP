package gui.lsystem;

import grammar.Grammar;
import grammar.Production;
import grammar.UnrestrictedGrammar;
import grammar.lsystem.LSystem;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.exception.EdtViolationException;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.junit.v4_5.runner.GUITestRunner;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.timing.Timeout;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertThat;

@RunWith(GUITestRunner.class)
public class DisplayPaneTest extends AssertJSwingJUnitTestCase {

	@Rule
	public final ErrorCollector errorCollector = new ErrorCollector();

	private Optional<Thread.UncaughtExceptionHandler> handlerOptional;
	private FrameFixture window;
	private DisplayPane displayPane;
	private JSpinnerFixture derivation;
	private JProgressBarFixture progressBar;
	private JLabelFixture imageDisplay;

	@Override
	protected void onSetUp() {
		handlerOptional = Optional.ofNullable(Thread.getDefaultUncaughtExceptionHandler());
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			if (e instanceof EdtViolationException) {
				errorCollector.addError(e);
			}
			handlerOptional.ifPresent(handler -> handler.uncaughtException(t, e));
		});

		Grammar grammar = new UnrestrictedGrammar();
		grammar.addProductions(new Production[]{
			new Production("B", "[ ~ ## T L - B + + B ]"),
			new Production("L", "[ { - g + + g % - - g } ]"),
			new Production("R", "! @@ R"),
			new Production("T", "T g"),
		});
		LSystem treeExample = new LSystem("R ~ ## B", grammar, Collections.emptyMap());

		window = new FrameFixture(robot(), GuiCallable.execute(() -> {
			displayPane = new DisplayPane(treeExample);
			JFrame frame = new JFrame();
			frame.add(displayPane);
			frame.setContentPane(displayPane);
			return frame;
		})).show().maximize();

		derivation = window.spinner("derivation");
		progressBar = window.progressBar();
		imageDisplay = window.label("image");
	}

	@Test
	@GUITest
	public void treeExample_abortedDerivation13DoesNotReturn() {
		long derivation13EstimatedTimeNs = timeInNs(() -> {
			derivation.select(13);
			waitUntilDerivationFinishes();
		});
		// Prepare to switch back to derivation 13, then immediately back to 0.
		derivation.select(0);
		waitUntilDerivationFinishes();

		derivation.select(13).select(0);
		waitUntilDerivationFinishes();

		// Assumptions:
		// * Different runs of the same derivation last approximately the same.
		// * If the bug exists, then theoretically, the cancelled derivation 13 may run
		//   after derivation 0 finishes. If, after derivation 0 finishes,
		//   the image never changes within the time derivation 13 would take,
		//   then assume the bug does not exist.
		try {
			Pause.pause(new Condition("Has changed image") {
				private final Image initialImage = getPanelImage();

				@Override
				public boolean test() {
					return getPanelImage() != initialImage;
				}
			}, Timeout.timeout(derivation13EstimatedTimeNs, TimeUnit.NANOSECONDS));
		} catch (WaitTimedOutError expected) {
			// I would use ExpectedException but GUITestRunner mistakes the expected error
			// as an actual error.
		}
	}

	@Test
	@GUITest
	public void treeExample_abortedDerivation13DoesNotCorrupt() {
		derivation.select(12);
		waitUntilDerivationFinishes();
		BufferedImage expectedImage = getPanelImage();

		derivation.increment().decrement();
		waitUntilDerivationFinishes();

		assertThat(getPanelImage(), sameDataAs(expectedImage));
	}

	@Test
	@GUITest
	public void treeExample_printingWhileRenderingInPanelDoesNotCorrupt() {
		derivation.select(13);
		waitUntilDerivationFinishes();
		BufferedImage expectedPanelImage = getPanelImage();
		Callable<BufferedImage> getPrintedImage = () -> {
			BufferedImage image = new BufferedImage(
				expectedPanelImage.getWidth(), expectedPanelImage.getHeight(), expectedPanelImage.getType());
			Graphics graphics = image.createGraphics();
			try {
				displayPane.print(graphics);
				return image;
			} finally {
				graphics.dispose();
			}
		};
		BufferedImage expectedPrintedImage = GuiCallable.execute(getPrintedImage);
		derivation.select(0);
		waitUntilDerivationFinishes();

		derivation.select(13);
		assertThat(GuiCallable.execute(getPrintedImage), sameDataAs(expectedPrintedImage));
		waitUntilDerivationFinishes();
		assertThat(getPanelImage(), sameDataAs(expectedPanelImage));
	}

	@Override
	protected void onTearDown() {
		window.cleanUp();
		Thread.setDefaultUncaughtExceptionHandler(handlerOptional.orElse(null));
	}

	private void waitUntilDerivationFinishes() {
		progressBar.waitUntilValueIs(GuiCallable.execute(progressBar.target()::getMaximum));
	}

	private BufferedImage getPanelImage() {
		return GuiCallable.execute(() -> (BufferedImage) ((ImageIcon) imageDisplay.target().getIcon()).getImage());
	}

	private static long timeInNs(Runnable runnable) {
		long start = System.nanoTime();
		runnable.run();
		return System.nanoTime() - start;
	}

	private static Matcher<BufferedImage> sameDataAs(BufferedImage image) {
		return new SameDataAs(image);
	}

	private static final class GuiCallable<P> extends GuiQuery<P> {

		private final Callable<P> callable;

		public GuiCallable(Callable<P> callable) {
			this.callable = callable;
		}

		@Override
		protected P executeInEDT() throws Exception {
			return callable.call();
		}

		public static <P> P execute(Callable<P> callable) {
			return GuiActionRunner.execute(new GuiCallable<>(callable));
		}
	}

	private static final class SameDataAs extends ArgumentMatcher<BufferedImage> {

		private final BufferedImage wanted;

		public SameDataAs(BufferedImage wanted) {
			this.wanted = wanted;
		}

		@Override
		public boolean matches(Object item) {
			if (item == wanted) return true;
			if (!(item instanceof BufferedImage)) return false;
			BufferedImage image = (BufferedImage) item;

			int minX = wanted.getMinX();
			int minY = wanted.getMinY();
			int width = wanted.getWidth();
			int height = wanted.getHeight();

			if (!(image.getMinX() == minX
				&& image.getMinY() == minY
				&& image.getWidth() == width
				&& image.getHeight() == height)) return false;

			for (int x = minX; x < minX + width; ++x) {
				for (int y = minY; y < minY + height; ++y) {
					if (image.getRGB(x, y) != wanted.getRGB(x, y)) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public void describeTo(Description description) {
			super.describeTo(description);
			description.appendText(" ").appendValue(wanted);
		}
	}

}
