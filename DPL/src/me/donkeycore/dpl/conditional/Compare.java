package me.donkeycore.dpl.conditional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.conditional.booleanexpression.BooleanExpression;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.MalformedBooleanException;
import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.statement.IStatement;
import me.donkeycore.dpl.statement.Statement;
import me.donkeycore.dpl.variables.Variable;

/**
 * Compute a boolean value from a statement
 * 
 * @since 1.0
 */
public class Compare {
	
	/**
	 * The statement being evaluated
	 * 
	 * @since 1.0
	 */
	private final Statement statement;
	
	/**
	 * Create a new {@link Compare} object
	 * 
	 * @param statement The statement with the conditional statement
	 * @see Compare
	 * @since 1.0
	 */
	public Compare(Statement statement) {
		this.statement = statement;
	}
	
	/**
	 * Retrieve the statement with the conditional statement
	 * 
	 * @return The {@link Statement} passed in the constructor
	 * @since 1.0
	 */
	public Statement getStatement() {
		return statement;
	}
	
	/**
	 * Evaluate if the conditional is true
	 * 
	 * @return Whether the conditional is true
	 * @since 1.0
	 */
	public Boolean isTrue() {
		String c = statement.getStatement();
		c = c.replaceAll(".*(if|while)\\(", "").replaceAll("\\).*", "");
		for(Variable v : Variable.getVariables())
			c = v.replace(c);
		for(IMethod m : Donkey.getMethods()) {
			if (c.startsWith(m.getName() + "(")) {
				try {
					Object o = m.run(statement, c.replace(m.getName(), "").split(","));
					if (o instanceof Boolean)
						return (Boolean) o;
				} catch(DonkeyException e) {
					Donkey.printError(e);
				}
			}
		}
		for(IStatement s : Donkey.getStatements()) {
			if (c.equals(s.getName())) {
				try {
					Object o = s.run(statement, c.replace(s.getName(), ""));
					if (o instanceof Boolean)
						return (Boolean) o;
				} catch(DonkeyException e) {
					Donkey.printError(e);
				}
			}
		}
		if (!c.matches(".*\\d+.*")) {
			Pattern p = Pattern.compile("\\s*\"?(.+)\"?\\s*(==|!=)\\s*\"?(.+)\"?.*");
			Matcher m = p.matcher(c);
			if (m.matches()) {
				String s1 = m.group(1).trim();
				String s2 = m.group(3).trim();
				switch(m.group(2)) {
					default:
						break;
					case "==":
						c = c.replaceAll("[" + s1 + "]\\s*==\\s*[" + s2 + "]", s1.equalsIgnoreCase(s2) + ""); //Change to equals when methods are added for lowercase/uppercase conversions
						break;
					case "!=":
						c = c.replaceAll("[" + s1 + "]\\s*!=\\s*[" + s2 + "]", !s1.equalsIgnoreCase(s2) + ""); //Change to !equals when methods are added for lowercase/uppercase conversions
						break;
				}
				c = c.replaceAll("^.*(true|false).*$", "$1");
			}
		} else {
			Pattern p = Pattern.compile("(\\d+|\\d+\\.\\d+)\\s*(<|>|==|!=|>=|<=)\\s*(\\d+|\\d+\\.\\d+).*");
			Matcher m = p.matcher(c);
			if (m.matches()) {
				Double d1 = Double.parseDouble(m.group(1));
				Double d2 = Double.parseDouble(m.group(3));
				switch(m.group(2)) {
					default:
						break;
					case "<":
						c = c.replaceAll("[" + d1 + "]\\s*<\\s*[" + d2 + "]", (d1 < d2) + "");
						break;
					case ">":
						c = c.replaceAll("[" + d1 + "]\\s*>\\s*[" + d2 + "]", (d1 > d2) + "");
						break;
					case "<=":
						c = c.replaceAll("[" + d1 + "]\\s*<=\\s*[" + d2 + "]", (d1 <= d2) + "");
						break;
					case ">=":
						c = c.replaceAll("[" + d1 + "]\\s*>=\\s*[" + d2 + "]", (d1 >= d2) + "");
						break;
					case "==":
						c = c.replaceAll("[" + d1 + "]\\s*==\\s*[" + d2 + "]", (d1 == d2) + "");
						break;
					case "!=":
						c = c.replaceAll("[" + d1 + "]\\s*!=\\s*[" + d2 + "]", (d1 != d2) + "");
						break;
				}
			}
			c = c.replaceAll("^[\\d+]?(true|false)[\\d+]$", "$1");
		}
		try {
			return BooleanExpression.readLeftToRight(c).booleanValue();
		} catch(MalformedBooleanException e) {
			e.printStackTrace();
			return false;
		}
	}
}