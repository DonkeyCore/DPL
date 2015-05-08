package me.donkeycore.dpl.exceptions;

/**
 * Thrown when a file does not end with .donkey or is not a valid Donkey class file.
 * 
 * @since 1.0
 * @see InvalidFileException#InvalidFileException(String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class InvalidFileException extends DonkeyException {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = 5903122842407398498L;
	
	/**
	 * Thrown when a file does not end with .donkey or is not a valid Donkey class file.
	 * 
	 * @since 1.0
	 * @see InvalidFileException
	 */
	public InvalidFileException(String msg) {
		super(msg);
	}
}
