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
 
package gui.grammar.parse;

import grammar.parse.LRParseTable;
import gui.LeftTable;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Set;

/**
 * This holds a LR parse table.
 * 
 * @author Thomas Finley
 */

public class LRParseTablePane extends LeftTable {
    /**
     * Instantiates a new parse table pane for a parse table.
     * @param table the table pane's parse table
     */
    public LRParseTablePane(LRParseTable table) {
	super(table);
	this.table = table;
	setCellSelectionEnabled(true);
	setDefaultRenderer(Object.class, new LRParseTablePane.CellRenderer());
	ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     * Highlights a particular cell.  Overridden to make sure that the
     * highlighted cells use the same special rendering components
     * this table uses for other entries.
     * @param row the row index of the cell to highlight
     * @param column the column index of the cell to highlight
     */
    public void highlight(int row, int column) {
	highlight(row, column, THRG);
    }

    /**
     * Retrieves the parse table in this pane.
     * @return the parse table in this pane
     */
    public LRParseTable getParseTable() {
	return table;
    }

    /**
     * Since there may be ambiguity in the LR parse table, each
     * description for each entry appears on a separate line, so the
     * tool tips must have seperate lines.
     * @return the tool tip creation
     */
    public JToolTip createToolTip() {
	return new gui.JMultiLineToolTip();
    }

    /**
     * This extends the concept of the cell renderer.
     */
    class CellRenderer extends DefaultTableCellRenderer {
	public java.awt.Component getTableCellRendererComponent
	    (JTable aTable, Object value, boolean isSelected,
	     boolean hasFocus, int row, int column) {
	    JComponent c = (JComponent) super.getTableCellRendererComponent
		(aTable, value, isSelected, hasFocus, row, column);
	    c.setToolTipText(table.getContentDescription(row, column));
	    return c;
       }

    }

    /** The built in renderer. */
    private TableHighlighterRendererGenerator THRG =
	new TableHighlighterRendererGenerator() {
	    public TableCellRenderer getRenderer(int row, int column) {
		if (renderer == null) {
		    renderer = new CellRenderer();
		    renderer.setBackground(new Color(255,150,150));
		}
		return renderer;
	    }
	    private DefaultTableCellRenderer renderer = null;
	};

    /** The parse table for this pane. */
    private LRParseTable table;
}
