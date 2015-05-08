package me.donkeycore.dpl.variables;

import me.donkeycore.dpl.exceptions.IncompatibleVariableTypesException;

/**
 * The {@link Variable} interface implemented for variables
 * 
 * @see Variable
 * @since 1.0
 */
public interface IVariable {
	
	/**
	 * The constructor of the {@link Variable} object to be run within the code
	 * 
	 * @param args The arguments given to the constructor
	 * @return The variable object
	 * @since 1.0
	 */
	public IVariable constructor(String[] args);
	
	/**
	 * The key that the {@link Variable} is identified as. <h1>Example:</h1> <code>
	 * string KEY = some string;
	 * </code>
	 * 
	 * @return The identifier of the variable
	 * @since 1.0
	 */
	public String getKey();
	
	/**
	 * Sets the value of the {@link Object}
	 * 
	 * @param v The new variable object to be set
	 * @since 1.0
	 */
	public <T> void set(T v) throws IncompatibleVariableTypesException;
	
	/**
	 * The type of the variable <h1>Example:</h1> <code>
	 * public String getName(){<br />
	 * return "string";<br />
	 * }
	 * </code> <h1>Can be used as:</h1> <code>
	 * string somevar = some string;
	 * </code>
	 * 
	 * @return The type of the variable
	 * @since 1.0
	 */
	public String getName();
}