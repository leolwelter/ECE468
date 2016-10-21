import java.io.IOException;
import java.io.File;
import java.lang.Object;
import java.util.*;

public class PostfixTreeNode {
  public String value;
  public PostfixTreeNode left = null;
  public PostfixTreeNode right = null;

  //**CONSTRUCTORS
  public PostfixTreeNode (String value, PostfixTreeNode left, PostfixTreeNode right) {
    this.value = value;
    this.left = left;
    this.right = right;
  }
  
  public PostfixTreeNode (String value){
  	this.value = value;
  	this.left = null;
  	this.right = null;
  }

  //**INSTANCE METHODS
  public void printTree(PostfixTreeNode node){
  	//check node == null
  	if(node == null)
  		return;

	PostfixTreeNode left = node.left;
    PostfixTreeNode right = node.right;
    if(left != null)
      printTree(left);
    if(right != null)
      printTree(right);

    System.out.println("Val: " + node.value);
  }

  public void toIRList(PostfixTreeNode node, LinkedList<IRNode> meTooThanks){
  	PostfixTreeNode left = node.left;
  	PostfixTreeNode right = node.right;

  	if(left != null)
  		toIRList(left, meTooThanks);
  	if(right != null)
  		toIRList(right, meTooThanks);

  	//Post-order Processing:
  	if(isleaf(left) && isleaf(right)){
  		//This is a subexpression; merge it
  		// System.out.println("subexpression: " + left.value + node.value + right.value);
  		mergeTreeNodes(node, left, right, meTooThanks);
  	}
  }

  public boolean isleaf(PostfixTreeNode node){
  	if(node == null)
  		return false;
  	if((node.left == null) && (node.right == null))
  		return true;
  	return false;
  }  

  public void mergeTreeNodes(PostfixTreeNode node, PostfixTreeNode left, PostfixTreeNode right, LinkedList<IRNode> meIRL){
  	IRNode ir = new IRNode();
  	ir.treeToIR(node, left, right); //creates ir from subexpression
  	meIRL.add(ir); 
  	node.value = ir.result;
  	node.left = null;
  	node.right = null;
  	
  }
}
