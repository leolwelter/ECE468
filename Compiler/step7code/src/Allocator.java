import java.util.*;

public class Allocator{
	//Fields
	public Function func;
	public SymbolTable globalVars;
	public LinkedList<TinyNode> tinyList;

	//Constructors
	public Allocator(Function func, SymbolTable globalVars, LinkedList<TinyNode> tinyList){
		this.func = func;
		this.globalVars = globalVars;
		this.tinyList = tinyList;
	}

	//Methods
	public void allocate(){
		ArrayList<String> no_reg = new ArrayList<String>();
		no_reg.add("JUMP");
		no_reg.add("LABEL");		
		no_reg.add("WRITES");		
		no_reg.add("LINK");
		no_reg.add("RET");
		no_reg.add("JSR");

		IRNode tempnode;
		String target = "";
		String opone = "";
		for (IRNode node : func.meIRL) {
			//If node's result is a target (opcode is conditional)
			target = "r3";
			opone = "r1";

			//Special Case
			if((node.opcode.equals("POP")) && (node.result.equals("")))
				target = "";

			if((node.opcode.equals("PUSH")) && (node.op1.equals(""))){
				opone = "";
			}

			if( (node.opcode.equals("GE")) ||
				(node.opcode.equals("GT")) ||				
				(node.opcode.equals("LE")) ||
				(node.opcode.equals("LT")) ||
				(node.opcode.equals("EQ")) ||
				(node.opcode.equals("NE")) ){
				target = node.result;
			}

			if(no_reg.contains(node.opcode)){
				node.irToTiny(tinyList, func);
			} else{
				ensure(node.op1, "r1");
				ensure(node.op2, "r2");
				if(node.opType != null){
					tempnode = new IRNode(node.opcode, opone, "r2", target, node.opType);
				} else {
					tempnode = new IRNode(node.opcode, opone, "r2", target);
				}
				tempnode.irToTiny(tinyList, func);
				save(node);
			}			
		}
	}

	//Keeps registers consistent
	public void ensure(String operand, String reg){
		if(operand == null){
			return;
		}

		if(!operand.equals("")){
			ArrayList<List<String>> varList = globalVars.varMap.get("GLOBAL");
			for(List<String> varData : varList){		
				if(operand.equals(varData.get(0))){
					tinyList.add(new TinyNode("move", operand, reg));
					return;
				}
			}
			//is in register
			String tinyReg;
			if(operand.startsWith("$P")){
				tinyReg = tempToReg(operand.toCharArray(), operand, func);				
				tinyList.add(new TinyNode("move", tinyReg, reg));
			} else if(operand.startsWith("$L")){
				tinyReg = tempToReg(operand.toCharArray(), operand, func);				
				tinyList.add(new TinyNode("move", tinyReg, reg));
			} else if(operand.startsWith("$T")){
				tinyReg = tempToReg(operand.toCharArray(), operand, func);				
				tinyList.add(new TinyNode("move", tinyReg, reg));
			} else {
				//Is a constant
				tinyList.add(new TinyNode("move", operand, reg));
			}
		}
	}

	//Saves registers to memory
	public void save(IRNode node){		
		if(node.result == null)
			return;
		//If result is global
		ArrayList<List<String>> varList = globalVars.varMap.get("GLOBAL");
		for(List<String> varData : varList){		
			if(node.result.equals(varData.get(0))){
				tinyList.add(new TinyNode("move", "r3", node.result));
				return;
			}
		}
		
		String tinyReg = tempToReg(node.result.toCharArray(), node.result, func);				

		//If result is parameter
		if(node.result.startsWith("$P")){
			tinyList.add(new TinyNode("move", "r3", tinyReg));
		} else if(node.result.startsWith("$L")){
			tinyList.add(new TinyNode("move", "r3", tinyReg));			
		} else if(node.result.startsWith("$T")){
			tinyList.add(new TinyNode("move", "r3", tinyReg));
		} else if(node.result.startsWith("$R")){
			tinyList.add(new TinyNode("move", "r3", tinyReg));			
		}
	}


	public String tempToReg(char[] irop, String tvar, Function fy){
		//Take in a raw L/T/P, convert to Tiny
		//If not a REGISTER ($) just return tvar
		String tinyReg = tvar;
		//T->R
		// System.out.println("TVAR :" + tvar);
		if(tvar.startsWith("$")){
			if(tvar.toCharArray()[1] == 'T'){
				tinyReg = "$-" + (Integer.parseInt(tvar.split("T")[1]) + fy.localCnt);
				//tinyReg = "$-" + (fy.localCnt + (Integer.parseInt(tvar.split("T")[1])));
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
									if(tvar.startsWith("$")) {
										if(tvar.toCharArray()[1] == 'T'){
											tinyReg = "r" + (Integer.parseInt(tvar.split("T")[1]) - 1 + fy.localCnt);
											//tinyReg = "$-" + (fy.localCnt + (Integer.parseInt(tvar.split("T")[1])));
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
		}
		return tinyReg;
	}	
}