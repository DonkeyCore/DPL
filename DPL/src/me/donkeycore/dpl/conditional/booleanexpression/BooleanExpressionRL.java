package me.donkeycore.dpl.conditional.booleanexpression;

import me.donkeycore.dpl.exceptions.MalformedBooleanException;

/**
 * The main class to evaluate Boolean String Expressions from the right to the
 * left.<br>
 * <style> a.link { color: black; text-decoration: none; } a.link:hover { text-decoration:
 * underline; } </style> <br>
 * <b>Example:</b><blockquote><code>
 * 		String strBoolExpr = <font color="#2A00FF">"!true&amp;&amp;false||true"</font>;<br>
 * 		<a class="link" href="BooleanExpression.html">BooleanExpression</a> boolExpr = <font color="#7f0055"><b>null</b></font>;<br>
 * 		<font color="#7f0055"><b>try</b></font> {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;boolExpr = <a class="link" href="BooleanExpression.html">BooleanExpression</a>.<a class="link" href="BooleanExpression.html#readRightToLeft(java.lang.String)"><b>readRightToLeft</b></a>(strBoolExpr);<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#7F0055"><b>boolean</b></font> bool = boolExpr.<a class="link" href="#booleanValue()">booleanValue</a>();<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// bool == <b>false</b></font><br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(boolExpr.toString() + " == " + bool);<br>
 *  	&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// (!(true&amp;&amp;(false||true))) == <b>false</b></font><br>
 * 		} <font color="#7F0055"><b>catch</b></font> (<a class="link" href="MalformedBooleanException.html">MalformedBooleanException</a> e) {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br>
 * 		}<br>
 * </code></blockquote>
 * 
 * @author Adolfo Sanz De Diego
 */
final class BooleanExpressionRL extends BooleanExpression {
	
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
	BooleanExpressionRL(final String newBooleanExpression) throws MalformedBooleanException {
		this.booleanExpression = newBooleanExpression;
		this.iBoolean = toIBoolean(BooleanUtil.validAndformat(newBooleanExpression), 0);
	}
	
	/**
	 * Evaluate the boolean expression supplied in the constructor from the
	 * right to the left.
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
		char firstChar = getFirstChar(formatedBooleanExpression);
		if (new Character(firstChar).toString().matches("\\s")) {
			firstChar = ' ';
		}
		String substring = getSubstringWithoutFirstChar(formatedBooleanExpression);
		switch(firstChar) {
			case ' ':
				IBoolean boolWhitespace = toIBoolean(substring, index + 1);
				return boolWhitespace;
			case '(':
				String beginToClose = getFromBeginToCloseParenthesis(substring, index + 1);
				String closeToEnd = getFromCloseParenthesisToEnd(substring, index + 1);
				IBoolean boolBeginToClose = toIBoolean(beginToClose, index + 1);
				IBoolean boolOpen = toIBoolean(boolBeginToClose, closeToEnd, index + 1);
				return boolOpen;
			case 'T':
				IBoolean boolTrue = toIBoolean(new Boolean(true), substring, index + 4);
				return boolTrue;
			case 'F':
				IBoolean boolFalse = toIBoolean(new Boolean(false), substring, index + 5);
				return boolFalse;
			case '!':
				IBoolean boolAll = toIBoolean(substring, index + 1);
				IBoolean boolNot = new BooleanNotOperation(boolAll);
				return boolNot;
			default:
				throw new MalformedBooleanException("Expected [ (, true, flase, ! ]", index, this.booleanExpression);
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
		char firstChar = getFirstChar(formatedBooleanExpression);
		if (new Character(firstChar).toString().matches("\\s")) {
			firstChar = ' ';
		}
		String substring = getSubstringWithoutFirstChar(formatedBooleanExpression);
		switch(firstChar) {
			case ' ':
				return toIBoolean(lastIBoolean, substring, index + 1);
			case '.':
				return lastIBoolean;
			case ')':
				return toIBoolean(lastIBoolean, substring, index + 1);
			case '|':
				return new BooleanOrOperation(lastIBoolean, toIBoolean(substring, index + 2));
			case '&':
				return new BooleanAndOperation(lastIBoolean, toIBoolean(substring, index + 2));
			default:
				throw new MalformedBooleanException("Expected [ ' ', ), ||, && ]", index, this.booleanExpression);
		}
	}
	
	/**
	 * Returns the first <code>char</code> of the supplied formated boolean
	 * expression, or '.' if the supplied formated boolean expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression to get the first <code>char</code>.
	 * @return firstChar The first <code>char</code> of the supplied formated
	 *         boolean expression, or '.' if the supplied formated boolean
	 *         expression is <code>null</code> or void.
	 */
	private char getFirstChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression.length() == 0) {
			return '.';
		}
		return formatedBooleanExpression.charAt(0);
	}
	
	/**
	 * Returns the supplied formated boolean expression without his first <code>char</code>, or "" if the supplied formated boolean expression
	 * is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @return substringWithoutFirstChar The supplied formated boolean
	 *         expression without his first <code>char</code>, or "" if the
	 *         supplied formated boolean expression is <code>null</code> or
	 *         void.
	 */
	private String getSubstringWithoutFirstChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		return formatedBooleanExpression.substring(1, formatedBooleanExpression.length());
	}
	
	/**
	 * Returns the substring from the begin of the supplied formated boolean
	 * expression to the first close parenthesis or "" if the supplied formated
	 * boolean expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return leftParenthesisFormatedBooleanExpresion The substring from the
	 *         begin of the supplied formated boolean expression to the first
	 *         close parenthesis or "" if the supplied formated boolean
	 *         expression is <code>null</code> or void.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private String getFromBeginToCloseParenthesis(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = 0;
		int toIndex = getIndexOfCloseParenthesis(formatedBooleanExpression, index);
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}
	
	/**
	 * Returns the substring from the first close parenthesis to the end of the
	 * supplied formated boolean expression, or "" if the supplied formated
	 * boolean expression is <code>null</code> or void.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return rightParenthesisFormatedBooleanExpresion The substring from the
	 *         first close parenthesis to the end of the supplied formated
	 *         boolean expression, or "" if the supplied formated boolean
	 *         expression is <code>null</code> or void.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private String getFromCloseParenthesisToEnd(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = getIndexOfCloseParenthesis(formatedBooleanExpression, index);
		int toIndex = formatedBooleanExpression.length();
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}
	
	/**
	 * Returns the index of the close parenthesis of the supplied formated
	 * boolean expression.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param index
	 *            The index in the global boolean expression.
	 * @return indexOfCloseParenthesis The index of the close parenthesis of the
	 *         supplied formated boolean expression.
	 * @throws MalformedBooleanException
	 *             If the supplied formated boolean expression is malformed.
	 */
	private int getIndexOfCloseParenthesis(final String formatedBooleanExpression, final int index) throws MalformedBooleanException {
		int lastIndexOfOpenParenthesis = getIndexOf(formatedBooleanExpression, "(", -1);
		int lastIndexOfCloseParenthesis = getIndexOf(formatedBooleanExpression, ")", -1);
		while(lastIndexOfOpenParenthesis != -1 && lastIndexOfOpenParenthesis < lastIndexOfCloseParenthesis) {
			lastIndexOfOpenParenthesis = getIndexOf(formatedBooleanExpression, "(", lastIndexOfOpenParenthesis);
			lastIndexOfCloseParenthesis = getIndexOf(formatedBooleanExpression, ")", lastIndexOfCloseParenthesis);
		}
		if (lastIndexOfCloseParenthesis == -1) {
			int parenthesisIndex = index + lastIndexOfOpenParenthesis;
			throw new MalformedBooleanException("Have a open parenthesis without a close parenthesis", parenthesisIndex, this.booleanExpression);
		}
		return lastIndexOfCloseParenthesis;
	}
	
	/**
	 * Return the index of the supplied searched string. The search begins at
	 * the supplied from index and finish at the end of the supplied formated
	 * boolean expresion.
	 * 
	 * @param formatedBooleanExpression
	 *            The formated boolean expression.
	 * @param searchedString
	 *            The searched string.
	 * @param fromIndex
	 *            The index where the search begins.
	 * @return indexOf The index of the supplied searched string. The search
	 *         begins at the supplied from index and finish at the end of the
	 *         supplied formated boolean expresion.
	 */
	private int getIndexOf(final String formatedBooleanExpression, final String searchedString, final int fromIndex) {
		int newFromIndex = fromIndex;
		if (newFromIndex == -1) {
			return formatedBooleanExpression.indexOf(searchedString);
		}
		newFromIndex++;
		int length = formatedBooleanExpression.length();
		if (newFromIndex > length) {
			return -1;
		}
		return formatedBooleanExpression.indexOf(searchedString, newFromIndex);
	}
	
	/**
	 * A String representation of this {@link BooleanExpressionRL}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.iBoolean.toString();
	}
}
