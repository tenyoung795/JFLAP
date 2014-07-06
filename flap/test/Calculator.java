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
import java.util.*;

public class Calculator {
    /**
     * Given a string representing a mathematical expression, this
     * returns the value of that expression.  If there are any
     * variables in the expression they should be contained within the
     * map of values.
     * @param string the mathematical expression
     * @param values the map of string objects to number objects
     */
    public static double valueOf(String string, Map values) {
	string = string.replaceAll("-", " -");
	StringReader reader = new StringReader(string);
	StreamTokenizer st = new StreamTokenizer(reader);
	st.ordinaryChar('/');
	ArrayList list = new ArrayList();
	Number zero = new Integer(0);
	boolean number = false;
	Character plus = new Character('+');

	System.out.println("Evaluating "+string+" with "+st);

	try {
	    while (st.nextToken() != st.TT_EOF) {
		System.out.println("Got a new symbol!");
		switch (st.ttype) {
		case StreamTokenizer.TT_WORD:
		    // Attempt to resolve the symbol to a number.
		    Number n = (Number) values.get(st.sval);
		    if (number) list.add(plus);
		    number = true;
		    list.add(n == null ? zero : n);
		    break;
		case StreamTokenizer.TT_NUMBER:
		    if (number) list.add(plus);
		    number = true;
		    list.add(new Double(st.nval));
		    break;
		case StreamTokenizer.TT_EOL:
		    // Who cares?
		    break;
		default:
		    number = false;
		    list.add(new Character((char) st.ttype));
		    break;
		}
	    }
	} catch (IOException e) {
	    return Double.NaN; // We canna do it, captain!
	}
	// So now we have all these symbols in a list... great!
	System.out.println("SYMBOLS "+list);
	Iterator it = list.iterator();
	return valueOf(it);
    }

    /**
     * The recursive helper function for the <CODE>valueOf</CODE>
     * function.
     * @param it the iterator through operators and numbers
     */
    private static double valueOf(Iterator it) {
	Stack values = new Stack();
	Stack operators = new Stack();
	values.push(new Double(0.0));
	
	System.out.println("IN THE VALUE FUNCTION!");

	while (it.hasNext()) {
	    Object o = it.next();
	    System.out.println("Processing "+o);
	    if (o instanceof Number) {
		values.push(o);
		continue;
	    }
	    Character character = (Character) o;
	    char c = character.charValue();
	    if (c == ')') break; // Done!
	    if (c == '(') {
		values.push(new Double(valueOf(it)));
		continue;
	    }
	    while (!operators.isEmpty()) {
		boolean toCollapse=false;
		char last = ((Character)operators.peek()).charValue();
		switch (c) {
		case '+':
		case '-':
		    if (last == '-' || last == '+') toCollapse=true;
		case '*':
		case '/':
		    if (last == '*' || last == '/') toCollapse=true;
		case '^':
		    if (last == '^') toCollapse = true;
		default:
		    // Eh.
		}
		if (!toCollapse) break; // Let it be.
		// Collapse!
		double b=((Number)values.pop()).doubleValue(),
		    a=((Number)values.pop()).doubleValue();
		operators.pop(); // Get rid of it...
		switch (last) {
		case '^':
		    a = Math.pow(a,b);
		    break;
		case '*':
		    a *= b;
		    break;
		case '/':
		    a /= b;
		    break;
		case '+':
		    a += b;
		    break;
		case '-':
		    a -= b;
		    break;
		default:
		    // Eh.
		}
		values.push(new Double(a));
	    }
	    operators.push(character);
	    continue;
	    
	}
	// We've run out, or it's time to return.  Do pending ops and leave.

	while (!operators.isEmpty()) {
	    // Collapse!
	    char last = ((Character)operators.pop()).charValue();
	    double b=((Number)values.pop()).doubleValue(),
		a=((Number)values.pop()).doubleValue();
	    switch (last) {
	    case '^':
		a = Math.pow(a,b);
		break;
	    case '*':
		a *= b;
		break;
	    case '/':
		a /= b;
		break;
	    case '+':
		a += b;
		break;
	    case '-':
		a -= b;
		break;
	    default:
		// Eh.
	    }
	    values.push(new Double(a));
	}
	System.out.println("OUT THE VALUE FUNCTION!");

	return ((Number)values.pop()).doubleValue();
    }

    public static final void main(String args[]) {
	Map m = new HashMap();
	for (int i=0; i<args.length; i++) {
	    System.out.println(valueOf(args[i], m));
	}
    }
}
