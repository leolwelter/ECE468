import java.util.*;

public class Function{
	//Fields
	public SymbolTable st;
	public LinkedList<IRNode> meIRL;
	public String name;
	public int localCnt;
	public int paramCnt;


	//Constructors
	public Function(SymbolTable s, LinkedList<IRNode> ll, String name, int num1, int num2){
		this.st = s;
		this.meIRL = ll;
		this.name = name;
		this.localCnt = num1;
		this.paramCnt = num2;
	}
}
