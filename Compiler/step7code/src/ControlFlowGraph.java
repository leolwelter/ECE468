import java.util.*;

public class ControlFlowGraph{
	//Fields
	public ArrayList<IRNode> irList;
	public LinkedHashMap<String, Function> functionTable;

	//Constructors
	public ControlFlowGraph(LinkedHashMap<String, Function> functionTable){
		this.functionTable = functionTable;
		createCFG(functionTable);
	}

	//Methods
	public void createCFG(LinkedHashMap<String, Function> functionTable){
	 	for(Function func : functionTable.values()){
			if(func.meIRL.size() != 0) {
				IRNode p = func.meIRL.get(0);
				for(int i = 1; i < func.meIRL.size(); i++){
					if(!(p.opcode.equals("JUMP"))){
						func.meIRL.get(i).addPredecessor(p);
					}
					p = func.meIRL.get(i);
			 	}

				IRNode s;
				IRNode temp;
				for(int i = 0; i < func.meIRL.size(); i++){
					temp = func.meIRL.get(i);
					if(i+1 < func.meIRL.size() && !(temp.opcode.equals("JUMP"))){
						s = func.meIRL.get(i + 1);
						temp.addSuccessor(s);
					}

					if(temp.opcode.equals("GT") || temp.opcode.equals("GE") || temp.opcode.equals("LT") || temp.opcode.equals("LE") || temp.opcode.equals("NE") || temp.opcode.equals("EQ") || temp.opcode.equals("JUMP")){
						int j = 0;
						boolean labelNotFound = true;
						String labelName = temp.result;
						while((j < func.meIRL.size()) && labelNotFound){
							if(func.meIRL.get(j).opcode.equals("LABEL") && func.meIRL.get(j).result.equals(labelName)){
								s = func.meIRL.get(j);
								temp.addSuccessor(s);
								p = temp;
								func.meIRL.get(j).addPredecessor(p);
								labelNotFound = false;
							}
							j++;
						}
					}
			 	}
		 	}
		}		
	}//createCFG

	public void printCFG(){
	 	//PRINT FUNCTION CFG NODES
	 	for(Function func : functionTable.values()){	 		
			for(int i = 0; i < func.meIRL.size(); i++){
				System.out.println("--------------------------------------------------------------");
				//Print Predecessors
				System.out.println("Predecessors : ");
				for(IRNode p1 : func.meIRL.get(i).predecessors){
					p1.printNode();
				}
				System.out.println();
				
				//Print Current
				System.out.println("Current : ");
				func.meIRL.get(i).printNode();
				System.out.println();
				

				//Print Successors
				System.out.println("Successors : ");
				for(IRNode s1 : func.meIRL.get(i).successors){
					s1.printNode();
				}
		 	}
	 	}
	}
	
}