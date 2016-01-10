package me.donkeycore.dpl.exceptions;

/**
 * Represents an instance of an exception that happens.
 * 
 * @since 1.0
 * @see DonkeyException#DonkeyException(String)
 * @see FileDirectoryException
 * @see IncompatibleVariableTypesException
 * @see InvalidFileException
 * @see InvalidStatementException
 * @see MalformedBooleanException
 * @see MathException
 * @see MethodUnsatisfiedException
 * @see NoFileException
 * @see NoReadException
 * @see StatementUnsatisfiedException
 * @see TypeDoesNotExistException
 * @see VariableAlreadyDeclaredException
 */
public abstract class DonkeyException extends Throwable {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = -8134493745328001452L;
	
	/**
	 * Represents an instance of an exception that happens.
	 * 
	 * @param msg The message to be displayed at runtime
	 * @since 1.0
	 * @see DonkeyException
	 */
	public DonkeyException(String msg) {
		super(msg);
	}
}
