package me.donkeycore.dpl.exceptions;

/** Thrown when a statement, method, or variable is unable to load
 * @since 1.0
 * @see LoadException#LoadException(String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class LoadException extends DonkeyException {
	
	private static final long serialVersionUID = -3776196857939504690L;
	
	/** Thrown when a statement, method, or variable is unable to load
	 * @param msg An explanation of why the object could not load
	 * @since 1.0
	 * @see LoadException
	 */
	public LoadException(String msg) {
		super(msg);
	}
	
}