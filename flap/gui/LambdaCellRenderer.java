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
 
package gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This is a cell renderer that displays the lambda character if the
 * quantity to display is the empty string.
 * 
 * @author Thomas Finley
 */

public class LambdaCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent
	(JTable table, Object value, boolean isSelected,
	 boolean hasFocus, int row, int column) {
	JLabel l = (JLabel) super.getTableCellRendererComponent
	    (table,value,isSelected,hasFocus,row,column);
	if (hasFocus && table.isCellEditable(row,column)) return l;
	if (!"".equals(value)) return l;
	l.setText("\u03BB");
	return l;
    }
}
