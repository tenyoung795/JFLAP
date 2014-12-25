package gui.menu;

import automata.Automaton;
import com.athaydes.automaton.Swinger;
import gui.action.ExportAction;
import gui.action.NewAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ThickMenuBarCreatorTest {

    private EnvironmentFrame frame;

    @Before
    public void setUp() {
        NewAction.showNew();

        frame = new EnvironmentFrame(new AutomatonEnvironment(Mockito.mock(Automaton.class)));
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    @Test
    public void testMenuBar_automatonEnvironment_canExportToSVG() {
        JMenuItem menuItem = (JMenuItem) Swinger.getUserWith(frame)
            .clickOn("text:File")
            .moveTo("text:Save Image As...")
            .getAt("text:Export to SVG");

        assertThat(menuItem.getAction(), instanceOf(ExportAction.class));
    }

    @After
    public void tearDown() {
        frame.close();
        NewAction.closeNew();
    }
}
