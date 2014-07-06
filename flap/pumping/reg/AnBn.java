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

package pumping.reg;

import pumping.*;

/**
 * The regular pumping lemma for <i>L</i> = 
 * {<i>a<sup>n</sup>b<sup>n</sup></i> : <i>n</i> &#8805; 0}.
 * 
 * @author Jinghui Lim & Chris Morgan
 */
public class AnBn extends RegularPumpingLemma 
{
    public String getTitle()
    {
        return "a^n b^n : n >= 0";
    }
    
    public String getHTMLTitle() 
    {
        return "<i>a<sup>n</sup>b<sup>n</sup></i> : <i>n</i> " + GREATER_OR_EQ + " 0";
    }    
    
    public void setDescription()
    {
    	partitionIsValid = false;
    	explanation = "For any <i>m</i> value, a possible value for <i>w</i> is \"a<sup><i>m</i></sup>" +
		"b<sup><i>m</i></sup>\".  The <i>y</i> value thus would be a multiple of \"a\".  " +
		"For any <i>i</i> "+ NOT_EQUAL +" 1, n<sub>a</sub> "+ NOT_EQUAL +" n<sub>b</sub>, giving a string " +
		"which is not in the language.  Thus, the language is not regular.";	
    }
    
    protected void chooseW() 
    {
        w = pumpString("a", m) + pumpString("b", m);
    }
    
    public void chooseI()
    {
        i = LemmaMath.flipCoin();
    }
    
    protected void setRange()
    {
        myRange = new int[]{4, 18};
    }
    
    public boolean isInLang(String s)
    {
    	int a, b;
    	char[] list = new char[]{'a','b'};
    	if (LemmaMath.isMixture(s, list))
    		return false;
    	
    	a = LemmaMath.countInstances(s, 'a');
    	b = LemmaMath.countInstances(s, 'b');
    	if (a==b)
    		return true;    	    	
        return false;
    }
}
