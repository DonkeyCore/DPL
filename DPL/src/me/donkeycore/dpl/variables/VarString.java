package me.donkeycore.dpl.variables;

import me.donkeycore.dpl.exceptions.VariableAlreadyDeclaredException;

public class VarString extends Variable{
	
	public VarString(String key, Object value) throws VariableAlreadyDeclaredException{
		super(key, value);
	}

	public IVariable constructor(String[] args) {
		return this;
	}
	
	public String getName() {
		return "string";
	}
	
}
