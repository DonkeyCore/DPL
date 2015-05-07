package me.donkeycore.dpl.exceptions;

import me.donkeycore.dpl.statement.IStatement;
import me.donkeycore.dpl.statement.Statement;

/** Thrown when a statement is unsatisfied for some reason.
 * @since 1.0
 * @see StatementUnsatisfiedException#StatementUnsatisfiedException(Statement, IStatement, String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class StatementUnsatisfiedException extends DonkeyException{
	
	private static final long serialVersionUID = -2144195569557874445L;
	
	/** Thrown when a statement is unsatisfied for some reason.
	 * @param statement The {@link Statement} that caused the statement to be unsatisfied
	 * @param s The {@link IStatement} that was unsatisfied
	 * @param reason The reason that the statement was unsatisfied
	 * @since 1.0
	 * @see StatementUnsatisfiedException
	 */
	public StatementUnsatisfiedException(Statement statement, IStatement s, String reason){
		super(format(statement, s, reason));
	}
	
	private static String format(Statement statement, IStatement s, String r){
		return "The statement " + s.getName() + " was unsatisfied when called in " + statement.getRawStatement() + " on line " + statement.getLineNumber() + " in " + Statement.getFile().getName() + " for the reason: " + r;
	}
	
}