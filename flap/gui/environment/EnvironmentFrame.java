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

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The <CODE>EnvironmentFrame</CODE> is the general sort of frame for
 * holding an environment.
 * 
 * @author Thomas Finley
 */

public class EnvironmentFrame extends JFrame {
    /**
     * Instantiates a new <CODE>EnvironmentFrame</CODE>.  This does
     * not fill the environment with anything.
     * @param environment the environment that the frame is created for
     */
    public EnvironmentFrame(Environment environment) {
	this.environment = environment;
	environment.addFileChangeListener(new FileChangeListener() {
		public void fileChanged(FileChangeEvent e) {refreshTitle();}
	    });
	initMenuBar();
	this.getContentPane().setLayout(new BorderLayout());
	this.getContentPane().add(environment, BorderLayout.CENTER);

	// Register this frame with the universe.
	myNumber = Universe.registerFrame(this);
	refreshTitle();

	this.addWindowListener(new Listener());
	this.setLocation(50,50);
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /**
     * Returns a simple identifying string for this frame.
     * @return a simple string that identifies this frame
     */
    public String getDescription() {
	if (environment.getFile() == null) return "<untitled"+myNumber+">";
	else return "("+environment.getFile().getName()+")";
    }
    
    /**
     * Sets the title on this frame to be the name of the file for the
     * environment, or untitled if there is no file for this
     * environment yet.
     */
    protected void refreshTitle() {
	String title = DEFAULT_TITLE + " : " + getDescription();
	setTitle(title);
    }

    /**
     * Initializes the menu bar for this frame.
     */
    protected void initMenuBar() {
	this.setJMenuBar(gui.menu.MenuBarCreator.getMenuBar(this));
    }

    /**
     * Returns the environment for this frame.
     * @return the environment for this frame
     */
    public Environment getEnvironment() {
	return environment;
    }

    /**
     * Saves the environment's object to a file.  This serializes the
     * object found in the environment, and then writes it to the
     * file of the environment.
     * @param saveAs if <CODE>true</CODE> this will prompt the user
     * with a save file dialog box no matter what, otherwise the user
     * will only be prompted if the environment has no file
     * @return <CODE>true</CODE> if the save was a success,
     * <CODE>false</CODE> if it was not
     */
    public boolean save(boolean saveAs) {
	File file = saveAs ? null : environment.getFile();
	while (file == null) {
	    int result = Universe.CHOOSER.showSaveDialog(this);
	    if (result != JFileChooser.APPROVE_OPTION)
		return false;
	    file = Universe.CHOOSER.getSelectedFile();
	    if (file.exists()) {
		result = JOptionPane.showConfirmDialog
		    (this, "Overwrite "+file.getName()+"?");
		switch (result) {
		case JOptionPane.CANCEL_OPTION:
		    return false;
		case JOptionPane.NO_OPTION:
		    file = null;
		    continue;
		default:
		}
	    }
	}

	try {
	    ObjectOutputStream stream = new ObjectOutputStream
		(new BufferedOutputStream
		 (new FileOutputStream(file)));
	    stream.writeObject(environment.getObject());
	    stream.flush();
	    stream.close();
	    environment.setFile(file);
	    environment.clearDirty();
	    return true;
	} catch (NotSerializableException e) {
	    System.err.println("Could not serialize object.");
	    System.err.println(e);
	    return false;
	} catch (IOException e) {
	    JOptionPane
		.showMessageDialog(this, "Could not open file to write!",
				   "IO Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
    }

    /**
     * Attempts to close an environment frame.
     * @return <CODE>true</CODE> if the window was successfully
     * closed, <CODE>false</CODE> if the window could not be closed at
     * this time (probably user intervention)
     */
    public boolean close() {
	if (environment.isDirty()) {
	    File file = environment.getFile();
	    String title;
	    if (file == null) title = "untitled";
	    else title = file.getName();

	    int result =
		JOptionPane.showConfirmDialog(this, "Save "+title+
					      " before closing?");
	    if (result == JOptionPane.CANCEL_OPTION) return false;
	    if (result == JOptionPane.YES_OPTION) save(false);
	}
	dispose();
	Universe.unregisterFrame(this);
	return true;
    }

    /**
     * Returns the string that describes this frame.
     * @return the string that describes this frame
     */
    public String toString() {
	return getDescription();
    }

    /** The environment that this frame displays. */
    private Environment environment;
    /** The number of this environment frames. */
    private int myNumber = 0xdeadbeef;
    /** The default title for these frames. */
    private static final String DEFAULT_TITLE = "JFLAP";

    /**
     * The window listener for this frame.
     */
    private class Listener extends WindowAdapter {
	public void windowClosing(WindowEvent event) {
	    close();
	}
    }
}
