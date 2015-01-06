import gui.action.NewAction;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.junit.Test;

public class JFLAPTest extends AssertJSwingJUnitTestCase {

	@Override
	protected void onSetUp() {
	}

	@Test
	@GUITest
	public void main_swingStaysInEDT() {
		ApplicationLauncher.application(JFLAP.class).start();
	}

	@Override
	protected void onTearDown() {
		GuiActionRunner.execute(new TearDown());
	}

	private static final class TearDown extends GuiTask {

		@Override
		protected void executeInEDT() {
			NewAction.closeNew();
		}
	}
}
