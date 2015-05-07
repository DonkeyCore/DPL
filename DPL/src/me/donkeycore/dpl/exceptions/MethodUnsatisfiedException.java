package me.donkeycore.dpl.exceptions;

import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.statement.Statement;

/** Thrown when a method is unsatisfied for some reason.
 * @since 1.0
 * @see MethodUnsatisfiedException#MethodUnsatisfiedException(Statement, IMethod, String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class MethodUnsatisfiedException extends DonkeyException{
	
	private static final long serialVersionUID = -2144195569557874445L;
	
	/** Thrown when a method is unsatisfied for some reason.
	 * @since 1.0
	 * @param statement The {@link Statement} that unsatisfied the method
	 * @param m The {@link IMethod} that was unsatisfied
	 * @param reason The reason that it was unsatisfied
	 * @see MethodUnsatisfiedException
	 */
	public MethodUnsatisfiedException(Statement statement, IMethod m, String reason){
		super(format(statement, m, reason));
	}
	
	private static String format(Statement s, IMethod m, String r){
		return "The method " + m.getName() + "() was unsatisfied when called in " + s.getRawStatement() + " on line " + s.getLineNumber() + " in " + Statement.getFile().getName() + " for the reason: " + r;
	}
	
}