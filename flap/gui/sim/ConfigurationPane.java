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
 
package gui.sim;

import automata.Automaton;
import automata.Configuration;
import automata.AutomatonSimulator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * The <CODE>ConfigurationPane</CODE> is the pane where the
 * configurations are displayed and selected.
 * 
 * @see automata.Configuration
 * 
 * @author Thomas Finley
 */

public class ConfigurationPane extends JPanel implements ActionListener {
    /**
     * Creates a <CODE>ConfigurationPane</CODE>.  The instance as
     * created has no configurations loaded into it yet.
     * @param automaton the automaton that configurations will come
     * from
     */
    public ConfigurationPane(Automaton automaton) {
	this.automaton = automaton;
	//this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Adds a configuration to the configuration pane.
     * @param configuration the configuration to add
     */
    public void add(Configuration configuration) {
	add(configuration, ConfigurationButton.NORMAL);
    }

    /**
     * Adds configurations with the given state.
     * @param configuration the configuration to add
q     * @param state the state of the configuration, either NORMAL,
     * ACCEPT, REJECT, or FREEZE
     */
    public void add(Configuration configuration, int state) {
	if (contains(configuration)) return;
	ConfigurationButton button =
	    new ConfigurationButton(configuration, state);
	configurationToButtonMap.put(configuration, button);
	add(button);
	button.addActionListener(this);
    }

    /**
     * Given a configuration, returns the state for that
     * configuration.
     * @param configuration the configuration
     * @return the state for that configuration
     */
    public int getState(Configuration configuration) {
	return ((ConfigurationButton)configurationToButtonMap
		.get(configuration)).state;
    }

    /**
     * Determines if this pane already contains this transition.
     * @param transition the transition to test for membership
     * @return <CODE>true</CODE> if the pane holds this transition,
     * <CODE>false</CODE> if it does not
     */
    public boolean contains(Configuration configuration) {
	return configurationToButtonMap.containsKey(configuration);
    }

    /**
     * Sets a configuration to be a reject configuration.
     * @param configuration the configuration to set to be a reject
     * configuration
     */
    public void setReject(Configuration configuration) {
	ConfigurationButton button =
	    (ConfigurationButton) configurationToButtonMap.get(configuration);
	if (button == null) return;
	if (button.state == ConfigurationButton.NORMAL)
	    button.setState(ConfigurationButton.REJECT);
    }

    /**
     * Sets a configuration to be frozen.  Only normal configurations
     * can be frozen.
     * @param configuration the configuration to freeze
     */
    public void setFrozen(Configuration configuration) {
	ConfigurationButton button =
	    (ConfigurationButton) configurationToButtonMap.get(configuration);
	if (button == null) return;
	if (button.state == ConfigurationButton.NORMAL)
	    button.setState(ConfigurationButton.FREEZE);
    }

    /**
     * Sets a configuration to be normal.  Only frozen configuratinos
     * can be made normal through this method.
     * @param configuration the configuration to thaw
     */
    public void setNormal(Configuration configuration) {
	ConfigurationButton button =
	    (ConfigurationButton) configurationToButtonMap.get(configuration);
	if (button == null) return;
	if (button.state == ConfigurationButton.FREEZE)
	    button.setState(ConfigurationButton.NORMAL);
    }
    

    /**
     * Removes a configuration from the configuration pane.
     * @param configuration the configuration to remove
     */
    public void remove(Configuration configuration) {
	Component comp =
	    (Component) configurationToButtonMap.remove(configuration);
	if (comp == null) return;
	selected.remove(configuration);
	remove(comp);
    }

    /**
     * Removes all configurations from this pane.
     */
    public void clear() {
	configurationToButtonMap.clear();
	selected.clear();
	super.removeAll();
    }

    /**
     * Renders all components deselected.
     */
    public void deselectAll() {
	selected.clear();
    }
    
    /**
     * Returns an array of selected configurations.
     * @return an array of selected configurations
     */
    public Configuration[] getSelected() {
	return (Configuration[]) selected.toArray(new Configuration[0]);
    }

    /**
     * Returns an array of all configurations.
     * @return an array of all configurations
     */
    public Configuration[] getConfigurations() {
	return (Configuration[]) configurationToButtonMap
	    .keySet().toArray(new Configuration[0]);
    }

    /**
     * Returns an array of configurations that are, as far as is
     * known, valid configurations for moving to other configurations.
     * @return an array of "valid" configurations
     */
    public Configuration[] getValidConfigurations() {
	// A state is valid for return if it is normal.
	ArrayList list = new ArrayList();
	Iterator it = configurationToButtonMap.values().iterator();
	while (it.hasNext()) {
	    ConfigurationButton button = (ConfigurationButton) it.next();
	    if (button.state == ConfigurationButton.NORMAL)
		list.add(button.getConfiguration());
	}
	return (Configuration[]) list.toArray(new Configuration[0]);
    }

    /**
     * Clears out all configurations which are "final" configurations,
     * i.e., those that are marked either as accept or reject
     * configurations.
     */
    public void clearFinal() {
	// Avoid concurrent modification exceptions.
	ArrayList list = new ArrayList();
	list.addAll(configurationToButtonMap.values());
	Iterator it = list.iterator();
	
	while (it.hasNext()) {
	    ConfigurationButton button = (ConfigurationButton) it.next();
	    if (button.state == ConfigurationButton.ACCEPT ||
		button.state == ConfigurationButton.REJECT)
		remove(button.getConfiguration());
	}
    }

    /**
     * Clears old all configurations which are not frozen.
     */
    public void clearThawed() {
	// Avoid concurrent modification exceptions.
	ArrayList list = new ArrayList();
	list.addAll(configurationToButtonMap.values());
	Iterator it = list.iterator();
	
	while (it.hasNext()) {
	    ConfigurationButton button = (ConfigurationButton) it.next();
	    if (button.state != ConfigurationButton.FREEZE)
		remove(button.getConfiguration());
	}
    }
    
    /**
     * Responds to actions, presumably generated by some button
     * belonging to this view.
     * @param e the action event generated
     */
    public void actionPerformed(ActionEvent e) {
	ConfigurationButton button = null;
	try {
	    button = (ConfigurationButton) e.getSource();
	} catch (ClassCastException ex) {
	    return; // Then, we don't care.
	}
	Configuration config = button.getConfiguration();
	if (!configurationToButtonMap.containsKey(config)) return;
	if (button.isSelected()) selected.add(config);
	else selected.remove(config);
	distributeSelectionEvent(new ConfigurationSelectionEvent(this));
    }

    /**
     * Adds a <CODE>ConfigurationSelectionListener</CODE> to this
     * object.
     * @param listener the listener to add
     */
    public void addSelectionListener(ConfigurationSelectionListener listener) {
	selectionListeners.add(listener);
    }

    /**
     * Remove a <CODE>ConfigurationSelectionListener</CODE> from this
     * object.
     * @param listener the listener to remove
     */
    public void removeSelectionListener
	(ConfigurationSelectionListener listener) {
	selectionListeners.remove(listener);
    }

    /**
     * Gives a <CODE>ConfigurationSelectionEvent</CODE> to all
     * listeners.
     * @param listener the listener to add
     */
    void distributeSelectionEvent(ConfigurationSelectionEvent event) {
	Iterator it = selectionListeners.iterator();
	while (it.hasNext()) {
	    ConfigurationSelectionListener listener =
		(ConfigurationSelectionListener) it.next();
	    listener.configurationSelectionChange(event);
	}
    }
    
    /** The configurations in this pane will be from this automaton. */
    private Automaton automaton;
    /** The map from configurations to their buttons. */
    private HashMap configurationToButtonMap = new HashMap();
    /** The set of selected configurations. */
    private HashSet selected = new HashSet();
    
    /** The set of listeners to selection events. */
    private transient HashSet selectionListeners = new HashSet();
}
