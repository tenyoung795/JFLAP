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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import automata.Automaton;
import automata.State;

import gui.editor.EditBlockPane;
import gui.editor.EditorPane;
import gui.environment.Environment;
import gui.environment.tag.PermanentTag;
import gui.environment.tag.Tag;

/**
 * The <code>CloseButton</code> is a button for removing tabs in
 * an environment. It automatically detects changes in the activation 
 * of panes in the environment, and changes its enabledness whether 
 * a pane in the environment is permanent (i.e. should not be closed).
 * 
 * @see CloseAction
 * @author Jinghui Lim
 *
 */
public class CloseButton extends javax.swing.JButton 
{
    /**
     * The environment to handle closing tabs for.
     */
    private Environment env;
    
    /**
     * Instantiates a <code>CloseButton</code>, and sets its values
     * with {@link #setDefaults()}.
     * 
     * @param environment the environment to handle the closing for
     */
    public CloseButton(Environment environment) 
    {
        super();
        setDefaults();
        env = environment;
        env.addChangeListener(new ChangeListener() 
            {
                public void stateChanged(ChangeEvent e) 
                { 
                    checkEnabled(); 
                }
            });
        addActionListener(new ActionListener()
            {
               /* public void actionPerformed(ActionEvent arg0) 
               {
                   env.remove(env.getActive());
               }*/

                public void actionPerformed(ActionEvent e) 
                {
                    boolean editor = false;
                    Automaton inside = null;
                    State block = null;
                    if(env.getActive() instanceof EditBlockPane)
                    {
                        editor = true;
                        EditBlockPane blockEditor = (EditBlockPane) env.getActive();
                        inside = blockEditor.getAutomaton();
                        block = blockEditor.getBlock();
                    }
                    env.remove(env.getActive());
                    if(editor) 
                    {
                        EditorPane higherEditor = (EditorPane) env.getActive();
                        Automaton higher = higherEditor.getAutomaton();
                        higher.replaceBlock(block, inside);
                    }
                }
            });
        checkEnabled();
    }
    
    /**
     * A convenience method that sets the button with certian values. 
     * The icon, size, and tooltip are set.
     *
     */
    public void setDefaults() 
    {
        setIcon(new ImageIcon(getClass().getResource("/ICON/x.gif")));
        setPreferredSize(new Dimension(22, 22));
        setToolTipText("Dismiss Tab");
    }

    /**
     * Checks the environment to see if the currently active object
     * has the <CODE>PermanentTag</CODE> associated with it, and if it
     * does, disables this action; otherwise it makes it activate.
     */
    private void checkEnabled() 
    {
        Tag tag = env.getTag(env.getActive());
//        setEnabled(!(tag instanceof PermanentTag));
        if(env.tabbed.getTabCount() == 1)
            setEnabled(false);
        else 
            setEnabled(!(tag instanceof PermanentTag));
    }
}
