import java.io.IOException;
import java.io.File;
import java.lang.Object;
import java.util.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.*;

/*Symbol table <scope_name>
name <var_name> type <type_name>
name <var_name> type <type_name> value <string_value>
...*/

//new scopes created by:
/*
IF<block x>,
ELSIF<block x>
FOR<block x>,
WHILE<block x>,
BEGIN<block x>,

FUNCTION<name>,
*/
public class SymbolTable{
  public static int blockNo; //used to keep track of block scopes

  private SymbolTable parent; //parent scope
  private ArrayList<SymbolTable> children; //child scopes
  private String scope = null; //used to keep track of new scopes
  private LinkedHashMap<String, ArrayList<String>> varList = 
          new LinkedHashMap<String, ArrayList<String>>();

  //Constructors
  public SymbolTable(){
    this.setParent(null);
    this.children = null;
    this.scope = "GLOBAL";
    this.blockNo = 0;
  }

  public SymbolTable(String scope, SymbolTable pNode){
    this.scope = scope;
    this.setParent(pNode);
    pNode.addChild(this);
  }

  //Instance Methods
  public void setParent(SymbolTable pNode){
    this.parent = pNode;
  }
  public SymbolTable getParent(){
    return this.parent;
  }
  public void addChild(SymbolTable child){
    this.children.add(child);
  }
  public ArrayList<SymbolTable> getChildren(){
    return this.children;
  }
  public int getBlockNumber(){
    blockNo += 1;
    return blockNo;
  }

}
