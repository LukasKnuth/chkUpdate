package org.knuth.chkupdate.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;
import org.knuth.chkupdate.Provider;
import org.knuth.chkupdate.UpdateResult;
import org.knuth.chkupdate.exceptions.UpdateRequestException;
import org.knuth.chkupdate.exceptions.UpdateResponseException;

/**
 * A Provider used to check for a new Update
 *  on GitHub.
 * @author Lukas Knuth
 * @see <a href="http://github.com">GitHub-Webseite</a>
 */
public class GitHubProvider extends Provider{
	
	/** The Base of the Request-URL */
	private final String BASE_URL = "http://github.com/api/v2/json";
	
	private final String TAG_START = "/repos/show";
	private final String TAG_COMMAND = "/tags";
	private final String COMMIT_START = "/commits/show";
	
	/** The given UserName */
	private String username;
	/** The given Repo from "username" */
	private String repo;
	
	/**
	 * Creates a new Provider for <b>GitHub</b>
	 * @param username The user-name on GitHub.
	 * @param repo The Repo's name on GitHub.
	 */
	public GitHubProvider(String username, String repo){
		this.username = username;
		this.repo = repo;
	}

	@Override
	public UpdateResult doCheck() 
	  throws UpdateRequestException, UpdateResponseException{
		// Request the list of tags (Versions):
		String list;
		try {
			list = this.requestReader( createListURL() );
		} catch (MalformedURLException e) {
			throw new UpdateRequestException(getName());
		}
		// Get the Commit-ID of the last tag:
		TagWrapper tag = this.parseList(list);
		// Get the Commit-Infos:
		String commit;
		try {
			commit = this.requestReader( createCommitURL(tag.getTagID()) );
		} catch (MalformedURLException e) {
			throw new UpdateRequestException(getName());
		}
		// Parse the Commit-Infos:
		Date date = this.parseCommitDate(commit);
		// Create a UpdateResult:
		return new UpdateResult(tag.getTagString(), date);
	}
	
	/**
	 * Parses the JSON-Response to a "Date"-Object.
	 * @param response The JSON-String.
	 * @return A "Date"-object parsed from the JSON-String.
	 * @throws UpdateResponseException Thrown when the
	 *  Date could not be parsed.
	 */
	private Date parseCommitDate(String response) throws UpdateResponseException{
		try {
			JSONObject all = new JSONObject(response);
			JSONObject commit = all.getJSONObject("commit");
			String date_str = commit.getString("committed_date");
			// Parse Date:
			Calendar cal = DatatypeConverter.parseDateTime(date_str);
			return cal.getTime();
		} catch (JSONException e) {
			throw new UpdateResponseException(getName());
		}
	}
	
	/**
	 * Parses the tag-list JSON-String and returns the first
	 *  (last added) item of it.
	 * @param response The JSON-String. 
	 * @return The first (last added) item of the list
	 *  in a Wrapper.
	 * @throws UpdateResponseException Thrown when the JSON-
	 *  String could not be parsed.
	 */
	@SuppressWarnings("static-access")
	private TagWrapper parseList(String response) throws UpdateResponseException{
		try {
			JSONObject list = new JSONObject(response);
			JSONObject tags = list.getJSONObject("tags");
			String key = tags.getNames(tags)[0];
			return new TagWrapper(key, tags.getString(key));
		} catch (JSONException e) {
			throw new UpdateResponseException(getName());
		}
	}
	
	/**
	 * Reads the plain-text response of the GitHub API for
	 *  the given request-URL.
	 * @param url The request-URL.
	 * @return The plain-text response of the GitHub API
	 *  as a String.
	 * @throws UpdateRequestException Thrown when the GitHub
	 *  API could not be reached.
	 */
	private String requestReader(URL url) throws UpdateRequestException{
		// Create the request-URL:
		BufferedReader in = null;
		try {
			URLConnection con = url.openConnection();
			in = new BufferedReader(
					new InputStreamReader(con.getInputStream())
			);
			StringBuilder str = new StringBuilder();
			// Read response:
			String line = "";
			while ( (line = in.readLine()) != null ){
				str.append(line+"\n");
			}
			return str.toString();
		} catch (Exception e) {
			throw new UpdateRequestException(getName());
		} finally {
			// Clean up:
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Creates an "URL"-Object from the given URL-parts
	 *  to query the GitHub API for a list of tags.
	 * @return The URL to query the GitHub API
	 * @throws MalformedURLException Thrown when the URL
	 *  could not be created.
	 */
	private URL createListURL() throws MalformedURLException{
		String url = BASE_URL + TAG_START + "/" +
			username + "/" + repo +
			TAG_COMMAND;
		return new URL(url);
	}
	
	/**
	 * Creates an "URL"-Object to query the GitHub API
	 *  for a single commit.
	 * @param commit_id The ID of the commit.
	 * @return The URL to query the GitHub API.
	 * @throws MalformedURLException Thrown when the URL
	 *  could not be created.
	 */
	private URL createCommitURL(String commit_id) throws MalformedURLException{
		String url = BASE_URL + COMMIT_START + "/" +
			username + "/" + repo + "/" + commit_id;
		return new URL(url);
	}

	@Override
	public String getName() {
		return "GitHub";
	}
	
	/**
	 * Wrapper which is used to wrap the commit-ID and
	 *  it's date in one Object.
	 * @author Lukas Knuth
	 *
	 */
	private class TagWrapper{
		
		private String tag_string;
		private String tag_id;
		
		public TagWrapper(String tag_string, String tag_id){
			this.tag_id = tag_id;
			this.tag_string = tag_string;
		}
		
		public String getTagString(){
			return this.tag_string;
		}
		
		public String getTagID(){
			return this.tag_id;
		}
	}

}
