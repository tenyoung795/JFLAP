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

package gui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 * The <CODE>NewHelpAction</CODE> is an extension of the <CODE>HelpAction</CODE>
 * that, whenever an action is received, puts up the help code for the
 * {@link gui.action.NewAction}. This class is intended only for the special
 * purpose of being used in the new structure creation window, which is just a
 * list of buttons and does not have the same structure as a document window.
 * 
 * @author Thomas Finley
 */

public class NewHelpAction extends HelpAction {
	/**
	 * Instantiates an <CODE>EnvironmentHelpAction</CODE>.
	 */
	public NewHelpAction() {

	}

	/**
	 * Displays help according to the current display of the automaton.
	 * 
	 * @param event
	 *            the action event
	 */
	public void actionPerformed(ActionEvent event) {
		/* Formerly the help page was shown, but for now, we just refer
		 * one to the tutorial.
		 */
		//displayHelp(NewAction.class);
		
		//Temporary command
		JOptionPane.showMessageDialog(null, "For help, feel free to access the JFLAP tutorial at\n" +
				"                          www.jflap.org.", "Help", JOptionPane.PLAIN_MESSAGE);
	}
}
