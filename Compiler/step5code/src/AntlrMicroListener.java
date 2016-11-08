import java.lang.*;
import java.util.*;

public class AntlrMicroListener extends MicroBaseListener {
	//Fields
	public SymbolTable st;
	public LinkedList<IRNode> meIRL;
	public ArrayList<String> infixS = new ArrayList<String>();
	public Stack<String> enterStack;
	public Stack<String> endStack;
	public static int condCount = 0; //conditional count
	public static int tf_flag = 0;

	//Custom Constructor
	public AntlrMicroListener(SymbolTable st, LinkedList<IRNode> irList){
		this.st = st;
		this.meIRL = irList;
		this.enterStack = new Stack<String>();
		this.endStack = new Stack<String>();
	}

	//Prints the scopes as they're entered
	//Functions
	@Override public void enterFunc_decl(MicroParser.Func_declContext ctx) { 
		if(ctx.getText().compareTo("END") != 0){
			String txt = ctx.getText().split("BEGIN")[0];
			txt = txt.split("INT|FLOAT|VOID|STRING")[1];
			txt = txt.split("\\(")[0];
			//Set new SymbolTable scope
			st.next = new SymbolTable(txt);
			st = st.next;
		}
	}

	//Loops
	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) { 
		//Set new SymbolTable scope
		st.next = new SymbolTable("BLOCK");
		st = st.next;	

		//Push while to cond stack

	}

	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) { 
		//Set new SymbolTable scope
		st.next = new SymbolTable("BLOCK");
		st = st.next;

		//Conditional State Tracking
		endStack.push("if_" + ++condCount);
		enterStack.push("if_" + condCount);
		
	}

	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) { 	
		if((ctx.getText().compareTo("") != 0) && 
			(ctx.getText().compareTo("ENDIF") != 0)){	
			//Set new SymbolTable scope
			st.next = new SymbolTable("BLOCK");
			st = st.next;			
		}

		//Conditional State Tracking
		meIRL.add(new IRNode("JUMP", "", "", endStack.peek(), null));
		meIRL.add(new IRNode("LABEL", "", "", enterStack.pop(), null));		
		enterStack.push("if_" + ++condCount);
	}

	//ENDIF handling
	@Override public void exitIf_stmt(MicroParser.If_stmtContext ctx) { 
		//Create IRNodes related to ENDIF statements
		meIRL.add(new IRNode("JUMP", "", "", endStack.peek(), null));
		meIRL.add(new IRNode("LABEL", "", "", endStack.pop(), null));
	}
	//IRNode( opcode,  op1,  op2,  result,  bTarget){

	@Override public void enterCond(MicroParser.CondContext ctx) { 
		//clear the infix (make way for more expressions)
		infixS.clear();
		if (ctx.getText().equals("TRUE")) {
			infixS.add("TRUE");
		}else if(ctx.getText().equals("FALSE")){
			infixS.add("FALSE");
		}

	}

	//1. eval LHS (infixS)
	//2. add IRNode(s)    
	//3. infixS.clear();
	@Override public void enterCompop(MicroParser.CompopContext ctx) { 
		System.out.println("Enter compop: " + infixS);
		//1:
		

	}

	@Override public void exitCompop(MicroParser.CompopContext ctx) { 
		infixS.clear(); //make way for RHS		
	}



	//1. eval RHS (infixS)  
	//2. add IRNode(s) (like exitAssign_stmt)   
	//3. infixS.clear();
	//OR if tf_flag == 1, just evaluate TRUE/FALSE
	@Override public void exitCond(MicroParser.CondContext ctx) { 
		System.out.println("Exit cond: " + infixS);
	}


	/* ********* PRINTS OUT VARIABLE INFORMATION ******* */
	//Parameter Declarations
	@Override public void enterParam_decl_list(MicroParser.Param_decl_listContext ctx) { 
		String txt = ctx.getText();
		if(txt.compareTo("") != 0) {			
			String [] vars = txt.split(",");
			ArrayList<String> tdata = new ArrayList<String>();

			for (int i = 0; i < vars.length ; i++) {
				String name = vars[i].split("INT|FLOAT")[1];
				String type = vars[i].split(name)[0];

				//Add Tiny to IRList
				tdata.clear();
				tdata.add(name);
				this.meIRL.add(new IRNode(tdata, "var"));

				//Add variable info to current scope's val
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(name);
				temp.add(type);
				ArrayList<List<String>> stHash = st.varMap.get(st.scope);
				if(stHash == null){
					stHash = new ArrayList<List<String>>();
				}
				st.checkDeclError(name);				
				stHash.add(temp);
				st.varMap.put(st.scope, stHash);
			}
		}
	}

	//INT/FLOAT
	@Override public void enterVar_decl(MicroParser.Var_declContext ctx){
	    String idlist = ctx.getText().split("INT|FLOAT")[1];
	    String type = ctx.getText().split(idlist)[0];
		ArrayList<String> tdata = new ArrayList<String>();

	    idlist = idlist.split(";")[0];
	    String [] ids = idlist.split(","); //split the ids into separate fields
	    for (int i = 0; i < ids.length; i++) {
	    	//Add variable info to current scope's val   	
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(ids[i]);
			temp.add(type);

			//Add Tiny to IRList
			tdata.clear();
			tdata.add(ids[i]);
			this.meIRL.add(new IRNode(tdata, "var"));

			ArrayList<List<String>> stHash = st.varMap.get(st.scope);
			if(stHash == null){
				stHash = new ArrayList<List<String>>();
			}
			st.checkDeclError(ids[i]);				
			stHash.add(temp);
			st.varMap.put(st.scope, stHash);
	    }
	}

	//STRING
	@Override public void enterString_decl(MicroParser.String_declContext ctx) { 
		String txt = ctx.getText();
		String [] id_val = txt.split(":=");
		String val = id_val[1].split(";")[0];
		String id = id_val[0].split("STRING")[1];
		ArrayList<String> tdata = new ArrayList<String>();

		//Add Tiny to IRList
		tdata.add(id);
		tdata.add(val);
		this.meIRL.add(new IRNode(tdata, "str"));

		//Add variable info to current scope's val
		ArrayList<List<String>> table = st.varMap.get(st.scope);
		if(table == null){
			table = new ArrayList<List<String>>();
		}
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(id);
		temp.add("STRING");
		temp.add(val);
		st.checkDeclError(id);
		table.add(temp); //add the constructed vardata
		st.varMap.put(st.scope, table);
	}

	@Override public void enterAssign_stmt(MicroParser.Assign_stmtContext ctx) { 
		infixS.clear();
	}

	@Override public void exitAssign_stmt(MicroParser.Assign_stmtContext ctx) { 		String txt = ctx.getText();
		String id = txt.split(":=")[0];
		String expr = txt.split(":=")[1].split(";")[0];

		// System.out.println(expr);
		//if the variable was declared, add its data
		try{
			if(Integer.valueOf(expr) instanceof Integer){
				for (List<String> vardata : st.varMap.get("GLOBAL")){
					if(id.equals(vardata.get(0))){
						IRNode.tempCnt++;
						this.meIRL.add(new IRNode("STOREI", expr, "", "$T" + IRNode.tempCnt, null));
						this.meIRL.add(new IRNode("STOREI", "$T" + IRNode.tempCnt, "", id, null));
					}
				}
			}
		} catch (Exception err1){
			try{					
				if(Float.valueOf(expr) instanceof Float){
					for (List<String> vardata : st.varMap.get("GLOBAL")){
						if(id.equals(vardata.get(0))){
							IRNode.tempCnt++;
							this.meIRL.add(new IRNode("STOREF", expr, "", "$T" + IRNode.tempCnt, null));
							this.meIRL.add(new IRNode("STOREF", "$T" + IRNode.tempCnt, "", id, null));
						}
					}
				}			
			}
			catch(Exception err2){	
					String type = "";
					ArrayList<List<String>> varList = st.varMap.get("GLOBAL"); 
				    if(varList != null){  
				      for(List<String> varData : varList){
				      	if(varData.get(0).equals(id)){
				      		type = varData.get(1);
				      	}
				      }
				    }

					ShuntingYard sy = new ShuntingYard();
					String postfixS = sy.infixToPostfix(infixS);

					//Tests Postfix Tree
					PostfixTree pfTree = new PostfixTree();
					PostfixTreeNode root = pfTree.createTree(postfixS);

					//adds tree to IRList
					root.toIRList(root, this.meIRL, type);
				  	this.meIRL.add(new IRNode("STOREI", "$T"+ IRNode.tempCnt, "", id, null));
				  							
			}
		} 
	}

	@Override public void enterAddop(MicroParser.AddopContext ctx) { 
		infixS.add(ctx.getText());
	}
	@Override public void enterMulop(MicroParser.MulopContext ctx) { 
		infixS.add(ctx.getText());
	}
	@Override public void enterPrimary(MicroParser.PrimaryContext ctx) { 
		if(ctx.getText().toCharArray()[0] == '('){
			infixS.add("(");
		} else{
			infixS.add(ctx.getText());	
		}	
	}	

	@Override public void exitPrimary(MicroParser.PrimaryContext ctx) { 
		if(ctx.getText().toCharArray()[0] == '('){
			infixS.add(")");
		}
	}	

	@Override public void enterWrite_stmt(MicroParser.Write_stmtContext ctx) { 
		//TODO: split based on LIST of ids
		String txt = ctx.getText();
		String id = txt.split("\\(")[1].split("\\)")[0];

		String type = "";
		ArrayList<List<String>> varList = st.varMap.get("GLOBAL"); 
	    if(varList != null){  
	      for(List<String> varData : varList){
	      	if(varData.get(0).equals(id)){
	      		type = varData.get(1);
	      	}
	      }
	    }		

		if(type.equals("INT"))
			this.meIRL.add(new IRNode("WRITEI", id, "", "", null));
		if(type.equals("FLOAT"))
			this.meIRL.add(new IRNode("WRITEF", id, "", "", null));			
	}

}