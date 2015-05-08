package me.donkeycore.dpl.statement;

/**
 * Display a message in the console, without a line break. <br />
 * Syntax: <code>print message</code>
 * 
 * @param message The message to display
 * @since 1.0
 */
public class Print implements IStatement {
	
	public String getName() {
		return "print";
	}
	
	public Object run(Statement statement, String args) {
		System.out.print(args.replace("\\n", "\n"));
		return null;
	}
}