public class TinyNode{

	//fields
	public  String opcode;
	public  String op1;
	public  String op2;
	public static int regNo = 0;

	//constructors
	public TinyNode(String opcode, String op1, String op2){
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;
	}
	//Instance methods

	public void printNode(){
		System.out.println(opcode + " " + op1 +  " " + op2);
	}



}