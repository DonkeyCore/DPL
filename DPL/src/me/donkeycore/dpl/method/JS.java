package me.donkeycore.dpl.method;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.MethodUnsatisfiedException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Used to run JavaScript by using {@link ScriptEngine} <br>
 * Syntax: <code>@js(script)</code><br>
 * 
 * <b>script</b> The JavaScript code to run
 * @since 1.0
 */
public class JS implements IMethod {
	
	/**
	 * JavaScript engine evaluating all expressions
	 * 
	 * @since 1.0
	 */
	public static final ScriptEngine js = new ScriptEngineManager().getEngineByName("js");
	
	public String getName() {
		return "@js";
	}
	
	@Override
	public Object run(Statement statement, String[] args) throws DonkeyException {
		if (args.length == 0 || args[0].equals(""))
			return null;
		try {
			return js.eval(args[0]);
		} catch(ScriptException e) {
			throw new MethodUnsatisfiedException(statement, this, "Invalid JS");
		}
	}
}