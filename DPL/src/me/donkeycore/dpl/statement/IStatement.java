package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.exceptions.DonkeyException;

/**
 * Interface for statements to extend
 * 
 * @since 1.0
 */
public interface IStatement {
	
	/**
	 * Retrieve the name of the method <h1>Example:</h1> <code>
	 * public String getName(){ <br/>
	 * return "example"; <br/>
	 * }<br/>
	 * </code> <h1>Can be called as:</h1> <code>example ...;</code>
	 * 
	 * @since 1.0
	 */
	public String getName();
	
	/**
	 * Run the method. Feel free to throw a {@link StatementUnsatisfiedException} if you want to ;)
	 * 
	 * @param statement The {@link Statement} that called the statement
	 * @param args The arguments the method has been given
	 * @throws DonkeyException If something goes wrong
	 * @since 1.0
	 * @return The return type that the statement may return, or <code>null</code> if there is none.
	 */
	public Object run(Statement statement, String args) throws DonkeyException;
}