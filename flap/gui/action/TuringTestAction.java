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

import file.Codec;
import file.DataException;
import file.ParseException;
import gui.environment.Environment;
import gui.environment.EnvironmentFrame;
import gui.environment.FrameFactory;
import gui.environment.Universe;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import automata.Automaton;
import automata.turing.TuringMachine;

/**
* The <CODE>TestTuringAction</CODE> is an action to load a structure from a file,
* and create a new environment with that object.
* 
* @author Stephen Reading
*/

public class TuringTestAction extends TestAction {


    /**
     * Instantiates a new <CODE>Turing Test Action</CODE>.
     */
    public TuringTestAction() {
        //super("Test Turing Machines", null);
        super("Test Turing Machines", KeyEvent.VK_T);       
    }
    
    protected void displayMultipleRunPane(Environment env){
    		MultipleOutputSimulateAction act = new MultipleOutputSimulateAction((Automaton)myObjects.get(0),env);
    		act.performAction(env);
    		env.getTag(env.getActive());
    	}

    

}
