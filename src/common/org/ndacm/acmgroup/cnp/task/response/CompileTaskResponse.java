package org.ndacm.acmgroup.cnp.task.response;

public class CompileTaskResponse extends TaskResponse {
	
	private String compilerMessage;
	private boolean success;
	
	public CompileTaskResponse(String compilerMessage, boolean success) {
		this.compilerMessage = compilerMessage;
		this.success = success;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}