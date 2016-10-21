import java.io.IOException;
import java.io.File;
import java.lang.Object;
import java.util.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.*;

//java -cp lib/antlr.jar:classes/ Micro [path to test.micro] > [output file]


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

		//Intermediate Representation List
	 	LinkedList<IRNode> meIRL = new LinkedList<IRNode>();

		//Symbol Table for parser
		SymbolTable st = new SymbolTable();

		AntlrMicroListener mlistener = new AntlrMicroListener(st, meIRL);
	 	walker.walk(mlistener, programContext);

	 	//Prints Intermediate Representation List
	 	for(int i = 0; i < meIRL.size(); i++){
	 		//meIRL.get(i).printNode();
	 	}

		// To test Infix to Postfix
		ArrayList<String> infixS = new ArrayList<String>(Arrays.asList("c", "+", "a", "*", "b", "+", "(", "a", "*", "b", "+", "c", ")", "/", "a", "+", "d" ));
		ShuntingYard sy = new ShuntingYard();
		String postfixS = sy.infixToPostfix(infixS);
		System.out.println(infixS);
		System.out.println(postfixS);

		// To test Postfix Tree
		PostfixTree pfTree = new PostfixTree();
		PostfixTreeNode root = pfTree.createTree(postfixS);

		// Print Tree to Check



	 	/*Harika: !!!
	 		So far I have:
	 			defined the IR node type
	 			created a list of IR nodes
	 			found an algorithm to parse complex expressions
	 		To do:
	 			use that algorithm (found in ShuntingYard) to
	 			actually parse the expressions found in the IR
	 			(IR store the expressions in "IRNode.result")

	 			use the parsed expressions to make new IRNodes (i.e. a := 2 + 3 -> (intermediate code))

	 			write a method (in IRNode) to create an equivalent "Tiny" instruction node(s) based on its fields

	 			Good luck! and don't worry about finishing tonight, we have tomorrow as well!
	 	*/

	 	/*
	 	Notes:
	 	Expression node{
			Expression op1, op2
			String val, //literal, symbol name, operator
	 	}
	 	create tree based on the AST (look up Shunting-yard)
	 								 ("how to make an AST")
	 	the point is to keep PEMDAS in assignments
	 	*/

	 	//STEP 3 TESTING
		//Prints results of Symbol Table parsing
	 	// if(st.declErr != null){
	 	// 	System.out.println(st.declErr);
	 	// }else{
	 	// 	SymbolTable dummy = st;
			// while(dummy != null){
			// 	dummy.printTable();
			// 	dummy = dummy.next;
			// 	System.out.println("\n");
			// }
	 	// }
	}
}


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
