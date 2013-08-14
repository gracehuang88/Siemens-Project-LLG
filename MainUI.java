package com.simens.contest.gll;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.kerberos.KerberosPrincipal;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.WebAuthSession;
/**
 * 
 * @author GHuang
 */
public class MainUI extends JFrame {
	private static JButton btnUploadFile = new JButton("Upload File");
	private static JFileChooser fileChooser = new JFileChooser();
	private DropboxAPI<WebAuthSession> dpSession = null;

	public MainUI() {
	}

	private void runUI() {

		JButton btnGenerateKeyPairs = new JButton("Generate Key Pairs");
		btnGenerateKeyPairs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeyPairUtil kp = new KeyPairUtil();
				kp.generateKeyPair();
				JOptionPane
						.showMessageDialog(
								null,
								"KeyPair files are generated at "
										+ PkiEncryption.defaultEncryptedFileDirectory
										+ " directory with file names private.key and public.key.");
			}
		});

		File f = new File(PkiEncryption.defaultEncryptedFileDirectory
				+ "private.key");
		if (f.exists()) {
			btnGenerateKeyPairs.setEnabled(false);
		}

		JSeparator separator_1 = new JSeparator();

		JButton btnViewKeyPair = new JButton("View Key Pairs");
		btnViewKeyPair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeyPairUtil kp = new KeyPairUtil();
				String keyPairString = kp.viewDefaultKeyPair();
				JTextArea textArea = new JTextArea();
				textArea.setSize(100, 100);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setText(keyPairString);
				textArea.setVisible(true);
				textArea.grabFocus();
				textArea.requestFocus();

			}
		});

		JButton btnViewKeyPairs = new JButton("Back Up Key Pairs");
		btnViewKeyPairs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JOptionPane
						.showMessageDialog(
								null,
								"Key files are generated and saved in your PC at the folder \" "
										+ PkiEncryption.defaultEncryptedFileDirectory
										+ "\" please copy both files to a safe location");
			}
		});

		JSeparator separator_2 = new JSeparator();

		JSeparator separator = new JSeparator();

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		btnUploadFile.setVerticalAlignment(SwingConstants.BOTTOM);

		btnUploadFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				{
					int option = fileChooser.showDialog(new MainUI(), "Upload");
					if (option == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						encryptAndUploadFile(selectedFile);
					}
				}
			}
		});

		JSeparator separator_3 = new JSeparator();

		JSeparator separator_4 = new JSeparator();

		JButton btnDownloadFile = new JButton("Decrypt File");
		btnDownloadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int option = fileChooser.showDialog(new MainUI(), "Decrypt");
				if (option == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					decryptelectedFile(selectedFile);
				}

				// write code to download file
			}
		});
		btnDownloadFile.setVerticalAlignment(SwingConstants.BOTTOM);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(16)
																		.addComponent(
																				btnGenerateKeyPairs)
																		.addGap(5)
																		.addComponent(
																				separator_1,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(5)
																		.addComponent(
																				btnViewKeyPair)
																		.addGap(5)
																		.addComponent(
																				separator_2,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(5)
																		.addComponent(
																				btnViewKeyPairs)
																		.addGap(5)
																		.addComponent(
																				separator,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(108)
																		.addComponent(
																				btnUploadFile)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addPreferredGap(
																												ComponentPlacement.UNRELATED)
																										.addComponent(
																												separator_3,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(5)
																										.addComponent(
																												separator_4,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(39)
																										.addComponent(
																												btnDownloadFile)))))
										.addContainerGap(21, Short.MAX_VALUE)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(5)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																btnGenerateKeyPairs)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(11)
																		.addComponent(
																				separator_1,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																btnViewKeyPair)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(11)
																		.addComponent(
																				separator_2,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																btnViewKeyPairs)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(11)
																		.addComponent(
																				separator,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)))
										.addGap(29)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																groupLayout
																		.createParallelGroup(
																				Alignment.LEADING)
																		.addComponent(
																				separator_4,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				btnUploadFile)
																		.addComponent(
																				separator_3,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																btnDownloadFile))));
		getContentPane().setLayout(groupLayout);
		setSize(471, 143);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	protected void decryptelectedFile(File selectedFile) {
		// assume that you have the dropbox installed in your PC and you can
		// point to your dropbox folder
		PkiEncryption pEnc = new PkiEncryption();
		File plainFile = pEnc.decryptFile(selectedFile);
		System.out.println("the decrypted file is located at "
				+ plainFile.getAbsolutePath());
		JOptionPane.showMessageDialog(null, "the decrypted file is located at "
				+ plainFile.getAbsolutePath());

	}

	protected void encryptAndUploadFile(File file) {

		// encrypt the file and then upload

		// 1: Encrypt the file and write the file to a tem location with .enc as
		// suffix
		PkiEncryption pEnc = new PkiEncryption();
		File encryptedFile = pEnc.encryptFile(file);

		// 2: get Authenticatd with dropbox with OAuth and save the access keys
		if (null == dpSession) {
			DropBoxEcnryptor dp = new DropBoxEcnryptor();
			dpSession = dp.getAuthorizedDropBoxSesssion();
		}

		// 3: load File based on path

		FileInputStream inputStream = null;

		// create new file input stream
		try {
			inputStream = new FileInputStream(encryptedFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("account info is " + mDBApi.accountInfo().uid);
		try {
			dpSession.putFile("/enc/" + file.getName(), inputStream,
					file.length(), null, null);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();

				encryptedFile.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("file at local path is at " + file.getPath());
			JOptionPane.showMessageDialog(null,
					"File is Encryted and upLoaded to Dropbox at  " + "/enc/"
							+ file.getName());

		}
	}

	public static void main(String[] args) {

		/* Enabling Multiple Selection */
		// fileChooser.setMultiSelectionEnabled(true);

		/* Setting Current Directory */
		fileChooser.setCurrentDirectory(new File(
				PkiEncryption.defaultEncryptedFileDirectory));

		/* Adding action listener to open file */

		/* Running the Application */
		MainUI mu = new MainUI();

		mu.runUI();
	}

	public DropboxAPI<WebAuthSession> getDpSession() {
		return dpSession;
	}

	public void setDpSession(DropboxAPI<WebAuthSession> dpSession) {
		this.dpSession = dpSession;
	}
}