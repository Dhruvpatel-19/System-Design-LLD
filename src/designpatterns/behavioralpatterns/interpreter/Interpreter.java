package designpatterns.behavioralpatterns.interpreter;

/*
- The Interpreter Pattern is used to define a grammar for a language and 
  provide an interpreter to evaluate sentences in that language.

Main Components:
  1. AbstractExpression → common interface
  2. TerminalExpression → basic units (numbers, variables)
  3. NonTerminalExpression → operations (AND, OR, +, -)
  4. Context → holds input data
*/

//Expression interface
interface Expression {
    int interpret();
}

//Terminal expression (Number)
class NumberExpression implements Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret() {
        return number;
    }
}

//Non-Terminal expressions
//Addition
class AddExpression implements Expression {
    private Expression left;
    private Expression right;

    public AddExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() + right.interpret();
    }
}

//Subtraction
class SubtractExpression implements Expression {
    private Expression left;
    private Expression right;

    public SubtractExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() - right.interpret();
    }
}
class InterpreterMain {
    public static void main(String[] args) {

        //Expression: (5 + 3) - 2
        Expression five = new NumberExpression(5);
        Expression three = new NumberExpression(3);
        Expression two = new NumberExpression(2);

        Expression add = new AddExpression(five, three); // 5 + 3
        Expression result = new SubtractExpression(add, two); // (5 + 3) - 2

        /* It builds tree structure. Each node calls interpret() and final result is achieved.
                (-)
               /   \
             (+)    2
            /   \
           5     3
        */
        System.out.println("Result: " + result.interpret());

        //can also be done in following way 
        //Expression: 5 + (10 - 3)
        Expression expression = new AddExpression(new NumberExpression(5), new SubtractExpression(new NumberExpression(10), new NumberExpression(3)));
        System.out.println("Result : "+expression.interpret());
    }
}
