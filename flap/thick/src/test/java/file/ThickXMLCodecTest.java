package file;

import automata.fsa.FiniteStateAutomaton;
import automata.mealy.MealyMachine;
import automata.mealy.MooreMachine;
import automata.pda.PushdownAutomaton;
import automata.turing.TuringMachine;
import grammar.Grammar;
import grammar.lsystem.LSystem;
import gui.pumping.CFPumpingLemmaChooser;
import gui.pumping.RegPumpingLemmaChooser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import regular.RegularExpression;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class ThickXMLCodecTest {

    @Parameterized.Parameters(name = "{0}")
    public static List<Class<? extends Serializable>> parameters() {
        return Arrays.asList(
            FiniteStateAutomaton.class,
            PushdownAutomaton.class,
            TuringMachine.class,
            Grammar.class,
            RegularExpression.class,
            LSystem.class,
            MealyMachine.class,
            MooreMachine.class,
            RegPumpingLemmaChooser.class,
            CFPumpingLemmaChooser.class
        );
    }

    @Parameterized.Parameter
    public Class<? extends Serializable> clazz;

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testXMLCodec_encode_doesNotThrow() throws IOException, IllegalAccessException, InstantiationException {
        new XMLCodec().encode(Modifier.isAbstract(clazz.getModifiers())
                ? Mockito.mock(clazz, Mockito.RETURNS_SMART_NULLS) : clazz.newInstance(),
            temporaryFolder.newFile(),
            Collections.emptyMap());
    }

}
