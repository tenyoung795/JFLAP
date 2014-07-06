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

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;
import gui.SplitPaneFactory;
import gui.editor.ArrowDisplayOnlyTool;
import gui.environment.Environment;
import gui.environment.Universe;
import gui.environment.tag.CriticalTag;
import gui.sim.TraceWindow;
import gui.sim.multiple.InputTableModel;
import gui.viewer.AutomatonPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This is the action used for the simulation of multiple inputs on an
 * automaton with no interaction.  This method can operate on any
 * automaton.
 * 
 * @author Thomas Finley
 */

public class MultipleSimulateAction extends NoInteractionSimulateAction {
    /**
     * Instantiates a new <CODE>MultipleSimulateAction</CODE>.
     * @param automaton the automaton that input will be simulated on
     * @param environment the environment object that we shall add our
     * simulator pane to
     */
    public MultipleSimulateAction(Automaton automaton,
				  Environment environment) {
	super(automaton, environment);
	putValue(NAME, "Multiple Run");
	//putValue(ACCELERATOR_KEY, null);
    }

    /**
     * Returns the title for the type of compontent we will add to the
     * environment.
     * @return in this base class, returns "Multiple Inputs"
     */
    public String getComponentTitle() {
	return "Multiple Run";
    }

    /**
     * This will search configurations for an accepting configuration.
     * @param automaton the automaton input is simulated on
     * @param simulator the automaton simulator for this automaton
     * @param configs the initial configurations generated from a
     * single input
     * @param initialInput the object that represents the initial
     * input; this is a String object in most cases, but for Turing
     * Machines is an array of String objects
     * @param associatedConfigurations the first accepting
     * configuration encountered will be added to this list, or the
     * last configuration considered if there was no accepted
     * configuration
     * @return <CODE>0</CODE> if this was an accept, <CODE>1</CODE> if
     * reject, and <CODE>2</CODE> if the user cancelled the run
     */
    protected int handleInput(Automaton automaton,
			      AutomatonSimulator simulator,
			      Configuration[] configs, Object initialInput,
			      List associatedConfigurations) {
	JFrame frame = Universe.frameForEnvironment(getEnvironment());
	// How many configurations have we had?
	int numberGenerated = 0;
	// When should the next warning be?
	int warningGenerated = WARNING_STEP;
	Configuration lastConsidered = configs[configs.length-1];
	while (configs.length > 0) {
	    numberGenerated += configs.length;
	    // Make sure we should continue.
	    if (numberGenerated >= warningGenerated) {
		if (!confirmContinue(numberGenerated, frame)) {
		    associatedConfigurations.add(lastConsidered);
		    return 2;
		}
		while (numberGenerated >= warningGenerated)
		    warningGenerated *= 2;
	    }
	    // Get the next batch of configurations.
	    ArrayList next = new ArrayList();
	    for (int i=0; i<configs.length; i++) {
		lastConsidered = configs[i];
		if (configs[i].isAccept()) {
		    associatedConfigurations.add(configs[i]);
		    return 0;
		} else {
		    next.addAll(simulator.stepConfiguration(configs[i]));
		}
	    }
	    configs = (Configuration[]) next.toArray(new Configuration[0]);
	}
	associatedConfigurations.add(lastConsidered);
	return 1;
    }

    /**
     * Provides an initialized multiple input table object.
     * @param automaton the automaton to provide the multiple input
     * table for
     * @return a table object for this automaton
     * @see gui.sim.multiple.InputTableModel
     */
    protected JTable initializeTable(Automaton automaton) {
	InputTableModel model = InputTableModel.getModel(getAutomaton());
	JTable table = new JTable(model);
	// In this regular multiple simulate pane, we don't care about
	// the outputs, so get rid of them.
	TableColumnModel tcmodel = table.getColumnModel();
	for (int i=model.getInputCount(); i>0; i--) {
	    tcmodel.removeColumn(tcmodel.getColumn(model.getInputCount()));
	}
	// Set up the last graphical parameters.
	table.setShowGrid(true);
	table.setGridColor(Color.lightGray);
	return table;
    }

    /**
     * Handles the creation of the multiple input pane.
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
	if (getAutomaton().getInitialState() == null) {
	    JOptionPane.showMessageDialog
		((Component)e.getSource(),
		 "Simulation requires an automaton\n"+"with an initial state!",
		 "No Initial State", JOptionPane.ERROR_MESSAGE);
	    return;
	}

	final JTable table = initializeTable(getAutomaton());
	JPanel panel = new JPanel(new BorderLayout());
	JToolBar bar = new JToolBar();
	panel.add(new JScrollPane(table), BorderLayout.CENTER);
	panel.add(bar, BorderLayout.SOUTH);
	// Add the running input thing.
	bar.add(new AbstractAction("Run Inputs") {
		public void actionPerformed(ActionEvent e) {
		    try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		    } catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		    }
		    InputTableModel model = (InputTableModel)table.getModel();
		    AutomatonSimulator simulator =
			SimulatorFactory.getSimulator(getAutomaton());
		    String[][] inputs = model.getInputs();
		    for (int r=0; r<inputs.length; r++) {
			Configuration[] configs = null;
			Object input = null;
			// Is this a Turing machine?
			if (getAutomaton() instanceof TuringMachine) {
			    configs = ((TMSimulator)simulator)
				.getInitialConfigurations(inputs[r]);
			    input = inputs[r];
			} else { // If it's a Turing machine.
			    configs = simulator.getInitialConfigurations
				(inputs[r][0]);
			    input = inputs[r][0];
			}
			List associated = new ArrayList();
			int result=handleInput(getAutomaton(), simulator,
					       configs, input, associated);
			Configuration c = null;
			if (associated.size() != 0)
			    c = (Configuration) associated.get(0);
			model.setResult(r, RESULT[result], c);
		    }
		}
	    });
	// Add the clear button.
	bar.add(new AbstractAction("Clear") {
		public void actionPerformed(ActionEvent e) {
		    try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		    } catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		    }
		    InputTableModel model = (InputTableModel)table.getModel();
		    model.clear();
		}
	    });
	bar.add(new AbstractAction("Enter Lambda") {
		public void actionPerformed(ActionEvent e) {
		    int row = table.getSelectedRow();
		    if (row == -1) return;
		    for (int column=0; column<table.getColumnCount()-1;
			 column++)
			table.getModel().setValueAt("", row, column);
		}
	    });
	bar.add(new AbstractAction("View Trace") {
		public void actionPerformed(ActionEvent e) {
		    int[] rows = table.getSelectedRows();
		    InputTableModel tm=(InputTableModel)table.getModel();
		    List nonassociatedRows = new ArrayList();
		    for (int i=0; i<rows.length; i++) {
			if (rows[i] == tm.getRowCount()-1) continue;
			Configuration c=tm.
			    getAssociatedConfigurationForRow(rows[i]);
			if (c == null) {
			    nonassociatedRows.add(new Integer(rows[i]+1));
			    continue;
			}
			TraceWindow window = new TraceWindow(c);
			window.show();
			window.toFront();
		    }
		    // Print the warning message about rows without
		    // configurations we could display.
		    if (nonassociatedRows.size() > 0) {
			StringBuffer sb = new StringBuffer("Row");
			if (nonassociatedRows.size() > 1) sb.append("s");
			sb.append(" ");
			sb.append(nonassociatedRows.get(0));
			for (int i=1; i<nonassociatedRows.size(); i++) {
			    if (i == nonassociatedRows.size()-1) {
				// Last one!
				sb.append(" and ");
			    } else {
				sb.append(", ");
			    }
			    sb.append(nonassociatedRows.get(i));
			}
			sb.append("\ndo");
			if (nonassociatedRows.size() == 1) sb.append("es");
			sb.append(" not have end configurations.");
			JOptionPane.showMessageDialog
			    ((Component)e.getSource(),
			     sb.toString(),
			     "Bad Rows Selected", JOptionPane.ERROR_MESSAGE);
		    }
		}
	    });

	// Set up the final view.
	AutomatonPane ap = new AutomatonPane(getAutomaton());
	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
	JSplitPane split = SplitPaneFactory.createSplit
	    (getEnvironment(), true, 0.5, ap, panel);
	MultiplePane mp = new MultiplePane(split);
	getEnvironment().add(mp, getComponentTitle(), new CriticalTag(){});
	getEnvironment().setActive(mp);
    }

    /**
     * This auxillary class is convenient so that the help system can
     * easily identify what type of component is active according to
     * its class.
     */
    private class MultiplePane extends JPanel {
	public MultiplePane(JSplitPane split) {
	    super(new BorderLayout());
	    add(split, BorderLayout.CENTER);
	}
    }

    private static String[] RESULT = {"Accept", "Reject", "Cancelled"};
    private static Color[] RESULT_COLOR = {Color.green, Color.red,
					   Color.black};
}
