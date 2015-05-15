package me.donkeycore.dpl.method;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Interface for methods
 * 
 * @since 1.0
 */
public interface IMethod {
	
	/**
	 * Retrieve the name of the method <h1>Example:</h1> <code>
	 * public String getName() { <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;return "example"; <br>
	 * }<br>
	 * </code> <h1>Can be called as:</h1> <code>example(...)</code>
	 * 
	 * @return The name of this method
	 * @since 1.0
	 */
	public abstract String getName();
	
	/**
	 * Run the method. Throw a {@link me.donkeycore.dpl.exceptions.MethodUnsatisfiedException MethodUnsatisfiedException} needed
	 * 
	 * @param statement The {@link Statement} that called the method
	 * @param args The arguments the method has been given
	 * @throws DonkeyException If something goes wrong
	 * @since 1.0
	 * @return The return type that the method may return, or <code>null</code> if there is none.
	 */
	public abstract Object run(Statement statement, String[] args) throws DonkeyException;
}