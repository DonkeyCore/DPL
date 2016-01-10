package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Donkey.LogLevel;
import me.donkeycore.dpl.exceptions.DonkeyException;

/**
 * Logs messages to console
 * 
 * @since 1.0
 */
public class Logger {
	
	/**
	 * Log a debug message to console <br>
	 * Syntax: <code>@log:debug message</code><br>
	 * 
	 * <b>message</b> The message to display
	 * 
	 * @since 1.0
	 */
	public static class Debug implements IStatement {
		
		public String getName() {
			return "@log:debug";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.DEBUG, args, "Script");
			return null;
		}
	}
	
	/**
	 * Logs an information message to console <br>
	 * Syntax: <code>@log:info message</code><br>
	 * 
	 * <b>message</b> The message to display
	 * 
	 * @since 1.0
	 */
	public static class Info implements IStatement {
		
		public String getName() {
			return "@log:info";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.INFO, args, "Script");
			return null;
		}
	}
	
	/**
	 * Logs a warning message to console <br>
	 * Syntax: <code>@log:warning message</code><br>
	 * 
	 * <b>message</b> The message to display
	 * 
	 * @since 1.0
	 */
	public static class Warning implements IStatement {
		
		public String getName() {
			return "@log:warning";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.WARNING, args, "Script");
			return null;
		}
	}
	
	/**
	 * Log a non-fatal error message to console <br>
	 * Syntax: <code>@log:error message</code><br>
	 * 
	 * <b>message</b> The message to display
	 * 
	 * @since 1.0
	 */
	public static class Error implements IStatement {
		
		public String getName() {
			return "@log:error";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.ERROR, args, "Script");
			return null;
		}
	}
	
	/**
	 * Record a fatal error message to console. Does <b>not</b> automatically exit program. <br>
	 * Syntax: <code>@log:fatal message</code><br>
	 * 
	 * <b>message</b> The message to display
	 * 
	 * @since 1.0
	 */
	public static class Fatal implements IStatement {
		
		public String getName() {
			return "@log:fatal";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.FATAL, args, "Script");
			return null;
		}
	}
}