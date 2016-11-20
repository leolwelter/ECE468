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

public class CustomErrorStrategy extends DefaultErrorStrategy{
	public CustomErrorStrategy(){}

	public void reportError(Parser p, RecognitionException rep){
		throw rep;
	}
}