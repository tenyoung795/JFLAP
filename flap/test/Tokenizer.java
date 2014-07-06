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
 
package test;

import java.io.*;

public class Tokenizer {
    public static void main(String args[]) throws Throwable {
	Reader r = new BufferedReader(new InputStreamReader(System.in));
	StreamTokenizer st = new StreamTokenizer(r);
	int code;
	while (st.nextToken() != st.TT_EOF) {
	    switch (st.ttype) {
	    case StreamTokenizer.TT_EOL:
		System.out.println("End of line read!");
		break;
	    case StreamTokenizer.TT_NUMBER:
		System.out.println("Number read : "+st.nval);
		break;
	    case StreamTokenizer.TT_WORD:
		System.out.println("Word read : "+st.sval);
		break;
	    default:
		char c = (char) st.ttype;
		System.out.println("Character read: "+c);
	    }
	}
    }
}
