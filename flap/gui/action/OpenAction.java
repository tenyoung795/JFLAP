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
 
package gui.action;

import java.io.*;
import gui.environment.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;

/**
 * The <CODE>OpenAction</CODE> is an action to load a serializable
 * object from a file, and create a new environment with that object.
 * 
 * @author Thomas Finley
 */

public class OpenAction extends RestrictedAction {
    /**
     * Instantiates a new <CODE>OpenAction</CODE>.
     */
    public OpenAction() {
	super("Open...", null);
	putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke
		 (KeyEvent.VK_O, MAIN_MENU_MASK));
	this.fileChooser = Universe.CHOOSER;
    }

    /**
     * This reads the serializable object from the indicated file, and
     * returns it
     * @param file the file to retrieve the object from
     * @param parent the parent component for this open action so we
     * know where to display error messages, or <CODE>null</CODE> if
     * there is no parent
     * @return the object read from the file, or <CODE>null</CODE> if
     * an object could not be read
     */
    protected Serializable open(File file, Component parent) {
	Serializable object = null;
        try {
            ObjectInputStream stream = new ObjectInputStream
                (new BufferedInputStream
                 (new FileInputStream(file)));
            object = (Serializable) stream.readObject();
            stream.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
					  "Could not open file to read!",
                                          "IO Error",
					  JOptionPane.ERROR_MESSAGE);
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(parent, "Bad Class Read!",
                                          "Class Read Error",
                                          JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent, "Unrecognized Class Read!",
                                          "Class Read Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
	return object;
    }


    /**
     * If an open is attempted, call the methods that handle the
     * retrieving of an object, then create a new frame for the
     * environment.
     * @param event the action event
     */
    public void actionPerformed(ActionEvent event) {
	Component source = null;
	try {
	    source = (Component) event.getSource();
	} catch (Throwable e) {
	    // Might not be a component, or the event may be null.
	    // Who cares.
	}

	int result = fileChooser.showOpenDialog(source);
	if (result != JFileChooser.APPROVE_OPTION) return;
	File file = fileChooser.getSelectedFile();

	// Is this file already open?
	if (Universe.frameForFile(file) != null) {
	    Universe.frameForFile(file).toFront();
	    return;
	}

	Serializable object = open(fileChooser.getSelectedFile(), source);
	if (object == null) return; // Something bad happened...
	// Set the file on the thing.
	EnvironmentFrame ef = FrameFactory.createFrame(object);
	if (ef == null) return;
	ef.getEnvironment().setFile(fileChooser.getSelectedFile());
    }

    /**
     * The open action is completely environment independent.
     * @param object some object, which is ignored
     * @return always returns <CODE>true</CODE>
     */
    public static boolean isApplicable(Object object) {
	return true;
    }

    /** The file chooser. */
    private JFileChooser fileChooser; 
}
