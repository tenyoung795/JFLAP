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

package gui.editor;

import gui.viewer.SelectionDrawer;
import automata.Automaton;
import automata.State;

/**
 * This is a view that holds a tool bar and the canvas where the automaton is
 * displayed.
 * 
 * @author Thomas Finley
 */

public class EditBlockPane extends EditorPane {
	/**
	 * Instantiates a new editor pane for the given automaton.
	 * 
	 * @param automaton
	 *            the automaton to create the editor pane for
	 */
	public EditBlockPane(Automaton automaton) {
		super(new SelectionDrawer(automaton));
	}

	public void setBlock(State state) {
		myBlock = state;
	}

	public State getBlock() {
		return myBlock;
	}
	
	public void setOldBlock(State state) {
		myOldBlock = state;
	}

	public State getOldBlock() {
		return myOldBlock;
	}

	protected State myBlock = null;
	protected State myOldBlock = null;

}
