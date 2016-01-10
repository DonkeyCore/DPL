package me.donkeycore.dpl.method;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.conditional.Compare;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Evaluate if a statement is true <br>
 * Syntax: <code>if(condition) { ...</code><br>
 * 
 * <b>condition</b> The condition being tested
 * @since 1.0
 */
public class If implements IMethod {
	
	public String getName() {
		return "if";
	}
	
	public Object run(Statement statement, String[] args) throws DonkeyException {
		boolean b = new Compare(statement).isTrue();
		try {
			statement.runBlock(b);
		} catch(ScriptException e) {
			Donkey.printError(e);
		}
		return b;
	}
}