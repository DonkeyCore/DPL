package me.donkeycore.dpl.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * If the Boolean String Expression is malformed.
 * 
 * @author Adolfo Sanz De Diego
 * @since 1.0
 * @see MalformedBooleanException#MalformedBooleanException(String, String)
 * @see MalformedBooleanException#MalformedBooleanException(String, List, String)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public final class MalformedBooleanException extends Exception {
	
	/**
	 * Serial version UID for this class
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The error indexes.
	 */
	private List<Integer> booleanExpressionErrorIndexes;
	/**
	 * The Boolean String Expression.
	 */
	private String booleanExpression;
	/**
	 * The error message.
	 */
	private String booleanExpressionErrorMessage;
	
	/**
	 * Constructor.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param errorIndex
	 *            The index with error.
	 * @param newBooleanExpression
	 *            The boolean expression.
	 * @since 1.0
	 */
	public MalformedBooleanException(final String errorMessage, final int errorIndex, final String newBooleanExpression) {
		this(errorMessage, toList(errorIndex), newBooleanExpression);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param errorIndexes
	 *            The {@link List} with the indexes with error.
	 * @param newBooleanExpression
	 *            The boolean expression.
	 * @since 1.0
	 */
	public MalformedBooleanException(final String errorMessage, final List<Integer> errorIndexes, final String newBooleanExpression) {
		super(format(errorMessage, errorIndexes, newBooleanExpression));
		this.booleanExpression = newBooleanExpression;
		this.booleanExpressionErrorIndexes = errorIndexes;
		this.booleanExpressionErrorMessage = errorMessage;
	}
	
	/**
	 * Returns the Boolean String Expression.
	 * 
	 * @return The Boolean String Expression.
	 */
	public String getBooleanExpression() {
		return this.booleanExpression;
	}
	
	/**
	 * Returns the error indexes.
	 * 
	 * @return The error indexes.
	 */
	public List<Integer> getBooleanExpressionErrorIndexes() {
		return this.booleanExpressionErrorIndexes;
	}
	
	/**
	 * Returns the error message.
	 * 
	 * @return The error message.
	 */
	public String getBooleanExpressionErrorMessage() {
		return this.booleanExpressionErrorMessage;
	}
	
	/**
	 * Returns a new {@link List} with the supplied error index.
	 * 
	 * @param errorIndex
	 *            The index with error.
	 * @return A new {@link List} with the supplied error index.
	 */
	private static List<Integer> toList(final int errorIndex) {
		List<Integer> errorIndexes = new ArrayList<Integer>();
		errorIndexes.add(new Integer(errorIndex));
		return errorIndexes;
	}
	
	/**
	 * Returns the exception message formated.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param errorIndexes
	 *            The {@link List} with the indexes with error.
	 * @param newBooleanExpression
	 *            The boolean expression.
	 * @return The error message formated.
	 */
	private static String format(final String errorMessage, final List<Integer> errorIndexes, final String newBooleanExpression) {
		if (errorMessage == null || errorMessage.equals("")) {
			throw new IllegalArgumentException("errorMessage is null or void");
		}
		if (errorIndexes == null || errorIndexes.isEmpty()) {
			throw new IllegalArgumentException("errorIndexes is null or void");
		}
		if (newBooleanExpression == null || newBooleanExpression.equals("")) {
			throw new IllegalArgumentException("newBooleanExpression is null or void");
		}
		StringBuilder error = new StringBuilder();
		error.append(errorMessage);
		error.append(" in [ ");
		int size = errorIndexes.size();
		int lastIndex = 0;
		for(int i = 0; i < size; i++) {
			int index = ((Integer) errorIndexes.get(i)).intValue();
			error.append(newBooleanExpression.substring(lastIndex, index));
			error.append("_");
			lastIndex = index;
		}
		error.append(newBooleanExpression.substring(lastIndex, newBooleanExpression.length()));
		error.append(" ]");
		if (size == 1) {
			error.append(" - Index [");
		} else if (size > 1) {
			error.append(" - Indexes [");
		}
		for(int i = 0; i < size; i++) {
			error.append(errorIndexes.get(i));
			if (i != (size - 1)) {
				error.append(", ");
			}
		}
		if (size > 0) {
			error.append("]");
		}
		return error.toString();
	}
}
