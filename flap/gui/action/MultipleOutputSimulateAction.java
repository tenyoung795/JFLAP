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

import automata.Automaton;
import gui.JTableExtender;
import gui.environment.Environment;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import gui.sim.multiple.InputTableModel;
import automata.turing.TuringMachine;

/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction, and it also produces the output that a machine produces.
 * This is useful in situations where you are running input on a Turing machine
 * as a transducer. This is almost identical to its superclass except for a few
 * different names, and this one does not remove the columns corresponding to
 * the output.
 * 
 * @author Thomas Finley
 */

public class MultipleOutputSimulateAction extends MultipleSimulateAction {
	/**
	 * Instantiates a new <CODE>MultipleOuptutSimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public MultipleOutputSimulateAction(Automaton automaton,
			Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Multiple Run (Transducer)");
         putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T,
                    MAIN_MENU_MASK));
	}

	/**
	 * Returns the title for the type of compontent we will add to the
	 * environment.
	 * 
	 * @return in this base class, returns "Multiple Inputs"
	 */
	public String getComponentTitle() {
		return "Multiple Runs";
	}

	/**
	 * Provides an initialized multiple input table object.
	 * 
	 * @param automaton
	 *            the automaton to provide the multiple input table for
	 * @return a table object for this automaton
	 * @see gui.sim.multiple.InputTableModel
	 */
	protected JTableExtender initializeTable(Object obj) {
        boolean multiple = false;
        if(this.getEnvironment().myObjects!=null) multiple = true;
		TableModel model = InputTableModel.getModel((Automaton)getObject(), multiple);
		JTableExtender table = new JTableExtender(model, this);
		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
        if(multiple){
            ArrayList autos  = this.getEnvironment().myObjects;
            ArrayList strings = this.getEnvironment().myTestStrings;
            ArrayList outs = this.getEnvironment().myTransducerStrings;
            int offset = strings.size();
            int tapeNum = 1;
            if(autos.get(0) instanceof TuringMachine){
            	tapeNum = ((TuringMachine)autos.get(0)).tapes;
            	offset = offset/tapeNum;
            }     
            int row = 0;
            for(int m = 0; m < autos.size(); m++){      
                for(int k = 0; k < strings.size(); k++){
                    row = k/tapeNum+offset*m;
                    model.setValueAt(((Automaton)autos.get(m)).getFileName(), row, 0);  
                    model.setValueAt((String)strings.get(k), row, k%tapeNum+1);
                }
                
            }
        }
		return table;
	}

	/**
	 * This simulate action is only applicable to those types of automata that
	 * can be considered to generate output, that is, Turing machines.
	 * 
	 * @param object
	 *            to object to test for applicability
	 */
	public static boolean isApplicable(Object object) {
		return object instanceof TuringMachine;
	}
}
