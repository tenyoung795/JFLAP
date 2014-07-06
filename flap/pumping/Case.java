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

package pumping;

/**
 * A <code>Case</code> is an object that lets the user or program
 * know if certain cases had been done, depending on the conditions 
 * of the case, specified in {@link #isCase(String, String)}. 
 * 
 * It is the programmer's responsibility to write individual cases
 * for each class of {@link pumping.PumpingLemma}.
 * 
 * @author Jinghui Lim
 *
 */
public abstract class Case 
{
    /**
     * The <i>i</i> of this case.
     */
    protected int i;
    
    /**
     * Previously entered user input. It is <code>null</code> if the 
     * user has not entered any input.
     */
    protected int[] userInput;
    
    /**
     * Constructs a new case with no user input.
     *
     */    
    
    public Case()
    {
        reset();
    }
    
    /**
     * Determines if a certain decomposition is an example of this case. 
     * For a {@link RegularPumpingLemma}, both <code>v</code> and <code>y</code> 
     * should hold the <i>y</i> segment of the decomposition. For a 
     * {@link ContextFreePumpingLemma}, <code>v</code> and <code>y</code> 
     * should hold their respective segments of the decomposition.
     * 
     * @param v the <i>v</i> segment of string <i>w</i>
     * @param y the <i>y</i> segment of string <i>w</i>
     * @return <code>true</code> if this decomposition is an example
     * of this case, <code>false</code> otherwise
     */
    public abstract boolean isCase(String v, String y);
    
    
    /**
     * Given a <i>w</i> value, returns in an int[] the dividers separating
     * u, v, x, y, & z
     * Used by the computer when it goes first. 
     *
    public int[] chooseDecompositionOfCase(String s) 
    {
    	int size = s.length();
    	for (int w=0; w<s.length(); w++)
    		for (int x=w; x<s.length(); x++)
    			for (int y=x; y<s.length(); y++)
    				for (int z=y; z<s.length(); z++)
    					if (isCase(s.substring(w, x), s.substring(y, z)))
    						return new int[] {size - w - x - y - z,
    										  size - x - y - z,
    										  size - y - z,
    										  size - z}; 
    	return null; //shouldn't happen
    }*/
    
    /**
     * Returns a short description of this case.
     * 
     * @return a short description of this case
     */
    public abstract String description(); 
    
    /**
     * Returns a string representing of this case. Equivalent to calling
     * {@link #description()}.
     * 
     * @return a string representing this case
     */
    public String toString()
    {
        return description();
    }
    
    /**
     * Checks if two cases are equivalent. In this case, it merely 
     * compares their descriptions, returned by {@link #description()} 
     * or {@link #toString()} and returns <code>true</code> if they 
     * are the the same, and <code>false</code> otherwise. Thus,
     * <code>Case</code>s should not be compared between two different
     * pumping lemmas.
     * 
     * @return <code>true</code> if the two cases are equivalent,
     * <code>false</code> otherwise
     */
    public boolean equals(Object o)
    {
        try
        {
            return toString().equals(((Case)o).toString());
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }
    
    /**
     * Get a preset decomposition of this case. In most subclasses of
     * {@link pumping.Case}, this method should have access to <i>m</i>
     * of the {@link pumping.PumpingLemma} either as an internal
     * class or through {@link PumpingLemma#getM()}.
     * 
     * @return a preset decomposition of this case
     */
    public abstract int[] getPreset();
    
    /**
     * Returns a decomposition that suits this case, a previously set
     * user decomposition (set by {@link #setUserInput(int[])}) or if
     * that has not been done, the preset decomposition we would get 
     * if we called {@link #getPreset()}.
     * 
     * @return a previously set user decomposition or a preset decomposition
     */
    public int[] getInput()
    {
        if(userInput == null)
            return getPreset();
        else
            return userInput;
    }
    
    /**
     * Sets the user's decomposition. This will subsequently be retrieved
     * when {@link #getInput()} is called.
     * 
     * @param n the user's decomposition of <i>w</i>
     */
    public void setUserInput(int[] n)
    {
        userInput = n;
    }
    
    /**
     * Sets the <i>i</i> of this case.
     * 
     * @param num the number to set <i>i</i> to
     */
    public void setI(int num)
    {
        i = num;
    }
    
    /**
     * Returns the <i>i</i> of this case.
     * 
     * @return the <i>i</i> of this case
     */
    public int getI()
    {
        return i;
    }
    
    /**
     * Resets the case, clearing user input and <i>i</i>.
     *
     */
    public void reset()
    {
        userInput = null;
        i = -1;
    }
}
