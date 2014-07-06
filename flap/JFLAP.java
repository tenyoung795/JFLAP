/* -- JFLAP 6.3 --
 *
 * Copyright information:
 *
 * Susan H. Rodger, Thomas Finley
 * Computer Science Department
 * Duke University
 * May 23, 2008
 * Supported by National Science Foundation DUE-9752583.
 *
 * Copyright (c) 2008
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

import file.Codec;
import file.ParseException;
import file.xml.Transducer;
import file.xml.TransducerFactory;
import gui.action.NewAction;
import gui.action.OpenAction;
import gui.environment.Profile;
import gui.environment.Universe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is the class that starts JFLAP.
 * 
 * @author Thomas Finley
 * @author Moti Ben-Ari
 *   All code moved to gui.Main
 *   Parameter dontQuit false for command line invocation
 */

public class JFLAP {
	public static void main(String[] args) {
		gui.Main.main(args, false);
	}
}
