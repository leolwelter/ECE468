public class AntlrMicroListener extends MicroBaseListener {

	@Override public void enterVar_decl(MicroParser.Var_declContext ctx){
	    System.out.println(ctx.getText() + "\n");
	}

	@Override public void enterString_decl(MicroParser.String_declContext ctx) { 
		System.out.println(ctx.getText() + "\n");
	}

}