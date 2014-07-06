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

import java.awt.event.MouseEvent;
import automata.State;
import gui.viewer.*;

/**
 * This is a subclass of a <code>MealyArrowTool</code> for Moore 
 * machines that allows the arrow tool to change the output of 
 * a state. It is identical in every way except that, when a 
 * mouse is clicked on a state, instead of doing nothing, it 
 * prompts the user for a new state output. Other mouse methods 
 * for right-clicking and click-and-drag remain the same.
 *
 * @see #mouseClicked(MouseEvent)
 * @see automata.mealy.MooreMachine
 * @see MooreStateTool
 * @see MooreToolBox
 * @author Jinghui Lim
 *
 */
public class MooreArrowTool extends MealyArrowTool 
{
    /**
     * Instantiates a new arrow tool.
     * 
     * @param view the view where the automaton is drawn
     * @param drawer the object that draws the automaton
     * @param creator the transition creator used for editing transitions
     */
    public MooreArrowTool(AutomatonPane view, AutomatonDrawer drawer,
            TransitionCreator creator)
    {
        super(view, drawer, creator);
    }
    
    /**
     * Instantiates a new arrow tool.
     * 
     * @param view the view where the automaton is drawn
     * @param drawer the object that draws the automaton
     */            
    public MooreArrowTool(AutomatonPane view, AutomatonDrawer drawer) 
    {
        super(view, drawer);
    }
    
    /**
     * Checks if the mouse was clicked on a state, and offers to change
     * the state output if so. Otherwise, {@link ArrowTool#mouseClicked(MouseEvent)}
     * is called so that other mouse events proceed as specified in
     * <code>ArrowTool</code>.
     * 
     * @param event the mouse event
     * @see ArrowTool#mouseClicked(MouseEvent)
     */
    public void mouseClicked(MouseEvent event) 
    {
        if(event.getButton() == MouseEvent.BUTTON1)
        {
            State state = getDrawer().stateAtPoint(event.getPoint());
            if(state == null)
                super.mouseClicked(event);
            else
                MooreStateTool.editState(state);
        }
    }
}
