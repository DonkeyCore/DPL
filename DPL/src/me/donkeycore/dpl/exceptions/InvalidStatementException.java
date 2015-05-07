package me.donkeycore.dpl.exceptions;

import java.sql.Statement;

/** Thrown when a statement is invalid.
 * @since 1.0
 * @see InvalidStatementException#InvalidStatementException(String, Integer)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class InvalidStatementException extends DonkeyException {
	
	private static final long serialVersionUID = 5559608147502430699L;
	
	public Statement statement;
	
	/** Thrown when a statement is invalid.
	 * @since 1.0
	 * @param msg The statement
	 * @param line The line number that the statement is on
	 * @see InvalidStatementException
	 */
	public InvalidStatementException(String statement, int line){
		super(format(statement, line));
	}

	/** Thrown when a statement is invalid.
	 * @since 1.0
	 * @param msg The message
	 * @see InvalidStatementException
	 */
	public InvalidStatementException(String message) {
		super(message);
	}

	private static String format(String statement, int l){
		return "Invalid statement at line " + l + ": " + statement;
	}

}