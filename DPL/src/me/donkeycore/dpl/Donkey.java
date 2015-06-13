package me.donkeycore.dpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import me.donkeycore.dpl.exceptions.FileDirectoryException;
import me.donkeycore.dpl.exceptions.IncompatibleVariableTypesException;
import me.donkeycore.dpl.exceptions.InvalidFileException;
import me.donkeycore.dpl.exceptions.InvalidStatementException;
import me.donkeycore.dpl.exceptions.NoFileException;
import me.donkeycore.dpl.exceptions.NoReadException;
import me.donkeycore.dpl.exceptions.TypeDoesNotExistException;
import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;
import me.donkeycore.dpl.gui.ScriptGUI;
import me.donkeycore.dpl.io.DonkeyClass;
import me.donkeycore.dpl.io.FileCreator;
import me.donkeycore.dpl.io.FileCreator.FileConfiguration;
import me.donkeycore.dpl.method.IMethod;
import me.donkeycore.dpl.plugin.DPlugin;
import me.donkeycore.dpl.plugin.PluginLoader;
import me.donkeycore.dpl.statement.IStatement;
import me.donkeycore.dpl.statement.Statement;

/**
 * Donkey Programming Language [DPL]
 * 
 * @author DonkeyCore
 * @version 1.0
 */
public final class Donkey {
	
	/**
	 * Decides whether to display debug messages
	 * 
	 * @see Donkey#debug(String)
	 * @since 1.0
	 */
	public static boolean debug = true;
	/**
	 * Displays the time that the program was started and is used for measuring time to run the program.
	 * 
	 * @since 1.0
	 */
	private long startTime;
	/**
	 * The file that will be run
	 * 
	 * @since 1.0
	 */
	private final File file;
	/**
	 * The {@link DonkeyClass} object responsible for the file
	 * 
	 * @since 1.0
	 */
	private final DonkeyClass clazz;
	
	/**
	 * An array of registered {@link DPlugin} objects
	 * 
	 * @since 1.0
	 */
	private final DPlugin[] plugins;
	
	/**
	 * If given a null argument or 0 parameters, this will open a GUI for choosing a script. Given one or more file paths, this will run the code of those files. <br>
	 * This method will instantly run each file's code and is useful for running several files of code, where creating a new {@link Donkey} object will allow you to access methods being used and is better for executing only one file's code.
	 * 
	 * @param args The array of files to execute
	 * @see Donkey#Donkey(File)
	 * @throws NoFileException If the file does not exist
	 * @throws NoReadException If the file cannot be read
	 * @throws FileDirectoryException If the file is a directory
	 * @throws InvalidFileException If the file is not a .donkey file or is not a valid Donkey class file
	 * @throws IncompatibleVariableTypesException If a variable is assigned something that it cannot represent
	 * @throws TypeDoesNotExistException If a variable being assigned has not been declared
	 * @throws VariableAlreadyDeclaredException If a variable has already been declared
	 * @throws InvalidStatementException If a statement parses as invalid
	 * @since 1.0
	 */
	public static void main(String... args) throws NoFileException, NoReadException, FileDirectoryException, InvalidFileException, InvalidStatementException, TypeDoesNotExistException, IncompatibleVariableTypesException, VariableAlreadyDeclaredException {
		if (args == null || args.length == 0)
			new ScriptGUI();
		else {
			for(String file : args)
				new Donkey(new File(file)).runCode();
		}
	}
	
	/**
	 * Create a Donkey class from a file. <br>
	 * Creating a new {@link Donkey} object is useful for accessing methods being used and only having one file to execute, where using the {@link Donkey#main(String[])} method is useful for having multiple files to execute without a need for other methods.
	 * 
	 * @param read The file to read from
	 * @throws NoFileException If the file does not exist
	 * @throws NoReadException If the file cannot be read
	 * @throws FileDirectoryException If the file is a directory
	 * @throws InvalidFileException If the file is not a .donkey file or is not a valid Donkey class file
	 * @see Donkey#main(String[])
	 * @since 1.0
	 */
	public Donkey(File read) throws NoFileException, NoReadException, FileDirectoryException, InvalidFileException {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> printError(e));
		this.file = read;
		if (!file.exists())
			throw new NoFileException("The file " + file.getAbsolutePath() + " does not exist!");
		if (!file.canRead())
			throw new NoReadException("The file " + file.getAbsolutePath() + " cannot be read!");
		if (file.isDirectory())
			throw new FileDirectoryException("The file " + file.getAbsolutePath() + " is a directory!");
		if (!file.getName().toLowerCase().endsWith(".donkey") && !file.getName().toLowerCase().endsWith(".dpl"))
			throw new InvalidFileException("The file " + file.getAbsolutePath() + " is not a Donkey class!");
		PluginLoader.stopAccepting();
		plugins = PluginLoader.getPluginLoader().getPlugins();
		this.clazz = new DonkeyClass(this, file);
		FileCreator.loadFilesAndFolders();
		FileConfiguration fc = FileCreator.getFile("recentFiles", "log");
		File f = fc.getFile();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String s;
			while((s = r.readLine()) != null) {
				if (s.equals(file.getAbsolutePath())) {
					r.close();
					return;
				}
			}
			r.close();
		} catch(Exception e) {}
		if (Statement.getMaxLine(f) > 5) {
			fc.withoutLastLine().withCode(file.getAbsolutePath());
		} else
			fc.withCode(file.getAbsolutePath());
	}
	
	public DPlugin[] getPlugins() {
		DPlugin[] p = new DPlugin[plugins.length];
		System.arraycopy(plugins, 0, p, 0, plugins.length);
		return p;
	}
	
	/**
	 * Run the code. Automatically called in {@link Donkey#main(String[])}, but you must manually run it when using the {@link Donkey#Donkey(File)} constructor.
	 * 
	 * @throws InvalidFileException If the file does not end with .donkey or is not a valid Donkey class file
	 * @throws IncompatibleVariableTypesException If a variable is assigned something that it cannot represent
	 * @throws TypeDoesNotExistException If a variable being assigned has not been declared
	 * @throws VariableAlreadyDeclaredException If a variable has already been declared
	 * @see Donkey#main(String[])
	 * @see Donkey#Donkey(File)
	 * @since 1.0
	 */
	public void runCode() throws InvalidFileException, TypeDoesNotExistException, IncompatibleVariableTypesException, VariableAlreadyDeclaredException {
		startTime = System.currentTimeMillis();
		getDonkeyClass().runCode();
	}
	
	/**
	 * Retrieve the file being used.
	 * 
	 * @return The {@link File} that is being executed
	 * @since 1.0
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Retrieve the Donkey class file.
	 * 
	 * @return A {@link DonkeyClass} representing the file
	 * @see DonkeyClass
	 * @since 1.0
	 */
	public DonkeyClass getDonkeyClass() {
		return this.clazz;
	}
	
	/**
	 * Retrieve the time that the program was started
	 * @return The program start time
	 * @since 1.0
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Retrieve the default plugin loader
	 * @return The default plugin loader
	 * @since 1.0
	 */
	public static PluginLoader getDefaultPluginLoader() {
		return PluginLoader.getDefaultPluginLoader();
	}
	
	/**
	 * Retrieve the current plugin loader
	 * @return The current plugin loader
	 * @since 1.0
	 */
	public static PluginLoader getPluginLoader() {
		return PluginLoader.getPluginLoader();
	}
	
	/**
	 * Print an error parsed from a {@link Throwable}
	 * 
	 * @param e The throwable to print
	 * @since 1.0
	 */
	public static void printError(Throwable e) {
		log(LogLevel.FATAL, "An error has occurred! Details as follows:", "Donkey");
		if (e.getCause() != null)
			log(LogLevel.FATAL, "Cause: " + e.getCause().getClass().getSimpleName() + " : " + e.getMessage(), "Donkey");
		else
			log(LogLevel.FATAL, "Cause: " + e.getMessage(), "Donkey");
		int id = 1;
		for(StackTraceElement s : e.getStackTrace()) {
			if (!s.getClassName().startsWith("java"))
				log(LogLevel.FATAL, "Error #" + id++ + ": " + s.getClassName() + " at line " + s.getLineNumber() + " in " + s.getMethodName(), "Donkey");
		}
		System.exit(1);
	}
	
	/**
	 * Get all the statements in an array from the {@link Statement} class
	 * 
	 * @return An array of registered {@link IStatement} objects
	 * @see Statement#getStatements()
	 * @since 1.0
	 */
	public static IStatement[] getStatements() {
		return Statement.getStatements();
	}
	
	/**
	 * Get all the methods in an array from the {@link Statement} class
	 * 
	 * @return An array of registered {@link IMethod} objects
	 * @see Statement#getMethods()
	 * @since 1.0
	 */
	public static IMethod[] getMethods() {
		return Statement.getMethods();
	}
	
	/**
	 * Add a custom statement to the {@link Statement} class
	 * 
	 * @param statement The statement to add
	 * @see Statement#addStatement(IStatement)
	 * @since 1.0
	 */
	public static void addStatement(IStatement statement) {
		Statement.addStatement(statement);
	}
	
	/**
	 * Add a custom method to the {@link Statement} class
	 * 
	 * @param method The method to add
	 * @see Statement#addMethod(IMethod)
	 * @since 1.0
	 */
	public static void addMethod(IMethod method) {
		Statement.addMethod(method);
	}
	
	/**
	 * Clear the console using <code>cls</code> on Windows and <code>clear</code> on other platforms.
	 * 
	 * @since 1.0
	 */
	public static void clearConsole() {
		try {
			String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				FileCreator.getBatchFile("cls").withNoCode().withCode("cls").run();
			} else {
				FileCreator.getBashScript("clear").withNoCode().withCode("clear").run();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Log a message to the console. Same effect as calling {@link Donkey#log(LogLevel, String, String) log(level, message, "Plugin")}
	 * 
	 * @param level The level to log the message as
	 * @param message The message to log
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel
	 * @since 1.0
	 */
	public static void log(LogLevel level, String message) {
		log(level, message, "Plugin");
	}
	
	/**
	 * Log a message to the console, with a prefix
	 * 
	 * @param level The level to log the message as
	 * @param message The message to log
	 * @param prefix A custom prefix for the log message
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel
	 * @since 1.0
	 */
	public static void log(LogLevel level, String message, String prefix) {
		if (level.equals(LogLevel.DEBUG) && !debug)
			return;
		String hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR) + "";
		String minute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE) + "";
		String second = java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) + "";
		int ampm = java.util.Calendar.getInstance().get(java.util.Calendar.AM_PM);
		if (Integer.parseInt(hour) < 10)
			hour = "0" + hour;
		if (Integer.parseInt(minute) < 10)
			minute = "0" + minute;
		if (Integer.parseInt(second) < 10)
			second = "0" + second;
		String timestamp = "[" + hour + ":" + minute + ":" + second + ":" + (ampm == 0 ? "AM" : "PM") + "] ";
		System.out.println(timestamp + "[" + prefix + "/" + level.name() + "]: " + message);
	}
	
	/**
	 * Log a message to the console if {@link Donkey#debug} is true
	 * 
	 * @param message The message to log
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel#DEBUG
	 * @since 1.0
	 */
	public static void debug(String message) {
		if (debug)
			log(LogLevel.DEBUG, message);
	}
	
	/**
	 * Log a message to the console
	 * 
	 * @param message The message to log
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#debug(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel#INFO
	 * @since 1.0
	 */
	public static void info(String message) {
		log(LogLevel.INFO, message);
	}
	
	/**
	 * Log a message to the console
	 * 
	 * @param message The message to log
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel#WARNING
	 * @since 1.0
	 */
	public static void warning(String message) {
		log(LogLevel.WARNING, message);
	}
	
	/**
	 * Log a message to the console
	 * 
	 * @param message The message to log
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel#ERROR
	 * @since 1.0
	 */
	public static void error(String message) {
		log(LogLevel.ERROR, message);
	}
	
	/**
	 * Log a message to the console
	 * 
	 * @param message The message to log
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see LogLevel#FATAL
	 * @since 1.0
	 */
	public static void fatal(String message) {
		log(LogLevel.FATAL, message);
	}
	
	/**
	 * LogLevels for {@link Donkey#log(LogLevel, String)}
	 * 
	 * @see Donkey#log(LogLevel, String)
	 * @see Donkey#debug(String)
	 * @see Donkey#info(String)
	 * @see Donkey#warning(String)
	 * @see Donkey#error(String)
	 * @see Donkey#fatal(String)
	 * @see LogLevel#DEBUG
	 * @see LogLevel#INFO
	 * @see LogLevel#WARNING
	 * @see LogLevel#ERROR
	 * @see LogLevel#FATAL
	 * @since 1.0
	 */
	public static enum LogLevel {
		/**
		 * Used for debugging; can be toggled using {@link Donkey#debug}
		 * 
		 * @see Donkey#debug(String)
		 * @see LogLevel#INFO
		 * @see LogLevel#WARNING
		 * @see LogLevel#ERROR
		 * @see LogLevel#FATAL
		 * @since 1.0
		 */
		DEBUG,
		/**
		 * Standard level; Used for displaying information
		 * 
		 * @see Donkey#info(String)
		 * @see LogLevel#DEBUG
		 * @see LogLevel#WARNING
		 * @see LogLevel#ERROR
		 * @see LogLevel#FATAL
		 * @since 1.0
		 */
		INFO,
		/**
		 * Used to notify that there could be an error
		 * 
		 * @see Donkey#warning(String)
		 * @see LogLevel#DEBUG
		 * @see LogLevel#INFO
		 * @see LogLevel#ERROR
		 * @see LogLevel#FATAL
		 * @since 1.0
		 */
		WARNING,
		/**
		 * Used to signify an error that happened
		 * 
		 * @see Donkey#error(String)
		 * @see LogLevel#DEBUG
		 * @see LogLevel#INFO
		 * @see LogLevel#WARNING
		 * @see LogLevel#FATAL
		 * @since 1.0
		 */
		ERROR,
		/**
		 * Used to explain a reason for a fatal error
		 * 
		 * @see Donkey#fatal(String)
		 * @see LogLevel#DEBUG
		 * @see LogLevel#INFO
		 * @see LogLevel#WARNING
		 * @see LogLevel#ERROR
		 * @since 1.0
		 */
		FATAL;
	}
}