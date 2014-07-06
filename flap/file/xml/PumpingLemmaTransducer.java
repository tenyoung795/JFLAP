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

import java.io.Serializable;

import org.w3c.dom.Document;

/**
 * This is an abstract implementation of a transducer for
 * {@link pumping.PumpingLemma} objects.
 * 
 * @author Jinghui Lim
 * @see gui.pumping.PumpingLemmaChooser
 *
 */
public abstract class PumpingLemmaTransducer extends AbstractTransducer 
{
    /**
     * The tag for the name of the pumping lemma.
     */
    public static String LEMMA_NAME = "name";
    /**
     * The tag for who goes first.
     */
    public static String FIRST_PLAYER = "first_player";
    /**
     * The tag for the <i>m</i> value of the pumping lemma.
     */
    public static String M_NAME = "m";
    /**
     * The tag for the <i>w</i> value of the pumping lemma.
     */
    public static String W_NAME = "w";
    /**
     * The tag for the <i>i</i> value of the pumping lemma.
     */
    public static String I_NAME = "i";
    /**
     * The tag for a representation of a prior attempt.
     */
    public static String ATTEMPT = "attempt";
    /**
     * The comment for <i>m</i>.
     */
    public static String COMMENT_M = "The user's input of m.";
    /**
     * The comment for <i>i</i>. The value of <i>i</i> is needed because
     * it is sometimes randomized.
     */
    public static String COMMENT_I = "The program's value of i.";
}
