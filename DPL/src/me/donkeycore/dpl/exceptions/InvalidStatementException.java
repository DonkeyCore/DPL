package me.donkeycore.dpl.exceptions;

import java.sql.Statement;

/**
 * Thrown when a statement is invalid.
 * 
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 * @since 1.0
 */
public class InvalidStatementException extends DonkeyException {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = 5559608147502430699L;
	public Statement statement;
	
	/**
	 * Thrown when a statement is invalid.
	 * 
	 * @param statement The statement with the error
	 * @param line The line number that the error occurred on
	 * @see InvalidStatementException
	 * @since 1.0
	 */
	public InvalidStatementException(String statement, int line) {
		super(format(statement, line));
	}
	
	/**
	 * Thrown when a statement is invalid.
	 * 
	 * @param message The message to be displayed at runtime
	 * @see InvalidStatementException
	 * @since 1.0
	 */
	public InvalidStatementException(String message) {
		super(message);
	}
	
	/**
	 * Format a statement and line number to to an error message
	 * @param statement The statement with the error
	 * @param l The line number that the error occurred on
	 * @return The formatted message
	 * @see InvalidStatementException
	 * @since 1.0
	 */
	private static String format(String statement, int l) {
		return "Invalid statement at line " + l + ": " + statement;
	}
}