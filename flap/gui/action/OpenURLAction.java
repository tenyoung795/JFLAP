package gui.action;


import gui.environment.Universe;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;


import javax.swing.Action;

public class OpenURLAction extends RestrictedAction {
	
	public OpenURLAction(){
		super("Open URL", null);
	}

	public boolean isEnabled() {
		if(Universe.CHOOSER == null) return true; 
		return false;
	}

	public void actionPerformed(ActionEvent e) {
		
	}

}
