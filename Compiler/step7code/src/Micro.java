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

	public static void initProgram(LinkedList<TinyNode> tinyList){
		tinyList.add(new TinyNode("push", "", ""));
		tinyList.add(new TinyNode("push", "r0", ""));
		tinyList.add(new TinyNode("push", "r1", ""));
		tinyList.add(new TinyNode("push", "r2", ""));
		tinyList.add(new TinyNode("push", "r3", ""));
		tinyList.add(new TinyNode("jsr", "main", ""));
		tinyList.add(new TinyNode("sys", "halt", ""));
	}

	public static void main(String[] args) throws IOException {

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

	 	//Tiny Instruction List
	 	LinkedList<TinyNode> tinyList = new LinkedList<TinyNode>();

		//Symbol Table for parser
		SymbolTable st = new SymbolTable();

		IRNode.regNo = 0;
		IRNode.tempCnt = 0;

		AntlrMicroListener mlistener = new AntlrMicroListener(st, meIRL);
	 	walker.walk(mlistener, programContext);

	 	//Print Function information
	 	//System.out.println(mlistener.functionTable);
	 	for(Function func : mlistener.functionTable.values()){
		 	//Prints Intermediate Representation List
		 	/* for(int i = 0; i < func.meIRL.size(); i++){
		 		func.meIRL.get(i).printNode();
		 	}

			System.out.println(); */
// -------------------------- CREATING CFG ----------------------------//
			if(func.meIRL.peekFirst() != null && func.meIRL.size() != 0) {
				IRNode p = func.meIRL.get(0);
				for(int i = 1; i < func.meIRL.size(); i++){
					if(!(p.opcode.equals("JUMP"))){
						func.meIRL.get(i).addPredecessor(p);
					}
					p = func.meIRL.get(i);
			 	}

				IRNode s;
				IRNode temp;
				for(int i = 0; i < func.meIRL.size(); i++){
					temp = func.meIRL.get(i);
					if(i+1 < func.meIRL.size() && !(temp.opcode.equals("JUMP"))){
						s = func.meIRL.get(i + 1);
						temp.addSuccessor(s);
					}
					// |GE|LT|LE|NE|EQ|JUMP"
					if(temp.opcode.equals("GT") || temp.opcode.equals("GE") || temp.opcode.equals("LT") || temp.opcode.equals("LE") || temp.opcode.equals("NE") || temp.opcode.equals("EQ") || temp.opcode.equals("JUMP")){
						//System.out.println("HEREEE");
						int j = 0;
						boolean labelNotFound = true;
						String labelName = temp.result;
						while((j < func.meIRL.size()) && labelNotFound){
							if(func.meIRL.get(j).opcode.equals("LABEL") && func.meIRL.get(j).result.equals(labelName)){
								s = func.meIRL.get(j);
								temp.addSuccessor(s);
								p = temp;
								func.meIRL.get(j).addPredecessor(p);
								labelNotFound = false;
							}
							j++;
						}
					}
			 	}
// --------------------- CREATING GEN & KILL SETS ----------------------------
				for(int i = 0; i < func.meIRL.size(); i++){
					temp = func.meIRL.get(i);
					if(temp.opcode.equals("POP")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.kill.add(temp.op1);
						}
					}
					else if(temp.opcode.equals("READI") || temp.opcode.equals("READF")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.kill.add(temp.op1);
						}
					}
					else if(temp.opcode.equals("STOREI") || temp.opcode.equals("STOREF")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.kill.add(temp.op1);
						}
						if((!(temp.result.isEmpty())) && temp.result.startsWith("$")){
							temp.kill.add(temp.result);
						}
					}
					else if(temp.opcode.equals("PUSH")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.gen.add(temp.op1);
						}
					}
					else if(temp.opcode.equals("WRITEI") || temp.opcode.equals("WRITEF") || temp.opcode.equals("WRITES")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.gen.add(temp.op1);
						}
					}
					else if(temp.opcode.equals("ADDI") || temp.opcode.equals("ADDF") ||
					temp.opcode.equals("SUBI") || temp.opcode.equals("SUBF") ||
					temp.opcode.equals("MULTI") || temp.opcode.equals("MULTF") ||
					temp.opcode.equals("DIVI") || temp.opcode.equals("DIVF")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.gen.add(temp.op1);
						}
						if((!(temp.op2.isEmpty())) && temp.op2.startsWith("$")){
							temp.gen.add(temp.op2);
						}
						if((!(temp.result.isEmpty())) && temp.result.startsWith("$")){
							temp.kill.add(temp.result);
						}
					}
					else if(temp.opcode.equals("GT") || temp.opcode.equals("GE") ||
					temp.opcode.equals("LT") || temp.opcode.equals("LE") ||
					temp.opcode.equals("NE") || temp.opcode.equals("EQ") ||
					temp.opcode.equals("JUMP")){
						if((!(temp.op1.isEmpty())) && temp.op1.startsWith("$")){
							temp.gen.add(temp.op1);
						}
						if((!(temp.op2.isEmpty())) && temp.op2.startsWith("$")){
							temp.gen.add(temp.op2);
						}
					}

				}

// --------------------- DONE CREATING GEN & KILL SETS ------------------------

// -------------------------- Start Worklist -----------------------------

				ArrayDeque<IRNode> initialWorklist = new ArrayDeque<IRNode>();
				//for(int i = 0; i < func.meIRL.size(); i++){
					//temp = func.meIRL.get(i);
				for(IRNode node : func.meIRL){
					if(node.opcode.equals("RET")){
						initialWorklist.add(node);
						node.out.clear();
						for(ArrayList<String> vardata : functionTable.get("GLOBAL")){
							node.out.add(vardata.get())
							//IN PROGRESS
						}					
					} else {
						node.in.clear();
						node.out.clear();
					}
				}
				func.meIRL.get(0).createWorklist(func.meIRL, initialWorklist);

// -------------------------- End Worklist -----------------------------

				// Printing for IRNodes and CFG
				for(int i = 0; i < func.meIRL.size(); i++){
					System.out.println("Main Node : ");
					func.meIRL.get(i).printNode();
					System.out.println("Predecessors : ");
					for(IRNode p1 : func.meIRL.get(i).predecessors){
						p1.printNode();
					}
					System.out.println("Successors : ");
					for(IRNode s1 : func.meIRL.get(i).successors){
						s1.printNode();
					}
					System.out.println("Gen : ");
					for(String q : func.meIRL.get(i).gen){
						System.out.print(" " + q);
					}
					System.out.println("\nKill : ");
					for(String w : func.meIRL.get(i).kill){
						System.out.print(" " + w);
					}
					System.out.println("\nIn : ");
					for(String e : func.meIRL.get(i).in){
						System.out.print(" " + e);
					}
					System.out.println("\nOut : ");
					for(String r : func.meIRL.get(i).out){
						System.out.print(" " + r);
					}
					System.out.println();
					System.out.println();

			 	}

				System.out.println();
// ------------------- CREATING & PRINTING CFG DONE ---------------------//
			 	//func.st.printTable();
				//System.out.println("-----End of Symbol Table-----");
		 	}
		}

/*
	 	//Convert Functions to Tiny
	 	//GLOBAL DECLARATIONS
	 	Function fc1 = mlistener.functionTable.get("GLOBAL");
		for(int i = 0; i < fc1.meIRL.size(); i++){
			fc1.meIRL.get(i).irToTiny(tinyList, fc1);
		}

		//PROGRAM INIT
		initProgram(tinyList);

		//FUNCTION IR
	 	for(Function func : mlistener.functionTable.values()){
	 		if(func.name.equals("GLOBAL")){
	 			continue;
	 		} else {
			 	//Prints Tiny List
			 	for(int i = 0; i < func.meIRL.size(); i++){
			 		func.meIRL.get(i).irToTiny(tinyList, func);
			 		// tinyList.add(new TinyNode(meIRL.get(i)));
			 	}
	 		}
	 	}
	 	//Add halt to end of Tiny list
		tinyList.add(new TinyNode("end", "", ""));


	 	//Print TinyList
		for (int i = 0; i < tinyList.size(); i++) {
			tinyList.get(i).printNode();
		}
*/
		/*
		*/
	 	/*
		System.out.println("ENTER Stack");
	 	for (String s : mlistener.enterStack){
	 		System.out.println(s);
	 	}

	 	System.out.println("END Stack");
	 	for (String s : mlistener.endStack){
	 		System.out.println(s);
	 	}
	 	*/




	 	/*
	 	//**** ABSTRACT SYNTAX TREE TESTING****
		//Tests Infix to Postfix
		ArrayList<String> infixS = new ArrayList<String>(Arrays.asList("c", "+", "a", "*", "b", "+", "(", "a", "*", "b", "+", "c", ")", "/", "a", "+", "d" ));
		ShuntingYard sy = new ShuntingYard();
		String postfixS = sy.infixToPostfix(infixS);

		//Tests Postfix Tree
		PostfixTree pfTree = new PostfixTree();
		PostfixTreeNode root = pfTree.createTree(postfixS);

		//Tests tree structure
		// root.printTree(root);

		//Tests subexpression elimination
		root.toIRList(root, meIRL);
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
