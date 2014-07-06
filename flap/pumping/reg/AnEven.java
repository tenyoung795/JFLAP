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
 * The regular pumping lemma for <i>L</i> = {<i>a<sup>n</sup></i> 
 * : <i>n</i> is even}.
 * 
 * @author Jinghui Lim & Chris Morgan
 */
public class AnEven extends RegularPumpingLemma 
{
    public String getTitle() 
    {
        return "a^n : n is even";
    }

    public String getHTMLTitle() 
    {
        return "<i>a<sup>n</sup></i> : <i>n</i> is even";
    }
    
    public void setDescription()
    {
    	partitionIsValid = true;
    	explanation = "Because this is a regular language, a valid decomposition exists.  If <i>m</i> " + GREATER_OR_EQ +" 2, " +
    			"the <i>y</i> value \"aa\" will always pump the string.";
    }

    protected void chooseW() 
    {
        if(getM() % 2 == 0)
            w = pumpString("a", getM());
        else
            w = pumpString("a", getM() + 1);
    }

    public void chooseI() 
    {
        i = LemmaMath.flipCoin();
    }

    protected void setRange() 
    {
        myRange = new int[]{2, 18};
    }
    
    public void chooseDecomposition()
    {
    	setDecomposition(new int[] {0, 2});
    }
    
    /**
     * Checks if the pumped string is in the language.
     * 
     * @return <code>true</code> if it is, <code>false</code> otherwise
     */
    public boolean isInLang(String s)
    {
    	String temp;
    	char[] list = new char[]{'a'};
    	if (LemmaMath.otherCharactersFound(s, list))
    		return false;
    	
    	if (s.length() == 0)
    		temp = createPumpedString();
    	else
    		temp = s;    	    	
	    return temp.length() % 2 == 0;
    }
}
