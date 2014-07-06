package gui.environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.DOMPrettier;

public class Profile {
    public static String LAMBDA = "\u03BB";     // Jinghui Lim added stuff
    public static String EPSILON = "\u03B5";    // see MultipleSimulateAction
	public String lambda = "\u03BB";
	public String epsilon = "\u03B5";
	public String lambdaText = "u03BB";
	public String epsilonText = "u03B5";
	private String emptyString = lambda;
	
	/** The tag bane for the empty string preference. */
	public String EMPTY_STRING_NAME = "empty_string";

	/** The tag name for the root of a structure. */
	public static final String STRUCTURE_NAME = "structure";

	/** The tag name for the type of structure this is. */
	public static final String STRUCTURE_TYPE_NAME = "type";
	
	/** The tag bane for the empty string preference. */
	public String TURING_FINAL_NAME = "turing_final";
	
	/**
	 * Determines whether transitions can be issued from the final
	 * state of a Turing machine.
	 * 
	 * @author Chris Morgan
	 */
	private boolean transTuringFinal;
	/**
	 * A JCheckBoxMenuItem that displays and allows one to change transTuringFinal.
	 */
	private JCheckBoxMenuItem transTuringFinalCheckBox; 
	
	public String pathToFile = "";		
	
	public Profile(){
		emptyString = lambda;
		transTuringFinal = false;
		transTuringFinalCheckBox = new JCheckBoxMenuItem("Enable Transitions From Turing Machine Final States");
		transTuringFinalCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	setTransitionsFromTuringFinalStateAllowed(transTuringFinalCheckBox.isSelected());
            	savePreferences();
            }
        });
	}
	
	/**
	 * Sets the empty string.
	 * 
	 * @param empty the empty string
	 */
	public void setEmptyString(String empty){
		emptyString = empty;
	}
	
	/**
	 * Returns the empty string.
	 * 
	 * @return the empty string
	 */
	public String getEmptyString(){
		return emptyString;
	}
	
	/**
	 * Sets whether transitions leading from Turing machine final states are allowed.
	 * 
	 * @param t whether the transitions are allowed
	 */
	public void setTransitionsFromTuringFinalStateAllowed(boolean t) {
		transTuringFinal = t;
		transTuringFinalCheckBox.setSelected(t);
	}
	
	/**
	 * Returns whether transitions from Turing machine final states are allowed.
	 * 
	 * @return whether the transitions are allowed
	 */
	public boolean transitionsFromTuringFinalStateAllowed() {
		return transTuringFinal;
	}
	
	/**
	 * Returns the JCheckBoxMenuItem that can allow the user to change whether
	 * Turing machine final states are allowed.
	 */
	public JCheckBoxMenuItem getTuringFinalCheckBox() {
		return transTuringFinalCheckBox;
	}

	/**
	 * Saves the preferences stored in this profile in jflapPreferences.xml.
	 */
	public void savePreferences() {
		String empty = "";
		if(emptyString.equals(lambda)) empty = lambdaText;
	    else if(emptyString.equals(epsilon)) empty = epsilonText;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory
		.newInstance();
		DocumentBuilder builder;
		try {
			File file = new File(pathToFile);
			
			builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			doc.appendChild(doc.createComment("Created with JFLAP "
					+ gui.AboutBox.VERSION + "."));
			// Create and add the <structure> element.
			Element structureElement = createElement(doc, STRUCTURE_NAME, null,
					null);
			doc.appendChild(structureElement);
			Element se = doc.getDocumentElement();		
			Element element = createElement(doc, EMPTY_STRING_NAME, null, ""+empty);
			se.appendChild(element);
			element = createElement(doc, TURING_FINAL_NAME, null, ""+transTuringFinal);
			se.appendChild(element);
			
			DOMPrettier.makePretty(doc);
			Source s = new DOMSource(doc);
			Result r = new StreamResult(file);
			Transformer t;
			try {
				t = TransformerFactory.newInstance().newTransformer();
				try {
					t.transform(s, r);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (TransformerConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerFactoryConfigurationError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static Element createElement(Document document, String tagname,
			Map attributes, String text) {
		// Create the new element.
		Element element = document.createElement(tagname);
		
		// Add the text element.
		if (text != null)
			element.appendChild(document.createTextNode(text));
		return element;
	}
}
