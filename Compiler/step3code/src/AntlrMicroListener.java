import java.lang.*;
import java.util.*;

public class AntlrMicroListener extends MicroBaseListener {
  	public static int blockNo = 1; //used to keep track of block scopes

	//Used to interface with HashMap functions
	public SymbolTable sym = new SymbolTable();
	public HashMap<String, String> st = new HashMap<String, String>();
	public String pOut = "";
	public String declError = null;

	//Prints the scopes as they're entered

	//Functions
	@Override public void enterFunc_decl(MicroParser.Func_declContext ctx) { 
		if(ctx.getText().compareTo("END") != 0){
			this.st = new HashMap<String, String>();	
			String txt = ctx.getText().split("BEGIN")[0];
			txt = txt.split("INT|FLOAT|VOID|STRING")[1];
			txt = txt.split("\\(")[0];
			this.pOut += "\nSymbol table " + txt + "\n";
		}
	}

	//Loops
	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) { 
		this.st = new HashMap<String, String>();		
		this.pOut += "\nSymbol table BLOCK " + blockNo + "\n";
		blockNo++;	
	}

	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) { 
		this.st = new HashMap<String, String>();		
		this.pOut += "\nSymbol table BLOCK " + blockNo + "\n";
		blockNo++;
	}

	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) { 
		this.st = new HashMap<String, String>();		
		if((ctx.getText().compareTo("") != 0) && 
			(ctx.getText().compareTo("ENDIF") != 0)){	
			this.pOut += "\nSymbol table BLOCK " + blockNo + "\n";
			blockNo++;
		}
	}



	/* ********* PRINTS OUT VARIABLE INFORMATION ******* */
	//Parameter Declarations
	@Override public void enterParam_decl_list(MicroParser.Param_decl_listContext ctx) { 
		String txt = ctx.getText();
		if(txt.compareTo("") != 0) {			
			String [] vars = txt.split(",");
			for (int i = 0; i < vars.length ; i++) {
				String name = vars[i].split("INT|FLOAT")[1];
				String type = vars[i].split(name)[0];
				if(this.st.get(name) != null){
					if(this.declError == null)					
					this.declError = "DECLARATION ERROR " + name;
				}
				this.st.put(name, type);
				this.pOut += "name " + name + " type " + type + "\n";
			}
		}
	}

	//INT/FLOAT
	@Override public void enterVar_decl(MicroParser.Var_declContext ctx){
	    String idlist = ctx.getText().split("INT|FLOAT")[1];
	    String type = ctx.getText().split(idlist)[0];

	    idlist = idlist.split(";")[0];
	    String [] ids = idlist.split(","); //split the ids into separate fields
	    for (int i = 0; i < ids.length; i++) {
			if(this.st.get(ids[i]) != null){
				if(this.declError == null)
				this.declError = "DECLARATION ERROR " + ids[i];
			}
			this.st.put(ids[i], type);	    	
	    	this.pOut += "name " + ids[i] + " type " + type + "\n";
	    }
	}

	//STRING
	@Override public void enterString_decl(MicroParser.String_declContext ctx) { 
		String txt = ctx.getText();
		String [] id_val = txt.split(":=");
		String val = id_val[1].split(";")[0];
		String id = id_val[0].split("STRING")[1];
		if(this.st.get(id) != null){
			if(this.declError == null)			
			this.declError = "DECLARATION ERROR " + id;
		}
		this.st.put(id, "STRING " + val);		
		this.pOut += "name " + id + " type STRING value " + val + "\n";
	}

}