import java.io.IOException;
import java.io.File;
import java.lang.Object;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.*;



public class Micro {
	public static void main(String[] args) throws IOException {
	/*Step3 code
	*/		
	  	ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroLexer lexer = new MicroLexer(input);

		//lists matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//tokens -> parser
		MicroParser parser = new MicroParser(tokens);


		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);	

		//Entry point
		MicroParser.ProgramContext programContext = parser.program();

		//Walk the parser & attach listener
		ParseTreeWalker walker = new ParseTreeWalker();	
		AntlrMicroListener mlistener = new AntlrMicroListener();
	 	walker.walk(mlistener, programContext);
	
	}
}
// private void printDrink(String drinkSentence) {
//     // Get our lexer
//     DrinkLexer lexer = new DrinkLexer(new ANTLRInputStream(drinkSentence));
 
//     // Get a list of matched tokens
//     CommonTokenStream tokens = new CommonTokenStream(lexer);
 
//     // Pass the tokens to the parser
//     DrinkParser parser = new DrinkParser(tokens);
 
//     // Specify our entry point
//     DrinkSentenceContext drinkSentenceContext = parser.drinkSentence();
 
//     // Walk it and attach our listener
//     ParseTreeWalker walker = new ParseTreeWalker();
//     AntlrDrinkListener listener = new AntlrDrinkListener();
//     walker.walk(listener, drinkSentenceContext);
// }

/*	Step1 code
	  ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroLexer lexer = new MicroLexer(input);

		//lists matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//tokens -> parser
		MicroParser parser = new MicroParser(tokens);


		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);
		while(true) {
			Token token = lexer.nextToken();
			if(token.getType() == Lexer.EOF) {
				break;
			}
			System.out.println("Token Type: " + MicroLexer.tokenNames[token.getType()]);
			System.out.println("Value: " + token.getText());
		}
*/		

/* Step2 code
	  ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroLexer lexer = new MicroLexer(input);

		//lists matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//tokens -> parser
		MicroParser parser = new MicroParser(tokens);


		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		boolean isAccepted = true;

		try{
			parser.program(); //start parser
		}
		catch(Exception err)
		{
		    isAccepted = false;
			System.out.println("Not Accepted");
		}
		if(isAccepted){
		    System.out.println("Accepted");
		}
*/


