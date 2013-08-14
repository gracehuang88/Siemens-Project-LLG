package com.simens.contest.gll;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author GHuang
 */
public class DropBoxEcnryptor {
	private static final String APP_KEY = ExternalProperty.getString("DropBoxEcnryptor.key"); //$NON-NLS-1$
	private static final String APP_SECRET = ExternalProperty.getString("DropBoxEcnryptor.secret"); //$NON-NLS-1$
	private static final AccessType ACCESS_TYPE = AccessType.DROPBOX;
	private static DropboxAPI<WebAuthSession> dropBoxAPISession;

	public static void main(String[] args) throws Exception {
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
		WebAuthInfo authInfo = session.getAuthInfo();
		RequestTokenPair pair = authInfo.requestTokenPair;
		String url = authInfo.url;
		Desktop.getDesktop().browse(new URL(url).toURI());

		JOptionPane.showMessageDialog(null,
				"Please make sure you have authorized this App to Access Dropbox before click Continue here."); //$NON-NLS-1$
		session.retrieveWebAccessToken(pair);
		
		dropBoxAPISession = new DropboxAPI<WebAuthSession>(session);

		System.out.println();
		System.out.print("Uploading file..."); //$NON-NLS-1$

		FileInputStream inputStream = null;
		try {
			File file = new File("TestUpload.txt"); //$NON-NLS-1$
			inputStream = new FileInputStream(file);
			System.out.println("account info is " + dropBoxAPISession.accountInfo().uid); //$NON-NLS-1$
			Entry newEntry = dropBoxAPISession.putFile("/enc/uploaded.txt", inputStream, //$NON-NLS-1$
					file.length(), null, null);
		} catch (Exception e) {
			System.out.println("Exeption occured during upload and stack trace is " + e); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		File file = new File("toBedownLoaded.txt"); //$NON-NLS-1$
		FileOutputStream outputStream = new FileOutputStream(file);
		DropboxFileInfo info = dropBoxAPISession.getFile("/enc/uploaded.txt", null, //$NON-NLS-1$
				outputStream, null);
		System.out.println("file is downloaded  at " + file.getAbsolutePath()); //$NON-NLS-1$
	}

	public DropboxAPI<WebAuthSession> getAuthorizedDropBoxSesssion() {
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
		WebAuthInfo authInfo = null;
		try {
			authInfo = session.getAuthInfo();
		} catch (DropboxException e) {
			
			e.printStackTrace();
		}
		RequestTokenPair pair = authInfo.requestTokenPair;
		String url = authInfo.url;
		try {
			Desktop.getDesktop().browse(new URL(url).toURI());
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null,
				"Press ok to continue once you have authenticated."); //$NON-NLS-1$
		try {
			session.retrieveWebAccessToken(pair);
		} catch (DropboxException e) {
			
			e.printStackTrace();
		}

		return new DropboxAPI<WebAuthSession>(session);

	}
}