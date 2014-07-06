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

import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

import java.util.List;

/**
 * This is a special <code>ToolBox</code> for Mealy machines
 * that loads the <code>MealyArrowTool</code> instead of the 
 * default <code> ArrowTool</code>.
 * 
 * @see automata.mealy.MealyMachine
 * @see MealyArrowTool
 * @author Jinghui Lim
 * 
 */
public class MealyToolBox implements ToolBox 
{
    /**
     * Returns a list of tools for Mealy machines, similar to
     * the <code>DefaultToolBox</code>. This includes a
     * <code>MealyArrowTool</code>, <code>StateTool</code>
     * <code>TransitionTool</code>, and <code>DeleteTool</code>
     * in that order.
     * 
     * @param view the component that the automaton will be drawn in
     * @param drawer the drawer that will draw the automaton in the
     * view
     * @return a list of <CODE>Tool</CODE> objects.
     */
    public List tools(AutomatonPane view, AutomatonDrawer drawer) 
    {
        List list = new java.util.ArrayList();
        list.add(new MealyArrowTool(view, drawer));
        list.add(new StateTool(view, drawer));
        list.add(new TransitionTool(view, drawer));
        list.add(new DeleteTool(view, drawer));
        return list;
    }
}
