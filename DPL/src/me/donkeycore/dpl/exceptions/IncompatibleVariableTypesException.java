package me.donkeycore.dpl.exceptions;

/** Thrown when a variable is assigned something that it cannot represent
 * @since 1.0
 * @see IncompatibleVariableTypesException#IncompatibleVariableTypesException(Object, Object)
 * @see DonkeyException
 * @see DonkeyException#DonkeyException(String)
 */
public class IncompatibleVariableTypesException extends DonkeyException{
	
	private static final long serialVersionUID = 5584982289717227302L;
	
	/** Thrown when a variable is assigned something that it cannot represent
	 * @param v1 The variable being assigned to the second variable
	 * @param v2 The variable being assigned by the first variable
	 * @since 1.0
	 * @see IncompatibleVariableTypesException
	 */
	public IncompatibleVariableTypesException(Object v1, Object v2){
		super(format(v1, v2));
	}
	
	private static String format(Object v, Object v2){
		return "The variable type " + (v.toString().matches(v2.getClass().getName() + "@.*") ? v.getClass().getSimpleName():v.toString()) + " is not compatible with the variable type " + (v2.toString().matches(v2.getClass().getName() + "@.*") ? v2.getClass().getSimpleName():v2.toString());
	}
	
}