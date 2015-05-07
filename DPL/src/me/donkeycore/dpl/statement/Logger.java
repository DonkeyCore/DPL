package me.donkeycore.dpl.statement;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Donkey.LogLevel;
import me.donkeycore.dpl.exceptions.DonkeyException;

public class Logger {
	
	public static class Debug implements IStatement{
		
		public String getName() {
			return "@log:debug";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.DEBUG, args, "Script");
			return null;
		}
		
	}
	
	public static class Info implements IStatement{
		
		public String getName() {
			return "@log:info";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.INFO, args, "Script");
			return null;
		}
		
	}
	
	public static class Warning implements IStatement{
		
		public String getName() {
			return "@log:warning";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.WARNING, args, "Script");
			return null;
		}
		
	}
	
	public static class Error implements IStatement{
		
		public String getName() {
			return "@log:error";
		}
		
		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.ERROR, args, "Script");
			return null;
		}
		
	}
	
	public static class Fatal implements IStatement{

		public String getName() {
			return "@log:fatal";
		}

		public Object run(Statement statement, String args) throws DonkeyException {
			Donkey.log(LogLevel.FATAL, args, "Script");
			return null;
		}
		
	}
	
}