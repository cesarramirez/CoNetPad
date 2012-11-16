package org.ndacm.acmgroup.cnp.task.response;

public class JoinSessionTaskResponse extends TaskResponse {
	
	private String sessionName;
	private boolean success;

	public JoinSessionTaskResponse(String sessionName, boolean success) {
		this.sessionName = sessionName;
		this.success = success;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public String getSessionName() {
		return sessionName;
	}

	public boolean isSuccess() {
		return success;
	}
}
