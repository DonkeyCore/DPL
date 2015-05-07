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

public class Compare {
	
	private final Statement statement;
	
	public Compare(Statement statement){
		this.statement = statement;
	}
	
	public Statement getStatement(){
		return statement;
	}
	
	public Boolean isTrue(){
		String c = statement.getStatement();
		c = c.replaceAll(".*(if|while)\\(", "").replaceAll("\\).*", "");
		for(Variable v : Variable.getVariables())
			c = v.replace(c);
		
		for(IMethod m : Donkey.getMethods()){
			if(c.startsWith(m.getName() + "(")){
				try {
					Object o = m.run(statement, c.replace(m.getName(), "").split(","));
					if(o instanceof Boolean)
						return (Boolean) o;
				}catch(DonkeyException e){Donkey.printError(e);}
			}
		}
		
		for(IStatement s : Donkey.getStatements()){
			if(c.equals(s.getName())){
				try {
					Object o = s.run(statement, c.replace(s.getName(), ""));
					if(o instanceof Boolean)
						return (Boolean) o;
				}catch(DonkeyException e){Donkey.printError(e);}
			}
		}
		
		Pattern p = Pattern.compile("(\\d+|\\d+\\.\\d+)\\s*(<|>|==|!=|>=|<=)\\s*(\\d+|\\d+\\.\\d+).*");
		Matcher m = p.matcher(c);
		if(m.matches()){
			Double d1 = Double.parseDouble(m.group(1));
			Double d2 = Double.parseDouble(m.group(3));
			switch(m.group(2)){
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
		
		try{
			return BooleanExpression.readLeftToRight(c).booleanValue();
		}catch (MalformedBooleanException e){
			e.printStackTrace();
			return false;
		}
	}
	
}