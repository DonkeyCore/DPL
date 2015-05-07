package me.donkeycore.dpl.method;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.conditional.Compare;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

public class If implements IMethod{

	public String getName() {
		return "if";
	}

	public Object run(Statement statement, String[] args) throws DonkeyException {
		boolean b = new Compare(statement).isTrue();
		try {
			statement.runBlock(b);
		} catch (ScriptException e) {
			Donkey.printError(e);
		}
		return b;
	}
	
}