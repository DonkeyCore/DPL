package me.donkeycore.dpl.io;

import java.io.File;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.exceptions.IncompatibleVariableTypesException;
import me.donkeycore.dpl.exceptions.InvalidFileException;
import me.donkeycore.dpl.exceptions.InvalidStatementException;
import me.donkeycore.dpl.exceptions.TypeDoesNotExistException;
import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;

/** Manages basic attributes of the class file and creates an {@link IOClass} for the file
 * @since 1.0
 */
public final class DonkeyClass {
	
	private final File file;
	private final Donkey donkey;
	private final IOClass c;
	
	/** Creates a new {@link DonkeyClass} instance.
	 * @since 1.0
	 */
	public DonkeyClass(Donkey donkey, File file) {
		this.donkey = donkey;
		this.file = file;
		this.c = new IOClass(this);
	}
	
	/** Retrieve the {@link Donkey} instance managing this class file.
	 * @return The {@link Donkey} object managing the file execution
	 * @since 1.0
	 */
	public Donkey getDonkey(){
		return this.donkey;
	}
	
	/** Retrieve the {@link IOClass} instance that runs the file.
	 * @return The {@link IOClass} object managing the file execution
	 * @since 1.0
	 */
	public IOClass getIOClass(){
		return this.c;
	}
	
	/** Retrieve the {@link File} being used.
	 * @return The {@link File} being executed
	 * @since 1.0
	 */
	public File getFile(){
		return this.file;
	}
	
	/** Run the code inside the file. 
	 * @throws InvalidFileException If the file does not end with .donkey or is not a valid Donkey class file
	 * @throws IncompatibleVariableTypesException If a variable is assigned something that it cannot represent
	 * @throws TypeDoesNotExistException If a variable being assigned has not been declared
	 * @throws VariableAlreadyDeclaredException If a variable has already been declared
	 * @since 1.0
	 */
	public void runCode() throws InvalidFileException, TypeDoesNotExistException, IncompatibleVariableTypesException, VariableAlreadyDeclaredException{
		try{
			c.runCode();
		}catch(InvalidStatementException e){
			System.err.println();
			Donkey.printError(e);
			System.err.println("\nCode failed while running!");
		}
	}
	
}
