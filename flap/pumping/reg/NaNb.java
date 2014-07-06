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

import pumping.LemmaMath;
import pumping.RegularPumpingLemma;

/**
 * The regular pumping lemma for <i>L</i> =
 * {<i>w</i> &#8712; {<i>a</i>, <i>b</i>}* : <i>n<sub>a</sub></i> 
 * (<i>w</i>) &#60; <i>n<sub>b</sub></i> (<i>w</i>)}.
 * 
 * @author Jinghui Lim & Chris Morgan
 */
public class NaNb extends RegularPumpingLemma 
{
    public String getHTMLTitle() 
    {
        return "<i>w</i> " + ELEMENT_OF + " " + AB_STAR + " : <i>n<sub>a</sub></i> (<i>w</i>) " +
            LESS_THAN + " <i>n<sub>b</sub></i> (<i>w</i>)";
    }
	
	public String getTitle() 
    {
        return "w element_of {ab}* : na(w) < nb(w)";
    }

    public void setDescription()
    {
    	partitionIsValid = false;
    	explanation = "For any <i>m</i> value, a possible value for <i>w</i> is \"a<sup><i>m</i></sup>" +
			"b<sup><i>m</i>+1</sup>\".  The <i>y</i> value thus would be a multiple of \"a\".  " +
			"For any <i>i</i> " + GREATER_THAN + " 1, n<sub>a</sub> " + GREATER_OR_EQ + " n<sub>b</sub>, " +
			"giving a string which is not in the language.  Thus, the language is not regular.";
    }
    
    protected void chooseW() 
    {
        w = pumpString("a", getM()) + pumpString("b", getM() + 1);
    }

    public void chooseDecomposition() 
    {
    	setDecomposition(new int[] {Math.min(m-1, w.indexOf('b')), 1});
    }
    
    public void chooseI() 
    {
        i = 2;
    }
    
    protected void setRange()
    {
        myRange = new int[]{2, 17};
    }        
    
    public boolean isInLang(String s)
    {
    	int a, b;
    	char[] list = new char[]{'a', 'b'};
    	if (LemmaMath.otherCharactersFound(s, list))
    		return false;
    	
    	a = LemmaMath.countInstances(s, 'a');
    	b = LemmaMath.countInstances(s, 'b');    	
    	if (a < b)
    		return true;
        return false;
    }
}
