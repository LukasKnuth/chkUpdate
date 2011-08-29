package org.knuth.chkupdate;

import org.knuth.chkupdate.exceptions.UpdateRequestException;
import org.knuth.chkupdate.exceptions.UpdateResponseException;

/**
 * Abstract base-class for a Provider.
 * @author Lukas Knuth
 *
 */
public abstract class Provider {
	
	/**
	 * Performs the Update-Check.
	 * @return An "UpdateResult"-Object holding
	 *  informations about the newest update.
	 * @throws UpdateRequestException Thrown when
	 *  there was a problem requesting informations
	 *  from the Provider.  
	 * @throws UpdateResponseException Thrown when
	 *  there was a problem parsing the response
	 *  from the Provider.
	 */
	public abstract UpdateResult doCheck() 
		throws UpdateRequestException, UpdateResponseException;
	
	/**
	 * Returns the Name of the Service used by
	 *  this Provider
	 * @return The Service-Name
	 */
	public abstract String getName();

}
