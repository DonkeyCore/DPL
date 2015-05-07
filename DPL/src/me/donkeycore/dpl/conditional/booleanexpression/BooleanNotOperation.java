package me.donkeycore.dpl.conditional.booleanexpression;

/**
 * To evaluate NOT operations with a {@link IBoolean}.
 * 
 * @author Adolfo Sanz De Diego
 */
final class BooleanNotOperation implements IBoolean {

	/**
	 * The {@link IBoolean}.
	 */
	private IBoolean iBoolean;

	/**
	 * Constructor.
	 * 
	 * @param newIBoolean
	 *            The new The {@link IBoolean}.
	 */
	BooleanNotOperation(final IBoolean newIBoolean) {
		if (newIBoolean == null) {
			throw new IllegalArgumentException("newIBoolean is null");
		}
		this.iBoolean = newIBoolean;
	}

	/**
	 * Evaluate the NOT operation of the {@link IBoolean} supplied in the
	 * constructor.
	 * 
	 * @return <code>true</code> if the boolean value of the {@link IBoolean}
	 *         supplied in the constructor is <code>false</code> or
	 *         <code>false</code> otherwise.
	 * 
	 * @see IBoolean#booleanValue()
	 */
	public boolean booleanValue() {
		return (!this.iBoolean.booleanValue());
	}

	/**
	 * A String representation of the NOT operation of the {@link IBoolean}
	 * supplied in the constructor.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(!");
		buffer.append(this.iBoolean);
		buffer.append(")");
		return buffer.toString();
	}
}
