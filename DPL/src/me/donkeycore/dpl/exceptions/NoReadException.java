package me.donkeycore.dpl.exceptions;

/** Thrown when a file is unable to be read.
 * @since 1.0
 * @see NoReadException#NoReadException(String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class NoReadException extends DonkeyException {
	
	private static final long serialVersionUID = -3776196857939504690L;
	
	/** Thrown when a file is unable to be read.
	 * @param msg An explanation of why the file could not be read
	 * @since 1.0
	 * @see NoReadException
	 */
	public NoReadException(String msg) {
		super(msg);
	}
	
}