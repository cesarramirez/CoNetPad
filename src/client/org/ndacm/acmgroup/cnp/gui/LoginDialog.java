package org.ndacm.acmgroup.cnp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;

import org.ndacm.acmgroup.cnp.CNPClient;

public class LoginDialog extends JDialog {

	private LoginDialog loginDialog;
	private CNPClient client;

	/**
	 * Create the dialog.
	 */
	public LoginDialog(final CNPClient client) {
		loginDialog = this;
		this.client = client;
		this.client.setLogDialog(this);
		setTitle("CoNetPad Client");
		setBounds(100, 100, 418, 173);
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		JLabel lblUsername = new JLabel("Username:");
		JLabel lblPassword = new JLabel("Password:");

		JLabel lblSession = new JLabel("Session:");

		final JFormattedTextField formattedUsername = new JFormattedTextField();

		final JFormattedTextField formattedPassword = new JFormattedTextField();

		JFormattedTextField formattedSession = new JFormattedTextField();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 816,
								Short.MAX_VALUE)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																lblPassword)
														.addComponent(
																lblUsername)
														.addComponent(
																lblSession))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																formattedSession,
																GroupLayout.DEFAULT_SIZE,
																734,
																Short.MAX_VALUE)
														.addComponent(
																formattedUsername,
																GroupLayout.DEFAULT_SIZE,
																736,
																Short.MAX_VALUE)
														.addComponent(
																formattedPassword,
																GroupLayout.DEFAULT_SIZE,
																736,
																Short.MAX_VALUE))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblUsername)
														.addComponent(
																formattedUsername,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(6)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblPassword)
														.addComponent(
																formattedPassword,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblSession)
														.addComponent(
																formattedSession,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(panel,
												GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)));
		{
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.closeConnection();
					dispose();
				}
			});
			JButton btnLogIn = new JButton("Log In");
			btnLogIn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.loginToAccount(formattedUsername.getText(),
							formattedPassword.getText());
				}
			});

			JButton btnRegister = new JButton("Register");
			btnRegister.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					RegisterDialog dialog = new RegisterDialog(client);
					dialog.setModalityType(ModalityType.APPLICATION_MODAL);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					System.out.println("sdfsdf");

				}
			});

			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
					Alignment.LEADING).addGroup(
					gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnRegister)
							.addPreferredGap(ComponentPlacement.RELATED, 105,
									Short.MAX_VALUE).addComponent(btnLogIn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel).addContainerGap()));
			gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
					Alignment.TRAILING).addGroup(
					gl_panel.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE,
									Short.MAX_VALUE)
							.addGroup(
									gl_panel.createParallelGroup(
											Alignment.BASELINE)
											.addComponent(btnCancel)
											.addComponent(btnLogIn)
											.addComponent(btnRegister))
							.addContainerGap()));
			panel.setLayout(gl_panel);
			getContentPane().setLayout(groupLayout);
		}
	}

	public void openMainFrame() {
		MainFrame frame = new MainFrame(client);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		loginDialog.setVisible(false);
		loginDialog.dispose();

	}
}
