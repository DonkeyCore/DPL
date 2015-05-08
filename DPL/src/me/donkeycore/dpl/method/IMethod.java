package me.donkeycore.dpl.method;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Interface for methods to extend
 * 
 * @since 1.0
 */
public interface IMethod {
	
	/**
	 * Retrieve the name of the method <h1>Example:</h1> <code>
	 * public String getName() { <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;return "example"; <br/>
	 * }<br/>
	 * </code> <h1>Can be called as:</h1> <code>example(...)</code>
	 * 
	 * @since 1.0
	 */
	public abstract String getName();
	
	/**
	 * Run the method. Feel free to throw a {@link MethodUnsatisfiedException} if you want to ;)
	 * 
	 * @param statement The {@link Statement} that called the method
	 * @param args The arguments the method has been given
	 * @throws DonkeyException If something goes wrong
	 * @since 1.0
	 * @return The return type that the method may return, or <code>null</code> if there is none.
	 */
	public abstract Object run(Statement statement, String[] args) throws DonkeyException;
}