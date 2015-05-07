package me.donkeycore.dpl.exceptions;

/** Thrown when a Donkey class file is a directory.
 * @since 1.0
 * @see FileDirectoryException#FileDirectoryException(String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class FileDirectoryException extends DonkeyException {
	
	private static final long serialVersionUID = -2756338002372564303L;
	
	/** Thrown when a Donkey class file is a directory. 
	 * @since 1.0
	 * @see FileDirectoryException
	 */
	public FileDirectoryException(String msg) {
		super(msg);
	}

}
