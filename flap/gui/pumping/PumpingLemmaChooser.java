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

import java.io.Serializable;
import java.util.ArrayList;

import pumping.PumpingLemma;

/**
 * A <code>PumpingLemmaChooser</code> holds a list of 
 * {@link pumping.PumpingLemma}s that allows the user to select 
 * which pumping lemma they want to work on.
 * 
 * @author Jinghui Lim
 *
 */
public abstract class PumpingLemmaChooser implements Serializable
{
    /**
     * The list of pumping lemmas
     */
    protected ArrayList myList;
    /**
     * The index of the current (or most recently opened) pumping lemma.
     */
    protected int myCurrent;
    
    /**
     * Resets the pumping lemma at index <code>i</code>.
     * 
     * @param i the index of the pumping lemma we wish to reset
     * @see pumping.PumpingLemma#clearDoneCases()
     */
    public void reset(int i)
    {
    	PumpingLemma pl = (PumpingLemma) myList.get(i);
        pl.clearDoneCases();
        pl.clearAttempts();
        pl.reset();
    }
    
    /**
     * Resets all the pumping lemmas.
     * 
     * @see #reset(int)
     */
    public void reset()
    {
        for(int i = 0; i < myList.size(); i++)
            reset(i);
    }
    
    /**
     * Returns the <code>PumpingLemma</code> at index <code>i</code>.
     * 
     * @param i the pumping lemma we wish to retrieve
     * @return the <code>PumpingLemma<code> at index <code>i</code>
     */
    public PumpingLemma get(int i)
    {
        return (PumpingLemma)myList.get(i);
    }
    
    /**
     * Returns the current (or most recently opened) pumping lemma.
     * 
     * @return the current (or most recently opened) pumping lemma
     */
    public PumpingLemma getCurrent()
    {
        return get(myCurrent);
    }
    
    /**
     * Sets the current pumping lemma.
     * 
     * @param i the index of the current pumping lemma
     */
    protected void setCurrent(int i)
    {
        myCurrent = i;
    }
    
    /**
     * Returns the total number of pumping lemmas.
     * 
     * @return the total number of pumping lemmas
     */
    public int size()
    {
        return myList.size();
    }
    
    /**
     * Replace a pumping lemma in the chooser with another of the same
     * class. The old pumping lemma of the same class will be removed
     * and the new pumping lemma will be added. This is mainly used for
     * loading.
     *  
     * @param pl the pumping lemma to be added
     */
    public void replace(PumpingLemma pl)
    {
        for(int i = 0; i < myList.size(); i++)
        {
            if(pl.getClass().equals(myList.get(i).getClass()))
            {
                myList.remove(i);
                myList.add(i, pl);
            }
        }
    }
}
