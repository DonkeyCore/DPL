package me.donkeycore.dpl.conditional.booleanexpression;

/**
 * The simple implementation of the {@link IBoolean} interface.
 * 
 * @author Adolfo Sanz De Diego
 */
final class Boolean implements IBoolean {

	/**
	 * The boolean value.
	 */
	private boolean booleanValue;

	/**
	 * Constructor.
	 * 
	 * @param newBooleanValue
	 *            The new boolean value.
	 */
	Boolean(final boolean newBooleanValue) {
		this.booleanValue = newBooleanValue;
	}

	/**
	 * Returns the boolean value supplied in the constructor.
	 * 
	 * @see IBoolean#booleanValue()
	 */
	public boolean booleanValue() {
		return this.booleanValue;
	}

	/**
	 * A String representation of the boolean value supplied in the constructor.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "" + this.booleanValue;
	}

}
