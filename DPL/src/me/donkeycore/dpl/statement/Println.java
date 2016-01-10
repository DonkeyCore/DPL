package me.donkeycore.dpl.statement;

/**
 * Display a message in the console, with a linebreak. <br>
 * Syntax: <code>println message</code><br>
 * 
 * <b>message</b> The message to display, followed by a newline
 * @since 1.0
 */
public class Println implements IStatement {
	
	public String getName() {
		return "println";
	}
	
	public Object run(Statement statement, String args) {
		System.out.println(args.replace("\\n", "\n"));
		return null;
	}
}