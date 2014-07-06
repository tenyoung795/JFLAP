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

import automata.*;

/**
 * The Mealy machine step by state simulator simulates the behavior
 * of a Mealy machine. It takes a <code>MooreMachine</code> object
 * and runs an input string on the object. The
 * <code>MooreStepByStateSimulator</code> is different from the
 * <code>MealyStepByStateSimulator</code> in that it produces output
 * in states, not transitions, and output is produced in the first
 * state.
 * 
 * <p>It simulates the machine's behavior by stepping through one state
 * at a time. Output of the machine can be accessed through 
 * {@link MealyConfiguration#getOutput()} and is printed out on the 
 * tape in the simulation window. This does not deal with lambda
 * transitions.
 * 
 * @author Jinghui Lim
 * @see automata.mealy.MealyConfiguration
 *
 */
public class MooreStepByStateSimulator extends MealyStepByStateSimulator 
{
    /**
     * Creates a Moore step by state simulator for the given automaton.
     * 
     * @param automaton the machine to simulate
     */
    public MooreStepByStateSimulator(Automaton automaton)
    {
        super(automaton);
    }
    
    /**
     * Returns a <code>MooreConfiguration</code> that represents the 
     * initial configuration of the Moore machine, before any input
     * has been processed. This returns an array of length one.
     * 
     * @param input the input string to simulate
     */
    public Configuration[] getInitialConfigurations(String input) 
    {
        Configuration[] configs = new Configuration[1];
        configs[0] = new MealyConfiguration(myAutomaton.getInitialState(), null, input, 
                input, ((MooreMachine)myAutomaton).getOutput(myAutomaton.getInitialState()));
        return configs;
    }
}
