package me.donkeycore.dpl.plugin;

import me.donkeycore.dpl.exceptions.LoadException;
import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.statement.IStatement;
import me.donkeycore.dpl.statement.Statement;
import me.donkeycore.dpl.variables.Variable;

/**
 * The main class of which all DPL plugins extend from. Provides several methods to load objects.
 * 
 * @see DPlugin#loadStatement(Class)
 * @see DPlugin#loadMethod(Class)
 * @see DPlugin#loadVariable(Class)
 * @since 1.0
 */
public abstract class DPlugin {
	
	/**
	 * Called when the plugin is initialized. Used to register statements, methods, and variables.
	 * 
	 * @see DPlugin#loadStatement(Class)
	 * @see DPlugin#loadMethod(Class)
	 * @see DPlugin#loadVariable(Class)
	 * @since 1.0
	 */
	public abstract void onLoad();
	
	/**
	 * Load a custom {@link IStatement} object to DPL.
	 * 
	 * @param c The class of the custom {@link IStatement} object to load.
	 * @throws LoadException If there was a problem instantiating the class
	 * @since 1.0
	 */
	public void loadStatement(Class<? extends IStatement> c) throws LoadException {
		try {
			Statement.addStatement(c.getConstructor().newInstance());
		} catch(Throwable t) {
			throw new LoadException(t.getMessage());
		}
	}
	
	/**
	 * Load a custom {@link IMethod} object to DPL.
	 * 
	 * @param c The class of the custom {@link IMethod} object to load.
	 * @throws LoadException If there was a problem instantiating the class
	 * @since 1.0
	 */
	public void loadMethod(Class<? extends IMethod> c) throws LoadException {
		try {
			Statement.addMethod(c.getConstructor().newInstance());
		} catch(Throwable t) {
			throw new LoadException(t.getMessage());
		}
	}
	
	/**
	 * Load a custom {@link Variable} object to DPL.
	 * 
	 * @param c The class of the custom {@link Variable} object to load.
	 * @throws LoadException If there was a problem instantiating the class
	 * @since 1.0
	 */
	public void loadVariable(Class<? extends Variable> c) throws LoadException {
		try {
			c.getConstructor().newInstance();
		} catch(Throwable t) {
			throw new LoadException(t.getMessage());
		}
	}
}
