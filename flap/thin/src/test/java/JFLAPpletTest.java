import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.AppletLauncher;
import org.junit.Test;

public class JFLAPpletTest extends AssertJSwingJUnitTestCase {

    @Override
    protected void onSetUp() {
    }

    @Test
    @GUITest
    public void start_swingStaysInEDT() {
        AppletLauncher.applet(JFLAPplet.class).start().unloadApplet();
    }
}
