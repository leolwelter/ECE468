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

  public void toIRList(PostfixTreeNode node, LinkedList<IRNode> meTooThanks, String type, Function fy){
  	PostfixTreeNode left = node.left;
  	PostfixTreeNode right = node.right;
    //System.out.println(node.value);
  	if(left != null)
  		toIRList(left, meTooThanks, type, fy);
  	if(right != null)
  		toIRList(right, meTooThanks, type, fy);

  	//Post-order Processing:
  	if(isleaf(left) && isleaf(right)){
  		//This is a subexpression; merge it
  		// System.out.println("subexpression: " + left.value + node.value + right.value);
  		mergeTreeNodes(node, left, right, meTooThanks, type, fy);
  	}
  }

  public boolean isleaf(PostfixTreeNode node){
  	if(node == null)
  		return false;
  	if((node.left == null) && (node.right == null))
  		return true;
  	return false;
  }

  public void mergeTreeNodes(PostfixTreeNode node, PostfixTreeNode left, PostfixTreeNode right, LinkedList<IRNode> meIRL, String type, Function fy){
  	IRNode ir = new IRNode();
  	ir.treeToIR(node, left, right, type, fy); //creates ir from subexpression
    //ir.printNode();
    meIRL.add(ir);
  	node.value = ir.result;
  	node.left = null;
  	node.right = null;

  }
}
