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

package file.xml;

import pumping.*;
import pumping.reg.*;
import pumping.cf.*;

/**
 * 
 * @author Jinghui Lim
 *
 */
public class PumpingLemmaFactory 
{
    public static PumpingLemma createPumpingLemma(String type, String name)
    {
        if(type.equals(RegPumpingLemmaTransducer.TYPE))
        {
            if(name.equals(new ABnAk().getTitle()))
                return new ABnAk();
            else if(name.equals(new AnBkCnk().getTitle()))
                return new AnBkCnk();
            else if(name.equals(new AnBlAk().getTitle()))
                return new AnBlAk();
            else if(name.equals(new pumping.reg.AnBn().getTitle()))
                return new pumping.reg.AnBn();
            else if(name.equals(new AnEven().getTitle()))
                return new AnEven();
            else if(name.equals(new NaNb().getTitle()))
                return new NaNb();
            else if(name.equals(new Palindrome().getTitle()))
                return new Palindrome();
            
            else if (name.equals(new BBABAnAn().getTitle()))
            	return new BBABAnAn();
            else if (name.equals(new B5W().getTitle()))
            	return new B5W();
            else if (name.equals(new BkABnBAn().getTitle()))
            	return new BkABnBAn();
            else if (name.equals(new AnBk().getTitle()))
            	return new AnBk();
            else if (name.equals(new AB2n().getTitle()))
            	return new AB2n();
            else if (name.equals(new B5Wmod().getTitle()))
            	return new B5Wmod();            
            else    // this should not happen 
                return null;
        }
        else if(type.equals(CFPumpingLemmaTransducer.TYPE))
        {
            if(name.equals(new AnBjAnBj().getTitle()))
                return new AnBjAnBj();
            else if(name.equals(new AiBjCk().getTitle()))
                return new AiBjCk();
            else if(name.equals(new pumping.cf.AnBn().getTitle()))
                return new pumping.cf.AnBn();
            else if(name.equals(new AnBnCn().getTitle()))
                return new AnBnCn();
            else if(name.equals(new NagNbeNc().getTitle()))
                return new NagNbeNc();
            else if(name.equals(new NaNbNc().getTitle()))
                return new NaNbNc();
            else if(name.equals(new WW().getTitle()))
                return new WW();
            
            else if (name.equals(new WW1WrEquals().getTitle()))
            	return new WW1WrEquals();
            else if (name.equals(new W1BnW2().getTitle()))
            	return new W1BnW2();
            else if (name.equals(new W1CW2CW3CW4().getTitle()))
            	return new W1CW2CW3CW4();
            else if (name.equals(new WW1WrGrtrThanEq().getTitle()))
            	return new WW1WrGrtrThanEq();
            else if (name.equals(new AkBnCnDj().getTitle()))
            	return new AkBnCnDj();
            else if (name.equals(new W1VVrW2().getTitle()))
            	return new W1VVrW2();  
            else    // this shouldn't happen
                return null;
        }
        else    // this shouldn't happen
            return null;
    }
}
