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

import java.util.*;
import automata.*;

/**
 * The Mealy machine step by state simulator simulates the behavior
 * of a Mealy machine. It takes a <code>MealyMachine</code> object
 * and runs an input string on the object.
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
public class MealyStepByStateSimulator extends AutomatonSimulator 
{
    /**
     * Creates a Mealy machine step by state simulator for the given
     * automaton.
     * 
     * @param automaton the machine to simulate
     */
    public MealyStepByStateSimulator(Automaton automaton) 
    {
        super(automaton);
    }

    /**
     * Returns a <code>MealyConfiguration</code> that represents the 
     * initial configuration of the Mealy machine, before any input
     * has been processed. This returns an array of length one.
     * 
     * @param input the input string to simulate
     */
    public Configuration[] getInitialConfigurations(String input) 
    {
        Configuration[] configs = new Configuration[1];
        configs[0] = new MealyConfiguration(myAutomaton.getInitialState(), 
                null, input, input, "");
        return configs;
    }

    /**
     * Simulates one step for a particular configuration, adding all
     * possible configurations reachable in one step to a list of
     * possible configurations.
     * 
     * @param configuration the configuration simulate one step on
     */
    public ArrayList stepConfiguration(Configuration configuration)
    {
        ArrayList list = new ArrayList();
        MealyConfiguration config = (MealyConfiguration) configuration;
        
        String unprocessedInput = config.getUnprocessedInput();
        String totalInput = config.getInput();
        State currentState = config.getCurrentState();
        
        Transition[] transitions = myAutomaton.getTransitionsFromState(currentState);
        for(int i = 0; i < transitions.length; i++)
        {
            MealyTransition trans = (MealyTransition) transitions[i];
            String transLabel = trans.getLabel();
            if(unprocessedInput.startsWith(transLabel))
            {
                String input = "";
                if(transLabel.length() < unprocessedInput.length())
                    input = unprocessedInput.substring(transLabel.length());
                State toState = trans.getToState();
                String output = config.getOutput() + trans.getOutput();
                MealyConfiguration configToAdd = 
                    new MealyConfiguration(toState, config, totalInput, input, output);
                list.add(configToAdd);
            }
        }
        return list;
    }

    /**
     * Returns <code>true</code> if all the input has been processed and output 
     * generated. This calls the {@link MealyConfiguration#isAccept()}. It 
     * returns <code>false</code> otherwise.
     * 
     * @return <code>true</code> if all input has been processed, <code>false
     * </code> otherwise
     */
    public boolean isAccepted() 
    {
        Iterator it = myConfigurations.iterator();
        while(it.hasNext())
        {
            MealyConfiguration config = (MealyConfiguration) it.next();
            if(config.isAccept())
                return true;
        }
        return false;
    }

    /**
     * Simulated the input in the machine.
     * 
     * @param input the input string to run on the machine
     * @return <code>true</code> once the entire input string has been
     * processed.
     * @see #isAccepted()
     */
    public boolean simulateInput(String input) 
    {
        myConfigurations.clear();
        Configuration[] initialConfigs = getInitialConfigurations(input);
        myConfigurations.addAll(Arrays.asList(initialConfigs));

        while(!myConfigurations.isEmpty())
        {
            if(isAccepted())
                return true;
            ArrayList configurationsToAdd = new ArrayList();
            Iterator it = myConfigurations.iterator();
            while(it.hasNext())
            {
                MealyConfiguration config = (MealyConfiguration) it.next();
                configurationsToAdd.addAll(stepConfiguration(config));
                it.remove();
            }
            myConfigurations.addAll(configurationsToAdd);
        }
        return false;
    }
}
