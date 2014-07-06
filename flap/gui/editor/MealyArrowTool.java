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

import gui.viewer.*;

/**
 * This is a subclass of an <code>ArrowTool</code> for Mealy 
 * machines that removes the "final state" checkbox in the
 * right-click popup menu. It is otherwise identical in every 
 * way.
 *
 * @see automata.mealy.MealyMachine
 * @see MealyToolBox
 * @author Jinghui Lim
 *
 */
public class MealyArrowTool extends ArrowTool 
{
    /**
     * Instantiates a new arrow tool.
     * 
     * @param view the view where the automaton is drawn
     * @param drawer the object that draws the automaton
     * @param creator the transition creator used for editing transitions
     */
    public MealyArrowTool(AutomatonPane view, AutomatonDrawer drawer,
            TransitionCreator creator)
    {
        super(view, drawer, creator);
//        stateMenu.makeFinal.setEnabled(false);
        stateMenu.remove(stateMenu.makeFinal);
    }
    
    /**
     * Instantiates a new arrow tool.
     * 
     * @param view the view where the automaton is drawn
     * @param drawer the object that draws the automaton
     */            
    public MealyArrowTool(AutomatonPane view, AutomatonDrawer drawer) 
    {
        super(view, drawer);
//        stateMenu.makeFinal.setEnabled(false);
        stateMenu.remove(stateMenu.makeFinal);
    }
}
