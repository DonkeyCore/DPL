package me.donkeycore.dpl.method;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.conditional.Compare;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

public class While implements IMethod{

	public String getName() {
		return "while";
	}

	public Object run(Statement statement, String[] args) throws DonkeyException {
		boolean run = false;
		try {
			while(new Compare(Statement.getUpdatedStatement(statement)).isTrue()){
				run = true;
				statement.runBlock(true, true);
			}
		} catch (ScriptException e) {
			Donkey.printError(e);
		}
		return run;
	}
	
}