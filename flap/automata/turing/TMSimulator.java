/* -- JFLAP 4.0 --
 *
 * Copyright information:
 *
 * Susan H. Rodger, Thomas Finley
 * Computer Science Department
 * Duke University
 * April 24, 2003
 * Supported by National Science Foundation DUE-9752583.
 *
 * Copyright (c) 2003
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the author.  The name of the author may not be used to
 * endorse or promote products derived from this software without
 * specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package automata.turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.State;
import automata.Transition;

/**
 * The TM simulator progresses TM configurations on a possibly multitape Turing
 * machine.
 * 
 * @author Thomas Finley
 */

public class TMSimulator extends AutomatonSimulator {
	/**
	 * Creates a TM simulator for the given automaton.
	 * 
	 * @param automaton
	 *            the machine to simulate
	 * @throws IllegalArgumentException
	 *             if this automaton is not a Turing machine
	 */
	public TMSimulator(Automaton automaton) {
		super(automaton);
		if (!(automaton instanceof TuringMachine))
			throw new IllegalArgumentException(
					"Automaton is not a Turing machine, but a "
							+ automaton.getClass());
	}

	/**
	 * Returns a TMConfiguration object that represents the initial
	 * configuration of the TM, before any input has been processed. This
	 * returns an array of length one. This method exists only to provide
	 * compatibility with the general definition of <CODE>AutomatonSimulator</CODE>.
	 * One should use the version of this function that accepts an array of
	 * inputs instead.
	 * 
	 * @param input
	 *            the input string
	 */
	public Configuration[] getInitialConfigurations(String input) {
		int tapes = ((TuringMachine) myAutomaton).tapes();
		String[] inputs = new String[tapes];
		for (int i = 0; i < tapes; i++)
			inputs[i] = input;
		return getInitialConfigurations(inputs);
	}

	/**
	 * Returns a TMConfiguration object that represents the initial
	 * configuration of the TM, before any input has been processed. This
	 * returns an array of length one.
	 * 
	 * @param inputs
	 *            the input strings
	 */
	public Configuration[] getInitialConfigurations(String[] inputs) {
		inputStrings = (String[]) inputs.clone();
		Tape[] tapes = new Tape[inputs.length];
		for (int i = 0; i < tapes.length; i++)
			tapes[i] = new Tape(inputs[i]);
		Configuration[] configs = new Configuration[1];
		State initialState = myAutomaton.getInitialState();
		Automaton top = (Automaton) myAutomaton.getBlockMap().get(
				initialState.getInternalName());
		Stack initialAutos = new Stack();
		initialAutos.push(myAutomaton);
		Stack initialBlocks = new Stack();
		while (top != null) {
			initialBlocks.push(initialState);
			initialAutos.push(top);
			initialState = top.getInitialState();
			top = (Automaton) top.getBlockMap().get(
					initialState.getInternalName());
		}
		configs[0] = new TMConfiguration(initialState, null, tapes);

		configs[0].setAutoStack(initialAutos);
		configs[0].setBlockStack(initialBlocks);

		return configs;
	}
    
    private boolean isFinalStateInAutomaton(Automaton auto, State state){
        State[] finals = auto.getFinalStates();
        for(int m = 0; m < finals.length; m++){
          if(finals[m]==state){
              return true;
          }
        }
        return false;
    }

	/**
	 * Simulates one step for a particular configuration, adding all possible
	 * configurations reachable in one step to set of possible configurations.
	 * 
	 * @param config
	 *            the configuration to simulate the one step on
	 */
	public ArrayList stepConfiguration(Configuration config) {
		
		Stack parentAutos = config.getAutoStack();
		Stack parentBlocks = config.getBlockStack();
		ArrayList list = new ArrayList();
		TMConfiguration configuration = (TMConfiguration) config;
		/* get all information from configuration. */

		State currentState = configuration.getCurrentState();
		Transition[] transitions = new Transition[50];
		
		if (currentState.getFinalStateInBlock() == false) {
            //System.out.println("Not final state in block");
			transitions = ((Automaton) parentAutos.peek())
					.getTransitionsFromState(currentState);
		} else {
            parentBlocks.push(currentState);
              //System.out.println("final state in block");
			while(isFinalStateInAutomaton((Automaton)parentAutos.peek(), (State)parentBlocks.peek())){
                if (parentAutos.size() > 1) parentAutos.pop();
                if (parentBlocks.size() > 1) parentBlocks.pop();
    			transitions = ((Automaton) parentAutos.peek())
    					.getTransitionsFromState((State) parentBlocks.peek());
    			
                currentState = (State)parentBlocks.peek();
            }
            if (parentBlocks.size() >= 1) parentBlocks.pop();
		}
		for (int k = 0; k < transitions.length; k++) {
			Stack parentAutosClone = (Stack) parentAutos.clone();
			Stack parentBlocksClone = (Stack) parentBlocks.clone();
			TMTransition t = (TMTransition) transitions[k];
			Tape[] tapes = configuration.getTapes();
			boolean okay = true;
			for (int i = 0; okay && i < tapes.length; i++) {
				String charAtHead = tapes[i].read();
				String toRead = t.getRead(i);
				int index = toRead.indexOf("}");
				if (!charAtHead.equals(toRead) && !toRead.equals("~")) {
					if (toRead.startsWith("!")) {
						if (toRead.indexOf(charAtHead) != -1) {
							okay = false;
						}
					} else if (index != -1) {
						String postBracket = toRead.substring(index);
						while (postBracket.startsWith(" ")
								|| postBracket.startsWith("}")) {
							postBracket = postBracket.substring(1);
						}
						while (postBracket.endsWith(" ")) {
							postBracket = postBracket.substring(0, postBracket
									.length() - 1);
						}
						String preBracket = toRead.substring(0, index);
						if (preBracket.indexOf(charAtHead) == -1) okay = false;
						else{
                            varToChar.put(postBracket, charAtHead);
                        }
					} else if(varToChar.get(toRead) != null){
                        if(!varToChar.get(toRead).equals(charAtHead)) okay = false;
                    }
                    else
						okay = false;
				}
			}
			if (!okay)
				continue; // One of the toReads wasn't satisfied.
			State toState = t.getToState();
			if (toState.getInternalName() != null) {
				parentBlocksClone.push(toState);
				Automaton autoParent = (Automaton) ((Automaton) parentAutosClone
						.peek()).getBlockMap().get(toState.getInternalName());
				parentAutosClone.push(autoParent);
				toState = autoParent.getInitialState();
			} else if (toState.getParentBlock() != null) {
				toState.setParentBlock((State) parentBlocksClone.peek());
			}
			Tape[] tapes2 = new Tape[tapes.length];
			for (int i = 0; i < tapes.length; i++) {
				tapes2[i] = new Tape(tapes[i]);
				String toWrite = t.getWrite(i);
				String var = (String) varToChar.get(toWrite);
				if (var != null) {
					toWrite = var;
				}
				if (!toWrite.equals("~"))
					tapes2[i].write(toWrite);
				String direction = t.getDirection(i);
				tapes2[i].moveHead(direction);

			}
			TMConfiguration configurationToAdd = new TMConfiguration(toState,
					configuration, tapes2);
			configurationToAdd.setAutoStack(parentAutosClone);
			configurationToAdd.setBlockStack(parentBlocksClone);
			list.add(configurationToAdd);
		}
		return list;
	}

	/**
	 * Returns true if the simulation of the input string on the automaton left
	 * the machine in a final state.
	 * 
	 * @return true if the simulation of the input string on the automaton left
	 *         the machine in a final state
	 */
	public boolean isAccepted() {
		Iterator it = myConfigurations.iterator();
		while (it.hasNext()) {
			TMConfiguration configuration = (TMConfiguration) it.next();
			State currentState = configuration.getCurrentState();
			/**
			 * check if in final state. contents of tape are irrelevant.
			 */
			if (myAutomaton.isFinalState(currentState)) {
				return true;
			}

			//Stack blocks = configuration.getBlockStack();
			//State parent = null;
            Stack parentAutosClone = (Stack) configuration.getAutoStack().clone();
            Stack parentBlocksClone = (Stack) configuration.getBlockStack().clone();
            while(parentBlocksClone.size()>0){
                State up = (State)parentBlocksClone.pop();
                //System.out.println("CheckFinal " +up);
                if(!up.getFinalStateInBlock()) return false;
            }
//			if (blocks.capacity() > 0) {
//				parent = (State) blocks.get(0);
//			} 
//			if (((Automaton) configuration.getAutoStack().peek())
//					.isFinalState(currentState)
//					&& myAutomaton.isFinalState(parent)) {
//				return true;
//			}
		}
		return false;
	}

	/**
	 * Runs the automaton on the input string.
	 * 
	 * @param input
	 *            the input string to be run on the automaton
	 * @return true if the automaton accepts the input
	 */
	public boolean simulateInput(String input) {
		/** clear the configurations to begin new simulation. */
        //System.out.println("In Simulate Input");
		myConfigurations.clear();
		Configuration[] initialConfigs = getInitialConfigurations(input);
		for (int k = 0; k < initialConfigs.length; k++) {
			TMConfiguration initialConfiguration = (TMConfiguration) initialConfigs[k];
			myConfigurations.add(initialConfiguration);
		}
		while (!myConfigurations.isEmpty()) {
			//System.out.println("HERE!!!!!");
			if (isAccepted())
				return true;
			ArrayList configurationsToAdd = new ArrayList();
			Iterator it = myConfigurations.iterator();
			while (it.hasNext()) {
				TMConfiguration configuration = (TMConfiguration) it.next();
				ArrayList configsToAdd = stepConfiguration(configuration);
				configurationsToAdd.addAll(configsToAdd);
				it.remove();
			}
			myConfigurations.addAll(configurationsToAdd);
		}
		return false;
	}

	public String[] getInputStrings() {
		return inputStrings;
	}

	private String inputStrings[];

	private Map varToChar = new HashMap();
}
