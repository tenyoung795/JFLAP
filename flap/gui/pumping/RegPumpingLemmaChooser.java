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

import java.util.*;
import pumping.reg.*;

/**
 * A <code>RegPumpingLemmaChooser</code> is a <code>PumpingLemmaChooser</code> 
 * for {@link pumping.RegularPumpingLemma}s.
 * 
 * @author Jinghui Lim
 *
 */
public class RegPumpingLemmaChooser extends PumpingLemmaChooser 
{
    /**
     * Adds all the regular pumping lemmas.
     *
     */
    public RegPumpingLemmaChooser()
    {
        myList = new ArrayList();
        
        //old languages
        myList.add(new AnBn());
        myList.add(new NaNb());
        myList.add(new Palindrome());
        myList.add(new ABnAk());
        myList.add(new AnBkCnk());
        myList.add(new AnBlAk());
        myList.add(new AnEven());
        
        //new languages (JFLAP 6.2)
        myList.add(new AnBk());
        myList.add(new BBABAnAn());
        myList.add(new B5W());
        myList.add(new B5Wmod());
        myList.add(new BkABnBAn());
        myList.add(new AB2n());        
    }
}
