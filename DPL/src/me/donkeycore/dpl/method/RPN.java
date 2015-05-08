package me.donkeycore.dpl.method;

import me.donkeycore.dpl.Expression;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Evaluate a math expression using Reverse Polish Notation <br />
 * Syntax: <code>@rpn(expression)</code>
 * 
 * @param expression The expression being evaluated
 * @since 1.0
 */
public class RPN implements IMethod {
	
	public String getName() {
		return "@rpn";
	}
	
	public Object run(Statement statement, String[] args) throws DonkeyException {
		String s = "";
		for(String arg : args)
			s = s + "," + arg;
		s = s.replaceFirst(",", "");
		return Expression.evaluateRPN(s);
	}
}
