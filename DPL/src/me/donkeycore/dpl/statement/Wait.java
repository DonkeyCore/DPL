package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.InvalidStatementException;

public class Wait implements IStatement{

	public String getName() {
		return "wait";
	}

	public Object run(Statement statement, String args) throws DonkeyException{
		try{
			try{
				Thread.sleep(Long.parseLong(args));
				return true;
			}catch(Exception e){
				return false;
			}
		}catch(Exception e){
			throw new InvalidStatementException(args + " is not a digit!", statement.getLineNumber());
		}
	}
	
}
