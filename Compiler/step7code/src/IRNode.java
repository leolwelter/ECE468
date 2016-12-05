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

	public void treeToIR(PostfixTreeNode node, PostfixTreeNode left, PostfixTreeNode right, String type, Function fy){
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
		//op1     = left.value;
		//op2     = right.value;
		ArrayList<List<String>> varList = fy.st.varMap.get(fy.name);
		if(varList != null){
			for(List<String> varData : varList){
				if(varData.get(0).equals(left.value)){
					op1 = varData.get(3);
				}
				if(varData.get(0).equals(right.value)){
					op2 = varData.get(3);
				}
			}
		}
		if(op1 == null){
			op1     = left.value;
		}
		if(op2 == null){
			op2     = right.value;
		}
		//op1     = left.value;
		//op2     = right.value;
		result  = "$T" + ++tempCnt;
	}

	public String tempToReg(char[] irop, String tvar, Function fy){
		//Take in a raw L/T/P, convert to Tiny
		//If not a REGISTER ($) just return tvar
		String tinyReg = "";
		//T->R
		// System.out.println("TVAR :" + tvar);
		if(tvar.startsWith("$")){
			if(tvar.toCharArray()[1] == 'T'){
				tinyReg = "r" + (Integer.parseInt(tvar.split("T")[1]) - 1);
			} else if(tvar.toCharArray()[1] == 'L'){
				tinyReg = "$-" + (Integer.parseInt(tvar.split("L")[1]));
			} else if(tvar.toCharArray()[1] == 'P'){
				tinyReg = "$" + (6 + fy.paramCnt - Integer.parseInt(tvar.split("P")[1]));
			} else if(tvar.toCharArray()[1] == 'R'){
				tinyReg = "$" + (6 + fy.paramCnt);
			}
		} else {
			try{
				if(Integer.valueOf(tvar) instanceof Integer){
					tinyReg = tvar;
				}
			}
			catch (Exception err1){
				try{
					if(Float.valueOf(tvar) instanceof Float){
						tinyReg = tvar;
					}
				}
				catch(Exception err2){
						ArrayList<List<String>> varList = fy.st.varMap.get(fy.name);
						if(varList != null){
				      for(List<String> varData : varList){
								if(varData.get(0).equals(tvar)){
									tvar = varData.get(3);
									// System.out.println("TVAR :" + tvar);
									if(tvar.toCharArray()[1] == 'T'){
										tinyReg = "r" + (Integer.parseInt(tvar.split("T")[1]) - 1);
									} else if(tvar.toCharArray()[1] == 'L'){
										tinyReg = "$-" + (Integer.parseInt(tvar.split("L")[1]));
									} else if(tvar.toCharArray()[1] == 'P'){
										tinyReg = "$" + (6 + fy.paramCnt - Integer.parseInt(tvar.split("P")[1]));
									} else if(tvar.toCharArray()[1] == 'R'){
										tinyReg = "$" + (6 + fy.paramCnt);
									}
								}
							}
						}
				}
		}
	}
		return tinyReg;
	}

	public String checkBothMems(LinkedList<TinyNode> tinyList, String top1, String top2){
		if(top1.startsWith("$") && top2.startsWith("$")){
			tempCnt++;
			tinyList.add(new TinyNode("move", top2, "r" + tempCnt));
			return "r" + tempCnt;
		}
		else{
			return top2;
		}
	}

	public void irToTiny(LinkedList<TinyNode> tinyList, Function fy){
		String topcode, top1, top2 = "";
		char [] irResult = result.toCharArray();
		char [] irOp1    = op1.toCharArray();
		char [] irOp2	 = op2.toCharArray();
		int temp1 = -1;
		int temp2 = -1;

		// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
		// temp1 = Integer.parseInt(result.split("T")[1]) - 1;

		switch(opcode){
			//******* STACK OPERATIONS            ******//
			case("POP"):
				if(op1.equals("")){
					tinyList.add(new TinyNode("pop", "", ""));
				}else{
					top1 = tempToReg(irOp1, op1, fy);
					tinyList.add(new TinyNode("pop", top1, ""));
				}
				break;
			case("PUSH"):
				if(op1.equals("")){
					tinyList.add(new TinyNode("push", "", ""));
				}else{
					top1 = tempToReg(irOp1, op1, fy);
					tinyList.add(new TinyNode("push", top1, ""));
				}
				break;
			case("RET"):
					tinyList.add(new TinyNode("unlnk", "", ""));
					tinyList.add(new TinyNode("ret", "", ""));
				break;
			case("JSR"):
					tinyList.add(new TinyNode("push", "r0", ""));
					tinyList.add(new TinyNode("push", "r1", ""));
					tinyList.add(new TinyNode("push", "r2", ""));
					tinyList.add(new TinyNode("push", "r3", ""));
					tinyList.add(new TinyNode("jsr", op1, ""));
					tinyList.add(new TinyNode("pop", "r3", ""));
					tinyList.add(new TinyNode("pop", "r2", ""));
					tinyList.add(new TinyNode("pop", "r1", ""));
					tinyList.add(new TinyNode("pop", "r0", ""));
				break;
			case("LINK"): //TODO: make 10 the actual number of locals + temps
					tinyList.add(new TinyNode("link", "10", ""));
				break;
			//******* CONDITIONAL(int) OPERATIONS ******//
			case("GT"):
				//If operands are $T's, make them registers
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);

				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
				}
				tinyList.add(new TinyNode("jgt", result, "")); //jump
				break;
			case("GE"):
				//If operands are $T's, make them registers
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
				}
				tinyList.add(new TinyNode("jge", result, "")); //jump
				break;
			case("LT"):
				//If operands are $T's, make them registers
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
				}
				tinyList.add(new TinyNode("jlt", result, "")); //jump
				break;
			case("LE"):
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
				}
				tinyList.add(new TinyNode("jle", result, "")); //jump
				break;
			case("NE"):
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
				}
				tinyList.add(new TinyNode("jne", result, "")); //jump
				break;
			case("EQ"):
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irOp2, op2, fy);
				top2 = checkBothMems(tinyList, op1, top2);
				if(this.opType.equals("FLOAT")){
					tinyList.add(new TinyNode("cmpr", top1, top2)); //comparison
				}else{
					tinyList.add(new TinyNode("cmpi", top1, top2)); //comparison
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
			case("str"):
				topcode = "str";
				top1	= op1;
				top2	= result;
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;

			//******* INTEGER OPERATIONS ******//
			case("STOREI"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("WRITES"):
				topcode = "sys";
				top1	= "writes";
				top2	= op1;
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("WRITEI"):
				topcode = "sys";
				top1 	= "writei";
				top2 = tempToReg(irOp1, op1, fy);
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("READI"):
				topcode = "sys";
				top1 	= "readi";
				top2 = tempToReg(irOp1, op1, fy);
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("ADDI"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "addi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("SUBI"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "subi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("MULTI"):
				topcode = "move";
				top1 = tempToReg(irOp1, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "muli";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;

			case("DIVI"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "divi";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;

			//******* Float OPERATIONS ********//
			case("STOREF"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("WRITEF"):
				topcode = "sys";
				top1 	= "writer";
				top2 = tempToReg(irOp1, op1, fy);
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("READF"):
				topcode = "sys";
				top1 	= "readr";
				top2 = tempToReg(irOp1, op1, fy);
				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("ADDF"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "addr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("SUBF"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "subr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
			case("MULTF"):
				topcode = "move";
				top1 = tempToReg(irOp1, op1, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "mulr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;

			case("DIVF"):
				topcode = "move";
				top1 = tempToReg(irOp1, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));

				topcode = "divr";
				// temp1 = Integer.parseInt(op1.split("T")[1]) - 1;
				// temp1 = Integer.parseInt(result.split("T")[1]) - 1;
				top1 = tempToReg(irOp2, op2, fy);
				top2 = tempToReg(irResult, result, fy);

				tinyList.add(new TinyNode(topcode, top1, top2));
				break;
		}
	}
}
