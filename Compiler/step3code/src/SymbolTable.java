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

  //Fields
  public static int blockNo; //used to keep track of block scopes
  public SymbolTable next; //next scope
  public String scope = null; //this node's scope
  public LinkedHashMap<String, ArrayList<String>> varMap = 
          new LinkedHashMap<String, ArrayList<String>>();

  //Constructors
  public SymbolTable(){
    this.next = null;
    this.scope = "GLOBAL";
    this.blockNo = 0;
  }

  public SymbolTable(String scope){
    if(scope.equals("BLOCK")){
      this.scope = "BLOCK " + getBlockNumber();
    }else{
      this.scope = scope;      
    } 
  }  

  //Instance Methods
  // public void setNext(SymbolTable pNode){
  //   this.next = pNode;
  // }
  // public SymbolTable getNext(){
  //   return this.next;
  // }
  public int getBlockNumber(){
    return ++blockNo;
  }
  public void printTable(){
    System.out.println("Symbol table " + scope);
    for(Map.Entry<String, ArrayList<String>> entry : varMap.entrySet()){
      String key = entry.getKey();
      String val = entry.getValue().toString();
      System.out.println("Key: " + key + " Value: " + val);
    }
  }
}
