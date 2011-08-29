package org.knuth.chkupdate.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.json.JSONObject;
import org.knuth.chkupdate.Provider;
import org.knuth.chkupdate.UpdateResult;
import org.knuth.chkupdate.exceptions.UpdateRequestException;
import org.knuth.chkupdate.exceptions.UpdateResponseException;

/**
 * <p>A Provider which checks for a new version on
 *  SourceForge.
 * </p>
 * <p>
 *  This Provider is marked "deprecated" because the 
 *  SourceForge-API used to retrieve informations 
 *  is "unmaintained". However, this Provider should
 *  work probably.
 * </p>
 * @author Lukas Knuth
 * @deprecated
 * @see <a href="http://sourceforge.net">SourceForge-Webseite</a>
 */
public class SourceForgeProvider extends Provider{
	
	/** The Base of the Request-URL */
	private final String BASE_URL = "http://sourceforge.net/api";
	/** URL-Part to request the Project-ID */
	private final String GET_PROJECT_ID = "/project/name/:name:/json";
	/** URL-Part to request the Release-list */
	private final String GET_RELEASES = "/release/index/project-id/:id:/rss";
	/** Pattern used to parse the returned Date */
	private final String DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
	
	/** The Projects name on SourceForge */
	private String project_name;
	/** The Projects ID on SourceForge */
	private int project_id;
	
	/**
	 * Creates a new Provider for <b>SourceForge</b>
	 * <br>
	 * This constructor first querys the SourceForge-API
	 *  to get the projects ID. Using the ID to create the
	 *  Provider-Object might be faster (since it makes
	 *  only one request to the API).
	 * @param project_name The name of the Project on
	 *  SourceForge.
	 */
	public SourceForgeProvider(String project_name){
		this.project_name = project_name.toLowerCase();
		this.project_id = -1;
	}
	
	/**
	 * Creates a new Provider for <b>SourceForge</b>
	 * @param project_id The Projects ID on SourceForge.
	 */
	public SourceForgeProvider(int project_id){
		this.project_name = null;
		this.project_id = project_id;
	}

	@Override
	public UpdateResult doCheck() throws UpdateRequestException,
			UpdateResponseException {
		// Check if Project-ID is needed:
		if (this.project_id == -1){
			if (this.project_name == null){
				// No ID and no Name:
				throw new UpdateRequestException(getName());
			} else {
				// Get the Project-ID:
				this.project_id = getProjectID(this.project_name);
			}
		}
		// Check for the latest file-release:
		Date latest = this.latestFileRelease();
		return new UpdateResult(null, latest);
	}
	
	/**
	 * Querys the SourceForge API to get the Date of the
	 *  latest added file.
	 * @return The date of the latest added file as a
	 *  "Date"-object.
	 * @throws UpdateRequestException Thrown when the
	 *  SourceForge-API could not be reached.
	 * @throws UpdateResponseException Thrown when the
	 *  APIs response could not be parsed.
	 */
	private Date latestFileRelease() 
	  throws UpdateRequestException, UpdateResponseException{
		XMLStreamReader reader = null;
		try {
			URLConnection con = createFileReleaseUrl().openConnection();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			reader = factory.createXMLStreamReader(
					con.getInputStream()
			);
			// Read the RSS-Response:
			int tag_type;
			String tag_name = "";
			String date_str = null;
			boolean done = false;
			while (!done){
				// Get the Tag-Type:
				tag_type = reader.next();
				switch (tag_type){
				case XMLStreamConstants.START_ELEMENT:
					// Save the tags name:
					tag_name = reader.getLocalName();
					break;
				case XMLStreamConstants.CHARACTERS:
					if ( "pubDate".equals(tag_name) ){
						date_str = reader.getText();
						done = true;
					}
					break;
				}
			}
			// Check if Date was read:
			if (date_str == null){
				throw new UpdateResponseException(getName());
			}
			// Parse to "Date"-Object:
			SimpleDateFormat form = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
			return form.parse(date_str);
			
		} catch (MalformedURLException e) {
			throw new UpdateRequestException(getName());
		} catch (IOException e) {
			throw new UpdateResponseException(getName());
		} catch (XMLStreamException e) {
			throw new UpdateResponseException(getName());
		} catch (ParseException e) {
			throw new UpdateResponseException(getName());
		} finally {
			// Clean Up:
			if (reader != null)
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Querys the SourceForge API to get the Projects
	 *  ID from it's name.
	 * @param project_name The projects name on
	 *  SourceForge.
	 * @return The projects ID.
	 * @throws UpdateRequestException Thrown when the
	 *  SourceForge-API could not be reached.
	 */
	private int getProjectID(String project_name) throws UpdateRequestException{
		BufferedReader in = null;
		try {
			URLConnection con = createGetIdUrl().openConnection();
			in = new BufferedReader(
					new InputStreamReader(con.getInputStream())
			);
			StringBuilder str = new StringBuilder();
			// Read the response:
			String line = "";
			while ( (line = in.readLine()) != null ){
				str.append(line);
			}
			// Parse response:
			return parseProjectID(str.toString());
		} catch (Exception e){
			throw new UpdateRequestException(getName());
		} finally {
			// Clean Up:
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Parses the APIs JSON-Response to get the Projects
	 *  ID (called by {@link #getProjectID(String)}).
	 * @param json The JSON-String.
	 * @return The ID of the Project on SourceForge.
	 * @throws UpdateRequestException Thrown when
	 *  SourceForge API could not be reached.
	 * @see #getProjectID(String)
	 */
	private int parseProjectID(String json) throws UpdateRequestException{
		try {
			JSONObject all = new JSONObject(json);
			JSONObject project = all.getJSONObject("Project");
			return project.getInt("id");
		} catch (Exception e){
			throw new UpdateRequestException(getName());
		}
	}
	
	/**
	 * Creates the request-URL to get the Projects
	 *  latest file-releases.
	 * @return The "URL"-Object to query the
	 *  SourceForge-API for the file-release list.
	 * @throws MalformedURLException Thrown when the
	 *  URL could not be created.
	 */
	private URL createFileReleaseUrl() throws MalformedURLException{
		String url_str = BASE_URL +
		  GET_RELEASES.replaceFirst(":id:", this.project_id+"");
		return new URL(url_str);
	}
	
	/**
	 * Creates the request-URL to get the Projects
	 *  ID.
	 * @return The "URL"-Object to query the
	 *  SourceForge-API for the Projects ID.
	 * @throws MalformedURLException Thrown when the
	 *  URL could not be created.
	 */
	private URL createGetIdUrl() throws MalformedURLException{
		String url_str = BASE_URL + 
		  GET_PROJECT_ID.replaceFirst(":name:", this.project_name);
		return new URL(url_str);
	}

	@Override
	public String getName() {
		return "SourceForge";
	}

}
