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

package automata;

import gui.action.OpenAction;
import gui.environment.EnvironmentFrame;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import automata.event.AutomataStateEvent;
import automata.event.AutomataStateListener;
import automata.event.AutomataTransitionEvent;
import automata.event.AutomataTransitionListener;
import automata.mealy.MooreMachine;
import automata.turing.TuringMachine;

/**
 * The automata object is the root class for the representation of all forms of
 * automata, including FSA, PDA, and Turing machines. This object does NOT
 * simulate the behavior of any of those machines; it simply maintains a
 * structure that holds and maintains the data necessary to represent such a
 * machine.
 * 
 * @see automata.State
 * @see automata.Transition
 * 
 * @author Thomas Finley
 */

public class Automaton implements Serializable, Cloneable {
	/**
	 * Creates an instance of <CODE>Automaton</CODE>. The created instance
	 * has no states and no transitions.
	 */
	public Automaton() {
		states = new HashSet();
		transitions = new HashSet();
		finalStates = new HashSet();
		initialState = null;
	}

	/**
	 * Creates a clone of this automaton.
	 * 
	 * @return a clone of this automaton, or <CODE>null</CODE> if the clone
	 *         failed
	 */
	public Object clone() {
		Automaton a;
		// Try to create a new object.
		try {
			// I am a bad person for writing this hack.
			if (this instanceof TuringMachine)
				a = new TuringMachine(((TuringMachine) this).tapes());
			else
				a = (Automaton) getClass().newInstance();
		} catch (Throwable e) {
			// Well golly, we're sure screwed now!
			System.err.println("Warning: clone of automaton failed!");
			return null;
		}
		a.setEnvironmentFrame(this.getEnvironmentFrame());
		
		
		// Copy over the states.
		HashMap map = new HashMap(); // Old states to new states.
		Iterator it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			State nstate = new State(state.getID(),
					new Point(state.getPoint()), a);
			copyRelevantDataForBlocks(nstate, state, a);
			nstate.setLabel(state.getLabel());
			nstate.setName(state.getName());
			map.put(state, nstate);
			a.addState(nstate);
            /*
             * If it is a Moore machine, set the state output.
             */
            if(this instanceof MooreMachine)
            {
                MooreMachine m = (MooreMachine) a;
                m.setOutput(nstate, ((MooreMachine)this).getOutput(state));
            }
		}
		// Set special states.
		it = finalStates.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			a.addFinalState((State) map.get(state));
		}
		a.setInitialState((State) map.get(getInitialState()));

		// Copy over the transitions.
		it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			Transition[] ts = getTransitionsFromState(state);
			State from = (State) map.get(state);
			for (int i = 0; i < ts.length; i++) {
				State to = (State) map.get(ts[i].getToState());
				a.addTransition(ts[i].copy(from, to));
			}
		}
		for(int k = 0; k < this.getNotes().size(); k++){
			Note curNote = (Note)this.getNotes().get(k);		
			a.addNote(new Note(curNote.getAutoPoint(), curNote.getText()));
		}

		// Should be done now!
		return a;
	}

	//A messy way of copying the necessary data over when making a new block from another one.
	private void copyRelevantDataForBlocks(State state, State old,
			Automaton newAuto) {
		state.setParentBlock(old.getParentBlock()); //set the parent block
		String name = old.getInternalName();        //get the name
		if (name == null)
			return;
		state.setInternalName(name);				//if the block has a name, copy it
		putBlockContentsInAutomaton(state, (Automaton) this.getBlockMap().get(
				name), name, newAuto);
	}

	/**
	 * Retrieves all transitions that eminate from a state.
	 * 
	 * @param from
	 *            the <CODE>State</CODE> from which returned transitions
	 *            should come from
	 * @return an array of the <CODE>Transition</CODE> objects emanating from
	 *         this state
	 */
	public Transition[] getTransitionsFromState(State from) {
		Transition[] toReturn = (Transition[]) transitionArrayFromStateMap
				.get(from);
		if (toReturn == null) {
			List list = (List) transitionFromStateMap.get(from);
			toReturn = (Transition[]) list.toArray(new Transition[0]);
			transitionArrayFromStateMap.put(from, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions that travel from a state.
	 * 
	 * @param to
	 *            the <CODE>State</CODE> to which all returned transitions
	 *            should go to
	 * @return an array of all <CODE>Transition</CODE> objects going to the
	 *         State
	 */
	public Transition[] getTransitionsToState(State to) {
		Transition[] toReturn = (Transition[]) transitionArrayToStateMap
				.get(to);
		if (toReturn == null) {
			List list = (List) transitionToStateMap.get(to);
			toReturn = (Transition[]) list.toArray(new Transition[0]);
			transitionArrayToStateMap.put(to, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions going from one given state to another given
	 * state.
	 * 
	 * @param from
	 *            the state all returned transitions should come from
	 * @param to
	 *            the state all returned transitions should go to
	 * @return an array of all transitions coming from <CODE>from</CODE> and
	 *         going to <CODE>to</CODE>
	 */
	public Transition[] getTransitionsFromStateToState(State from, State to) {
		Transition[] t = getTransitionsFromState(from);
		ArrayList list = new ArrayList();
		for (int i = 0; i < t.length; i++)
			if (t[i].getToState() == to)
				list.add(t[i]);
		return (Transition[]) list.toArray(new Transition[0]);
	}

	/**
	 * Retrieves all transitions.
	 * 
	 * @return an array containing all transitions for this automaton
	 */
	public Transition[] getTransitions() {
		if (cachedTransitions == null)
			cachedTransitions = (Transition[]) transitions
					.toArray(new Transition[0]);
		return cachedTransitions;
	}

	/**
	 * Adds a <CODE>Transition</CODE> to this automaton. This method may do
	 * nothing if the transition is already in the automaton.
	 * 
	 * @param trans
	 *            the transition object to add to the automaton
	 */
	public void addTransition(Transition trans) {
		if (!getTransitionClass().isInstance(trans) || trans == null) {
			throw (new IncompatibleTransitionException());
		}
		if (transitions.contains(trans))
			return;
        if(trans.getToState() == null || trans.getFromState() == null) return;
		transitions.add(trans);
        if(transitionFromStateMap == null) transitionFromStateMap = new HashMap();
		List list = (List) transitionFromStateMap.get(trans.getFromState());
		list.add(trans);
        if(transitionToStateMap == null) transitionToStateMap = new HashMap();
		list = (List)transitionToStateMap.get(trans.getToState()) ;
		list.add(trans);
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new AutomataTransitionEvent(this, trans,
				true, false));
	}

	/**
	 * Replaces a <CODE>Transition</CODE> in this automaton with another
	 * transition with the same from and to states. This method will delete the
	 * old if the transition is already in the automaton.
	 * 
	 * @param oldTrans
	 *            the transition object to add to the automaton
	 * @param newTrans
	 *            the transition object to add to the automaton
	 */
	public void replaceTransition(Transition oldTrans, Transition newTrans) {
		if (!getTransitionClass().isInstance(newTrans)) {
			throw new IncompatibleTransitionException();
		}
		if (oldTrans.equals(newTrans)) {
			return;
		}
		if (transitions.contains(newTrans)) {
			removeTransition(oldTrans);
			return;
		}
		if (!transitions.remove(oldTrans)) {
			throw new IllegalArgumentException(
					"Replacing transition that not already in the automaton!");
		}
		transitions.add(newTrans);
		List list = (List) transitionFromStateMap.get(oldTrans.getFromState());
		list.set(list.indexOf(oldTrans), newTrans);
		list = (List) transitionToStateMap.get(oldTrans.getToState());
		list.set(list.indexOf(oldTrans), newTrans);
		transitionArrayFromStateMap.remove(oldTrans.getFromState());
		transitionArrayToStateMap.remove(oldTrans.getToState());
		cachedTransitions = null;
		distributeTransitionEvent(new AutomataTransitionEvent(this, newTrans,
				true, false));
	}

	/**
	 * Removes a <CODE>Transition</CODE> from this automaton.
	 * 
	 * @param trans
	 *            the transition object to remove from this automaton.
	 */
	public void removeTransition(Transition trans) {
		transitions.remove(trans);
		List l = (List) transitionFromStateMap.get(trans.getFromState());
		l.remove(trans);
		l = (List) transitionToStateMap.get(trans.getToState());
		l.remove(trans);
		// Remove cached arrays.
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new AutomataTransitionEvent(this, trans,
				false, false));
	}

	/**
	 * Creates a state, inserts it in this automaton, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public final State createBlock(Point point) {
		int i = 0;
		while (getStateWithID(i) != null)
			i++;
		OpenAction read = new OpenAction();
		OpenAction.setOpenOrRead(true);
		JButton button = new JButton(read);
		button.doClick();
		OpenAction.setOpenOrRead(false);
		return getAutomatonFromFile(i, point);
	}
	
	//Reads the automaton in from a file.
	private State getAutomatonFromFile(int i, Point point) {
		State block = new State(i, point, this);
		Serializable serial = OpenAction.getLastObjectOpened();
		File lastFile = OpenAction.getLastFileOpened();
		if (lastFile == null || OpenAction.isOpened() == false) {
			return null;
		}
		block = putBlockContentsInAutomaton(block, serial, lastFile.getName(),
				this);
		block.setName(lastFile.getName().substring(0, lastFile.getName().length() - 4));
		addState(block);
		return block;
	}
	//Takes the contents of a block and adds them to the automaton.
	private State putBlockContentsInAutomaton(State block, Serializable serial,
			String name, Automaton target) {
		if (serial instanceof Automaton) {
			Automaton automaton = (Automaton) serial;
			automaton.setEnvironmentFrame(target.getEnvironmentFrame());
			block.setInternalName(name);
			if (!target.getBlockMap().containsKey(name)) {
				//System.out.println("Put block in map as " + name);
				target.getBlockMap().put(name, automaton);
			}
			State[] newStates = automaton.getStates();
			for (int k = 0; k < newStates.length; k++) {
				State cur = newStates[k];
				cur.setParentBlock(block);
				if (automaton.isFinalState(cur)) cur.setFinalStateInBlock(true);
				cur.setAutomaton(target);
				List fromList = makeListFromArray(automaton
						.getTransitionsFromState(cur));
				List toList = makeListFromArray(automaton
						.getTransitionsToState(cur));
				target.transitionFromStateMap.put(cur, fromList);
				target.transitionToStateMap.put(cur, toList);
			}
		}

		return block;
	}

	/**
	 * Creates a state, inserts it in this automaton, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 * @param i
	 */
	public final State createBlockFromAutomaton(Point point, Serializable auto,
			String name, int i) {
		State block = new State(i, point, this);
		putBlockContentsInAutomaton(block, auto, name, this);
		addState(block);
		return block;
	}
	

	public final void replaceBlock(State block, Automaton inside) {
		//Someone should probably write a .equals method for automaton.
		if(((Automaton)blockMap.get(block.getInternalName())) == null) {
            JOptionPane.showMessageDialog(null, "JFLAP failed to find the block you were editing, your changes have not taken effect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
       
		List fromList = makeListFromArray(getTransitionsFromState(block));
		List toList = makeListFromArray(getTransitionsToState(block));
		int found = 0;
        boolean initial = false;
        boolean finalState = false;
		State[] states = this.getStates();
		
		for (int k = 0; k < states.length; k++) {
			State temp = states[k];
			//System.out.println("Temp d Name: " +temp.getID());
			if (temp.getInternalName() != null) {
				//System.out.println("Temp Internal Name: " +temp.getInternalName());
				if (temp.getInternalName().equals(block.getInternalName())) {
					if (temp.getID() == block.getID()){
                        if(this.getInitialState()==block) initial=true;
                        State[] finals = this.getFinalStates();
                        for(int m = 0; m < finals.length; m++){
                            if(finals[m]==block){
                                this.removeFinalState(block);
                                finalState = true;
                            }
                        }
                        removeState(temp);
                    }
					found++;
				}
			}
		}
		//System.out.println("Found " + found);
		if(found==1) blockMap.remove(block.getInternalName());
		State newBlock = new State(block.getID(), block.getPoint(), this);
		String newName = block.getInternalName();
		if(found>1){
			int add = block.getID();
			while(this.getBlockMap().containsKey(newName)){
				newName = newName.concat(Integer.toString(add));
				add++;
			}
		}
        

        newBlock.setLabel(block.getLabel());
        newBlock.setName(block.getName());     
		//System.out.println("New Name: "+newName);
        newBlock.setParentBlock(block.getParentBlock());
		newBlock.setInternalName(newName);
		
		putBlockContentsInAutomaton(newBlock, inside, newBlock
				.getInternalName(), this);
		addState(newBlock);     
        if(initial) this.setInitialState(newBlock);
        if(finalState) this.addFinalState(newBlock);
		for (int k = 0; k < fromList.size(); k++) {
			Transition tempFrom = (Transition) fromList.get(k);
			tempFrom.setFromState(newBlock);
			addTransition(tempFrom);
		}
		for (int k = 0; k < toList.size(); k++) {
			Transition tempTo = (Transition) toList.get(k);
			tempTo.setToState(newBlock);
			addTransition(tempTo);
		}
		return;
	}

	
	/**
	 * Moves objects from Array to List
	 * 
	 * @param point
	 * @return
	 */
	public static List makeListFromArray(Object[] array) {
		List list = new ArrayList();
		for (int k = 0; k < array.length; k++) {
			list.add(array[k]);
		}
		return list;
	}

	/**
	 * Creates a state, inserts it in this automaton, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public final State createState(Point point) {
		int i = 0;
		while (getStateWithID(i) != null)
			i++;
		State state = new State(i, point, this);
		addState(state);
		return state;
	}

	/**
	 * Creates a state, inserts it in this automaton, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public final State createStateWithId(Point point, int i) {
		State state = new State(i, point, this);
		addState(state);
		return state;
	}

	/**
	 * Adds a new state to this automata. Clients should use the <CODE>createState</CODE>
	 * method instead.
	 * 
	 * @param state
	 *            the state to add
	 */
	protected final void addState(State state) {
		states.add(state);
		transitionFromStateMap.put(state, new LinkedList());
		transitionToStateMap.put(state, new LinkedList());
		cachedStates = null;
		distributeStateEvent(new AutomataStateEvent(this, state, true, false,
				false));
	}

	/**
	 * Removes a state from the automaton. This will also remove all transitions
	 * associated with this state.
	 * 
	 * @param state
	 *            the state to remove
	 */
	public void removeState(State state) {
		Transition[] t = getTransitionsFromState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		t = getTransitionsToState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		distributeStateEvent(new AutomataStateEvent(this, state, false, false,
				false));
		states.remove(state);
		finalStates.remove(state);
		if (state == initialState)
			initialState = null;

		transitionFromStateMap.remove(state);
		transitionToStateMap.remove(state);

		transitionArrayFromStateMap.remove(state);
		transitionArrayToStateMap.remove(state);

		cachedStates = null;
		Iterator statIt = states.iterator();
		while (statIt.hasNext()) {
			State temp = (State) statIt.next();
			if (temp.getParentBlock() != null) {
				if (temp.getParentBlock().equals(state)) {
					removeState(temp);
				}
			}
		}
	}

	/**
	 * Sets the new initial state to <CODE>initialState</CODE> and returns
	 * what used to be the initial state, or <CODE>null</CODE> if there was no
	 * initial state. The state specified should already exist in the automata.
	 * 
	 * @param initialState
	 *            the new initial state
	 * @return the old initial state, or <CODE>null</CODE> if there was no
	 *         initial state
	 */
	public State setInitialState(State initialState) {
		State oldInitialState = this.initialState;
		this.initialState = initialState;
		return oldInitialState;
	}

	/**
	 * Returns the start state for this automaton.
	 * 
	 * @return the start state for this automaton
	 */
	public State getInitialState() {
		return this.initialState;
	}

	/**
	 * Returns an array that contains every state in this automaton. The array
	 * is gauranteed to be in order of ascending state IDs.
	 * 
	 * @return an array containing all the states in this automaton
	 */
	public State[] getStates() {
		if (cachedStates == null) {
			cachedStates = (State[]) states.toArray(new State[0]);
			Arrays.sort(cachedStates, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((State) o1).getID() - ((State) o2).getID();
				}

				public boolean equals(Object o) {
					return this == o;
				}
			});
		}
		return cachedStates;
	}
	
	public void selectStatesWithinBounds(Rectangle bounds){
		State[] states = getStates();
		for(int k = 0; k < states.length; k++){
			states[k].setSelect(false);
			if(bounds.contains(states[k].getPoint())){	
				states[k].setSelect(true);
			}
		}
	}
	
	public ArrayList getNotes() {
		return myNotes;
	}
	

	public void addNote(Note note){
		myNotes.add(note);
	}
	

	public void deleteNote(Note note){
		for(int k = 0; k < myNotes.size(); k++){
			if(note == myNotes.get(k)) myNotes.remove(k);
		}
	}
	/**
	 * Adds a single final state to the set of final states. Note that the
	 * general automaton can have an unlimited number of final states, and
	 * should have at least one. The state that is added should already be one
	 * of the existing states.
	 * 
	 * @param finalState
	 *            a new final state to add to the collection of final states
	 */
	public void addFinalState(State finalState) {
		cachedFinalStates = null;
		finalStates.add(finalState);
	}

	/**
	 * Removes a state from the set of final states. This will not remove a
	 * state from the list of states; it shall merely make it nonfinal.
	 * 
	 * @param state
	 *            the state to make not a final state
	 */
	public void removeFinalState(State state) {
		cachedFinalStates = null;
		finalStates.remove(state);
	}

	/**
	 * Returns an array that contains every state in this automaton that is a
	 * final state. The array is not necessarily gauranteed to be in any
	 * particular order.
	 * 
	 * @return an array containing all final states of this automaton
	 */
	public State[] getFinalStates() {
		if (cachedFinalStates == null)
			cachedFinalStates = (State[]) finalStates.toArray(new State[0]);
		return cachedFinalStates;
	}

	/**
	 * Determines if the state passed in is in the set of final states.
	 * 
	 * @param state
	 *            the state to determine if is final
	 * @return <CODE>true</CODE> if the state is a final state in this
	 *         automaton, <CODE>false</CODE> if it is not
	 */
	public boolean isFinalState(State state) {
		return finalStates.contains(state);
	}

	/**
	 * Determines if the state passed in is the initial states.
	 * Added for JFLAP 6.3
	 * @param state
	 *            the state to determine if is final
	 * @return <CODE>true</CODE> if the state is a final state in this
	 *         automaton, <CODE>false</CODE> if it is not
	 */
	public boolean isInitialState(State state) {
		return (state.equals(initialState));
	}
	
	/**
	 * Returns the <CODE>State</CODE> in this automaton with this ID.
	 * 
	 * @param id
	 *            the ID to look for
	 * @return the instance of <CODE>State</CODE> in this automaton with this
	 *         ID, or <CODE>null</CODE> if no such state exists
	 */
	public State getStateWithID(int id) {
		Iterator it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			if (state.getID() == id)
				return state;
		}
		return null;
	}

	/**
	 * Tells if the passed in object is indeed a state in this automaton.
	 * 
	 * @param state
	 *            the state to check for membership in the automaton
	 * @return <CODE>true</CODE> if this state is in the automaton, <CODE>false</CODE>otherwise
	 */
	public boolean isState(State state) {
		return states.contains(state);
	}

	/**
	 * Returns the particular class that added transition objects should be a
	 * part of. Subclasses may wish to override in case they want to restrict
	 * the type of transitions their automaton will respect. By default this
	 * method simply returns the class object for the abstract class <CODE>automata.Transition</CODE>.
	 * 
	 * @see #addTransition
	 * @see automata.Transition
	 * @return the <CODE>Class</CODE> object that all added transitions should
	 *         derive from
	 */
	protected Class getTransitionClass() {
		return automata.Transition.class;
	}

	/**
	 * Returns a string representation of this <CODE>Automaton</CODE>.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append('\n');
		State[] states = getStates();
		for (int s = 0; s < states.length; s++) {
			if (initialState == states[s])
				buffer.append("--> ");
			buffer.append(states[s]);
			if (isFinalState(states[s]))
				buffer.append(" **FINAL**");
			buffer.append('\n');
			Transition[] transitions = getTransitionsFromState(states[s]);
			for (int t = 0; t < transitions.length; t++) {
				buffer.append('\t');
				buffer.append(transitions[t]);
				buffer.append('\n');
			}
		}

		return buffer.toString();
	}

	/**
	 * Adds a <CODE>AutomataStateListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addStateListener(AutomataStateListener listener) {
		stateListeners.add(listener);
	}

	/**
	 * Adds a <CODE>AutomataTransitionListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addTransitionListener(AutomataTransitionListener listener) {
		transitionListeners.add(listener);
	}

	/**
	 * Gives an automata state change event to all state listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeStateEvent(AutomataStateEvent event) {
		Iterator it = stateListeners.iterator();
		while (it.hasNext()) {
			AutomataStateListener listener = (AutomataStateListener) it.next();
			listener.automataStateChange(event);
		}
	}

	/**
	 * Removes a <CODE>AutomataStateListener</CODE> from this automata.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeStateListener(AutomataStateListener listener) {
		stateListeners.remove(listener);
	}

	/**
	 * Removes a <CODE>AutomataTransitionListener</CODE> from this automata.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeTransitionListener(AutomataTransitionListener listener) {
		transitionListeners.remove(listener);
	}

	/**
	 * Gives an automata transition change event to all transition listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeTransitionEvent(AutomataTransitionEvent event) {
		Iterator it = transitionListeners.iterator();
		while (it.hasNext()) {
			AutomataTransitionListener listener = (AutomataTransitionListener) it
					.next();
			listener.automataTransitionChange(event);
		}
	}

	/**
	 * This handles deserialization so that the listener sets are reset to avoid
	 * null pointer exceptions when one tries to add listeners to the object.
	 * 
	 * @param in
	 *            the input stream for the object
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		// Reset all nonread objects.
		transitionListeners = new HashSet();
		stateListeners = new HashSet();
		transitionFromStateMap = new HashMap();
		transitionToStateMap = new HashMap();
		transitionArrayFromStateMap = new HashMap();
		transitionArrayToStateMap = new HashMap();
		transitions = new HashSet();
		states = new HashSet();

		// Do the reading in of objects.
		int version = in.readInt();
		if (version >= 0) { // Adjust by version.
			// The reading for version 0 of this object.
			Set s = (Set) in.readObject();
			Iterator it = s.iterator();
			while (it.hasNext())
				addState((State) it.next());

			initialState = (State) in.readObject();
			finalStates = (Set) in.readObject();
			// Let the class take care of the transition stuff.
			Set trans = (Set) in.readObject();
			it = trans.iterator();
			while (it.hasNext())
				addTransition((Transition) it.next());
			if (this instanceof TuringMachine) {
				((TuringMachine) this).tapes = in.readInt();
			}
		}
		while (!in.readObject().equals("SENT"))
			; // Read until sentinel.
	}

	/**
	 * This handles serialization.
	 */
	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		out.writeInt(0); // Version of the stream.
		// Version 0 outstuff...
		out.writeObject(states);
		out.writeObject(initialState);
		out.writeObject(finalStates);
		out.writeObject(transitions);
		if (this instanceof TuringMachine) {
			out.writeInt(((TuringMachine) this).tapes);
		}
		out.writeObject("SENT"); // The sentinel object.
	}

	/**
	 * Gets the map of blocks for this automaton.
	 *
	 * @return the map of blocks
	 */
	public Map getBlockMap() {
		return blockMap;
	}

	/**
	 * Gets the Environment Frame the automaton is in.
	 * @return the environment frame.
	 */
	public EnvironmentFrame getEnvironmentFrame() {
		return myEnvFrame;
	}

	/**
	 * Changes the environment frame this automaton is in.
	 * @param frame the environment frame
	 */
	public void setEnvironmentFrame(EnvironmentFrame frame) {
		myEnvFrame = frame;
	}
	
	public void setFilePath(String name){
		fileName = name;
	}
	
	public String getFileName(){
		int last = fileName.lastIndexOf("\\");
		if(last == -1) last = fileName.lastIndexOf("/");
		
		return fileName.substring(last+1);
	}
	
	public String getFilePath(){
		int last = fileName.lastIndexOf("\\");
		if(last == -1) last = fileName.lastIndexOf("/");
		
		return fileName.substring(0, last+1);
	}

	// AUTOMATA SPECIFIC CRAP
	// This includes lots of stuff not strictly necessary for the
	// defintion of automata, but stuff that makes it at least
	// somewhat efficient in the process.
    private String fileName = "";   // Jinghui bug fixing.

	private EnvironmentFrame myEnvFrame = null;

	/** The collection of states in this automaton. */
	private Set states;

	/** The cached array of states. */
	private State[] cachedStates = null;

	/** The cached array of transitions. */
	private Transition[] cachedTransitions = null;

	/** The cached array of final states. */
	private State[] cachedFinalStates = null;

	/**
	 * The collection of final states in this automaton. This is a subset of the
	 * "states" collection.
	 */
	private Set finalStates;

	/** The initial state. */
	private State initialState = null;

	/** The list of transitions in this automaton. */
	private Set transitions;

	/**
	 * A mapping from states to a list holding transitions from those states.
	 */
	private HashMap transitionFromStateMap = new HashMap();

	/**
	 * A mapping from state to a list holding transitions to those states.
	 */
	private HashMap transitionToStateMap = new HashMap();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayFromStateMap = new HashMap();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayToStateMap = new HashMap();

	/**
	 * A mapping from the name of an automaton to the automaton. Used for
	 * referencing the same automaton from multiple buliding blocks
	 */
	private HashMap blockMap = new HashMap();
	

	private ArrayList myNotes = new ArrayList();

	// LISTENER STUFF
	// Structures related to this object as something that generates
	// events, in particular as it pertains to the removal and
	// addition of states and transtions.
	private transient HashSet transitionListeners = new HashSet();

	private transient HashSet stateListeners = new HashSet();
	


}
