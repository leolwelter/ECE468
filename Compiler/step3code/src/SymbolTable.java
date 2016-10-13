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
  //@field Counter keeping track of block numbers
  public static int blockNo; 

  //@field Shared hashtable mapping SCOPE : VARLIST
  public static LinkedHashMap<String, ArrayList<String>> varMap;
  
  public SymbolTable next; //next node
  public String scope = null; //this node's scope

  //Constructors
  public SymbolTable(){
    this.next = null;
    this.scope = "GLOBAL";
    this.blockNo = 0;
    this.varMap = new LinkedHashMap<String, ArrayList<String>>();
  }

  public SymbolTable(String scope){
    if(scope.equals("BLOCK")){
      this.scope = "BLOCK " + getBlockNumber();
    }else{
      this.scope = scope;      
    } 
  }  

  //Instance Methods

  public int getBlockNumber(){
    return ++blockNo;
  }

  public void printTable(){
    System.out.println("Symbol table " + scope);
    if(varMap != null){      
      for (Map.Entry<String,ArrayList<String>> entry : varMap.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue().toString();
        System.out.println("key: " + key + " val: " + value);  
      }
    }
  }
}
