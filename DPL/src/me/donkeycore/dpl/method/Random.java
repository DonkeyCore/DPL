package me.donkeycore.dpl.method;

import me.donkeycore.dpl.exceptions.DonkeyException;
import me.donkeycore.dpl.statement.Statement;

/**
 * Used to generate pseudorandom values
 * 
 * @since 1.0
 */
public class Random {
	
	/**
	 * A {@link java.util.Random} instance used to calculate pseudorandom values
	 * 
	 * @since 1.0
	 */
	private static final java.util.Random r = new java.util.Random();
	
	/**
	 * Generate a random value: <code>true</code> or <code>false</code> <br>
	 * Syntax: <code>@randomBoolean()</code>
	 * 
	 * @since 1.0
	 */
	public static class RandomBoolean implements IMethod {
		
		public String getName() {
			return "@randomBoolean";
		}
		
		public Object run(Statement statement, String[] args) throws DonkeyException {
			return r.nextBoolean();
		}
	}
	
	/**
	 * Generate a random integer or by the bounds specified <br>
	 * Syntax: <code>@randomInteger()</code> <br>
	 * Syntax: <code>@randomInteger(high)</code> <br>
	 * Syntax: <code>@randomInteger(high, low)</code><br>
	 * 
	 * <b>high</b> The upper bound, exclusive<br>
	 * <b>low</b> The lower bound, inclusive
	 * @since 1.0
	 */
	public static class RandomInteger implements IMethod {
		
		public String getName() {
			return "@randomInteger";
		}
		
		public Object run(Statement statement, String[] args) throws DonkeyException {
			if (args.length == 0 || args[0].equals(""))
				return r.nextInt();
			if (args.length == 1)
				return r.nextInt(Integer.parseInt(args[0]));
			else {
				if (Integer.parseInt(args[1]) >= Integer.parseInt(args[0]))
					return r.nextInt(Integer.parseInt(args[1]) - Integer.parseInt(args[0])) + Integer.parseInt(args[0]);
				else
					return r.nextInt(Integer.parseInt(args[0]) - Integer.parseInt(args[1])) + Integer.parseInt(args[1]);
			}
		}
	}
}