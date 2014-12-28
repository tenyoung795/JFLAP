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
import java.util.Collections;

@RunWith(Parameterized.class)
public class ThickXMLCodecTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Serializable[] parameters() {
        return new Serializable[] {
            new FiniteStateAutomaton(),
            new PushdownAutomaton(),
            new TuringMachine(),
            Mockito.mock(Grammar.class, Mockito.RETURNS_SMART_NULLS),
            new RegularExpression(),
            new LSystem(),
            new MealyMachine(),
            new MooreMachine(),
            new RegPumpingLemmaChooser(),
            new CFPumpingLemmaChooser()
        };
    }

    @Parameterized.Parameter
    public Serializable structure;

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testXMLCodec_encode_doesNotThrow() throws IOException {
        new XMLCodec().encode(structure, temporaryFolder.newFile(), Collections.emptyMap());
    }

}
