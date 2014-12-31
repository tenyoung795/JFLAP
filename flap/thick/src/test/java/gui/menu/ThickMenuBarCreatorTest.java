package gui.menu;

import automata.Automaton;
import gui.action.ExportAction;
import gui.action.NewAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFrame;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.junit.v4_5.runner.GUITestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(GUITestRunner.class)
public class ThickMenuBarCreatorTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;

    @Override
    protected void onSetUp() {
        window = new FrameFixture(robot(), GuiActionRunner.execute(new SetUp()));
    }

    @Test
    @GUITest
    public void testMenuBar_automatonEnvironment_canExportToSVG() {
        assertThat(window.menuItemWithPath("File", "Save Image As...", "Export to SVG").target().getAction(),
            instanceOf(ExportAction.class));
    }

    private static final class SetUp extends GuiQuery<EnvironmentFrame> {

        @Override
        protected EnvironmentFrame executeInEDT() {
            NewAction.showNew();

            EnvironmentFrame frame = new EnvironmentFrame(new AutomatonEnvironment(Mockito.mock(Automaton.class)));
            frame.setSize(600, 400);
            frame.setVisible(true);
            return frame;
        }
    }
}
