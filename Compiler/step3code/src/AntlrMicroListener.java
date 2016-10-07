import java.lang.*;

public class AntlrMicroListener extends MicroBaseListener {

	//Prints the scopes as they're entered
	//Functions
	@Override public void enterFunc_declarations(MicroParser.Func_declarationsContext ctx) { 
		System.out.println(ctx.getText().split("BEGIN")[0] + "\n");
	}
	//Loops
	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) { 
		System.out.println("Entering DO WHILE block\n");
	}
	//IFs
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) { 
		System.out.println("Entering IF \n");
	}
	//ELSIFs
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) { 
		if(ctx.getText().compareTo("") != 0)
			System.out.println(ctx.getText() + "\n");
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