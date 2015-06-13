package me.donkeycore.dpl.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Donkey.LogLevel;
import me.donkeycore.dpl.exceptions.InvalidStatementException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Manages the I/O of the file and creates each statement to run them
 * 
 * @see IOClass#IOClass(DonkeyClass)
 * @since 1.0
 */
public final class IOClass {
	
	/**
	 * The {@link DonkeyClass} handling this object
	 * 
	 * @see DonkeyClass
	 * @since 1.0
	 */
	private final DonkeyClass clazz;
	
	/**
	 * The current line number
	 * @since 1.0
	 */
	private int line;
	
	/**
	 * Create a new {@link IOClass} from a {@link DonkeyClass}
	 * 
	 * @param clazz A {@link DonkeyClass} that manages this object
	 * @see DonkeyClass
	 * @since 1.0
	 */
	public IOClass(DonkeyClass clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Retrieve the {@link DonkeyClass} managing this {@link IOClass}
	 * 
	 * @return The {@link DonkeyClass} owning this {@link IOClass}
	 * @since 1.0
	 */
	public DonkeyClass getDonkeyClass() {
		return this.clazz;
	}
	
	/**
	 * Run the code inside the file.
	 * 
	 * @throws InvalidStatementException If a statement is invalid
	 * @since 1.0
	 */
	public void runCode() throws InvalidStatementException {
		try {
			String last = "";
			for(Statement s : getStatements()) {
				if (Statement.canRun(s.getLineNumber())) {
					line = s.getLineNumber();
					s.runStatement();
					if (s.getStatement().startsWith("print"))
						last = s.getStatement();
				}
			}
			if (last.matches("print\\s*;*"))
				System.out.println();
			System.out.println();
			long endTime = System.currentTimeMillis();
			float totalTimeMS = endTime - getDonkeyClass().getDonkey().getStartTime();
			float totalTimeD = totalTimeMS / 1000;
			String totalTime = totalTimeD + "";
			int n = 6;
			while(totalTime.length() > 3) {
				if (totalTime.length() >= n)
					totalTime = totalTime.substring(0, n);
				n--;
			}
			totalTime = Statement.destroyEverythingAfterLastInstanceOf(totalTime, "\\.0");
			if (totalTime.endsWith("."))
				totalTime = totalTime.replaceAll("\\.$", "");
			Donkey.log(LogLevel.DEBUG, "Finished in " + (totalTimeMS + "").replaceAll("\\.[0]\\b", "") + "ms (" + totalTime + " seconds) with exit code: " + Statement.errno, "Donkey");
			Donkey.log(LogLevel.DEBUG, "Press Enter to continue.", "Donkey");
			if (Donkey.debug) {
				BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
				r.readLine();
				r.close();
			}
			System.exit(Statement.errno);
		} catch(Throwable e) {
			if (e instanceof InvalidStatementException)
				throw new InvalidStatementException(e.getMessage());
			Donkey.log(LogLevel.FATAL, "A fatal error has occurred while running " + getDonkeyClass().getFile().getAbsolutePath() + " at line " + line + ":", "Donkey");
			Donkey.printError(e);
			System.exit(1);
		}
	}
	
	/**
	 * Get all the statements contained in the file.
	 * 
	 * @return An array of {@link Statement} objects in the {@link File}
	 * @since 1.0
	 */
	public Statement[] getStatements() {
		List<Statement> statements = new ArrayList<Statement>();
		LineNumberReader r = null;
		try {
			File f = clazz.getFile();
			Statement.setFile(f);
			r = new LineNumberReader(new BufferedReader(new FileReader(f)));
			String s;
			int line = 1;
			while((s = r.readLine()) != null) {
				if (s.length() > 0)
					statements.add(new Statement(getStatement(s, s), line));
				line++;
			}
			r.close();
			return statements.toArray(new Statement[statements.size()]);
		} catch(Throwable e) {
			Donkey.printError(e);
		} finally {
			if (r != null)
				try {
					r.close();
				} catch(IOException e) {
					Donkey.printError(e);
				}
		}
		return null;
	}
	
	/**
	 * Get the line number currently being read
	 * @return The line number
	 * @since 1.0
	 */
	public int getCurrentLineNumber() {
		return line;
	}
	
	/**
	 * Parse a statement
	 * @return The parsed statement
	 * @since 1.0
	 */
	private String getStatement(String s, String st) {
		if (s.indexOf(st) + st.length() < s.length())
			return st + (((s.charAt(s.indexOf(st) + (st.length()))) == ';') ? ";" : "");
		else
			return st + (((s.charAt(s.indexOf(st) + (st.length() - 1))) == ';') ? ";" : "");
	}
}