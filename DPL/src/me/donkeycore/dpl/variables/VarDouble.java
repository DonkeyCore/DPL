package me.donkeycore.dpl.variables;

import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;

/**
 * A {@link Variable} that represents a double value.
 * @since 1.0
 * @see Variable
 */
public class VarDouble extends Variable {

	public VarDouble(String key, Object value) throws VariableAlreadyDeclaredException {
		super(key, value);
	}

	public String getName() {
		return "double";
	}

}
