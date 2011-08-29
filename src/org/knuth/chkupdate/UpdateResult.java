package org.knuth.chkupdate;

import java.util.Date;

/**
 * A wrapper-class holding informations on the
 *  latest update checked by a Provider.
 * @author Lukas Knuth
 *
 */
public class UpdateResult {
	
	/** The latest version as a String */
	private String version_str;
	/** The date of the last update */
	private Date update_date;
	/** The latest version as a "Version"-Object */
	private Version version;
	
	/**
	 * Creates a new "UpdateResult"-Object and creates a
	 *  "Version"-Object for the given version string.
	 * @param version_str The Version-String of the latest
	 *  update.
	 * @param update_date The Date of the latest update.
	 */
	public UpdateResult(String version_str, Date update_date){
		this.version_str = version_str;
		this.update_date = update_date;
		// Create the Version-Object:
		if (version_str != null){
			this.version = new Version(version_str);
		} else this.version = null;
	}
	
	/**
	 * Returns the version of this update as a
	 *  "Version"-Object.
	 * @return A "Version"-Object for this Update. 
	 */
	public Version getVersion(){
		return this.version;
	}
	
	/**
	 * Returns the version of this update as a
	 *  String. 
	 * @return A string with the latest version.
	 */
	public String getVersionString(){
		return this.version_str;
	}
	
	/**
	 * The Date of the latest version for this Update.
	 * @return The Date of this Update.
	 */
	public Date getUpdateDate(){
		return this.update_date;
	}

}
