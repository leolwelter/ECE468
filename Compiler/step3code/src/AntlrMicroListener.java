public class AntlrMicroListener extends MicroBaseListener {

	@Override public void enterId(MicroParser.IdContext ctx){
	    System.out.println(ctx.getText());
	}

}