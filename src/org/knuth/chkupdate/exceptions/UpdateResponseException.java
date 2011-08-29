package org.knuth.chkupdate.exceptions;

/**
 * Thrown when there was a problem parsing/working with
 *  the response given by the Providers service.
 * @author Lukas Knuth
 *
 */
public class UpdateResponseException extends Exception{
	
	/** Default serial-ID */
	private static final long serialVersionUID = 1L;

	public UpdateResponseException(String provider){
		super("The response send by " +provider+ " couldn't be parsed.");
	}

}
