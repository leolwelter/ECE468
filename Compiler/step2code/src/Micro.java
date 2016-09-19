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
	  ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroLexer lexer = new MicroLexer(input);
		//LexerInterpreter lexer = new LexerInterpreter(input);

		//step2 code

		//lists matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//tokens -> parser
		MicroParser parser = new MicroParser(tokens);

		boolean isAccepted = true;

		ANTLRErrorStrategy es = new CustomErrorStrategy(); 
		parser.setErrorHandler(es);

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

		//step1 code
		// while(true) {
		// 	Token token = lexer.nextToken();
		// 	if(token.getType() == Lexer.EOF) {
		// 		break;
		// 	}
		// 	System.out.println("Token Type: " + MicroLexer.tokenNames[token.getType()]);
		// 	System.out.println("Value: " + token.getText());
		// }
	}
}

//problems: 17, 2, 3