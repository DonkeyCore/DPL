package me.donkeycore.dpl.method;

import me.donkeycore.dpl.Expression;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

public class RPN implements IMethod{

	public String getName() {
		return "@rpn";
	}

	public Object run(Statement statement, String[] args) throws DonkeyException{
		String s = "";
		for(String arg : args)
			s = s + "," + arg;
		s = s.replaceFirst(",", "");
		return Expression.evaluateRPN(s);
	}

}
