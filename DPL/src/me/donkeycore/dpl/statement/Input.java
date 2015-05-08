package me.donkeycore.dpl.statement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.exceptions.DonkeyException;

/**
 * Receive raw input from the user <br />
 * Syntax: <code>@input</code>
 * 
 * @since 1.0
 */
public class Input implements IStatement {
	
	public String getName() {
		return "@input";
	}
	
	public Object run(Statement statement, String args) throws DonkeyException {
		/*
		 * int mode = 0; //string
		 * 
		 * Matcher m = Pattern.compile("\\s*(integer|double).*").matcher(statement.getStatement());
		 * if(m.find()){
		 * String g = m.group().trim();
		 * if(g.equals("integer"))
		 * mode = 1; //int
		 * else if(g.equals("double"))
		 * mode = 2; //double
		 * }
		 */
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		try {
			return r.readLine();
		} catch(IOException e) {
			Donkey.printError(e);
			return null;
		}
	}
}
