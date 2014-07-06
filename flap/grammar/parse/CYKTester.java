package grammar.parse;

import grammar.Grammar;
import grammar.Production;
import grammar.UnrestrictedGrammar;

/**
 * CYK Parser tester.
 * @author Kyung Min (Jason) Lee
 *
 */
public class CYKTester {

	public static void main(String[] args)
	{
		Production[] productions=new Production[10];
		productions[0]=new Production("S","AD");
		productions[1]=new Production("D","SC");
		productions[2]=new Production("A","a");
		productions[3]=new Production("C","b");
		productions[4]=new Production("S","CB");
		productions[5]=new Production("B","CE");
		productions[6]=new Production("E","CB");
		productions[7]=new Production("S","SS");
		productions[8]=new Production("S","b");
		productions[9]=new Production("B","CC");
		
		Grammar g=new UnrestrictedGrammar();
		g.addProductions(productions);
		g.setStartVariable("S");
		String target="abbbb";
		System.out.println("aa");
		CYKParser parser=new CYKParser(g);
		System.out.println(parser.solve(target));
		System.out.println("Trace = "+parser.getTrace());
	}
}
