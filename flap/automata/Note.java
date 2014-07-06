package automata;

import gui.editor.DeleteTool;
import gui.editor.EditCanvas;
import gui.editor.EditorPane;
import gui.editor.Tool;
import gui.viewer.AutomatonPane;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextArea;

public class Note extends JTextArea{
	private Point myAutoPoint = null;
	public boolean moving = false;
	protected Point initialPointState;
	protected Point initialPointClick;
	protected AutomatonPane myView;
	public Point myViewPoint = new Point(0,0);
	public Note(Point p, String message){
		setLocationManually(p);
		this.setText(message);
	}
	
	public Note(String message){
		this.setText(message);
	}
	
	public Note(Point point) {
		setLocationManually(point);
	}
	
	public void initializeForView(AutomatonPane view){
		myView = view;
		setLocationManually(myAutoPoint);
        this.setDisabledTextColor(Color.BLACK);
		this.setBackground(new Color(255, 255, 150));
		this.addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e) {
				if (e.isPopupTrigger())
					return;
				if(!((Note)e.getSource()).isEditable()){					
					int diffX = e.getPoint().x - initialPointClick.x; 
					int diffY = e.getPoint().y - initialPointClick.y;

					int nowAtX = initialPointState.x+ diffX;
					int nowAtY =  initialPointState.y +diffY;
					((Note)e.getSource()).setLocationManually(new Point(nowAtX, nowAtY));
					initialPointState = new Point(((Note)e.getSource()).getAutoPoint());
				}
				else {
					//do normal select functionality
				}
				myView.repaint();
			}
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}				
		});
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {

				((Note)e.getComponent()).setEnabled(true);
				((Note)e.getComponent()).setEditable(true);
				((Note)e.getComponent()).setCaretColor(null);
			}

			public void mousePressed(MouseEvent e) {
				initialPointState = new Point(((Note)e.getSource()).getAutoPoint());
				initialPointClick = new Point(e.getPoint().x, e.getPoint().y);
				
				//delete the text box
				EditorPane pane = myView.getCreator();
				Tool curTool = pane.getToolBar().getCurrentTool();
				if(curTool instanceof DeleteTool){
					myView.remove((Note)e.getSource());
					myView.getDrawer().getAutomaton().deleteNote((Note)e.getSource());
					myView.repaint();
				}
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
        myView.add(this);
	}

	public Point getAutoPoint(){
		return myAutoPoint;
	}
	
	

	public void setLocationManually(Point point) {
		moving = true;
		myAutoPoint = point;
		if(myView != null){
			setLocation(myView.transformFromAutomatonToView(point));	
		}
	}
	
	public void setLocation(Point p){
		if(moving){		
			if(myView!=null){
				myViewPoint = p;
				super.setLocation(p);
			}
		}
	}
	
	public void setLocation(int x, int y){
		if(moving){
			super.setLocation(x, y);
		}
		moving = false;
	}


	public void updateView() {
		setLocationManually(myAutoPoint);
		
	}


	
	
}
