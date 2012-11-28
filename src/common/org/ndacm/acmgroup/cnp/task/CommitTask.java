package org.ndacm.acmgroup.cnp.task;

/*
 * This is the task for committing changes to the GIT repository
 * @author Cesar Ramirez
 */
public class CommitTask extends ServerTask {

	protected int userID;
	protected String userAuthToken;
	private int sessionID;
	private String sessionName;
	private String message;
	
	/**
	 * Default Constructor	
	 * @param userID			The USer ID who initialized the task
	 * @param userAuthToken		The User's authorized token
	 * @param sessionID			The unique ID of the session
	 * @param sessionName		The Unique Name of the session
	 * @param message			The message of the Commit
	 */
	public CommitTask(int userID, String userAuthToken, int sessionID, String sessionName, String message) {
		this.userID = userID;
		this.userAuthToken = userAuthToken;
		this.sessionID = sessionID;
		this.sessionName = sessionName;
		this.message = message;
	}

	/**
	 * This runs the task in a thread
	 */
	@Override
	public void run() {
		server.executeTask(this);
		
	}

	/**
	 * Gets the User's id
	 * @return		The User ID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * This gets the User's Authorized Token
	 * @return		User's authorized token
	 */
	public String getUserAuthToken() {
		return userAuthToken;
	}

	/**
	 * This gets the Session Id
	 * @return		The Sesion ID
	 */
	public int getSessionID() {
		return sessionID;
	}

	/**
	 * This sets the Session ID
	 * @param sessionID		The unique ID of the session to set to the task
	 */
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * This gets the name of the session
	 * @return		The unique name of the session
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * This sets the name of the session
	 * @param sessionName		Unique name of the session
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * This gets the commit message
	 * @return		The commit message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * This sets the message of the git commit
	 * @param message		The message for the git commit
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
