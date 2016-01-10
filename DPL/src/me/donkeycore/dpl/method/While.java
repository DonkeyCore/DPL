package me.donkeycore.dpl.method;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.conditional.Compare;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Repeat a block until a statement is evaluated to false <br>
 * Syntax: <code>while(condition) { ...</code><br>
 * 
 * <b>condition</b> The condition being tested
 * @since 1.0
 */
public class While implements IMethod {
	
	public String getName() {
		return "while";
	}
	
	public Object run(Statement statement, String[] args) throws DonkeyException {
		boolean run = false;
		try {
			while(new Compare(Statement.getUpdatedStatement(statement)).isTrue()) {
				run = true;
				statement.runBlock(true, true);
			}
		} catch(ScriptException e) {
			Donkey.printError(e);
		}
		return run;
	}
}