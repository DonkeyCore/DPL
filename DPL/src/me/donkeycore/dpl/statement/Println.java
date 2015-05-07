package me.donkeycore.dpl.statement;

public class Println implements IStatement{

	public String getName() {
		return "println";
	}

	public Object run(Statement statement, String args) {
		System.out.println(args.replace("\\n", "\n"));
		return null;
	}
	
}