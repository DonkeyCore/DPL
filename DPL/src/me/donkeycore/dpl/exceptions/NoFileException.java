package me.donkeycore.dpl.exceptions;

/** Thrown when a file does not exist.
 * @since 1.0
 * @see NoFileException#NoFileException(String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class NoFileException extends DonkeyException{

	private static final long serialVersionUID = 5167495210909463985L;
	
	/** Thrown when a file does not exist.
	 * @param msg An explanation of the file not being found
	 * @since 1.0
	 * @see NoFileException 
	 */
	public NoFileException(String msg){
		super(msg);
	}
	
}
