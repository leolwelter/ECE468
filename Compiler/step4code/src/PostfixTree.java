import java.io.IOException;
import java.io.File;
import java.lang.Object;
import java.util.*;

public class PostfixTree {
  static PostfixTreeNode createTree(String postfix) {
    Stack<PostfixTreeNode> s = new Stack<>();
    List<String> postfixArray = new ArrayList<String> (Arrays.asList(postfix.split("\\s+")));
    for (int i = 0; i < postfixArray.size(); i++) {
      //System.out.println(postfixArray.get(i));
      //check if it is an operator
      String c = postfixArray.get(i);
      if (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/")) {
        PostfixTreeNode left = s.pop();
        PostfixTreeNode right = s.pop();

        PostfixTreeNode newNode = new PostfixTreeNode(c, left, right);
        // System.out.println("Val: " + c);
        // System.out.println("left: " + left.value);
        // System.out.println("right: " +right.value + "\n");
        s.push(newNode);
      }
      else {
        //not an operator, so make node with left & right as null, and push on to stack
        PostfixTreeNode newNode = new PostfixTreeNode(c, null, null);
        // System.out.println("Val: " + c);
        // System.out.println("left: null");
        // System.out.println("right: null\n");        
        s.push(newNode);
      }
    }
    PostfixTreeNode root = s.pop();
    return root;
  }
}
