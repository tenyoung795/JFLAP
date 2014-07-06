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

package automata.mealy;

import automata.Automaton;

/**
 * This subclass of <code>Automaton</code> is specifically for
 * a definition of a Mealy machine.
 * 
 * @author Jinghui Lim
 *
 */
public class MealyMachine extends Automaton 
{
    /**
     * Creates a Mealy machine with no states or transitions.
     *
     */
    public MealyMachine()
    {
        super();
    }
    
    /**
     * Returns the class of <code>Transition</code> this automaton
     * must accept.
     * 
     * @return the <code>Class</code> object for the <code>
     * MealyTransition</code>
     */
    protected Class getTransitionClass()
    {
        return MealyTransition.class;
    }
}
