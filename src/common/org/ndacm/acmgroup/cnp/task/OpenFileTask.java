package org.ndacm.acmgroup.cnp.task;

import org.ndacm.acmgroup.cnp.network.CNPConnection;

/**
 * Task for opening a file.
 * 
 * @author Josh Tan
 *
 */
public class OpenFileTask extends SessionTask {
	
	protected int userID;
	protected int fileID;
	protected String userAuthToken;
	protected CNPConnection connection;

	public OpenFileTask(int userID, int sessionID, int fileID, String userAuthToken) {
		this.userID = userID;
		this.fileID = fileID;
		this.userAuthToken = userAuthToken;
		this.sessionID = sessionID;
	}

	@Override
	public void run() {
		session.executeTask(this);
		
	}

	public int getUserID() {
		return userID;
	}

	public int getFileID() {
		return fileID;
	}

	public String getUserAuthToken() {
		return userAuthToken;
	}

	public CNPConnection getConnection() {
		return connection;
	}

	public void setConnection(CNPConnection connection) {
		this.connection = connection;
	}
	
	

}
