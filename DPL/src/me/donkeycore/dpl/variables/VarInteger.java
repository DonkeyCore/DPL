package me.donkeycore.dpl.variables;

import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;

public class VarInteger extends Variable {

	public VarInteger(String key, Object value) throws VariableAlreadyDeclaredException{
		super(key, value);
	}
	
	public String getName() {
		return "integer";
	}

}
