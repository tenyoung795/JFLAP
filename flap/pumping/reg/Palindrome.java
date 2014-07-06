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
 * {<i>ww<sup>R</sup></i> : <i>w</i> &#8712; {<i>a</i>, <i>b</i>}*}.
 * 
 * @author Jinghui Lim & Chris Morgan
 */
public class Palindrome extends RegularPumpingLemma 
{
    public String getTitle() 
    {
        return "w w^R : w element_of {ab}*";
    }

    public String getHTMLTitle() 
    {
        return "<i>ww<sup>R</sup></i> : <i>w</i> " + ELEMENT_OF + " " + AB_STAR;
    }
    
    public void setDescription()
    {
    	partitionIsValid = false;
    	explanation = "For any <i>m</i> value, a possible value for <i>w</i> is \"a<sup><i>m</i></sup>bb" +
		"a<sup><i>m</i></sup>\".  The <i>y</i> value thus would be a multiple of \"a\" in 'w' and not in " +
		"'w<sup>R</sup>'.  If <i>i</i> = 0, then the total string becomes at most \"a<sup><i>m</i>-1</sup>bb" +
		"a<sup><i>m</i></sup>\", which is not in the language.  Thus, the language is not regular.";	
    }
    
    protected void chooseW() 
    {
        w = pumpString("a", m) + "bb" + pumpString("a", m);
    }
    
    public void chooseDecomposition() 
    {
    	setDecomposition(new int[] {Math.min(w.length()/2-1, m-2), 2});
    }

    public void chooseI() 
    {
        i = LemmaMath.flipCoin();
    }    
    
    protected void setRange()
    {
        myRange = new int[]{2, 10};
    }
        
    public boolean isInLang(String s)
    {
    	int size = s.length();
    	if (size == 0)
    		return true;
    	if (size % 2 == 1)
    		return false;
    	int halfSize = size / 2;  //works for odd or even lengths
    	char[] list = new char[]{'a', 'b'};
    	
    	if (LemmaMath.otherCharactersFound(s, list))
    		return false;    	
    	for (int i=0; i<=halfSize; i++) 
    		if (s.charAt(i) != s.charAt(size-i-1))
    			return false;
    	return true;
    }
}
