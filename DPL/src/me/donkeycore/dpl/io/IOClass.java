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

/** Manages the I/O of the file and creates each statement to run them
 * @see IOClass#IOClass(DonkeyClass)
 * @since 1.0
 */
public final class IOClass {
	
	private final DonkeyClass clazz;
	
	/** Create a new {@link IOClass} from a {@link DonkeyClass}
	 * @see DonkeyClass 
	 * @since 1.0
	 */
	public IOClass(DonkeyClass clazz){
		this.clazz = clazz;
	}
	
	/** Retrieve the {@link DonkeyClass} managing this {@link IOClass}
	 * @return The {@link DonkeyClass} owning this {@link IOClass}
	 * @since 1.0
	 */
	public DonkeyClass getDonkeyClass(){
		return this.clazz;
	}

	/** Run the code inside the file.
	 * @throws InvalidStatementException If a statement is invalid
	 * @since 1.0
	 */
	public void runCode() throws InvalidStatementException{
		//Statement st = null;
		try{
			String last = "";
			for(Statement s : getStatements()){
				if(Statement.canRun(s.getLineNumber())){
					s.runStatement();
					if(s.getStatement().startsWith("print"))
						last = s.getStatement();
				}
			}
			
			if(last.matches("print\\s*;"))
				System.out.println();
			System.out.println();
			long endTime = System.currentTimeMillis();
			float totalTimeMS = endTime - getDonkeyClass().getDonkey().startTime;
			float totalTimeD = totalTimeMS / 1000;
			String totalTime = totalTimeD + "";
			int n = 6;
			while(totalTime.length() > 3){
				if(totalTime.length() >= n)
					totalTime = totalTime.substring(0, n);
				n--;
			}
			totalTime = Statement.destroyEverythingAfterLastInstanceOf(totalTime, "\\.0");
			if(totalTime.endsWith("."))
				totalTime = totalTime.replaceAll("\\.$", "");
			Donkey.log(LogLevel.DEBUG, "Finished in " + (totalTimeMS + "").replaceAll("\\.[0]\\b", "") + "ms (" + totalTime + " seconds)", "Donkey");
			Donkey.log(LogLevel.DEBUG, "Press Enter to continue.", "Donkey");
			if(Donkey.debug)
				new BufferedReader(new InputStreamReader(System.in)).readLine();
			System.exit(0);
		}catch(Throwable e){
			if(e instanceof InvalidStatementException)
				throw new InvalidStatementException(e.getMessage());
			
			//for(int i = 0; i < 10; i++)
				//System.out.print("\b");
			//FileCreator.getBatchFile("error").withNoCode().withCode("@echo off").withCode("title Donkey - Error: " + e.getMessage()).withCode("echo -=[DPL Error Report]=-").withCode("echo.").withCode("echo A fatal error has occured while running " + getDonkeyClass().getFile().getAbsolutePath() + ":").withCode("echo " + e.getMessage()).withCode("echo.").withCode("").run();
			//if(st.attempt == 0){
				//Donkey.error(st.getStatement() + ":" + st.getLineNumber() + " (" + st.getRawStatement() + ")");
				Donkey.log(LogLevel.FATAL, "A fatal error has occured while running " + getDonkeyClass().getFile().getAbsolutePath() + ":", "Donkey");
				Donkey.log(LogLevel.FATAL, e.getMessage(), "Donkey");
				Donkey.log(LogLevel.FATAL, "Internal stack trace:", "Donkey");
				Donkey.printError(e);
				System.exit(1);
			/*}else{
				try{
					new Statement(st.getRawStatement(), st.attempt).runBlock(true);
				}catch(Throwable e1){
					Donkey.printError(e1);
				}
			}*/
		}
	}
	
	/** Get all the statements contained in the file.
	 * @return An array of {@link Statement} objects in the {@link File}
	 * @since 1.0
	 */
	public Statement[] getStatements(){
		List<Statement> statements = new ArrayList<Statement>();
		LineNumberReader r = null;
		try{
			File f = clazz.getFile();
			Statement.setFile(f);
			r = new LineNumberReader(new BufferedReader(new FileReader(f)));
			
			String s;
			//String[] l;
			while((s = r.readLine()) != null){
				//l = s.split(";");
				//for(String st : l)
				if(s.length() > 0)
					statements.add(new Statement(getStatement(s, s/*t*/), r.getLineNumber()));
			}
			
			r.close();
			return statements.toArray(new Statement[statements.size()]);
		}catch(Throwable e){
			Donkey.printError(e);
		}finally{
			if(r != null)
				try {
					r.close();
				} catch (IOException e) {
					Donkey.printError(e);
				}
		}
		return null;
	}
	
	private String getStatement(String s, String st){
		if(s.indexOf(st) + st.length() < s.length())
			return st + (((s.charAt(s.indexOf(st) + (st.length()))) == ';') ? ";":"");
		else
			return st + (((s.charAt(s.indexOf(st) + (st.length() - 1))) == ';') ? ";":"");
	}
	
}