import java.lang.*;

public class AntlrMicroListener extends MicroBaseListener {

	//Used to interface with SymbolTable functions
	SymbolTable st = new SymbolTable();

	//Prints the scopes as they're entered
	//Functions
	@Override public void enterFunc_decl(MicroParser.Func_declContext ctx) { 
		if(ctx.getText().compareTo("END") != 0){	
			String txt = ctx.getText().split("BEGIN")[0];
			txt = txt.split("INT|FLOAT|VOID|STRING")[1];
			txt = txt.split("\\(")[0];
			System.out.println("\nSymbol table " + txt + "\n");
		}
	}
	//Loops
	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) { 
		System.out.println("\nSymbol table BLOCK " + st.getBlockNumber());
	}
	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) { 
		System.out.println("\nSymbol table BLOCK " + st.getBlockNumber());
	}
	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) { 
		if((ctx.getText().compareTo("") != 0) && 
			(ctx.getText().compareTo("ENDIF") != 0)){	
			System.out.println("\nSymbol table BLOCK " + st.getBlockNumber());
		}
	}	

	/* PRINTS OUT VARIABLE INFORMATION */

	//INT/FLOAT
	@Override public void enterVar_decl(MicroParser.Var_declContext ctx){
	    System.out.println(ctx.getText() + "\n");
	}
	//STRING
	@Override public void enterString_decl(MicroParser.String_declContext ctx) { 
		System.out.println(ctx.getText() + "\n");
	}

}