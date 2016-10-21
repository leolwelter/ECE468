import java.io.IOException;
import java.io.File;
import java.lang.Object;
import java.util.*;

public class PostfixTreeNode {
  public String value;
  public PostfixTreeNode left = null;
  public PostfixTreeNode right = null;

  public PostfixTreeNode (String value, PostfixTreeNode left, PostfixTreeNode right) {
    this.value = value;
    this.left = left;
    this.right = right;
  }
  
}
