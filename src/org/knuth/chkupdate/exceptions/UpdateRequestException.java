package org.knuth.chkupdate.exceptions;

/**
 * Thrown when there was a problem requesting data from
 *  a Provider.
 * @author Lukas Knuth
 *
 */
public class UpdateRequestException extends Exception{
	
	/** Default serial-ID */
	private static final long serialVersionUID = 1L;

	public UpdateRequestException(String provider){
		super("Couldn't send a proper request to "+provider);
	}

}
