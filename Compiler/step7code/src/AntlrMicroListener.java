import java.lang.*;
import java.util.*;

public class AntlrMicroListener extends MicroBaseListener {
	//Fields
	public SymbolTable st;
	public LinkedList<IRNode> meIRL;
	public ArrayList<String> infixS = new ArrayList<String>();
	public Stack<String> enterStack;
	public Stack<String> endStack;
	public String cmp;
	public String lhsType, rhsType;
	public int lhsTemp;
	public LinkedHashMap<String, Function> functionTable;
	public static int condCount = 0; //conditional count
	public static int tf_flag = 0;
	public static Stack<String> programStack = null;
	public Function fy;
	public String lhsReg = "";
	public String rhsReg = "";
	public boolean isGlobal = true;
	public boolean retPushed = false;

	//Custom Constructor
	public AntlrMicroListener(SymbolTable st, LinkedList<IRNode> irList){
		this.st = st;
		this.meIRL = irList;
		this.enterStack = new Stack<String>();
		this.endStack = new Stack<String>();
		this.functionTable = new LinkedHashMap<String, Function>();
		this.functionTable.put("GLOBAL", new Function(st, meIRL, "GLOBAL", 1, 1));
	}

	//Prints the scopes as they're entered
	//Functions
	@Override public void enterFunc_decl(MicroParser.Func_declContext ctx) {
		isGlobal = false;
		if(ctx.getText().compareTo("END") != 0){
			String txt = ctx.getText().split("BEGIN")[0];
			txt = txt.split("INT|FLOAT|VOID|STRING")[1];
			txt = txt.split("\\(")[0];
			//Set new SymbolTable scope
			st = new SymbolTable(txt);
			//Make new IRList and SymbolTable for new Function
			meIRL = new LinkedList<IRNode>();

			//CLEAR ALL INSTANCE/GLOBALS
			infixS = new ArrayList<String>();
			enterStack.clear();
			endStack.clear();
			cmp = null;
			lhsType = null;
			rhsType = null;
			lhsTemp = 0;
			// condCount = 0;
			tf_flag = 0;
			fy = new Function(st, meIRL, txt, 1, 0);
			functionTable.put(txt, fy);
			//st = st.next;

			IRNode.regNo = 0;
			IRNode.tempCnt = 0;

			meIRL.add(new IRNode("LABEL", "", "", txt));
			meIRL.add(new IRNode("LINK", "", "", ""));
		}
	}

	@Override public void exitFunc_decl(MicroParser.Func_declContext ctx){
		if(!this.meIRL.get(this.meIRL.size() - 1).opcode.equals("RET"))
			meIRL.add(new IRNode("RET", "", "", ""));
	}

	@Override public void enterReturn_stmt(MicroParser.Return_stmtContext ctx) {
		infixS.clear();
	}

	@Override public void exitReturn_stmt(MicroParser.Return_stmtContext ctx) {
		String retInput = ctx.getText().split("RETURN")[1];
		retInput = retInput.split(";")[0];
		ArrayList<String> retVarsList = new ArrayList<String> (Arrays.asList(retInput.split("\\+|\\-|\\*|\\/")));
		Integer len = retVarsList.size();

		// System.out.println(retVarsList);

		if(!(retInput.equals(""))){

			try{
				if(Integer.valueOf(retInput) instanceof Integer){
					IRNode.tempCnt++;
					this.meIRL.add(new IRNode("STOREI", retInput, "", "$T" + IRNode.tempCnt));
					this.meIRL.add(new IRNode("STOREI", "$T" + IRNode.tempCnt, "", "$R"));
				}
			}
			catch (Exception err1){
				try{
					if(Float.valueOf(retInput) instanceof Float){
						IRNode.tempCnt++;
						this.meIRL.add(new IRNode("STOREF", retInput, "", "$T" + IRNode.tempCnt));
						this.meIRL.add(new IRNode("STOREF", "$T" + IRNode.tempCnt, "", "$R"));
					}
				}
				catch(Exception err2){
					String type = "";
					String retReg = "";
					ArrayList<List<String>> varList = st.varMap.get(fy.name);
					if(len == 1){
						if(varList != null){
								for(List<String> varData : varList){
									if(varData.get(0).equals(retInput)){
										type = varData.get(1);
										retReg = varData.get(3);
										// System.out.println(retReg);
										if(type.compareTo("FLOAT") == 0){
											this.meIRL.add(new IRNode("STOREF", retReg, "", "$T" + IRNode.tempCnt));
											this.meIRL.add(new IRNode("STOREF", "$T" + IRNode.tempCnt, "", "$R"));
										} else {
											this.meIRL.add(new IRNode("STOREI", retReg, "", "$T" + IRNode.tempCnt));
											this.meIRL.add(new IRNode("STOREI", "$T" + IRNode.tempCnt, "", "$R"));
										}
									}
								}
						}
					}
					else {
						for (String retVar : retVarsList) {
							if(varList != null){
									for(List<String> varData : varList){
										if(varData.get(0).equals(retVar)){
											type = varData.get(1);
										}
									}
								}
						}
					}
						if(retReg.compareTo("") == 0){
							ShuntingYard sy = new ShuntingYard();
							String postfixS = sy.infixToPostfix(infixS);

							//Tests Postfix Tree
							PostfixTree pfTree = new PostfixTree();
							PostfixTreeNode root = pfTree.createTree(postfixS);

							//adds tree to IRList
							root.toIRList(root, this.meIRL, type, fy);
							if(type.compareTo("FLOAT") == 0){
						  		this.meIRL.add(new IRNode("STOREF", "$T"+ IRNode.tempCnt, "", "$R"));
							} else {
								this.meIRL.add(new IRNode("STOREI", "$T"+ IRNode.tempCnt, "", "$R"));
							}
						}
				}
			}
		}
		this.meIRL.add(new IRNode("RET", "", "", ""));
	}


	//Loops
	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) {
		//Set new SymbolTable scope
		st.next = new SymbolTable("BLOCK");
		st = st.next;

		//TODO: Push while to cond stack
		enterStack.push("label" + condCount++);
		endStack.push("label" + condCount++);

		meIRL.add(new IRNode("LABEL", "", "", endStack.peek()));

	}

	@Override public void exitDo_while_stmt(MicroParser.Do_while_stmtContext ctx){
		meIRL.add(new IRNode("JUMP", "", "", endStack.pop()));
		meIRL.add(new IRNode("LABEL", "", "", enterStack.pop()));
	}

	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) {
		//Set new SymbolTable scope
		st.next = new SymbolTable("BLOCK");
		st = st.next;

		//Conditional State Tracking
		enterStack.push("label" + condCount++);
		endStack.push("label" + condCount++);

	}

	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) {
		//System.out.println("THIS IS ELSE PART:");
		//System.out.println(ctx.getText().split("\\(")[0]);
		//System.out.println(ctx.getText());
		if ((ctx.getText().compareTo("ENDIF") != 0) && (ctx.getText().compareTo("") !=0)){

			//Set new SymbolTable scope
			st.next = new SymbolTable("BLOCK");
			st = st.next;

			//Conditional State Tracking
			meIRL.add(new IRNode("JUMP", "", "", endStack.peek()));
			meIRL.add(new IRNode("LABEL", "", "", enterStack.pop()));

			enterStack.push("label" + condCount++);
		}

		/*
			st.next = new SymbolTable("BLOCK");
			st = st.next;

			//Conditional State Tracking
			meIRL.add(new IRNode("JUMP", "", "", endStack.peek()));
			while(enterStack.empty() == false){
				meIRL.add(new IRNode("LABEL", "", "", enterStack.pop()));
			}

			enterStack.push("label" + condCount++);
			*/

	}

	//ENDIF handling
	@Override public void exitIf_stmt(MicroParser.If_stmtContext ctx) {
		//Stack Handling

		//Create IRNodes related to ENDIF statements
		meIRL.add(new IRNode("JUMP", "", "", endStack.peek(), null));
		meIRL.add(new IRNode("LABEL", "", "", endStack.pop()));
		meIRL.add(new IRNode("LABEL", "", "", enterStack.pop()));
	}
	//IRNode( opcode,  op1,  op2,  result,  bTarget){

	@Override public void enterCond(MicroParser.CondContext ctx) {
		//clear the infix (make way for more expressions)
		infixS.clear();
		if (ctx.getText().equals("TRUE")) {
			infixS.add("1");
			tf_flag = 1;
		}else if(ctx.getText().equals("FALSE")){
			infixS.add("0");
			tf_flag = 1;
		}

	}

	//1. eval LHS (infixS)
	//2. add IRNode(s)
	//3. infixS.clear(); (done in exitCompop)
	@Override public void enterCompop(MicroParser.CompopContext ctx) {
//		System.out.println("Enter compop: " + infixS);
//		System.out.println(ctx.getText());

		String lhs = infixS.get(0); //WILL NOT WORK WITH EXPRESSIONS
		//Determine the type of var on LHS
		try{
			if(Integer.valueOf(lhs) instanceof Integer){
				IRNode.tempCnt++;
				this.meIRL.add(new IRNode("STOREI", lhs, "", "$T" + IRNode.tempCnt, "INT"));
				lhsType = "INT";
				lhsReg = "$T" + IRNode.tempCnt;
			}
		}
		catch (Exception err1){
			try{
				if(Float.valueOf(lhs) instanceof Float){
					IRNode.tempCnt++;
					this.meIRL.add(new IRNode("STOREF", lhs, "", "$T" + IRNode.tempCnt, "FLOAT"));
					lhsType = "FLOAT";
					lhsReg = "$T" + IRNode.tempCnt;
				}
			}
			catch(Exception err2){
					String type = "";
					ArrayList<List<String>> varList = st.varMap.get(fy.name);
				    if(varList != null){
				      for(List<String> varData : varList){
				      	if(varData.get(0).equals(lhs)){
				      		type = varData.get(1);
									lhsReg = varData.get(3);
									lhsType = type;
				      	}
				      }
				    }

					// System.out.println(lhsReg);

					if(lhsReg.equals("")) {
						for (String i : infixS) {
							if(varList != null){
									for(List<String> varData : varList){
										if(varData.get(0).equals(i)){
											type = varData.get(1);
										}
									}
								}
						}

						ShuntingYard sy = new ShuntingYard();
						String postfixS = sy.infixToPostfix(infixS);

						//Tests Postfix Tree
						PostfixTree pfTree = new PostfixTree();
						PostfixTreeNode root = pfTree.createTree(postfixS);

						//adds tree to IRList
						// System.out.println(type);
						root.toIRList(root, this.meIRL, type, fy);
						if(type.compareTo("FLOAT") == 0){
					  		this.meIRL.add(new IRNode("STOREF", lhs, "", "$T"+ IRNode.tempCnt, "FLOAT"));
						} else {
							this.meIRL.add(new IRNode("STOREI", lhs, "", "$T"+ IRNode.tempCnt, "INT"));
						}
							// System.out.println("Type: " + type);
					  	lhsType = type;
					  	lhsReg = "$T" + IRNode.tempCnt;
					}
			}
		}

	}

	@Override public void exitCompop(MicroParser.CompopContext ctx) {
		infixS.clear(); //make way for RHS
		cmp = ctx.getText();
	}



	//1. eval RHS (infixS)
	//2. add IRNode(s) (like exitAssign_stmt)
	//3. infixS.clear();
	//OR if tf_flag == 1, just evaluate TRUE/FALSE
	@Override public void exitCond(MicroParser.CondContext ctx) {
		//System.out.println("Exit cond: " + infixS);

		if ((tf_flag == 1) && (infixS.get(0).equals("1"))) {
			IRNode.tempCnt++;
			this.meIRL.add(new IRNode("STOREI", "1", "", "$T"+ IRNode.tempCnt));
			IRNode.tempCnt++;
		  	this.meIRL.add(new IRNode("STOREI", "1", "", "$T"+ IRNode.tempCnt));
		  	this.meIRL.add(new IRNode("NE", "$T"+IRNode.tempCnt, "$T"+(IRNode.tempCnt - 1), enterStack.peek(), lhsType));
				tf_flag = 0;
		} else if ((tf_flag == 1) && (infixS.get(0).equals("0"))) {
			IRNode.tempCnt++;
			this.meIRL.add(new IRNode("STOREI", "0", "", "$T"+ IRNode.tempCnt));
			IRNode.tempCnt++;
		  	this.meIRL.add(new IRNode("STOREI", "1", "", "$T"+ IRNode.tempCnt));
		  	this.meIRL.add(new IRNode("NE", "$T"+IRNode.tempCnt, "$T"+(IRNode.tempCnt - 1), enterStack.peek(), lhsType));
				tf_flag = 0;
		} else {
			//Evaluate RHS
			String rhs = infixS.get(0); //WILL NOT WORK WITH EXPRESSIONS
			//Determine the type of var on RHS
			try{
				if(Integer.valueOf(rhs) instanceof Integer){
					IRNode.tempCnt++;
					this.meIRL.add(new IRNode("STOREI", rhs, "", "$T" + IRNode.tempCnt));
					rhsType = "INT";
					rhsReg = "$T" + IRNode.tempCnt;
				}
			}
			catch (Exception err1){
				try{
					if(Float.valueOf(rhs) instanceof Float){
						IRNode.tempCnt++;
						this.meIRL.add(new IRNode("STOREF", rhs, "", "$T" + IRNode.tempCnt));
						rhsType = "FLOAT";
						rhsReg = "$T" + IRNode.tempCnt;
					}
				}
				catch(Exception err2){
						String type = "FLOAT";
						IRNode.tempCnt++;
						ArrayList<List<String>> varList = st.varMap.get(fy.name);
					    if(varList != null){
					      for(List<String> varData : varList){
					      	if(varData.get(0).equals(rhs)){
					      		type = varData.get(1);
										rhsReg = varData.get(3);
								/*if(type.compareTo("FLOAT") == 0){
							  		this.meIRL.add(new IRNode("STOREF", rhs, "", "$T"+ IRNode.tempCnt));
								} else {
									this.meIRL.add(new IRNode("STOREI", rhs, "", "$T"+ IRNode.tempCnt));
								}*/
					      	}
					      }
					    }

						ShuntingYard sy = new ShuntingYard();
						String postfixS = sy.infixToPostfix(infixS);

						//Tests Postfix Tree
						//System.out.println("postfixS: " + postfixS);
						PostfixTree pfTree = new PostfixTree();
						PostfixTreeNode root = pfTree.createTree(postfixS);

						//adds tree to IRList
						//System.out.println("Type: " + type);
						root.toIRList(root, this.meIRL, type, fy);

						//RHS is an expression (no store manual necessary)
					  rhsType = type;
						if(rhsReg.compareTo("") == 0){
							rhsReg = "$T"+ IRNode.tempCnt;
						}
				}
			}

			//Add Intermediate Nodes
			// System.out.println(lhsType);
			switch(cmp){
				case ">":
					this.meIRL.add(new IRNode("LE", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
				case ">=":
					this.meIRL.add(new IRNode("LT", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
				case "<":
					this.meIRL.add(new IRNode("GE", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
				case "<=":
					this.meIRL.add(new IRNode("GT", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
				case "!=":
					this.meIRL.add(new IRNode("EQ", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
				case "=":
					this.meIRL.add(new IRNode("NE", lhsReg, rhsReg, enterStack.peek(), lhsType));
					break;
			}
		}

		cmp = "";
		lhsType = "";
		rhsType = "";
		lhsTemp = -1;
		lhsReg = "";
		rhsReg = "";
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

				if(isGlobal){
					this.meIRL.add(new IRNode(tdata, "var"));
				}

				//Add variable info to current scope's val
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(name);
				temp.add(type);
				temp.add(null);
				temp.add("$P" + (++fy.paramCnt));
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
				temp.add(null);
				temp.add("$L" + this.fy.localCnt++);

				//Add Tiny to IRList
				tdata.clear();
				tdata.add(ids[i]);
				if(isGlobal){
					this.meIRL.add(new IRNode(tdata, "var"));
				}

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
		this.meIRL.add(new IRNode("str", id, "", val));

		//Add variable info to current scope's val
		ArrayList<List<String>> table = st.varMap.get(st.scope);
		if(table == null){
			table = new ArrayList<List<String>>();
		}
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(id);
		temp.add("STRING");
		temp.add(val);
		temp.add(null);
		st.checkDeclError(id);
		table.add(temp); //add the constructed vardata
		st.varMap.put(st.scope, table);
	}

	@Override public void enterAssign_stmt(MicroParser.Assign_stmtContext ctx) {
		infixS.clear();
	}

	@Override public void exitAssign_stmt(MicroParser.Assign_stmtContext ctx) {
		String txt = ctx.getText();
		String id = txt.split(":=")[0];
		String idReg = "";
		String exprReg = "";
		String expr = txt.split(":=")[1].split(";")[0];

		// System.out.println(expr);
		//if the variable was declared, add its data

		try{
			if(Integer.valueOf(expr) instanceof Integer){
				for (List<String> vardata : st.varMap.get(fy.name)){
					if(id.equals(vardata.get(0))){
						IRNode.tempCnt++;
						idReg = vardata.get(3);
						this.meIRL.add(new IRNode("STOREI", expr, "", "$T" + IRNode.tempCnt));
						this.meIRL.add(new IRNode("STOREI", "$T" + IRNode.tempCnt, "", idReg));
					}
				}
			}
		}
		catch (Exception err1){
			try{
				if(Float.valueOf(expr) instanceof Float){
					for (List<String> vardata : st.varMap.get(fy.name)){
						if(id.equals(vardata.get(0))){
							IRNode.tempCnt++;
							idReg = vardata.get(3);
							this.meIRL.add(new IRNode("STOREF", expr, "", "$T" + IRNode.tempCnt));
							this.meIRL.add(new IRNode("STOREF", "$T" + IRNode.tempCnt, "", idReg));
						}
					}
				}
			}
			catch(Exception err2){
					//IRNode.tempCnt++;
					String type = "";
					ArrayList<List<String>> varList = st.varMap.get(fy.name);
					//System.out.println(varList);
					if(varList != null){
						//System.out.println("VARLIST NOT EMPTY!!!!!!!!!");
				      for(List<String> varData : varList){
					      	if(varData.get(0).equals(id)){
								//System.out.println("GETTING TYPE : " + varData.get(1));
					      		type = varData.get(1);
								idReg = varData.get(3);
					      	}
							if(varData.get(0).equals(expr)){
								exprReg = varData.get(3);
					      	}
				      	}
				   	}

				    if(infixS.size() != 1){
							ShuntingYard sy = new ShuntingYard();
							String postfixS = sy.infixToPostfix(infixS);

							//Tests Postfix Tree
							PostfixTree pfTree = new PostfixTree();
							PostfixTreeNode root = pfTree.createTree(postfixS);

							//adds tree to IRList
							//st.printTable();
							//System.out.println("ID IS : " + id);
							//System.out.println("TYPE IS : " + type);
							root.toIRList(root, this.meIRL, type, fy);
							if(type.compareTo("FLOAT") == 0){
						  		this.meIRL.add(new IRNode("STOREF", "$T"+ IRNode.tempCnt, "", idReg));
							} else {
						 		this.meIRL.add(new IRNode("STOREI", "$T"+ IRNode.tempCnt, "", idReg));
							}
				    }
						else {
							if(meIRL.peekLast().opcode.equals("POP")){
								if(type.compareTo("FLOAT") == 0){
									this.meIRL.add(new IRNode("STOREF", "$T"+ IRNode.tempCnt, "", idReg));
								}
								else{
									this.meIRL.add(new IRNode("STOREI", "$T"+ IRNode.tempCnt, "", idReg));
								}
							}
							else{
					    	if(type.compareTo("FLOAT") == 0){
						  		this.meIRL.add(new IRNode("STOREF", exprReg, "", "$T"+ IRNode.tempCnt));
						  		this.meIRL.add(new IRNode("STOREF", "$T"+ IRNode.tempCnt, "", idReg));
								} else {
						 		this.meIRL.add(new IRNode("STOREI", exprReg, "", "$T"+ IRNode.tempCnt));
						 		this.meIRL.add(new IRNode("STOREI", "$T"+ IRNode.tempCnt, "", idReg));
								}
							}
				    }


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
		ArrayList<String> idList = new ArrayList<String>(Arrays.asList((txt.split("\\(")[1].split("\\)")[0]).split(",")));
		String idReg = "";
		String type = "";
		for(String id : idList){
			ArrayList<List<String>> varList = st.varMap.get(fy.name);
		    if(varList != null){
		      for(List<String> varData : varList){
							if(varData.get(0).equals(id)){
			      		type = varData.get(1);
								idReg = varData.get(3);
								if(type.equals("INT")){
									this.meIRL.add(new IRNode("WRITEI", idReg, "", ""));
								}
								else if(type.equals("FLOAT")){
									this.meIRL.add(new IRNode("WRITEF", idReg, "", ""));
								}
								else{
									this.meIRL.add(new IRNode("WRITES", idReg, "", ""));
								}
			      	}
		      }
		    }

				ArrayList<List<String>> varList2 = st.varMap.get("GLOBAL");
				//System.out.println(id);
				//System.out.println(varList2);
					if(varList2 != null){
			      for(List<String> varData2 : varList2){
				      	if(varData2.get(0).equals(id)){
				      		type = varData2.get(1);
									idReg = varData2.get(0);
									if(type.equals("INT")){
										this.meIRL.add(new IRNode("WRITEI", idReg, "", ""));
									}
									else if(type.equals("FLOAT")){
										this.meIRL.add(new IRNode("WRITEF", idReg, "", ""));
									}
									else{
										this.meIRL.add(new IRNode("WRITES", idReg, "", ""));
									}
				      	}
			      }
			    }
				}
		/*
		if(type.equals("INT")){
			this.meIRL.add(new IRNode("WRITEI", idReg, "", ""));
		}
		else if(type.equals("FLOAT")){
			this.meIRL.add(new IRNode("WRITEF", idReg, "", ""));
		}
		else{
			this.meIRL.add(new IRNode("WRITEF", idReg, "", ""));
		}
		*/
	}

	@Override public void enterRead_stmt(MicroParser.Read_stmtContext ctx) {
		//TODO: split based on LIST of ids
		String txt = ctx.getText();
		String id = txt.split("\\(")[1].split("\\)")[0];
		String idReg = "";
		String type = "";

		//Functions have access to their own scope
		ArrayList<List<String>> varList = st.varMap.get(fy.name);
	    if(varList != null){
	      for(List<String> varData : varList){
	      	if(varData.get(0).equals(id)){
	      		type = varData.get(1);
				idReg = varData.get(3);
	      	}
	      }
	    }

	    //Functions have access to GLOBAL scope
		ArrayList<List<String>> varList2 = st.varMap.get("GLOBAL");
		   if(varList2 != null){
		     for(List<String> varData2 : varList2){
		    	if(varData2.get(0).equals(id)){
		     		type = varData2.get(1);
					idReg = varData2.get(0);
		     	}
		    }
		}

		if(type.equals("INT"))
			this.meIRL.add(new IRNode("READI", idReg, "", ""));
		if(type.equals("FLOAT"))
			this.meIRL.add(new IRNode("READF", idReg, "", ""));
	}

	@Override public void enterCall_expr(MicroParser.Call_exprContext ctx) {
		//Push onto stack
		//(Is this correct functionality?)
		//System.out.println(ctx.getText());
		//this.meIRL.add(new IRNode("PUSH", "" , "", ""));
		//this.retPushed = true;
	}

	@Override public void exitExpr_list(MicroParser.Expr_listContext ctx) {
		//Only happens in Function calls
		//separated like: "something", "something else", "this"
		ArrayList<String> params = new ArrayList<>(Arrays.asList(ctx.getText().split(",")));
		// System.out.println(params);

		for(String param : params){
			try{
				if(Integer.valueOf(param) instanceof Integer){
							IRNode.tempCnt++;
							this.meIRL.add(new IRNode("STOREI", param, "", "$T" + IRNode.tempCnt));
							this.meIRL.add(new IRNode("PUSH", "$T" + IRNode.tempCnt, "", ""));
				}
			}
			catch (Exception err1){
				try{
					if(Float.valueOf(param) instanceof Float){
								IRNode.tempCnt++;
								this.meIRL.add(new IRNode("STOREF", param, "", "$T" + IRNode.tempCnt));
								this.meIRL.add(new IRNode("PUSH", "$T" + IRNode.tempCnt, "", ""));
					}
				}catch(Exception err2){
					String type = "";
					ArrayList<List<String>> varList = st.varMap.get(fy.name);

				    if(varList != null){
				      for(List<String> varData : varList){
				      	if(varData.get(0).equals(param)){
				      		type = varData.get(1);
									// Param is variable so push $L here
									if(!(retPushed)) {
										this.meIRL.add(new IRNode("PUSH", "" , "", ""));
										retPushed = true;
									}
									this.meIRL.add(new IRNode("PUSH", varData.get(3), "", ""));
				      	}
				      }
				    }

				    if(type.equals("")){
							type = "INT";
							ShuntingYard sy = new ShuntingYard();
							String postfixS = sy.infixToPostfix(infixS);

							//Tests Postfix Tree
							PostfixTree pfTree = new PostfixTree();
							PostfixTreeNode root = pfTree.createTree(postfixS);

							//adds tree to IRList
							root.toIRList(root, this.meIRL, type, fy);
							if(!(retPushed)) {
								this.meIRL.add(new IRNode("PUSH", "" , "", ""));
								retPushed = true;
							}
						  this.meIRL.add(new IRNode("PUSH", "$T"+ IRNode.tempCnt, "", ""));
				    }
				}
			}
		}
		// System.out.println("RP : " + retPushed);
		infixS.clear();
	}

	@Override public void exitCall_expr(MicroParser.Call_exprContext ctx) {
		String funcName = ctx.getText().split("\\(")[0];
		//System.out.println("RP : " + retPushed);
		if(!(retPushed)){
			this.meIRL.add(new IRNode("PUSH", "" , "", ""));
			//System.out.println("HEREEE");
		}
		retPushed = false;
		//Push per parameter of function
		Function func = functionTable.get(funcName);

		this.meIRL.add(new IRNode("JSR", funcName , "", "", ""));

		Function funct = functionTable.get(funcName);
		for (int i = 0; i < funct.paramCnt ; i++){
			this.meIRL.add(new IRNode("POP", "", "", ""));
		}
		IRNode.tempCnt++;
		this.meIRL.add(new IRNode("POP", "$T"  + IRNode.tempCnt, "", ""));
	}

}
