package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.StatementUnsatisfiedException;

public class Debug implements IStatement{

	public String getName() {
		return "@debug";
	}

	public Object run(Statement statement, String args) throws DonkeyException{
		String set = args.replace("@debug", "").replace("if(", "").replace(")", "").replace("&&", "").replace("||", "").trim();
		if(args.equals(""))
			return Donkey.debug;
		else{
			if(set.equalsIgnoreCase("true")){
				return Donkey.debug != (Donkey.debug = true);
			}else if(set.equalsIgnoreCase("false")){
				return Donkey.debug != (Donkey.debug = false);
			}
		}
		throw new StatementUnsatisfiedException(statement, this, "Invalid boolean: " + set);
	}

}
