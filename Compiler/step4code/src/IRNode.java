import java.util.*;

public class IRNode {
	//fields
	private IRNode bTarget;
	private String opcode;
	private String op1;
	private String op2;
	private String result;

	//constructors
	public IRNode(){
		this.bTarget = null;
		this.opcode = null;
		this.op1 = null;
		this.op2 = null;
		this.result = null;
	}

	public IRNode(String opcode, String op1, String op2, String result){
		this.bTarget = null;
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;
		this.result = result;		
	}

	//instance methods
	public void printNode(){
		System.out.println("INSTRUCTION:");
		System.out.println("bTarget: " + bTarget);
		System.out.println("opcode : " + opcode);
		System.out.println("op1    : " + op1);
		System.out.println("op2    : " + op2);
		System.out.println("result : " + result);
	}

	public void setbTarget(IRNode bTarget){
		this.bTarget = bTarget;
	}
}