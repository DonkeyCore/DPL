package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.exceptions.DonkeyException;

public class Stop implements IStatement{
	
	public String getName() {
		return "stop";
	}

	public Object run(Statement statement, String args) throws DonkeyException {
		for(int i = 1; i < Statement.getMaxLine(Statement.getFile()); i++)
			Statement.setDoNotRun(true, i);
		return null;
	}

}
