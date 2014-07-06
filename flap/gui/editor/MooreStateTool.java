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

import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import automata.State;
import automata.Transition;
import automata.mealy.MooreMachine;

import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

/**
 * This is a special <code>StateTool</code> for Moore machines 
 * that prompts the user for the output of the Moore state 
 * after the state is created (when the mouse is released).
 * It is otherwise the same as the <code>StateTool</code>.
 * 
 * @see #mouseReleased(MouseEvent)
 * @see automata.mealy.MooreMachine
 * @see MooreArrowTool
 * @see MooreToolBox
 * @author Jinghui Lim
 *
 */
public class MooreStateTool extends StateTool 
{
    /**
     * Instantiates a new state tool.
     * 
     * @param view the view where the automaton is drawn
     * @param drawer the object that draws the automaton
     */
    public MooreStateTool(AutomatonPane view, AutomatonDrawer drawer) 
    {
        super(view, drawer);
    }
    
    /**
     * This allows the user to edit the output of a Moore machine state.
     * It is called by {@link #mouseReleased(MouseEvent)} and
     * {@link MooreArrowTool#mouseClicked(MouseEvent)}.
     * 
     * @param s the state to edit the output of
     */
    protected static void editState(State s)
    {
        MooreMachine m = (MooreMachine) (s.getAutomaton());

        String output = (String) JOptionPane.showInputDialog(null, // I don't seem to need a parent component
            "Enter output:", "Set Output", JOptionPane.QUESTION_MESSAGE, null, null, m.getOutput(s));
          
        /*
         * null checking happens in setOutput too, but this is just to be safe
         */
        if(output == null)
            m.setOutput(s, "");
        else
            m.setOutput(s, output);
        /*
         * This is a cheap hack. It will not immediately display 
         * the output otherwise, and I don't really want to mess 
         * around with the guts of this program.
         */
        s.setLabel(s.getLabel());
    }
    
    /**
     * This prompts the user for the state output after a state is
     * created (when the mouse is released).
     * 
     * @param event the mouse event
     */
    public void mouseReleased(MouseEvent event)
    {
        editState(state);
    }
}
