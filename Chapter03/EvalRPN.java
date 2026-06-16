import java.util.Stack;

public class EvalRPN {
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "+": {
                    // [1] Pop b then a; order matters for - and /
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a + b);
                    break;
                }
                case "-": {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a - b); // [2] a - b, not b - a
                    break;
                }
                case "*": {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a * b);
                    break;
                }
                case "/": {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a / b); // [3] Java integer division truncates toward zero
                    break;
                }
                default: {
                    // [4] Numeric token — push as integer
                    stack.push(Integer.parseInt(token));
                }
            }
        }
        // [5] Final result is the only element left
        return stack.pop();
    }

    public static void main(String[] args) {
        EvalRPN e = new EvalRPN();
        // ["2","1","+","3","*"] = (2+1)*3 = 9
        System.out.println(e.evalRPN(new String[]{"2","1","+","3","*"})); // 9
        // ["4","13","5","/","+"] = 4 + (13/5) = 4 + 2 = 6
        System.out.println(e.evalRPN(new String[]{"4","13","5","/","+"})); // 6
    }
}

