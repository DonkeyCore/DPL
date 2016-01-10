package me.donkeycore.dpl;

/**
 * Contains escape expressions
 * 
 * @since 1.0
 */
public enum Escape {
	/**
	 * Semicolon (;)
	 * 
	 * @since 1.0
	 */
	SEMICOLON("semicolon", ';'),
	/**
	 * Backslash (\)
	 * 
	 * @since 1.0
	 */
	BACKSLASH("backslash", '\\'),
	/**
	 * Space ( )
	 * 
	 * @since 1.0
	 */
	SPACE("space", ' '),
	/**
	 * Tab ( )
	 * 
	 * @since 1.0
	 */
	TAB("tab", '\t'),
	/**
	 * Newline (
	 * )
	 * 
	 * @since 1.0
	 */
	NEWLINE("newline", "\\n"),
	/**
	 * Grave (`)
	 * 
	 * @since 1.0
	 */
	GRAVE("grave", '`'),
	/**
	 * Single quote (')
	 * 
	 * @since 1.0
	 */
	QUOTES("quote_s", '\''),
	/**
	 * Double quote (")
	 * 
	 * @since 1.0
	 */
	QUOTED("quote_d", '"');
	
	/**
	 * The string to temporarily replace with
	 * 
	 * @since 1.0
	 */
	private final String r;
	/**
	 * The character/string to be escaped
	 * 
	 * @since 1.0
	 */
	private final String c;
	
	/**
	 * Create a new escape sequence for a character with a replace string
	 * 
	 * @param replace The replace string
	 * @param character The escaped character
	 * @see Escape
	 * @since 1.0
	 */
	private Escape(String replace, Character character) {
		this(replace, character.toString());
	}
	
	/**
	 * Create a new escape sequence for a string with a replace string
	 * 
	 * @param replace The replace string
	 * @param character The escaped string
	 * @see Escape
	 * @since 1.0
	 */
	private Escape(String replace, String character) {
		this.r = "-§=§[§" + replace.toUpperCase() + "§]§=§-";
		this.c = character;
	}
	
	/**
	 * Retrieve the string to replace an escaped character with temporarily.
	 * 
	 * @return The temporary replace string
	 * @since 1.0
	 */
	public String getReplace() {
		return this.r;
	}
	
	/**
	 * Retrieve the character to be escaped
	 * 
	 * @return The escaped character
	 * @since 1.0
	 */
	public String getCharacterString() {
		return this.c;
	}
	
	public String toString() {
		return this.r;
	}
	
	/**
	 * Return an escaped character with its temporarily replace string in a given string
	 * 
	 * @param str The string to replace characters in
	 * @return The string with the escaped character replaced
	 * @since 1.0
	 */
	public String replace(String str) {
		return str.replace("\\" + getCharacterString(), getReplace());
	}
	
	/**
	 * Return the replaced escape string with the escaped character in a given string
	 * 
	 * @param str The string to return the escaped characters in
	 * @return The string with the replace string replaced with the original escape string
	 * @since 1.0
	 */
	public String reset(String str) {
		return str.replace(getReplace(), getCharacterString());
	}
}