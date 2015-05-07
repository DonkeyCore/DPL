package me.donkeycore.dpl;

/** Contains escape expressions
 * @since 1.0
 */
public enum Escape {
	
	SEMICOLON("semicolon", ';'),
	BACKSLASH("backslash", '\\'),
	SPACE("space", ' '),
	TAB("tab", '\t'),
	NEWLINE("newline", "\\n"),
	GRAVE("grave", '`'),
	QUOTES("quote_s", '\''),
	QUOTED("quote_d", '"');
	
	private final String r;
	private final String c;
	
	private Escape(String replace, Character character){
		this(replace, character.toString());
	}
	
	private Escape(String replace, String character){
		this.r = "-©=©[©" + replace.toUpperCase() + "©]©=©-";
		this.c = character;
	}
	
	public String getReplace(){
		return this.r;
	}
	
	public String getCharacterString(){
		return this.c;
	}
	
	public String toString(){
		return this.r;
	}
	
	public String replace(String str){
		return str.replace("\\" + getCharacterString(), getReplace());
	}
	
	public String reset(String str){
		return str.replace(getReplace(), getCharacterString());
	}
	
}