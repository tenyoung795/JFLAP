package file.xml;

import automata.turing.TMState;
import automata.turing.TuringMachine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.w3c.dom.Document;

import java.awt.*;
import java.util.*;

import static org.hamcrest.Matchers.*;

public class TMTransducerTest {

	@Rule
	public final ErrorCollector errorCollector = new ErrorCollector();

	@Test
	public void testFromDOM_ambiguousThenParentOfAmbiguous_correctlyScopesBlocks() {
		TuringMachine turingMachine = new TuringMachine();
		TMState emptyAmbiguous = turingMachine.createTMState(new Point());
		emptyAmbiguous.setInternalName("ambiguous");
		TMState parent = turingMachine.createTMState(new Point());
		parent.setInternalName("parent");
		{
			TuringMachine parentTM = parent.getInnerTM();
			TMState nonEmptyAmbiguous = parentTM.createTMState(new Point());
			nonEmptyAmbiguous.setInternalName("ambiguous");
			{
				TuringMachine nonEmptyAmbiguousTM = nonEmptyAmbiguous.getInnerTM();
				TMState child = nonEmptyAmbiguousTM.createTMState(new Point());
				child.setInternalName("child");
			}
		}
		Transducer transducer = TransducerFactory.getTransducer(turingMachine);
		Document document = transducer.toDOM(turingMachine);

		TuringMachine actualTuringMachine = (TuringMachine) transducer.fromDOM(document);

		errorCollector.checkThat(actualTuringMachine
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap(), equalTo(Collections.EMPTY_MAP));
		errorCollector.checkThat(actualTuringMachine
			.getBlockMap()
			.get("parent")
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap(), hasKey(notNullValue()));
	}

	@Test
	public void testFromDOM_parentOfAmbiguousThenAmbiguous_correctlyScopesBlocks() {
		TuringMachine turingMachine = new TuringMachine();
		TMState parent = turingMachine.createTMState(new Point());
		parent.setInternalName("parent");
		{
			TuringMachine parentTM = parent.getInnerTM();
			TMState nonEmptyAmbiguous = parentTM.createTMState(new Point());
			nonEmptyAmbiguous.setInternalName("ambiguous");
			{
				TuringMachine nonEmptyAmbiguousTM = nonEmptyAmbiguous.getInnerTM();
				TMState child = nonEmptyAmbiguousTM.createTMState(new Point());
				child.setInternalName("child");
			}
		}
		TMState emptyAmbiguous = turingMachine.createTMState(new Point());
		emptyAmbiguous.setInternalName("ambiguous");
		Transducer transducer = TransducerFactory.getTransducer(turingMachine);
		Document document = transducer.toDOM(turingMachine);

		TuringMachine actualTuringMachine = (TuringMachine) transducer.fromDOM(document);

		errorCollector.checkThat(actualTuringMachine
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap(), equalTo(Collections.EMPTY_MAP));
		errorCollector.checkThat(actualTuringMachine
			.getBlockMap()
			.get("parent")
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap(), hasKey(notNullValue()));
	}

	@Test
	public void testFromDOM_ambiguousInsideAmbiguous_terminates() {
		TuringMachine turingMachine = new TuringMachine();
		TMState parent = turingMachine.createTMState(new Point());
		parent.setInternalName("ambiguous");
		{
			TuringMachine parentTM = parent.getInnerTM();
			TMState child = parentTM.createTMState(new Point());
			child.setInternalName("ambiguous");
		}
		Transducer transducer = TransducerFactory.getTransducer(turingMachine);
		Document document = transducer.toDOM(turingMachine);

		TuringMachine actualTuringMachine = errorCollector.checkSucceeds(() ->
			(TuringMachine) transducer.fromDOM(document));
		errorCollector.checkThat(actualTuringMachine
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap()
			.get("ambiguous")
			.getBlockMap(), equalTo(Collections.EMPTY_MAP));
	}

}
