package me.donkeycore.dpl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import me.donkeycore.dpl.exceptions.MathException;

/**
 * Handles math expressions
 * 
 * @since 1.0
 */
public class Expression {
	
	/**
	 * Implemented to create a static class
	 * 
	 * @see Expression
	 * @since 1.0
	 */
	private Expression() {}
	
	/**
	 * Evaluate a math expression using the {@link ScriptEngine JavaScript} engine. <h1>Syntax:</h1>
	 * 
	 * <pre>
	 * A + B <code>Adds the numbers</code>
	 * A - B <code>Subtracts the numbers</code>
	 * A * B <code>Multiplies the numbers</code>
	 * A / B <code>Divides the numbers</code>
	 * A % B <code>Finds the remainder from dividing the numbers</code>
	 * </pre>
	 * 
	 * @param expression The expression to evaluate
	 * @return The value returned from the execution of the script
	 * @throws ScriptException If an error occurs in the script
	 */
	public static Double evaluate(String expression) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("js");
		System.out.println(expression);
		return Double.parseDouble(engine.eval(expression).toString());
	}
	
	/**
	 * Evaluate a math expression using RPN (Reverse Polish Notation) <h1>Syntax:</h1>
	 * 
	 * <pre>
	 * A B + <code>is the same as</code> A + B
	 * A B - <code>is the same as</code> A - B
	 * A B * <code>is the same as</code> A * B
	 * A B / <code>is the same as</code> A / B
	 * A B ^ <code>is the same as</code> A ^ B
	 * A B % <code>is the same as</code> A % B
	 * A B + C - <code>is the same as</code> A + B - C
	 * </pre>
	 * 
	 * @param s The string to evaluate
	 * @return A double representation of the answer
	 * @throws MathException If there was an error evaluating the math expression
	 * @since 1.0
	 */
	public static double evaluateRPN(String s) throws MathException {
		try {
			java.util.Stack<String> tks = new java.util.Stack<String>();
			tks.addAll(java.util.Arrays.asList(s.trim().split("[ \t,]+")));
			return evaluateRPN(s, tks);
		} catch(Exception e) {
			throw new MathException(s, "Invalid expression");
		}
	}
	
	private static double evaluateRPN(String expression, java.util.Stack<String> tks) throws MathException {
		String tk = tks.pop();
		double x, y;
		try {
			x = Double.parseDouble(tk);
		} catch(Exception e) {
			y = evaluateRPN(expression, tks);
			x = evaluateRPN(expression, tks);
			if (tk.equals("+"))
				x += y;
			else if (tk.equals("-"))
				x -= y;
			else if (tk.equals("*"))
				x *= y;
			else if (tk.equals("/"))
				x /= y;
			else if (tk.equals("%"))
				x %= y;
			else if (tk.equals("^"))
				x = java.lang.Math.pow(x, y);
			else
				throw new MathException(expression, "Invalid operation: " + tk);
		}
		return x;
	}
}