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

package gui.pumping;

import pumping.cf.*;
import java.util.ArrayList;

/**
 * A <code>CFPumpingLemmaChooser</code> is a {@link gui.pumping.PumpingLemmaChooser} 
 * for {@link pumping.ContextFreePumpingLemma}s.
 * 
 * @author Jinghui Lim
 *
 */
public class CFPumpingLemmaChooser extends PumpingLemmaChooser 
{
    /**
     * Adds all the context-free pumping lemmas.
     *
     */
    public CFPumpingLemmaChooser()
    {
        myList = new ArrayList();
        
        //old languages
        myList.add(new AnBnCn());
        myList.add(new WW());
        myList.add(new AnBjAnBj());
        myList.add(new NaNbNc());
        myList.add(new NagNbeNc());
        myList.add(new AiBjCk());
        myList.add(new AnBn());
        
        //new languages (JFLAP 6.2)
        myList.add(new AkBnCnDj());
        myList.add(new WW1WrGrtrThanEq());
        myList.add(new WW1WrEquals());
        myList.add(new W1BnW2());
        myList.add(new W1CW2CW3CW4());                
        myList.add(new W1VVrW2());
    }
}
