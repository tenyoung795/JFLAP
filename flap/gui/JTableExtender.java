package gui;

import gui.action.MultipleSimulateAction;
import gui.sim.multiple.InputTableModel;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableExtender extends JTable{
	public JTableExtender(TableModel model, MultipleSimulateAction mult){
		super(model);
		myMultSimAct = mult;
	}
	
	
	public void changeSelection (int row, int column, boolean toggle, boolean extend) {
		 super.changeSelection (row, column, toggle, extend);
		 myMultSimAct.viewAutomaton(this);
		 }
	
	private MultipleSimulateAction myMultSimAct;
}
