package me.donkeycore.dpl.conditional.booleanexpression;

import me.donkeycore.dpl.exceptions.MalformedBooleanException;

/**
 * The main class to evaluate Boolean String Expressions. <br>
 * <style> a.link { color: black; text-decoration: none; } a.link:hover { text-decoration:
 * underline; } </style> <br>
 * <b>Example 1:</b> read the Boolean String Expression <b>from the left to the
 * right.</b><blockquote><code>
 * 		String strBoolExpr = <font color="#2A00FF">"!true&amp;&amp;false||true"</font>;<br>
 * 		<a class="link" href="BooleanExpression.html">BooleanExpression</a> boolExpr = <font color="#7f0055"><b>null</b></font>;<br>
 * 		<font color="#7f0055"><b>try</b></font> {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;boolExpr = <a class="link" href="BooleanExpression.html">BooleanExpression</a>.<a class="link" href="#readLeftToRight(java.lang.String)"><b>readLeftToRight</b></a>(strBoolExpr);<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#7F0055"><b>boolean</b></font> bool = boolExpr.<a class="link" href="#booleanValue()">booleanValue</a>();<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// bool == <b>true</b></font><br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(boolExpr.toString() + " == " + bool);<br />
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// (((!true)&amp;&amp;false)||true) == <b>true</b></font><br />
 * 		} <font color="#7F0055"><b>catch</b></font> (<a class="link" href="MalformedBooleanException.html">MalformedBooleanException</a> e) {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br>
 * 		}<br>
 * </code></blockquote><b>Example 2:</b> read the Boolean String Expression
 * <b>from the right to the left.</b><blockquote><code>
 * 		String strBoolExpr = <font color="#2A00FF">"!true&amp;&amp;false||true"</font>;<br>
 * 		<a class="link" href="BooleanExpression.html">BooleanExpression</a> boolExpr = <font color="#7f0055"><b>null</b></font>;<br>
 * 		<font color="#7f0055"><b>try</b></font> {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;boolExpr = <a class="link" href="BooleanExpression.html">BooleanExpression</a>.<a class="link" href="#readRightToLeft(java.lang.String)"><b>readRightToLeft</b></a>(strBoolExpr);<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#7F0055"><b>boolean</b></font> bool = boolExpr.<a class="link" href="#booleanValue()">booleanValue</a>();<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// bool == <b>false</b></font><br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(boolExpr.toString() + " == " + bool);<br />
 *  	&nbsp;&nbsp;&nbsp;&nbsp;<font color="#3F7F5F">// (!(true&amp;&amp;(false||true))) == <b>false</b></font><br />
 * 		} <font color="#7F0055"><b>catch</b></font> (<a class="link" href="MalformedBooleanException.html">MalformedBooleanException</a> e) {<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br>
 * 		}<br>
 * </code></blockquote>
 * 
 * @author Adolfo Sanz De Diego
 */
public abstract class BooleanExpression implements IBoolean {
	
	/**
	 * Constructor.
	 */
	BooleanExpression() {
		// Nothing
	}
	
	/**
	 * Returns a {@link BooleanExpression} that read the Boolean String
	 * Expression from left to right.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to evaluate.
	 * @return A {@link BooleanExpression} that read the Boolean String
	 *         Expression from left to right.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression is malformed.
	 */
	public static BooleanExpression readLeftToRight(final String booleanExpression) throws MalformedBooleanException {
		return new BooleanExpressionLR(booleanExpression);
	}
	
	/**
	 * Returns a {@link BooleanExpression} that read the Boolean String
	 * Expression from right to left.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to evaluate.
	 * @return A {@link BooleanExpression} that read the Boolean String
	 *         Expression from right to left.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression is malformed.
	 */
	public static BooleanExpression readRightToLeft(final String booleanExpression) throws MalformedBooleanException {
		return new BooleanExpressionRL(booleanExpression);
	}
}
