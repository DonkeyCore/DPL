package me.donkeycore.dpl.conditional.booleanexpression;

/**
 * To evaluate OR operations between 2 {@link IBoolean}.
 * 
 * @author Adolfo Sanz De Diego
 */
final class BooleanOrOperation implements IBoolean {

	/**
	 * The first {@link IBoolean}.
	 */
	private IBoolean iBoolean1;

	/**
	 * The second {@link IBoolean}.
	 */
	private IBoolean iBoolean2;

	/**
	 * Constructor.
	 * 
	 * @param newIBoolean1
	 *            The first {@link IBoolean}.
	 * @param newIBoolean2
	 *            The second {@link IBoolean}.
	 */
	BooleanOrOperation(final IBoolean newIBoolean1, final IBoolean newIBoolean2) {
		if (newIBoolean1 == null) {
			throw new IllegalArgumentException("newIBoolean1 is null");
		}
		this.iBoolean1 = newIBoolean1;
		if (newIBoolean2 == null) {
			throw new IllegalArgumentException("newIBoolean2 is null");
		}
		this.iBoolean2 = newIBoolean2;

	}

	/**
	 * Evaluate the OR operation between the first {@link IBoolean} and the
	 * second {@link IBoolean} supplied in the constructor.
	 * 
	 * @return <code>true</code> or <code>false</code> dependenig the result
	 *         of the OR operation between the first {@link IBoolean} and the
	 *         second {@link IBoolean} supplied in the constructor.
	 * 
	 * @see IBoolean#booleanValue()
	 */
	public boolean booleanValue() {
		return (this.iBoolean1.booleanValue() || this.iBoolean2.booleanValue());
	}

	/**
	 * A String representation of the OR operation between the first
	 * {@link IBoolean} and the second {@link IBoolean} supplied in the
	 * constructor.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		buffer.append(this.iBoolean1);
		buffer.append("||");
		buffer.append(this.iBoolean2);
		buffer.append(")");
		return buffer.toString();
	}

}
