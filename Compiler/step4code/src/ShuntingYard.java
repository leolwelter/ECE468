import java.util.Stack;
import java.util.Hashtable;
import java.util.ArrayList;
import java.lang.Character;

public class ShuntingYard {

    // public static void main(String[] args) {
    //     String infix = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3";
    //     System.out.printf("infix:   %s%n", infix);
    //     System.out.printf("postfix: %s%n", infixToPostfix(infix));
    // }

    static String infixToPostfix(ArrayList<String> infix) {
      StringBuilder sb = new StringBuilder();
      Stack<String> s = new Stack<>();

      Hashtable<String, Integer> levels = new Hashtable<String, Integer>();
      levels.put("+", 1);
      levels.put("-", 1);
      levels.put("*", 2);
      levels.put("/", 2);

      for (int i = 0; i < infix.size(); i++) {

        String c = infix.get(i);

        if (levels.containsKey(c)) {
          // is operator
          if (s.isEmpty()){
            s.push(c);
          }
          else {
            String top = s.peek();
            if (top == "(" || top == ")") {
              s.push(c);
            }
            else {
              Integer currLevel = levels.get(c);
              Integer sTopLevel = levels.get(s.peek());

              while (!s.isEmpty() && currLevel <= sTopLevel) {
                sb.append(s.pop()).append(' ');
                if (!s.isEmpty()) {
                  if (s.peek() == "(" || s.peek() == ")") {
                    sTopLevel = 0;
                  }
                  else {
                    sTopLevel = levels.get(s.peek());
                  }
                }
              }
              s.push(c);
            }
          }
        }
        else if (c == "(") {
          // is opening parenthesis
          s.push(c);
        }
        else if (c == ")") {
          // is closing parenthesis
          String top = s.peek();
          while(top != "(") {
            sb.append(s.pop()).append(' ');
            top = s.peek();
          }
          s.pop();
        }
        else {
          sb.append(c).append(' ');
        }
      }

      return sb.toString();
    }

/*    static String infixToPostfix(String infix) {
        final String ops = "-+/*";
        StringBuilder sb = new StringBuilder();
        Stack<Integer> s = new Stack<>();

        for (String token : infix.split("\\s")) {
            if (token.isEmpty())
                continue;
            char c = token.charAt(0);
            int idx = ops.indexOf(c);

            // check for operator
            if (idx != -1) {
                if (s.isEmpty())
                    s.push(idx);

                else {
                    while (!s.isEmpty()) {
                        int prec2 = s.peek() / 2;
                        int prec1 = idx / 2;
                        if (prec2 > prec1 || (prec2 == prec1 && c != '^'))
                            sb.append(ops.charAt(s.pop())).append(' ');
                        else break;
                    }
                    s.push(idx);
                }
            }
            else if (c == '(') {
                s.push(-2); // -2 stands for '('
            }
            else if (c == ')') {
                // until '(' on stack, pop operators.
                while (s.peek() != -2)
                    sb.append(ops.charAt(s.pop())).append(' ');
                s.pop();
            }
            else {
                sb.append(token).append(' ');
            }
        }
        while (!s.isEmpty()){
            System.out.println(ops.charAt(s.pop()));
            sb.append(ops.charAt(s.pop())).append(' ');
        }
        return sb.toString();
    }*/
}
