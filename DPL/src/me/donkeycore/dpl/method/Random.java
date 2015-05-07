package me.donkeycore.dpl.method;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

public class Random {
	
	private static final java.util.Random r = new java.util.Random();
	
	public static class RandomBoolean implements IMethod{
		
		public String getName(){
			return "@randomBoolean";
		}
		
		public Object run(Statement statement, String[] args) throws DonkeyException {
			return r.nextBoolean();
		}
		
	}
	
	public static class RandomInteger implements IMethod{

		public String getName(){
			return "@randomInteger";
		}

		public Object run(Statement statement, String[] args) throws DonkeyException {
			if(args.length == 0 || args[0].equals(""))
				return null;
			if(args.length == 1)
				return r.nextInt(Integer.parseInt(args[0]));
			else{
				if(Integer.parseInt(args[1]) >= Integer.parseInt(args[0]))
					return r.nextInt(Integer.parseInt(args[1]) - Integer.parseInt(args[0])) + Integer.parseInt(args[0]);
				else
					return r.nextInt(Integer.parseInt(args[0]) - Integer.parseInt(args[1])) + Integer.parseInt(args[1]);
			}
		}
		
	}
	
}