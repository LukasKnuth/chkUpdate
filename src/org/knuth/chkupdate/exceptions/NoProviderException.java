package org.knuth.chkupdate.exceptions;

/**
 * Thrown when trying to check for an Update without
 *  a specified Provider.
 * @author Lukas Knuth
 *
 */
public class NoProviderException extends RuntimeException{

	/** Default Serial-ID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Throws the Exception with a standard Message.
	 */
	public NoProviderException(){
		super("Couldn't check for Update: No Provider specified!");
	}

}
