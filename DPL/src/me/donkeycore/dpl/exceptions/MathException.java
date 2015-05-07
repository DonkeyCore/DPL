package me.donkeycore.dpl.exceptions;

/** Thrown when there was a problem when solving a math expression
 * @since 1.0
 * @see MathException#MathException(String, String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class MathException extends DonkeyException {
	
	private static final long serialVersionUID = 1279225541842351987L;
	
	/** Thrown when there was a problem solving a math expression.
	 * @since 1.0
	 * @param expression The expression that failed to be solved
	 * @param error The reason it could not be solved
	 * @see MathException
	 */
	public MathException(String expression, String error) {
		super(format(expression, error));
	}
	
	private static String format(String expression, String error){
		return "The following math expression \"" + expression + "\" failed for the reason: " + error;
	}

}