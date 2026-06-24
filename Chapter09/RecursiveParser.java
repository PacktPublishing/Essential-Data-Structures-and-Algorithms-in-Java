public class RecursiveParser {

    public static int parse(String expression) {
        // [1] Index held in a size-1 array so the position is shared "by reference"
        //     across every recursive call — Java passes ints by value, arrays by reference.
        int[] pos = new int[]{0};
        return parseExpression(expression, pos);
    }

    // [2] Lowest precedence: + and - (evaluated last, so it's the entry point)
    private static int parseExpression(String str, int[] pos) {
        // [3] Grab the first term; precedence climbing means terms bind tighter than +/-
        int result = parseTerm(str, pos);

        while (pos[0] < str.length()) {
            char op = str.charAt(pos[0]);
            if (op == '+' || op == '-') {
                pos[0]++; // [4] Consume the operator
                int nextTerm = parseTerm(str, pos);
                // [5] Left-associative: fold each new term into the running result
                if (op == '+') {
                    result += nextTerm;
                } else {
                    result -= nextTerm;
                }
            } else {
                // [6] Not a +/- (lower-precedence op done, or a ')') — hand control back up
                break;
            }
        }
        return result;
    }

    // [7] Middle precedence: * and / (binds tighter than +/-, looser than factors)
    private static int parseTerm(String str, int[] pos) {
        int result = parseFactor(str, pos);

        while (pos[0] < str.length()) {
            char op = str.charAt(pos[0]);
            if (op == '*' || op == '/') {
                pos[0]++; // [8] Consume the operator
                int nextFactor = parseFactor(str, pos);
                if (op == '*') {
                    result *= nextFactor;
                } else {
                    result /= nextFactor; // [9] Truncated integer division (Java int semantics)
                }
            } else {
                break; // [10] +/-  or ')' seen — let parseExpression handle it
            }
        }
        return result;
    }

    // [11] Highest precedence: a parenthesized sub-expression or a literal number
    private static int parseFactor(String str, int[] pos) {
        char current = str.charAt(pos[0]);

        // [12] '(' resets precedence: recurse to parseExpression for the inner sub-expression
        if (current == '(') {
            pos[0]++;                                // [13] Consume '('
            int result = parseExpression(str, pos);  // [14] Evaluate everything up to the match ')'
            pos[0]++;                                // [15] Consume the matching ')'
            return result;
        }

        // [16] Otherwise accumulate a multi-digit number, MSD-first
        int result = 0;
        while (pos[0] < str.length() && Character.isDigit(str.charAt(pos[0]))) {
            result = result * 10 + (str.charAt(pos[0]) - '0'); // [17] shift left one decimal place, add digit
            pos[0]++;
        }
        return result;
    }

}
