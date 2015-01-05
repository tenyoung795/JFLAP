/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





import gui.Main;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * This allows one to run JFLAP as an applet. All it actually does is display a
 * small message, and runs the application normally.
 * 
 * @author Thomas Finley
 */

public class JFLAPplet extends JApplet {
	/**
	 * This instantiates a new JFLAPplet.
	 */
	public JFLAPplet() {
		getRootPane().putClientProperty("defeatSystemEventQueueCheck",
				Boolean.TRUE);
	}

	/**
	 * This will modify the applet display to show a short message, and then
	 * call the <CODE>JFLAP</CODE> class's main method so the program can run
	 * as normal.
	 */
	public void init() {
		// Show the message.
        try {
            SwingUtilities.invokeAndWait(() -> {
                JTextArea text = new JTextArea("Welcome to JFLAP "
                    + gui.AboutBox.VERSION + "!\n"
                    + "Report bugs to rodger@cs.duke.edu!");
                text.setEditable(false);
                text.setWrapStyleWord(true);
                JScrollPane scroller = new JScrollPane(text,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                getContentPane().add(text, BorderLayout.CENTER);
                // Start the application.
                myBase = this.getCodeBase();
                Main.main(new String[0], false);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            throw new AssertionError(cause);
        }
    }
	
	public static URL myBase = null;
}
