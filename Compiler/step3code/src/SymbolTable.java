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
  public static LinkedHashMap<String, ArrayList<List<String>>> varMap;
  
  public SymbolTable next; //next node
  public String scope = null; //this node's scope

  //Constructors
  public SymbolTable(){
    this.next = null;
    this.scope = "GLOBAL";
    this.blockNo = 0;
    this.varMap = new LinkedHashMap<String, ArrayList<List<String>>>();
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
    ArrayList<List<String>> varList = varMap.get(scope); 
    if(varList != null){  
      for(List<String> varData : varList){
        System.out.print("name " + varData.get(0) + " type " + varData.get(1));
        if(varData.size() == 3){
          System.out.print(" value " + varData.get(2));
        }
        System.out.println();
      }
    }
  }
}
