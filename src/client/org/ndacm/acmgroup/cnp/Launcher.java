package org.ndacm.acmgroup.cnp;

import javax.swing.JDialog;

import org.ndacm.acmgroup.cnp.gui.ServerConnectionDialog;

public class Launcher {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ServerConnectionDialog dialog = new ServerConnectionDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
