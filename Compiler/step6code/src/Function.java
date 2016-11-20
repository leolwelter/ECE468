import java.util.*;

public class Function{
	//Fields
	public SymbolTable st;
	public LinkedList<IRNode> meIRL;
	public String name;

	//Constructors
	public Function(SymbolTable s, LinkedList<IRNode> ll, String name){
		this.st = s;
		this.meIRL = ll;
		this.name = name;
	}
}