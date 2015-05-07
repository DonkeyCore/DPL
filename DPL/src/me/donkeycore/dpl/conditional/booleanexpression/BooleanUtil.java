package me.donkeycore.dpl.conditional.booleanexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.donkeycore.dpl.exceptions.MalformedBooleanException;

/**
 * The boolean expression class with utilities.
 */
final class BooleanUtil {

	/**
	 * Private constructor.
	 */
	private BooleanUtil() {
		// Nothing
	}

	/**
	 * Valid and format the supplied boolean expression.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to valid and format.
	 * @return A validated and formated boolean expression.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression is malformed.
	 */
	static String validAndformat(final String booleanExpression)
		throws MalformedBooleanException {
		validNull(booleanExpression);
		validRegexp(booleanExpression);
		validParenthesis(booleanExpression);
		return format(booleanExpression);
	}

	/**
	 * Valid if the supplied boolean expression is null or void.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to validate.
	 */
	private static void validNull(final String booleanExpression) {
		if (booleanExpression == null || booleanExpression.equals("")) {
			throw new IllegalArgumentException(
				"booleanExpression is null or void");
		}
	}

	/**
	 * Valid if the supplied boolean expression only contains allowed
	 * characters.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to validate.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression is malformed.
	 */
	private static void validRegexp(final String booleanExpression)
		throws MalformedBooleanException {
		String regexp = "(\\(|\\)|\\|{2}|\\&{2}|!|(false)|(true)|\\s)+";
		if (!booleanExpression.matches("^" + regexp + "$")) {
			Matcher matcher = Pattern.compile(regexp)
				.matcher(booleanExpression);
			List<Integer> errorIndexes = new ArrayList<Integer>();
			while (matcher.find()) {
				int start = matcher.start();
				if (start != 0) {
					errorIndexes.add(new Integer(start));
				}
				int end = matcher.end();
				if (end != booleanExpression.length()) {
					errorIndexes.add(new Integer(end));
				}
			}
			if (errorIndexes.isEmpty()) {
				errorIndexes.add(new Integer(0));
			}
			throw new MalformedBooleanException(
				"Expected [ ' ' ( ) || && ! true false ]", errorIndexes,
				booleanExpression);
		}
	}

	/**
	 * Valid parenthesis of the supplied boolean expression.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to validate.
	 * @throws MalformedBooleanException
	 *             If the supplied boolean expression have wrong parenthesis.
	 */
	private static void validParenthesis(final String booleanExpression)
		throws MalformedBooleanException {
		int length = booleanExpression.length();
		int openParenthesis = 0;
		int closeParenthesis = 0;
		int lastOpenParenthesisIndex = 0;
		for (int i = 0; i < length; i++) {
			char charAt = booleanExpression.charAt(i);
			switch (charAt) {
				case '(':
					lastOpenParenthesisIndex = i;
					openParenthesis++;
					break;
				case ')':
					closeParenthesis++;
					if (openParenthesis < closeParenthesis) {
						throw new MalformedBooleanException(
							"Have a close parenthesis without an open parenthesis",
							i, booleanExpression);
					}
					break;
				default:
					break;
			}
		}
		if (openParenthesis > closeParenthesis) {
			throw new MalformedBooleanException(
				"Have an open parenthesis without a close parenthesis",
				lastOpenParenthesisIndex, booleanExpression);
		}
	}

	/**
	 * Format the the supplied boolean expression.
	 * 
	 * @param booleanExpression
	 *            The boolean expression to format.
	 * @return The boolean expression formated.
	 */
	private static String format(final String booleanExpression) {
		String formatedBooleanExpression = booleanExpression.toUpperCase()
			.replaceAll("TRUE", "T").replaceAll("FALSE", "F").replaceAll(
				"\\|\\|", "|").replaceAll("&&", "&");
		return formatedBooleanExpression;
	}

}
