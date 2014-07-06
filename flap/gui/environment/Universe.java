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
 
package gui.environment;

import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 * The <CODE>Universe</CODE> class serves as a large global "registry"
 * for the active windows and their associated environments.
 * 
 * @author Thomas Finley
 */

public class Universe {
    /**
     * This class needn't have multiple instances, so we disable the
     * main constructor.
     */
    private Universe() { }

    /**
     * Returns the path for a file.  This attempts to retrieve the
     * canonical path, but if that fails (it shouldn't) returns the
     * absolute path
     * @param file the file to get the path for
     * @return the canonical path, or alternatively the absolute path
     */
    private static String getPath(File file) {
	try {
	    return file.getCanonicalPath();
	} catch (IOException e) {
	    return file.getAbsolutePath();
	}
    }

    /**
     * Registers an environment frame.
     * @param frame the environment frame to register
     * @return an integer for the number of frames that have been
     * registered sofar, including this one
     */
    public static int registerFrame(EnvironmentFrame frame) {
	Environment env = frame.getEnvironment();
	environmentToFrame.put(env, frame);
	File file = env.getFile();
	if (file != null) fileToFrame.put(getPath(file), frame);
	// Adds the listener that changes this object in the event
	// that the file of an environment changes.
	env.addFileChangeListener(FILE_LISTENER);
	// Hide the new dialog box.
	gui.action.NewAction.hideNew();
	
	return ++numberRegistered;
    }

    /**
     * Unregisters an environment frame.
     * @param frame the environment frame to unregister
     */
    public static void unregisterFrame(EnvironmentFrame frame) {
	try {
	    fileToFrame.remove(getPath(frame.getEnvironment().getFile()));
	} catch (NullPointerException e) {
	    // The environment doesn't have a file.
	}
	environmentToFrame.remove(frame.getEnvironment());

	// If there are no other frames open, prompt for newness.
	if (numberOfFrames() == 0)
	    gui.action.NewAction.showNew();
    }

    /**
     * Given a file, this returns the frame associated with that file.
     * @param file a file that may be an active file for some
     * environment
     * @return the environment frame associated with this file, or
     * <CODE>null</CODE> if there is no frame associated with this
     * file
     */
    public static EnvironmentFrame frameForFile(File file) {
	if (file == null) return null;
	return (EnvironmentFrame) fileToFrame.get(getPath(file));
    }

    /**
     * Given an environment, this returns the frame associated with
     * that environment.
     * @param environment an environment that may have some frame
     * @return the environment frame associated with this environment,
     * or <CODE>null</CODE> if there is no frame associated with this
     * environment
     */
    public static EnvironmentFrame frameForEnvironment
	(Environment environment) {
	return (EnvironmentFrame) environmentToFrame.get(environment);
    }

    /**
     * Returns a list of the registered environment frames.
     * @return an array containing all registered environment frames
     */
    public static EnvironmentFrame[] frames() {
	return (EnvironmentFrame[]) environmentToFrame.values()
	    .toArray(new EnvironmentFrame[0]);
    }

    /**
     * Returns the number of currently open frames that hold a
     * representation of a structure (i.e. automaton, grammar, or
     * regular expression).
     * @return the number of currently open frames
     */
    public static int numberOfFrames() {
	return environmentToFrame.size();
    }

    /** The mapping of environments to frames. */
    private static Map environmentToFrame = new HashMap();
    /** The mapping of files to frames. */
    private static Map fileToFrame = new HashMap();
    
    /** The universal JFileChooser. */
    public static JFileChooser CHOOSER = null;
    /** The number of frames that have been registered... this is used
     * to describe the untitled frames with something unique. */
    private static int numberRegistered = 0;
    /** This is the file listener that should be added to the
     * environments when their frames are created to ensure that no
     * file is opened twice. */
    private static FileChangeListener FILE_LISTENER=new FileChangeListener() {
	    public void fileChanged(FileChangeEvent e) {
		// We must update the index.
		File oldFile = e.getOldFile();
		EnvironmentFrame frame =
		    frameForEnvironment((Environment)e.getSource());
		if (oldFile != null)
		    fileToFrame.remove(getPath(oldFile));
		Environment env = (Environment) e.getSource();
		File newFile = env.getFile();
		if (newFile == null) return;
		fileToFrame.put(getPath(newFile), frame);
	    }
	};

    static {
	try {
	    CHOOSER = new JFileChooser();
	} catch (java.security.AccessControlException e) {
	    // Nothing to do.
	}
    }
}
