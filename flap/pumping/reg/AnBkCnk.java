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
 * {<i>a<sup>n</sup>b<sup>k</sup>c<sup>n+k</sup></i> : 
 * <i>n</i> &#8805; 0, <i>k</i> &#8805; 0}.
 * 
 * @author Jinghui Lim & Chris Morgan
 *
 */
public class AnBkCnk extends RegularPumpingLemma 
{
    public String getTitle() 
    {
        return "a^n b^k c^(n+k) : n >= 0, k >= 0";
    }

    public String getHTMLTitle() 
    {
        return "<i>a<sup>n</sup>b<sup>k</sup>c<sup>n+k</sup></i> : <i>n</i> " +
            GREATER_OR_EQ + " 0, <i>k</i> " + GREATER_OR_EQ + " 0";
    }
    
    public void setDescription()
    {
    	partitionIsValid = false;
    	explanation = "For any <i>m</i> value, a possible value for <i>w</i> is \"a<sup><i>m</i></sup>" +
			"b<sup><i>m</i></sup>c<sup>2<i>m</i></sup>\".  The <i>y</i> value thus would be a multiple of \"a\".  " +
			"If <i>i</i> = 0, the string becomes at most \"a<sup><i>m</i>-1</sup>b<sup><i>m</i></sup>" +
			"c<sup>2<i>m</i></sup>\", which is not in the language.  Thus, the language is not regular.";
    }

    protected void setRange()
    {
        myRange = new int[]{2, 9};
    }

    protected void chooseW() 
    {
        w = pumpString("a", getM()) + pumpString("b", getM()) + pumpString("c", getM() * 2);
    }
    
    public void chooseI() 
    {
        i = LemmaMath.flipCoin();
    }
    
    public boolean isInLang(String s)
    {
    	int a, b, c;
    	char[] list = new char[]{'a','b','c'};
    	if (LemmaMath.isMixture(s, list))
    		return false;
    	
    	a = LemmaMath.countInstances(s, 'a');
    	b = LemmaMath.countInstances(s, 'b');
    	c = LemmaMath.countInstances(s, 'c');
    	if (a+b == c)
    		return true;    	    	
    	return false;
    }
}
