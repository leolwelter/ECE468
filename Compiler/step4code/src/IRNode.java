import java.util.*;

public class IRNode {
	//fields
	private IRNode bTarget;
	private String opcode;
	private String op1;
	private String op2;
	private String result;
	public static int regNo;

	//constructors
	public IRNode(){
		this.bTarget = null;
		this.opcode = null;
		this.op1 = null;
		this.op2 = null;
		this.result = null;
	}

	// public IRNode(String opcode, String op1, String op2, String result){
	// 	this.bTarget = null;
	// 	this.opcode = opcode;
	// 	this.op1 = op1;
	// 	this.op2 = op2;
	// 	this.result = result;		
	// }

	public IRNode(ArrayList<String> token, String type){
		setParams(token, type);
	}

	//instance methods
	public void printNode(){
		System.out.println("\nINSTRUCTION:");
		System.out.print("opcode: " + opcode);
		System.out.print(" op1: " + op1);
		System.out.print(" op2: " + op2);
		System.out.print(" result: " + ShuntingYard.infixToPostfix(result));
		System.out.println(" bTarget: " + bTarget);
	}

	public void setbTarget(IRNode bTarget){
		this.bTarget = bTarget;
	}

	public void setParams(ArrayList<String> tdata, String type){
		//Takes token string from listener
		//then interprets the IR fields based on instr "type"
		switch(type){
			case "str":
				bTarget = null;
				opcode  = "str";
				op1     = tdata.get(0);
				op2     = null;
				result  = tdata.get(1);
				break;
			case "var":
				bTarget = null;
				opcode  = "var";
				op1     = null;
				op2     = null;
				result  = tdata.get(0);		
				break;
			case "storei":
				bTarget = null;
				opcode  = "STOREI";
				op1     = tdata.get(0);
				op2		= null;
				result  = tdata.get(1);
				break;
			case "addi":
				bTarget = null;
				opcode  = "ADDI";
				op1     = tdata.get(0);
				op2		= tdata.get(1);
				result	= tdata.get(2);
				break;
		}
	}
}