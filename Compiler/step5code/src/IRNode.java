import java.util.*;

public class IRNode {
	//fields
	public  IRNode bTarget;
	public  String opcode;
	public  String op1;
	public  String op2;
	public  String result;

	public String opType; //conditional flag (FALSE if int)

	public static int tempCnt;
	public static int regNo;

	//constructors
	public IRNode(){
		this.bTarget = null;
		this.opcode = null;
		this.op1 = null;
		this.op2 = null;
		this.result = null;
		this.opType = null;
	}

	public IRNode(ArrayList<String> token, String type){
		setParams(token, type);
	}

	public IRNode(String opcode, String op1, String op2, String result){
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;
		this.result = result;		
		this.bTarget = bTarget;
		this.opType = null;
	}

	public IRNode(String opcode, String op1, String op2, String result, String opType){
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;
		this.result = result;		
		this.bTarget = null;
		this.opType = opType;
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
		}
	}

	public void treeToIR(PostfixTreeNode node, PostfixTreeNode left, PostfixTreeNode right, String type){
		//TODO: actually create IR from tree data
		if(type.equals("INT")){			
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
		} else if(type.equals("FLOAT")){
			switch (node.value){
				case("*"):
					opcode = "MULTF"; break;
				case("/"):
					opcode = "DIVF"; break;
				case("+"):
					opcode = "ADDF"; break;
				case("-"):
					opcode = "SUBF"; break;
			}			
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
		int temp1 = -1;
		int temp2 = -1;

		// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
		// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
	
		switch(opcode){
			//******* CONDITIONAL(int) OPERATIONS ******//
			case("GT"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jgt", result, "")); //jump	
				break;
			case("GE"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jge", result, "")); //jump				
				break;
			case("LT"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jlt", result, "")); //jump	
				break;
			case("LE"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jle", result, "")); //jump					
				break;
			case("NE"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jne", result, "")); //jump	
				break;
			case("EQ"):
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", op1, op2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", op1, op2)); //comparison					
				}
				tinyList.add(new TinyNode("jeq", result, "")); //jump			
				break;
			case("JUMP"):
				topcode = "jmp";
				top1 	= result;
				top2	= "";
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("LABEL"):
				topcode = "label";
				top1 	= result; 
				top2	= "";
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;			

			//********* VARIABLE DECLARATIONS *****//
			case("var"):
				topcode = "var";
				top1 	= result;
				top2 	= "";
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;

			//******* INTEGER OPERATIONS ******//
			case("STOREI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}	
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("WRITEI"):
				topcode = "sys";
				top1 	= "writei";
				top2 	= op1;
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("ADDI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "addi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("SUBI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "subi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("MULTI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "muli";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;	

			case("DIVI"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "divi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;	

			//******* Float OPERATIONS ********//
			case("STOREF"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}	
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("WRITEF"):
				topcode = "sys";
				top1 	= "writer";
				top2 	= op1;
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("ADDF"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "addr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("SUBF"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "subr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;
			case("MULTF"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "mulr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;	

			case("DIVF"):
				topcode = "move";
				if(irOp1[0] == '$'){
					top1 = "r" + (Integer.parseInt(op1.split("T")[1]) - 1);
				} else {
					top1 = op1;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}				
				tinyList.add(new TinyNode(topcode, top1, top2));								
				
				topcode = "divr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				if(irOp2[0] == '$'){
					top1 = "r" + (Integer.parseInt(op2.split("T")[1]) - 1);
				} else {
					top1 = op2;					
				}
				if(irResult[0] == '$'){
					top2 = "r" + (Integer.parseInt(result.split("T")[1]) - 1);
				} else {
					top2 	= result;					
				}
				tinyList.add(new TinyNode(topcode, top1, top2));				
				break;								
		}
	}
}
