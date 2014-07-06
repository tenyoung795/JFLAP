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
 
import gui.action.NewAction;
import java.security.*;

/**
 * This is the class that starts JFLAP.
 * 
 * @author Thomas Finley
 */

public class JFLAP {
    /**
     * This sets various system properties before calling on 
     * {@link gui.action.NewAction#showNew}.
     * @param args the command line arguments, which at this time are
     * ignored
     */
    public static void main(String[] args) {
	// Apple is stupid.
	try {
	    // Well, Apple was stupid...
	    if (System.getProperty("os.name").startsWith("Mac OS") &&
		System.getProperty("java.specification.version").equals("1.3"))
	        System.setProperty("com.apple.hwaccel", "false");
	} catch (SecurityException e) {
	    // Bleh.
	}
	// Sun is stupider.
	try {
	    System.setProperty("java.util.prefs.syncInterval","2000000");
	} catch (SecurityException e) {
	    // Well, not key.
	}
	// Prompt the user for newness.
	NewAction.showNew();
    }
}
