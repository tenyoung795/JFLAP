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

import gui.environment.Universe;

import java.util.*;

import automata.State;

/**
 * A <code>MooreMachine</code> is a special subclass of a
 * <code>MealyMachine</code> which restricts the output of
 * all transitions to one state to be the same output
 * defined in the state. This is done with a map of outputs
 * to states, and is implemented through {@link 
 * #getOutput(State)} and {@link #setOutput(State, String)}. 
 * 
 * @see MealyMachine
 * @see MooreTransition
 * @author Jinghui Lim
 *
 */
public class MooreMachine extends MealyMachine 
{
    /**
     * Map of states (keys) to output (values).
     */
    private Map myMap;
    
    /**
     * Creates a Moore machine with no states or transitions.
     *
     */
    public MooreMachine()
    {
        super();
        myMap = new HashMap();
    }
    
    /**
     * Returns the class of <code>Transition</code> this automaton
     * must accept.
     * 
     * @return the <code>Class</code> object for the <code>
     * MooreTransition</code>
     */
    protected Class getTransitionClass()
    {
        return MooreTransition.class;
    }
    
    /**
     * Sets the output for a state to be the given string, <code>
     * output</code>.
     * 
     * @param state state to set the output for
     * @param output value to set the state output to
     */
    public void setOutput(State state, String output)
    {
        /*
         * The null check occurs here but the input string can also
         * be checked before.
         */
        if(output == null)
            myMap.put(state, "");
        else
            myMap.put(state, output);
    }
    
    /**
     * Returns the output a state produces.
     * 
     * @param state the state whose output value we want
     * @return the output of the state
     */
    public String getOutput(State state)
    {
        if(myMap.get(state) == null)
            return "";
        else
            return (String) myMap.get(state);
    }
    
    /**
     * Returns a description of a state. If the output is the
     * empty string, it returns <code>MealyTransition.LAMBDA</code>
     * otherwise, it returns the output of the state. Called by
     * {@link gui.viewer.MooreStateDrawer#drawState(Graphics, Automaton, State, Point, Color)}.
     * 
     * @param state the state whose description we want
     * @return a description of the state
     */
    public String getStateDescription(State state)
    {
        /*
         * If the output has not been set i.e. this is a brand new state
         * before the user has entered a state output, then an
         * empty string shows up instead of lamba. It is purely cosmetic.
         */
        if(myMap.get(state) == null)
            return "";
        else if(getOutput(state).length() == 0) // if output is empty string
            return Universe.curProfile.getEmptyString();
        else
            return getOutput(state);
    }
}
