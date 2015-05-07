package me.donkeycore.dpl.statement;

public class Print implements IStatement{
	
	public String getName() {
		return "print";
	}

	public Object run(Statement statement, String args) {
		System.out.print(args.replace("\\n", "\n"));
		return null;
	}

}