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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * This class defines methods for producing transforms that will allow.
 * 
 * @author Thomas Finley
 */

public abstract class TransformFitter {
	/**
	 * This produces a transform whereby content within rectangle <I>rectDraw</I>
	 * will fit entirely within the area defined by rectangle <I>rectSpace</I>.
	 * 
	 * @param rectDraw
	 *            the rectangle defining the area painting commands will be sent
	 * @param rectSpace
	 *            the rec
	 */
	public static AffineTransform fit(Rectangle rectDraw, Rectangle rectSpace) {
		return new AffineTransform();
	}
}
