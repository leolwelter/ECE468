import java.util.*;

public class IRNode {
	//fields
	public  IRNode bTarget;
	public  String opcode;
	public  String op1;
	public  String op2;
	public  String result;
	public static int tempCnt;
	public static int regNo;

	//constructors
	public IRNode(){
		this.bTarget = null;
		this.opcode = null;
		this.op1 = null;
		this.op2 = null;
		this.result = null;
	}

	public IRNode(ArrayList<String> token, String type){
		setParams(token, type);
	}

	public IRNode(String opcode, String op1, String op2, String result, IRNode bTarget){
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;
		this.result = result;		
		this.bTarget = bTarget;
	}

	//instance methods
	public void printNode(){
		System.out.print(";");
		if(bTarget != null){
			System.out.println(opcode + " " + op1 + " " + op2 + " " + result + bTarget);	
		} else {
			System.out.println(opcode + " " + op1 + " " + op2 + " " + result);	
		}
		// System.out.println("\nINSTRUCTION:");
		// System.out.print("opcode:" + opcode);
		// System.out.print(" op1:" + op1);
		// System.out.print(" op2:" + op2);
		// System.out.print(" result:" + result);
		// System.out.println(" bTarget:" + bTarget);
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
				op2     = "";
				result  = tdata.get(1);
				break;
			case "var":
				bTarget = null;
				opcode  = "var";
				op1     = "";
				op2     = "";
				result  = tdata.get(0);
				break;
			case "storei":
				bTarget = null;
				opcode  = "STOREI";
				op1     = tdata.get(0);
				op2		= "";
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

	public void treeToIR(PostfixTreeNode node, PostfixTreeNode left, PostfixTreeNode right){
		//TODO: actually create IR from tree data
		switch (node.value){
			case("*"):
				opcode = "MULTI"; break;
			case("/"):
				opcode = "DIVI"; break;
			case("+"):
				opcode = "ADDI"; break;
			case("-"):
				opcode = "SUBI"; break;
		}
		op1     = left.value;
		op2     = right.value;
		result  = "$T" + ++tempCnt; 		
	}

	public void irToTiny(LinkedList<TinyNode> tinyList){
		String topcode, top1, top2 = "";
		char [] irResult = result.toCharArray();
		char [] irOp1    = op1.toCharArray();
		char [] irOp2	 = op2.toCharArray();

		switch(opcode){
			case("var"):
				topcode = "var";
				top1 	= result;
				top2 	= "";
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("STOREI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + regNo; //?
				} else{
					top1 = op1;
				}
				if(irResult[0] == '$'){
					top2 = "r" + ++regNo;
				} else{
					top2 = result;
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("WRITEI"):
				topcode = "sys";
				top1 	= "writei";
				top2 	= op1;
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("MULTI"):
				topcode = "muli";
				if(irOp1[0] == '$'){
					top1 = 	
				}


		}
	}
}
