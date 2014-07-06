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

import javax.swing.JComponent;
import java.awt.*;
import java.net.URL;

/**
 * The <CODE>ImageDisplayComponent</CODE> is a single component that
 * displays an image within itself, and sets its size to match that of
 * the image.
 * 
 * @author Thomas Finley
 */

public class ImageDisplayComponent extends JComponent {
    /**
     * Instantiates a new <CODE>ImageDisplayComponent</CODE> without
     * an image at this time.
     */
    public ImageDisplayComponent() {
	this((Image) null);
    }

    /**
     * Instantiates a new <CODE>ImageDisplayComponent</CODE>.
     * @param image the new image for this component
     */
    public ImageDisplayComponent(Image image) {
	setImage(image);
    }

    /**
     * Instantiates a new <CODE>ImageDisplayComponent</CODE>.
     * @param url the new image for this component
     */
    public ImageDisplayComponent(URL url) {
	setImage(getBaseImage(url));
    }

    /**
     * Instantiates a new <CODE>ImageDisplayComponent</CODE>.
     * @param filename the path where to find the new image for this
     * component
     */
    public ImageDisplayComponent(String filename) {
	setImage(getBaseImage(filename));
    }

    /**
     * Returns the image associated with this component.
     * @return the image associated with this component
     */
    public Image getImage() {
	return myImage;
    }

    /**
     * Sets the image associated with this component.
     * @param image the new image for this component
     */
    public void setImage(Image image) {
	myImage = image;
	trackImage(getImage());
	redefineSize();
    }

    /**
     * Returns an untracked unloaded base image based on a path name.
     * @param path the path name
     */
    private Image getBaseImage(String path) {
	return Toolkit.getDefaultToolkit().getImage(path);
    }

    /**
     * Returns an untracked unloaded base image based on a URL.
     * @param url the url for the image
     */
    private Image getBaseImage(URL url) {
	return Toolkit.getDefaultToolkit().getImage(url);
    }

    /**
     * When this method returns, the image is sure to be fully loaded.
     * @param image the image to make sure is loaded
     * @return <TT>true</TT> if the tracking succeeded, <TT>false</TT>
     * if it was interrupted
     */
    private boolean trackImage(Image image) {
	if (image == null) return true; // Why not...
	MediaTracker tracker = new MediaTracker(this);
	tracker.addImage(image, 0);
	try {
	    tracker.waitForID(0);
	} catch (InterruptedException e) {
	    return false;
	}
	return true;
    }

    /**
     * Based on the image of this component, sets the preferred size.
     */
    private void redefineSize() {
	Dimension d = new Dimension(1,1);
	if (myImage != null) {
	    d = new Dimension(getImage().getWidth(this),
			      getImage().getHeight(this));
	}
	setPreferredSize(d);
    }

    /**
     * Paints this component.
     * @param g the graphics object to paint upon
     */
    public void paintComponent(Graphics g) {
	if (myImage != null) {
	    Rectangle r = getVisibleRect(),
		r2 = new Rectangle(getPreferredSize());
	    r = r.intersection(r2);

	    //long time = System.currentTimeMillis();
	    g.drawImage(myImage, r.x, r.y, r.x+r.width, r.y+r.height,
			r.x, r.y, r.x+r.width, r.y+r.height, this);
	    /*time = System.currentTimeMillis() - time;
	      System.out.println
	      ("Done drawing the image!  Took "+time+" ms");*/
	}
    }

    /** The image to display. */
    private Image myImage;
}
