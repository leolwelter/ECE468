import java.io.IOException;
import java.io.File;
import java.lang.Object;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.*;



public class Micro {

	public static void main(String[] args) throws IOException {
	        ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroLexer lexer = new MicroLexer(input);
		//LexerInterpreter lexer = new LexerInterpreter(input);
		while(true) {
			Token token = lexer.nextToken();
			if(token.getType() == Lexer.EOF) {
				break;
			}
			System.out.println("Token Type: " + token.getType());
			System.out.println("Value: " + token.getText());
		}
	}
}	
