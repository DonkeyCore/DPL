package me.donkeycore.dpl.conditional.booleanexpression;

import me.donkeycore.dpl.exceptions.MalformedBooleanException;

/**
 * The main class to evaluate Boolean String Expressions from the left to the
 * right.<br>
 * <style> a.link { color: black; text-decoration: none; } a.link:hover { text-decoration:
 * underline; } </style> <br>
 * <b>Example:</b><blockquote><code>
 * 		String strBoolExpr = <font color="#2A00FF">"!true&amp;&amp;false||true"</font>;<br>
 * 		<a class="link" href="BooleanExpression.html">BooleanExpression</a> boolExpr = <font color="#7f0055"><b>null</b></font>;<br>
 * 		<font color="#7f0055"><b>try</b></font> {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;boolExpr = <a class="link" href="BooleanExpression.html">BooleanExpression</a>.<a class="link" href="BooleanExpression.html#readLeftToRight(java.lang.String)"><b>readLeftToRight</b></a>(strBoolExpr);<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#7F0055"><b>boolean</b></font> bool = boolExpr.<a class="link" href="#booleanValue()">booleanValue</a>();<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(boolExpr.toString() + " == " + bool);<br />
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// (((!true)&amp;&amp;false)||true) == <b>true</b></font><br />
 * 		} <font color="#7F0055"><b>catch</b></font> (<a class="link" href="MalformedBooleanException.html">MalformedBooleanException</a> e) {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br>
 * 		}<br>
 * </code></blockquote>
 * 
 * @author Adolfo Sanz De Diego
 */
final class BooleanExpressionLR extends BooleanExpression {
	
	/**
	 * The boolean expression.
	 */
	private String booleanExpression;
	/**
	 * The {@link IBoolean}.
	 */
	private IBoolean iBoolean;
	
	/**
	 * Constructor.
	 * 
	 * @param newBooleanExpression
	 *            The boolean expression to evaluate.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression is malformed.
	 */
	BooleanExpressionLR(final String newBooleanExpression) throws MalformedBooleanException {
		this.booleanExpression = newBooleanExpression;
		this.iBoolean = toIBoolean(BooleanUtil.validAndformat(newBooleanExpression), newBooleanExpression.length());
	}
	
	/**
	 * Evaluate the boolean expression supplied in the constructor from the left
	 * to the right.
	 * 
	 * @return <code>true</code> or <code>false</code> depending the value
	 *         of the boolean expression supplied in the constructor.
	 */
	public boolean booleanValue() {
		return this.iBoolean.booleanValue();
	}
	
	/**
	 * Transform the supplied formated boolean expression to {@link IBoolean}.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression to transform to {@link IBoolean}.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return IBoolean The {@link IBoolean} estracted from the the supplied
	 *         formated boolean expression.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private IBoolean toIBoolean(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		char lastChar = getLastChar(formatedBooleanExpression);
		if (new Character(lastChar).toString().matches("\\s")) {
			lastChar = ' ';
		}
		String substring = getSubstringWithoutLastChar(formatedBooleanExpression);
		switch(lastChar) {
			case ' ':
				IBoolean boolWhitespace = toIBoolean(substring, index - 1);
				return boolWhitespace;
			case ')':
				String openToEnd = getFromOpenParenthesisToEnd(substring, index - 1);
				String beginToOpen = getFromBeginToOpenParenthesis(substring, index - 1);
				IBoolean boolOpenToEnd = toIBoolean(openToEnd, index - 1);
				IBoolean boolToClose = toIBoolean(boolOpenToEnd, beginToOpen, index - 1);
				return boolToClose;
			case 'T':
				IBoolean boolTrue = toIBoolean(new Boolean(true), substring, index - 4);
				return boolTrue;
			case 'F':
				IBoolean boolFalse = toIBoolean(new Boolean(false), substring, index - 5);
				return boolFalse;
			default:
				throw new MalformedBooleanException("Expected [ ' ', ), true, false ]", index, this.booleanExpression);
		}
	}
	
	/**
	 * Transform the supplied formated boolean expression to {@link IBoolean}.
	 * 
	 * @param lastIBoolean
	 *            The last {@link IBoolean}.
	 * @param formatedBooleanExpression
	 *            The formated boolean expression to transform to {@link IBoolean}.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return IBoolean The {@link IBoolean} estracted from the the supplied
	 *         formated boolean expression.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private IBoolean toIBoolean(final IBoolean lastIBoolean, final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		char lastChar = getLastChar(formatedBooleanExpression);
		if (new Character(lastChar).toString().matches("\\s")) {
			lastChar = ' ';
		}
		String substring = getSubstringWithoutLastChar(formatedBooleanExpression);
		switch(lastChar) {
			case ' ':
				IBoolean boolWhitespace = toIBoolean(lastIBoolean, substring, index - 1);
				return boolWhitespace;
			case '.':
				return lastIBoolean;
			case '(':
				IBoolean boolToOpen = toIBoolean(lastIBoolean, substring, index - 1);
				return boolToOpen;
			case '|':
				IBoolean boolFirstOr = toIBoolean(substring, index - 2);
				IBoolean boolOr = new BooleanOrOperation(boolFirstOr, lastIBoolean);
				return boolOr;
			case '&':
				IBoolean boolFirstAnd = toIBoolean(substring, index - 2);
				IBoolean boolAnd = new BooleanAndOperation(boolFirstAnd, lastIBoolean);
				return boolAnd;
			case '!':
				IBoolean boolNot = new BooleanNotOperation(lastIBoolean);
				IBoolean boolAll = toIBoolean(boolNot, substring, index - 1);
				return boolAll;
			default:
				throw new MalformedBooleanException("Expected [ ' ', ), ||, &&, ! ]", index, this.booleanExpression);
		}
	}
	
	/**
	 * Returns the last <code>char</code> of the supplied formated boolean
	 * expression, or '.' if the supplied formated boolean expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression to get the first <code>char</code>.
	 * @return lastChar The last <code>char</code> of the supplied formated
	 *         boolean expression, or '.' if the supplied formated boolean
	 *         expression is <code>null</code> or void.
	 */
	private char getLastChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression.length() == 0) {
			return '.';
		}
		return formatedBooleanExpression.charAt(formatedBooleanExpression.length() - 1);
	}
	
	/**
	 * Returns the supplied formated boolean expression without his last <code>char</code>, or "" if the supplied formated boolean expression
	 * is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @return substringWithoutLastChar The supplied formated boolean expression
	 *         without his last <code>char</code>, or "" if the supplied
	 *         formated boolean expression is <code>null</code> or void.
	 */
	private String getSubstringWithoutLastChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		return formatedBooleanExpression.substring(0, formatedBooleanExpression.length() - 1);
	}
	
	/**
	 * Returns the substring from the begin of the supplied formated boolean
	 * expression the open parenthesis, or "" if the supplied formated boolean
	 * expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return rightParenthesisFormatedBooleanExpresion The substring from the
	 *         begin of the supplied formated boolean expression the open
	 *         parenthesis, or "" if the supplied formated boolean expression is <code>null</code> or void.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private String getFromBeginToOpenParenthesis(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = 0;
		int toIndex = getIndexOfOpenParenthesis(formatedBooleanExpression, index);
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}
	
	/**
	 * Returns the substring from open parenthesis to the end of the supplied
	 * formated boolean expression, or "" if the supplied formated boolean
	 * expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return leftParenthesisFormatedBooleanExpresion The substring from open
	 *         parenthesis to the end of the supplied formated boolean
	 *         expression, or "" if the supplied formated boolean expression is <code>null</code> or void.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private String getFromOpenParenthesisToEnd(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = getIndexOfOpenParenthesis(formatedBooleanExpression, index) + 1;
		int toIndex = formatedBooleanExpression.length();
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}
	
	/**
	 * Returns the index of the open parenthesis of the supplied formated
	 * boolean expression.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return indexOfCloseParenthesis The index of the open parenthesis of the
	 *         supplied formated boolean expression.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private int getIndexOfOpenParenthesis(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		int lastIndexOfOpenParenthesis = getLastIndexOf(formatedBooleanExpression, "(", formatedBooleanExpression.length());
		int lastIndexOfCloseParenthesis = getLastIndexOf(formatedBooleanExpression, ")", formatedBooleanExpression.length());
		while(lastIndexOfCloseParenthesis != -1 && lastIndexOfOpenParenthesis < lastIndexOfCloseParenthesis) {
			lastIndexOfOpenParenthesis = getLastIndexOf(formatedBooleanExpression, "(", lastIndexOfOpenParenthesis);
			lastIndexOfCloseParenthesis = getLastIndexOf(formatedBooleanExpression, ")", lastIndexOfCloseParenthesis);
		}
		if (lastIndexOfOpenParenthesis == -1) {
			int parenthesisIndex = index - (formatedBooleanExpression.length() - lastIndexOfCloseParenthesis);
			throw new MalformedBooleanException("Have a close parenthesis without an open parenthesis", parenthesisIndex, this.booleanExpression);
		}
		return lastIndexOfOpenParenthesis;
	}
	
	/**
	 * Return the last index of the supplied searched string. The search begins
	 * at the end of the supplied formated boolean expresion and finish at the
	 * supplied from index.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param searchedString
	 *            The searched string.
	 * @param toIndex
	 *            The index where the search finish.
	 * @return indexOf The last index of the supplied searched string. The
	 *         search begins at the end of the supplied formated boolean
	 *         expresion and finish at the supplied from index.
	 */
	private int getLastIndexOf(final String formatedBooleanExpression, final String searchedString, final int toIndex) {
		if (toIndex < 0) {
			return -1;
		} else if (toIndex >= formatedBooleanExpression.length()) {
			return formatedBooleanExpression.lastIndexOf(searchedString);
		} else {
			String newFormatedBooleanExpression = formatedBooleanExpression.substring(0, toIndex);
			return newFormatedBooleanExpression.lastIndexOf(searchedString);
		}
	}
	
	/**
	 * A String representation of this {@link BooleanExpressionLR}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.iBoolean.toString();
	}
}
