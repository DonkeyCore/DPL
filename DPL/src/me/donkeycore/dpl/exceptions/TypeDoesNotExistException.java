package me.donkeycore.dpl.exceptions;

/**
 * Thrown when a {@link me.donkeycore.dpl.variables.Variable Variable} type or key does not exist and is used
 * 
 * @since 1.0
 * @see me.donkeycore.dpl.variables.Variable Variable
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class TypeDoesNotExistException extends DonkeyException {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = 5584982289717227302L;
	
	/**
	 * Thrown when a {@link me.donkeycore.dpl.variables.Variable Variable} type or key does not exist and is used
	 * 
	 * @param name The name of the type/key
	 * @since 1.0
	 * @see TypeDoesNotExistException
	 */
	public TypeDoesNotExistException(String name) {
		super(format(name));
	}
	
	/**
	 * Format a variable type to an error message
	 * @param name The variable type
	 * @return The formatted message
	 * @see TypeDoesNotExistException
	 * @since 1.0
	 */
	private static String format(String name) {
		return "The variable type " + name + " does not exist!";
	}
}