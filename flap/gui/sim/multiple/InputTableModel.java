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
 
package gui.sim.multiple;

import gui.GrowableTableModel;
import automata.Automaton;
import automata.Configuration;
import automata.turing.Tape;
import automata.turing.TMConfiguration;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import automata.turing.TuringMachine;

/**
 * The <CODE>InputTableModel</CODE> is a table specifically used for
 * the input of multiple inputs for simulation in an automaton.  It
 * has columns for each of the inputs, each of the outputs (when run
 * on a Turing machine and the user wants to treat it as a
 * transducer), and the final result.
 * 
 * @author Thomas Finley
 */

public class InputTableModel extends GrowableTableModel {
    /**
     * This instantiates an <CODE>InputTableModel</CODE>.
     * @param automaton the automaton that we're inputting stuff for
     */
    public InputTableModel(Automaton automaton) {
	super(2*inputsForMachine(automaton)+1);
    }

    /**
     * This instantiates a copy of the <CODE>InputTableModel</CODE>.
     * @param model the model to copy
     */
    public InputTableModel(InputTableModel model) {
	super(model);
    }

    /**
     * This instantiates an <CODE>InputTableModel</CODE>.
     * @param columns the number of columns for this input table model
     */
    protected InputTableModel(int columns) {
	super(columns);
    }

    /**
     * Initializes the contents of a new array to be all blank strings.
     */
    protected Object[] initializeRow(int row) {
	Object[] nr = super.initializeRow(row);
	Arrays.fill(nr, "");
	return nr;
    }

    /**
     * This returns the name of the columns.  In the input table
     * model, each of the first {@link #getColumnCount} columns is an
     * input column and is titled "Input #", where # is replaced with
     * the number of the column (e.g. "Input 1", then "Input 2" for a
     * two tape machine), unless there is only one input column, in
     * which case the single input column is just called "Input".
     * There are as many output columns as input columns, and are
     * numbered in a similar fashion except with the prefix "Output"
     * instead of "Input".  The last column is always reserved for the
     * result.
     * @param column the number of the column to get the name for
     * @return the name of the column
     */
    public String getColumnName(int column) {
	if (column == getColumnCount()-1) return "Result";
	String word = "Input";
	if (column >= getInputCount()) {
	    word = "Output";
	    column -= getInputCount();
	}
	if (getColumnCount() == 3) return word;
	return word+" "+(column+1);
    }

    /**
     * This returns an array of the inputs for the table.  The input
     * is organized by arrays of arrays of strings.  The first index
     * of the array is the row of the input.  The second index of the
     * array is the particular input, which will be a single element
     * array for most machines but an <i>n</i> element array for an
     * <i>n</i>-tape Turing machine.
     * @return an array of inputs, the first index corresponds
     * directly to the row, the second to the column
     */
    public String[][] getInputs() {
	String[][] inputs = new String[getRowCount()-1][getInputCount()];
	for (int r=0; r<inputs.length; r++)
	    for (int c=0; c<inputs[r].length; c++)
		inputs[r][c] = (String) getValueAt(r,c);
	return inputs;
    }

    /**
     * This returns if a cell is editable.  In this model, a cell is
     * editable if it's anything other than the last column, which is
     * where the results are reported.
     * @param row the row to check for editableness
     * @param column the column to check for editableness
     * @return by default this returns <CODE>true</CODE> if this is
     * any column other than the last column; in that instance this
     * returns <CODE>false</CODE>
     */
    public boolean isCellEditable(int row, int column) {
	return column < getInputCount();
    }

    /**
     * Returns the number of inputs needed for this type of automaton.
     * @param automaton the automaton to pass in
     * @return the number of input strings needed for this automaton;
     * e.g., n for an n-tape turing machine, 1 for most anything else
     */
    public static int inputsForMachine(Automaton automaton) {
	return automaton instanceof TuringMachine ? 
	    ((TuringMachine) automaton).tapes() : 1;
    }

    /**
     * This returns the number of inputs this table model supports.
     * @return the number of inputs for this table model
     */
    public int getInputCount() {
	return getColumnCount()/2;
    }

    /**
     * This returns the cached table model for an automaton of this
     * type.  It is desirable that automatons, upon asking to run
     * input, should be presented with the same data in the same table
     * since multiple inputs tables are oft used to test the same sets
     * of input on different automaton's again and again.  In the
     * event that there are multiple models active, this method
     * returns the last table model that was modified.  If there have
     * been no applicable table models cached yet, then a blank table
     * model is created.
     * @param automaton the automaton to get a model for
     * @return a copy of the model that was last edited with the
     * correct number of inputs for this automaton
     */
    public static InputTableModel getModel(Automaton automaton) {
	InputTableModel model = (InputTableModel) INPUTS_TO_MODELS
	    .get(new Integer(inputsForMachine(automaton)));
	if (model != null) {
	    model = new InputTableModel(model);
	    // Clear out the results column.
	    for (int i=0; i<(model.getRowCount()-1); i++)
		model.setResult(i, "", null);
	} else {
	    model = new InputTableModel(automaton);
	}
	model.addTableModelListener(LISTENER);
	return model;
    }

    /**
     * Sets the result string for a particular row.  If you wanted to
     * clear a row of all results, you would call this function
     * <CODE>setResult(row, "", null)</CODE>.
     * @param row the row to set the result of
     * @param result the result to put in the result column
     * @param config the associated configuration, or
     * <CODE>null</CODE> if you wish to not have a configuration
     * associated with a row
     */
    public void setResult(int row, String result, Configuration config) {
	int halfway = getInputCount();
	// Set the output columns.
	if (config != null && config.isAccept() &&
	    config instanceof TMConfiguration) {
	    TMConfiguration c = (TMConfiguration) config;
	    Tape[] tapes = c.getTapes();
	    for (int i=0; i<tapes.length; i++) {
		setValueAt(tapes[i].getOutput(), row, halfway+i);
	    }
	} else {
	    for (int i=0; i<halfway; i++)
		setValueAt("", row, halfway+i);
	}
	// Finally, set the result.
	setValueAt(result, row, getColumnCount()-1);
	// Store the accepting configuration at this entry.
	if (config == null)
	    rowToAssociatedConfiguration.remove(new Integer(row));
	else 
	    rowToAssociatedConfiguration.put(new Integer(row), config);
    }

    /**
     * This initializes the table so that it is completely blank
     * except for having one row.  The number of columns remains
     * unchanged.
     */
    public void clear() {
	if (rowToAssociatedConfiguration != null)
	    rowToAssociatedConfiguration.clear();
	super.clear();
    }

    /**
     * Returns the configuration associated with a row.  The
     * configuration would have been input via the {@link #setResult}
     * method.
     * @param row the row for which we want the associated accepting
     * configuration
     * @return the accepting configuration associated with a row, or
     * <CODE>null</CODE> if there is no associated accepting
     * configuration
     */
    public Configuration getAssociatedConfigurationForRow(int row) {
	return (Configuration) rowToAssociatedConfiguration
	    .get(new Integer(row));
    }

    /** The static table model listener for caching inputs. */
    protected final static TableModelListener LISTENER =
	new TableModelListener() {
	    public void tableChanged(TableModelEvent event) {
		InputTableModel model = (InputTableModel) event.getSource();
		if (event.getColumn() != event.ALL_COLUMNS &&
		    event.getColumn() >= model.getInputCount())
		    return; // If the inputs weren't changed, don't bother.
		Integer inputs = new Integer(model.getInputCount());
		INPUTS_TO_MODELS.put(inputs, model);
	    }
	};
    /** The map of number of inputs (stored as integers) to input
     * table models. */
    protected final static Map INPUTS_TO_MODELS = new HashMap();
    /** The map of row to the associated configuration.  If this row
     * does not have an associated configuration, this map should not
     * hold an entry. */
    private final Map rowToAssociatedConfiguration = new HashMap();
}
