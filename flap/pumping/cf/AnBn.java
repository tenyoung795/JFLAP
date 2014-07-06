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

package pumping.cf;

import pumping.Case;
import pumping.ContextFreePumpingLemma;
import pumping.LemmaMath;

/**
 * The context-free pumping lemam for <i>L</i> = 
 * {<i>a<sup>n</sup>b<sup>n</sup></i> : <i>n</i> &#8805; 0}.
 * 
 * @author Jinghui Lim & Chris Morgan
 *
 */
public class AnBn extends ContextFreePumpingLemma 
{
    public String getTitle() 
    {
        return "a^n b^n : n >= 0";
    }

    public String getHTMLTitle() 
    {
        return "<i>a<sup>n</sup>b<sup>n</sup></i> : <i>n</i> " + GREATER_OR_EQ + " 0";
    }

    protected void chooseW() 
    {
        w = pumpString("a", m) + pumpString("b", m);
    }
    
    public void setDescription()
    {
    	partitionIsValid = true;
    	explanation = "Because this is a context-free language, a valid decomposition exists.  If <i>m</i> " + GREATER_OR_EQ + 
    			" 2, one could choose <i>v</i> to be \"a\" and <i>y</i> to be \"b\", which will work for all values of " +
    			"<i>i</i>.";			
    }

    public void chooseI() 
    {
        i = 2;
    }

    protected void addCases() 
    {
        /*
         * v is string of a's and y is string of a's
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.indexOf("a") > -1 && v.indexOf("b") == -1 &&
                        y.indexOf("a") > -1 && y.indexOf("b") == -1)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a string of \"a\"s and y is a string of \"a\"s";
                }
                
                public int[] getPreset()
                {
                    return new int[]{0, 1, 0, 1};                    
                }
            });
        /*
         * v is string of a's and y is string of a's followed by b's
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.indexOf("a") > -1 && v.indexOf("b") == -1 &&
                        y.indexOf("a") > -1 && y.indexOf("b") > -1)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a string of \"a\"s and y is a string of \"a\"s followed by \"b\"s";
                }
                
                public int[] getPreset()
                {
                    return new int[]{m - 2, 1, 0, 2};
                }
            });
        /*
         * v is string of a's and y is string of b's
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.indexOf("a") > -1 && v.indexOf("b") == -1 && 
                        y.indexOf("a") == -1 && y.indexOf("b") > -1)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a string of \"a\"s and y is a string of \"b\"s";
                }
                
                public int[] getPreset()
                {
                    return new int[]{1, m - 1, 0, 1};
                }
            });
        /*
         * v is string of a's followed by b's and y is string of b's
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.indexOf("a") > -1 && v.indexOf("b") > -1 &&
                        y.indexOf("a") == -1 && y.indexOf("b") > -1)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a string of \"a\"s followed by \"b\"s and y is a string of \"b\"s";
                }
                
                public int[] getPreset()
                {
                    return new int[]{m - 1, 2, 0, 1};
                }
            });
        /*
         * v is a string of b's and y is a string of b's
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.indexOf("a") == -1 && v.indexOf("b") > -1 &&
                        y.indexOf("a") == -1 && y.indexOf("b") > -1)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a string of \"b\"s and y is a string of \"b\"s";
                }
                
                public int[] getPreset()
                {
                    return new int[]{m, 1, 0, 1};
                }
            });
        /*
         * v is an empty string and y is a non-empty string
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.length() == 0 && y.length() > 0)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is an empty string and y is a non-empty string";
                }
                
                public int[] getPreset()
                {
                    return new int[]{m, 0, 1, 1};
                }
            });
        /*
         * v is a non-empty string and y is an empty string
         */
        myAllCases.add(new Case()
            {
                public boolean isCase(String v, String y) 
                {
                    if(v.length() > 0 && y.length() == 0)
                        return true;
                    return false;
                }

                public String description() 
                {
                    return "v is a non-empty string and y is an empty string";
                }
                
                public int[] getPreset()
                {
                    return new int[]{m, 1, 0, 0};
                }
            });
    }

    protected void setRange() 
    {
        myRange = new int[]{4, 11};
    }
    
    public void chooseDecomposition()
	{
		//The last character in a^n and the first in b^n
		setDecomposition(new int[] {w.length()/2-1, 1, 0, 1});
	}
    
    /**
     * Checks if the pumped string is in the language.
     * 
     * @return <code>true</code> if it is, <code>false</code> otherwise
     */
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
