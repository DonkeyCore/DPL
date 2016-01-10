package me.donkeycore.dpl.variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Donkey.LogLevel;
import me.donkeycore.dpl.Expression;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.IncompatibleVariableTypesException;
import me.donkeycore.dpl.exceptions.TypeDoesNotExistException;
import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;
import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.statement.IStatement;
import me.donkeycore.dpl.statement.Statement;

/**
 * The {@link Variable} class that variables extend. This class implements {@link IVariable}
 * 
 * @since 1.0
 * @see IVariable
 */
public abstract class Variable implements IVariable {
	
	/**
	 * The list of registered {@link Variable} objects
	 * 
	 * @since 1.0
	 */
	private static final java.util.ArrayList<Variable> vars = new java.util.ArrayList<Variable>();
	
	/**
	 * Returns an array of {@link Variable} objects that were declared in the code
	 * 
	 * @return All declared variables
	 * @since 1.0
	 */
	public static final Variable[] getVariables() {
		return vars.toArray(new Variable[vars.size()]);
	}
	
	private static final void add(Variable v) {
		vars.add(v);
	}
	
	/**
	 * The name of a variable
	 * 
	 * @since 1.0
	 */
	private final String key;
	/**
	 * The value that this variable is set to
	 * 
	 * @since 1.0
	 */
	private Object value;
	
	/**
	 * The standard constructor to declare a {@link Variable}. When using this constructor simply use {@code super(key, value);}
	 * 
	 * @param key The identifier of the variable
	 * @param value The value assigned to the key
	 * @throws VariableAlreadyDeclaredException If a {@link Variable} is already declared with that key
	 * @since 1.0
	 */
	public Variable(String key, Object value) throws VariableAlreadyDeclaredException {
		this.key = key;
		this.value = value;
		for(Variable v : vars) {
			if (v.getKey().equals(key))
				throw new VariableAlreadyDeclaredException(this);
		}
		add(this);
	}
	
	/**
	 * Retrieve the key
	 * 
	 * @return The identifier of this variable
	 * @since 1.0
	 */
	public final String getKey() {
		return this.key;
	}
	
	/**
	 * Retrieve the {@link Object} that this {@link Variable} was assigned to
	 * 
	 * @see Object
	 * @return The variable's value
	 * @since 1.0
	 */
	public final Object getValue() {
		return this.value;
	}
	
	/**
	 * Set the new value of this variable
	 * 
	 * @param <T> The variable type
	 * @param v The new {@link Object} to assign
	 * @throws IncompatibleVariableTypesException If the variable is assigned something that it cannot represent
	 * @since 1.0
	 */
	public final <T> void set(T v) throws IncompatibleVariableTypesException {
		if (value.getClass().getSimpleName().equals(v.getClass().getSimpleName()))
			this.value = v;
		else
			throw new IncompatibleVariableTypesException(value, (Object) v);
	}
	
	/**
	 * Assign the variable based off a string such as {@code somevar = someobject}
	 * 
	 * @since 1.0
	 * @param str The string that assigns the variable
	 * @throws DonkeyException If something goes wrong
	 * @throws ScriptException If a math expression is invalid
	 */
	public static final void set(String str) throws DonkeyException, ScriptException {
		String[] s = str.split("=");
		String key;
		String type = "";
		try {
			key = s[0].trim().split(" ")[1].trim();
			type = replaceLast(s[0].trim(), key, "").trim();
		} catch(Exception e) {
			key = s[0].trim();
		}
		String value = s[1];
		for(int id = 2; id < s.length; id++)
			value = value + s[id];
		value = Statement.destroyEverythingAfterLastInstanceOf(value, ";");
		while(value.startsWith(" "))
			value = value.substring(1);
		try {
			while(value.endsWith(" "))
				value = value.substring(0, value.length() - 2);
		} catch(Exception e) {}
		for(IStatement m : Statement.getStatements()) {
			if (value.matches(".*" + m.getName() + ".*")) {
				Object o = m.run(null, value.substring(value.indexOf(m.getName())));
				if (o != null)
					o = o.toString().replaceAll("\\.0\\b", "");
				value = value.replace(m.getName(), o == null ? "null" : o.toString());
			}
		}
		long currentTime = System.currentTimeMillis();
		while(value.matches(".*\\w+\\s*\\(.*\\).*")) {
			if (System.currentTimeMillis() - currentTime > 30000) {
				Donkey.log(LogLevel.WARNING, "Timed out", "Donkey");
				break;
			}
			for(IMethod m : Statement.getMethods()) {
				if (value.matches(".*" + m.getName() + "\\(.*\\).*")) {
					Object o = m.run(null, value.substring(value.indexOf('(') + 1, findClosingParen(value.toCharArray(), value.indexOf('('))).split(","));
					if (o != null)
						o = o.toString().replaceAll("\\.0\\b", "");
					value = value.replaceFirst("@?\\w+\\s?\\(.*\\)", o == null ? "null" : o.toString());
				}
			}
		}
		Pattern pattern = Pattern.compile(".*((\\d*\\.\\d+)|(\\d+)|([\\+\\-\\*/%\\(\\)])).*");
		Matcher m = pattern.matcher(value);
		if(m.find()) {
			String g = m.group(0).replaceAll("[a-zA-Z]", " ").trim();
			value = value.replace(g, Expression.evaluate(g).toString().replaceAll("\\.0\\b", ""));
		}
		if (value.endsWith(".0"))
			value = value.replaceAll("\\.0$", "");
		if (value.startsWith("new ")) {
			value = value.replaceFirst("new\\s+", "");
			String[] args = value.replaceAll("\\(\\s*", "").replace("\\s*\\).*$", "").split(",");
			value = value.replaceAll("\\(.*\\).*$", "");
			for(Variable v : vars) {
				if (v.getKey().equals(key)) {
					v.set(getVariableFromName(value).constructor(args));
					return;
				}
			}
		} else {
			//primary type
			try {
				if (value.contains(".")) {
					Double n = Double.parseDouble(value);
					//VarDouble(n)
					if (type == "") { //edit variable
						for(Variable v : vars) {
							if (v.getKey().equals(key)) {
								v.set(n);
								return;
							}
						}
					} else { //declare variable
						if (type.equals("string")) {
							new VarString(key, n);
							return;
						}
						if (!type.equals("double"))
							throw new IncompatibleVariableTypesException(type, "double");
						new VarDouble(key, n);
						return;
					}
				} else {
					Integer n = Integer.parseInt(value);
					//VarInteger(n)
					if (type == "") { //edit variable
						for(Variable v : vars) {
							if (v.getKey().equals(key)) {
								v.set(n);
								return;
							}
						}
					} else { //declare variable
						if (type.equals("double")) {
							new VarDouble(key, n);
							return;
						}
						if (type.equals("string")) {
							new VarString(key, n);
							return;
						}
						if (!type.equals("integer"))
							throw new IncompatibleVariableTypesException(type, "integer");
						new VarInteger(key, n);
						return;
					}
				}
			} catch(NumberFormatException e) {
				if (value.equals("true")) {
					//VarBoolean(true)
					return;
				} else if (value.equalsIgnoreCase("false")) {
					//VarBoolean(false)
					return;
				} else {
					for(char c : value.toCharArray()) {
						if (c != ' ')
							break;
						else
							value = value.substring(value.indexOf(c + "") + 1);
					}
					while(value.endsWith(" "))
						value = value.replaceAll("\\s$", "");
					if (type == "") { //edit variable
						for(Variable v : vars) {
							if (v.getKey().equals(key)) {
								v.set((Object) value);
								return;
							}
						}
					} else { //declare variable
						if (!type.equals("string"))
							throw new IncompatibleVariableTypesException(type, "string");
						new VarString(key, value);
						return;
					}
				}
			}
		}
		throw new TypeDoesNotExistException(key);
	}
	
	/**
	 * Replace all instances of {@code `KEY`} where {@code KEY} represents the variable's key with the corresponding value of the variable
	 * 
	 * @param str The {@link String} to replace the key's instances with the values using their {@code toString()} method
	 * @return The new {@link String} with the instances replaced
	 * @since 1.0
	 */
	public final String replace(String str) {
		return str.replace('`' + key + '`', value.toString());
	}
	
	public String toString() {
		return getName();
	}
	
	private static final IVariable getVariableFromName(String name) throws TypeDoesNotExistException {
		for(IVariable v : getVariables()) {
			if (name.equals(v.getKey()))
				return v;
		}
		throw new TypeDoesNotExistException(name);
	}
	
	private static String replaceLast(String string, String substring, String replacement) {
		int index = string.lastIndexOf(substring);
		if (index == -1)
			return string;
		return string.substring(0, index) + replacement + string.substring(index + substring.length());
	}
	
	private static int findClosingParen(char[] text, int openPos) {
		int closePos = openPos;
		int counter = 1;
		while(counter > 0) {
			char c = text[++closePos];
			if (c == '(') {
				counter++;
			} else if (c == ')') {
				counter--;
			}
		}
		return closePos;
	}
	
	public IVariable constructor(String[] args) {
		return this;
	}
}