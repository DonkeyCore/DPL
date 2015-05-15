package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.StatementUnsatisfiedException;

/**
 * Stop the program <br>
 * Syntax: <code>stop</code> <br>
 * Syntax: <code>stop errno</code><br>
 * 
 * <b>errno</b> The exit code
 * 
 * @since 1.0
 */
public class Stop implements IStatement {
	
	public String getName() {
		return "stop";
	}
	
	public Object run(Statement statement, String args) throws DonkeyException {
		try {
			if (args != null && args.length() > 0)
				Statement.errno = Integer.parseInt(args);
		} catch(NumberFormatException e) {
			throw new StatementUnsatisfiedException(statement, this, "Invalid error number");
		}
		for(int i = 1; i < Statement.getMaxLine(Statement.getFile()); i++)
			Statement.setDoNotRun(true, i);
		return null;
	}
}
