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
 * {<i>a<sup>n</sup>b<sup>l</sup>a<sup>k</sup></i> : <i>n</i> 
 * &#62; 5, <i>l</i> &#62; 3, <i>k</i> &#8804; <i>l</i>}.
 * 
 * @author Jinghui Lim & Chris Morgan
 *
 */
public class AnBlAk extends RegularPumpingLemma 
{
    public String getTitle() 
    {
        return "a^n b^l a^k : n > 5, l > 3, k <= l";
    }

    public String getHTMLTitle() 
    {
        return "<i>a<sup>n</sup>b<sup>l</sup>a<sup>k</sup></i> : <i>n</i> " + GREATER_THAN +
            " 5, <i>l</i> " + GREATER_THAN + " 3, <i>k</i> " + LESS_OR_EQ + " <i>l</i>";
    }

    public void setDescription()
    {
    	partitionIsValid = false;
    	explanation = "For any <i>m</i> value " + GREATER_OR_EQ + " 4, a possible value for <i>w</i> is \"a<sup>6</sup>" +
			"b<sup><i>m</i></sup>a<sup><i>m</i></sup>\".  The <i>y</i> value thus would be a combination of \"a\"s " +
			"and \"b\"s, in that order.  If <i>i</i> = 0, either n " + LESS_OR_EQ + " 5, k > l, or both, giving a " +
			"string that is not in the language.  Thus, the language is not regular.";
    }
    
    protected void chooseW() 
    {
        if(getM() <= 3)
            w = pumpString("a", 6) + pumpString("b", 4) + pumpString("a", 4);
        else
            w = pumpString("a", 6) + pumpString("b", getM()) + pumpString("a", getM());
    }
    
    public void chooseDecomposition()
    {
    	int b = w.indexOf('b');
    	if (b > 6)
    		setDecomposition(new int[] {0, 1});
    	else
    		setDecomposition(new int[] {Math.min(b, m-1), 1});
    }

    public void chooseI() 
    {
        i = 0;
    }
    
    protected void setRange()
    {
        myRange = new int[]{2, 15};
    }    
    
    public boolean isInLang(String s)
    { 
    	int a, b, a2;
    	char[] list = new char[]{'a','b','a'};
    	if (LemmaMath.isMixture(s, list))
    		return false;
    	
    	b = LemmaMath.countInstances(s, 'b');
    	if (b==0)
    		return false;
    	
    	String ba2 = s.substring(s.indexOf('b'));  //deletes the a^n portion of the string
    	a = s.length() - ba2.length();    	
    	a2 = LemmaMath.countInstances(ba2, 'a');
    	if (a <= 5 || b <= 3 || a2 > b)    		
    		return false;    	    	
        return true;
    }
}
