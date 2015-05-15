package me.donkeycore.dpl.exceptions;

import me.donkeycore.dpl.variables.Variable;

/**
 * Thrown when a variable that is already defined is being declared
 * 
 * @since 1.0
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class VariableAlreadyDeclaredException extends DonkeyException {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = 5584982289717227302L;
	
	/**
	 * Thrown when a variable that is already defined is being declared
	 * 
	 * @param v The variable that was already declared
	 * @since 1.0
	 * @see VariableAlreadyDeclaredException
	 */
	public VariableAlreadyDeclaredException(Variable v) {
		super(format(v));
	}
	
	private static String format(Variable v) {
		return "The variable " + v.getKey() + " has already been declared!";
	}
}