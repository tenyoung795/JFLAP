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

import automata.LambdaTransitionChecker;
import automata.Transition;

/**
 * The <code>MealyLambdaTransitionChecker</code> can be used to determine 
 * if a Mealy or Moore machine's transition is a lambda transition.
 * 
 * @author Jinghui Lim
 *
 */
public class MealyLambdaTransitionChecker extends LambdaTransitionChecker 
{
    /**
     * Creates an instance of <code>MealyLambdaTransitionChecker</code>.
     */
    public MealyLambdaTransitionChecker() 
    {
        super();
    }

    /**
     * Returns <code>true</code> if <code>transition</code> is a lambda
     * transition (i.e. its label is the lambda string).
     * 
     * @param transition the transtion
     * @return <code>true</code> if the transition is a lambday transition,
     * <code>false</code> otherwise
     */
    public boolean isLambdaTransition(Transition transition) 
    {
        MealyTransition t = (MealyTransition) transition;
        if(t.getLabel().equals(LAMBDA))
            return true;
        else
            return false;
    }
}
