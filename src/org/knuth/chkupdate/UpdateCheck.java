package org.knuth.chkupdate;

import org.knuth.chkupdate.exceptions.NoProviderException;
import org.knuth.chkupdate.exceptions.UpdateRequestException;
import org.knuth.chkupdate.exceptions.UpdateResponseException;

/**
 * Handler-class which manages the Provider and checks
 *  for Updates.
 * @author Lukas Knuth
 *
 */
public class UpdateCheck {
	
	/**
	 * The current Provider.
	 */
	private Provider provider;
	
	/**
	 * Creates a new "UpdateCheck"-Object using the given
	 *  Provider.
	 * @param provider The Provider to use for the Update-
	 *  Check.
	 */
	public UpdateCheck(Provider provider){
		this.provider = provider;
	}
	
	/**
	 * Creates a new "UpdateCheck"-Object without any
	 *  Provider.
	 * @see #setProvider(Provider provider)
	 */
	public UpdateCheck(){
		this.provider = null;
	}
	
	/**
	 * Sets a new Provider for the Update-Check.
	 * @param provider The new Provider to use for the
	 *  Update-Check.
	 */
	public void setProvider(Provider provider){
		this.provider = provider;
	}
	
	/**
	 * Checks for an Update using the current Provider.
	 * @return An "UpdateResult"-Object holding the informations
	 *  on the new Update.
	 * @throws NoProviderException Thrown when no Provider
	 *  was specified.
	 * @throws UpdateRequestException Thrown when
	 *  there was a problem requesting informations
	 *  from the Provider.  
	 * @throws UpdateResponseException Thrown when
	 *  there was a problem parsing the response
	 *  from the Provider.
	 */
	public UpdateResult checkForUpdates()
	  throws UpdateRequestException, UpdateResponseException{
		// Check for Provider:
		if (this.provider == null){
			throw new NoProviderException();
		}
		// Check for Update:
		return this.provider.doCheck();
	}
	
	/**
	 * Checks for an update using the given Provider.
	 * @param provider The Provider to use for the Update-
	 *  Check.
	 * @return An "UpdateResult"-Object holding the informations
	 *  on the new Update.
	 * @throws UpdateRequestException Thrown when
	 *  there was a problem requesting informations
	 *  from the Provider.  
	 * @throws UpdateResponseException Thrown when
	 *  there was a problem parsing the response
	 *  from the Provider.
	 */
	public static UpdateResult checkForUpdates(Provider provider)
	  throws UpdateRequestException, UpdateResponseException{
		// Check for Update:
		return provider.doCheck();
	}

}
