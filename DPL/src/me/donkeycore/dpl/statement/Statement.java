package me.donkeycore.dpl.statement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptException;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Escape;
import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.exceptions.IncompatibleVariableTypesException;
import me.donkeycore.dpl.exceptions.InvalidStatementException;
import me.donkeycore.dpl.exceptions.TypeDoesNotExistException;
import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;
import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.method.If;
import me.donkeycore.dpl.method.RPN;
import me.donkeycore.dpl.method.Random;
import me.donkeycore.dpl.method.While;
import me.donkeycore.dpl.variables.Variable;

/**
 * Main statement and method handler
 * 
 * @see Statement#Statement(String, int)
 * @since 1.0
 */
public final class Statement {
	
	private static File file;
	private static final List<Integer> doNotRun = new ArrayList<Integer>();
	private static IStatement[] statements;
	private static IMethod[] methods;
	
	//private static Donkey donkey;
	/**
	 * Get all the statements in an array
	 * 
	 * @return An array of {@link IStatement} objects
	 * @since 1.0
	 */
	public static IStatement[] getStatements() {
		if (statements != null)
			return statements;
		List<IStatement> s = new ArrayList<IStatement>();
		s.add(new Println());
		s.add(new Print());
		s.add(new Stop());
		s.add(new Wait());
		s.add(new Debug());
		s.add(new Logger.Debug());
		s.add(new Logger.Info());
		s.add(new Logger.Warning());
		s.add(new Logger.Error());
		s.add(new Logger.Fatal());
		s.add(new Input());
		return statements = s.toArray(new IStatement[s.size()]);
	}
	
	/**
	 * Get all the methods in an array
	 * 
	 * @return An array of {@link IMethod} objects
	 * @since 1.0
	 */
	public static IMethod[] getMethods() {
		if (methods != null)
			return methods;
		List<IMethod> m = new ArrayList<IMethod>();
		m.add(new If());
		m.add(new While());
		m.add(new RPN());
		m.add(new Random.RandomBoolean());
		m.add(new Random.RandomInteger());
		return methods = m.toArray(new IMethod[m.size()]);
	}
	
	/**
	 * Add a custom statement
	 * 
	 * @param statement The statement to add
	 * @since 1.0
	 */
	public static void addStatement(IStatement statement) {
		List<IStatement> s = Arrays.asList(getStatements());
		s.add(statement);
		statements = s.toArray(new IStatement[s.size()]);
	}
	
	/**
	 * Add a custom method
	 * 
	 * @param method The method to add
	 * @since 1.0
	 */
	public static void addMethod(IMethod method) {
		List<IMethod> m = Arrays.asList(getMethods());
		m.add(method);
		methods = m.toArray(new IMethod[m.size()]);
	}
	
	/**
	 * Change the file being used. <br/>
	 * <b>Note: This is a method used by the system and should NOT be changed.</b>
	 * 
	 * @param f The file to change to
	 * @since 1.0
	 */
	public static final void setFile(File f) {
		file = f;
	}
	
	/**
	 * Retrieve the file being executed from.
	 * 
	 * @return The file being used
	 * @since 1.0
	 */
	public static final File getFile() {
		return file;
	}
	
	/**
	 * Get the amount of lines a {@link File} has.
	 * 
	 * @param f The file to get the information from
	 * @return The amount of lines the {@link File} has
	 * @since 1.0
	 */
	public static final int getMaxLine(File f) {
		int l = -1;
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			LineNumberReader lr = new LineNumberReader(r);
			while(lr.readLine() != null)
				l = lr.getLineNumber();
			lr.close();
		} catch(Exception e) {}
		return l + 1;
	}
	
	/**
	 * Get the line a {@link File} has on a certain line.
	 * 
	 * @param f The file to get the information from
	 * @return The line on that line number
	 * @since 1.0
	 */
	public static final String getLine(File f, int line) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			LineNumberReader lr = new LineNumberReader(r);
			String s;
			while((s = lr.readLine()) != null) {
				if (lr.getLineNumber() == line) {
					lr.close();
					return s;
				}
			}
			lr.close();
		} catch(Exception e) {}
		return "";
	}
	
	/**
	 * Make a statement not run according to its line number in the file.
	 * 
	 * @param stop Whether to stop the statement from running
	 * @param line The line to decline/allow
	 * @since 1.0
	 */
	public static final void setDoNotRun(boolean stop, Integer line) {
		if (stop && (!doNotRun.contains(line) || doNotRun.isEmpty()))
			doNotRun.add(line);
		else if (!stop)
			doNotRun.remove(line);
	}
	
	/**
	 * Checks for if the statement can run according to its line number
	 * 
	 * @param line The line to check for
	 * @return Whether the statement can run or not
	 * @since 1.0
	 */
	public static final boolean canRun(Integer line) {
		return !doNotRun.contains(line);
	}
	
	/**
	 * Updates the information on a statement
	 * 
	 * @param old The old statement
	 * @return The new statement
	 * @since 1.0
	 */
	public static final Statement getUpdatedStatement(Statement old) {
		try {
			return new Statement(getLine(getFile(), old.getLineNumber()), old.getLineNumber());
		} catch(Throwable e) {
			Donkey.printError(e);
		}
		return old;
	}
	
	/*
	 * /** Set the {@link Donkey} instance. Should not be used, as this is called by the main class.
	 * 
	 * @param donkey The {@link Donkey} instance to set
	 *//*
		 * public static final void setDonkey(Donkey donkey){
		 * Statement.donkey = donkey;
		 * }
		 * 
		 * private static final Donkey getDonkey(){
		 * return donkey;
		 * }
		 */
	private String statement;
	private final String raw;
	private final int line;
	
	/** The line of the attempt/fail block this {@link Statement} is inside. Defaults to <code>0</code> if not in an attempt/fail block. */
	//public int attempt = 0;
	/**
	 * Create a new {@link Statement}
	 * 
	 * @param statement The statement to execute
	 * @param lineNumber The statement's line number
	 * @since 1.0
	 * @see Statement
	 */
	public Statement(String statement, int lineNumber) throws TypeDoesNotExistException, IncompatibleVariableTypesException, VariableAlreadyDeclaredException {
		this.raw = statement;
		statement = statement.replaceAll("\t", " ");
		for(char c : statement.toCharArray()) {
			if (c != ' ')
				break;
			else
				statement = statement.substring(statement.indexOf(c + "") + 1);
		}
		while(statement.endsWith(" "))
			statement = statement.replaceAll("\\s$", "");
		statement = statement.replace("\\" + Escape.SEMICOLON.getCharacterString(), Escape.SEMICOLON.getReplace());
		statement = statement.replace("\\" + Escape.BACKSLASH.getCharacterString(), Escape.BACKSLASH.getReplace());
		statement = statement.replace("\\t", Escape.TAB.getReplace());
		statement = statement.replace("\\n", Escape.NEWLINE.getReplace());
		statement = statement.replace("\\" + Escape.GRAVE.getCharacterString(), Escape.GRAVE.getReplace());
		statement = statement.replace("\\", Escape.SPACE.getReplace());
		statement = statement.replace("\\'", Escape.QUOTES.getReplace());
		statement = statement.replace("\\\"", Escape.QUOTED.getReplace());
		statement = destroyEverythingAfterLastInstanceOf(statement, "//");
		if (statement.contains(";"))
			statement = destroyEverythingAfterLastInstanceOf(statement, ";");
		statement = statement.replace("'", "");
		statement = statement.replace("\"", "");
		statement = statement.replace(Escape.SEMICOLON.getReplace(), Escape.SEMICOLON.getCharacterString());
		statement = statement.replace(Escape.BACKSLASH.getReplace(), Escape.BACKSLASH.getCharacterString());
		statement = statement.replace(Escape.TAB.getReplace(), Escape.TAB.getCharacterString());
		statement = statement.replace(Escape.NEWLINE.getReplace(), Escape.NEWLINE.getCharacterString());
		statement = statement.replace(Escape.SPACE.getReplace(), Escape.SPACE.getCharacterString());
		statement = statement.replace(Escape.QUOTES.getReplace(), Escape.QUOTES.getCharacterString());
		statement = statement.replace(Escape.QUOTED.getReplace(), Escape.QUOTED.getCharacterString());
		this.statement = statement;
		this.line = lineNumber;
	}
	
	/**
	 * Retrieves the line number that the current statement is on
	 * 
	 * @return The line number of the {@link Statement}
	 * @since 1.0
	 */
	public final int getLineNumber() {
		return line;
	}
	
	/**
	 * Retrieves the text of the statement before it was parsed
	 * 
	 * @return The raw statement
	 * @since 1.0
	 */
	public final String getRawStatement() {
		return destroyEverythingAfterLastInstanceOf(raw, ";", false);
	}
	
	/**
	 * Retrieves the text of the {@link Statement} after it was parsed
	 * 
	 * @return The parsed statement
	 * @since 1.0
	 */
	public final String getStatement() {
		return statement;
	}
	
	/**
	 * Run the {@link Statement}
	 * 
	 * @since 1.0
	 * @return The object that the statement/method returns, or <code>null</code> if there is no return
	 * @throws DonkeyException If something goes wrong
	 * @throws ScriptException If a math expression is invalid
	 */
	public synchronized final Object runStatement() throws DonkeyException, ScriptException {
		return runStatement(false);
	}
	
	/**
	 * Run the {@link Statement}
	 * 
	 * @since 1.0
	 * @param force Force the statement to run
	 * @return The object that the statement/method returns, or <code>null</code> if there is no return
	 * @throws DonkeyException If something goes wrong
	 * @throws ScriptException If a math expression is invalid
	 */
	public synchronized final Object runStatement(boolean force) throws DonkeyException, ScriptException {
		if (statement.startsWith("//") || (!canRun(getLineNumber()) && !force) || statement.equalsIgnoreCase("{") || statement.equalsIgnoreCase("}") || statement.equalsIgnoreCase(""))
			return null;
		for(Variable v : Variable.getVariables())
			statement = v.replace(statement);
		statement = statement.replace(Escape.GRAVE.getReplace(), Escape.GRAVE.getCharacterString());
		/*
		 * if(statement.matches("\\s*attempt\\s*\\{.*")){
		 * attempt = getLineNumber();
		 * try{
		 * runBlock(true);
		 * }catch(Throwable e){
		 * int l = findCatch(getLineNumber());
		 * new Statement(getLine(file, l), l).runBlock(true);
		 * }
		 * return null;
		 * }else if(statement.matches("\\s*\\}\\s*fail\\s*\\{.*")){
		 * runBlock(false);
		 * return null;
		 * }
		 */
		for(IMethod m : getMethods()) {
			if (statement.matches(m.getName() + "\\s*\\(.*\\).*"))
				return m.run(this, statement.replaceAll(m.getName() + "\\s*\\(", "").replaceAll("\\).*", "").split(","));
		}
		for(IStatement s : getStatements()) {
			if (statement.matches(s.getName() + "\\s*.*"))
				return s.run(this, statement.replaceAll(s.getName() + "\\s+", ""));
		}
		if (statement.matches(".*=.*")) {
			Variable.set(statement);
			return null;
		}
		throw new InvalidStatementException(this.statement, getLineNumber());
	}
	
	/**
	 * Run the block that the statement controls
	 * 
	 * @param run Whether to run the contents of the block or not
	 * @throws DonkeyException If there's an error in the Donkey code
	 * @throws ScriptException If an error happens in the script
	 * @since 1.0
	 */
	public final synchronized void runBlock(boolean run) throws ScriptException, DonkeyException {
		runBlock(run, false);
	}
	
	/**
	 * Run the block that the statement controls
	 * 
	 * @param run Whether to run the contents of the block or not
	 * @param force If run is on, then force the statements to run
	 * @throws DonkeyException If there's an error in the Donkey code
	 * @throws ScriptException If an error happens in the script
	 * @since 1.0
	 */
	public final synchronized void runBlock(boolean run, boolean force) throws ScriptException, DonkeyException {
		if (statement.endsWith("{")) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				LineNumberReader lr = new LineNumberReader(r);
				String s;
				String[] l;
				int sNum = getLineNumber();
				int eNum = -1;
				long brackets = 0;
				while((s = lr.readLine()) != null) {
					l = s.split(";");
					for(String st : l) {
						if (lr.getLineNumber() > sNum && st.startsWith("}") && st.endsWith("{")) {// && brackets == 1){
							eNum = lr.getLineNumber();
							break;
						} else {
							if (lr.getLineNumber() >= sNum && st.endsWith("{"))
								brackets++;
							if (lr.getLineNumber() >= sNum && st.startsWith("}"))
								brackets--;
							if (st.startsWith("}") && lr.getLineNumber() > sNum && brackets == 0) {
								eNum = lr.getLineNumber();
								break;
							}
						}
					}
					if (eNum != -1)
						break;
				}
				r.close();
				if (eNum == -1)
					eNum = lr.getLineNumber();
				lr = new LineNumberReader(r = new BufferedReader(new FileReader(file)));
				while((s = lr.readLine()) != null) {
					l = s.split(";");
					for(String st : l) {
						if (lr.getLineNumber() > sNum && lr.getLineNumber() < eNum) {
							/*
							 * for(Statement sta : getDonkey().getDonkeyClass().getIOClass().getStatements()){
							 * if(sta.getRawStatement().equals(st)){
							 * if(attempt != 0)
							 * sta.attempt = attempt;
							 * if(run)
							 * sta.runStatement();
							 * Statement.setDoNotRun(true, lr.getLineNumber());
							 * break;
							 * }
							 * }
							 */
							///*
							Statement statement = new Statement(st, lr.getLineNumber());
							//if(attempt != 0)
							//statement.attempt = attempt;
							if (run)
								statement.runStatement(force);
							Statement.setDoNotRun(true, lr.getLineNumber());//*/
						}
					}
				}
				lr.close();
			} catch(IOException e) {}
		} else {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				LineNumberReader lr = new LineNumberReader(r);
				String s;
				String[] l;
				while((s = lr.readLine()) != null) {
					l = s.split(";");
					for(String st : l) {
						if (getLineNumber() + 1 == lr.getLineNumber() && !st.startsWith("//")) {
							Statement statement = new Statement(st, lr.getLineNumber());
							if (run)
								statement.runStatement(force);
							Statement.setDoNotRun(true, lr.getLineNumber());
						}
					}
				}
				lr.close();
			} catch(IOException e) {}
		}
	}
	
	/**
	 * Destroy every character after the last instance of a certain string, including that string. Equivilant to calling <code>destroyEverythingAfterLastInstanceOf(String, String, true)</code>
	 * 
	 * @param string The string to parse
	 * @param destroy The string that is removed along with everything after it
	 * @return The parsed string
	 * @since 1.0
	 */
	public static String destroyEverythingAfterLastInstanceOf(String string, String destroy) {
		return destroyEverythingAfterLastInstanceOf(string, destroy, false);
	}
	
	/**
	 * Destroy every character after the last instance of a certain string.
	 * 
	 * @param string The string to parse
	 * @param destroy The string that removes everything after it
	 * @param inclusive Whether to remove the <code>destroy</code> string along with everything after it.
	 * @return The parsed string
	 * @since 1.0
	 */
	public static String destroyEverythingAfterLastInstanceOf(String string, String destroy, boolean inclusive) {
		int index = string.lastIndexOf(destroy);
		if (index == -1)
			return string;
		if (inclusive)
			return string.substring(0, index - 1);
		return string.substring(0, index);
	}
	
}