import java.lang.*;
import java.util.*;

public class AntlrMicroListener extends MicroBaseListener {
	//Fields
	public SymbolTable st;
	public LinkedList<IRNode> meIRL;


	//Custom Constructor
	public AntlrMicroListener(SymbolTable st, LinkedList<IRNode> irList){
		this.st = st;
		this.meIRL = irList;
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
	}

	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) { 
		//Set new SymbolTable scope
		st.next = new SymbolTable("BLOCK");
		st = st.next;
	}

	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) { 	
		if((ctx.getText().compareTo("") != 0) && 
			(ctx.getText().compareTo("ENDIF") != 0)){	
			//Set new SymbolTable scope
			st.next = new SymbolTable("BLOCK");
			st = st.next;			
		}
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
		String txt = ctx.getText();
		String id = txt.split(":=")[0];
		String expr = txt.split(":=")[1].split(";")[0];

		ArrayList<String> tdata = new ArrayList<String>();

		//if the variable was declared, add its data
		for (List<String> vardata : st.varMap.get("GLOBAL")){
			if(id.equals(vardata.get(0))){
				tdata.add(id);
				tdata.add(expr);
				meIRL.add(new IRNode(tdata, "storei"));
			}
		}
	}
}