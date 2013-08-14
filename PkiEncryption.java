package com.simens.contest.gll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
/**
 * 
 * @author Lesia L
 */
public class PkiEncryption {
	public static String defaultEncryptedFileDirectory = "C:\\encrypted\\";
	public static String defaultDecryptedFileDirectory = "C:\\decrypted\\";
	public static String aESKeyFileName = "aes.txt";
	private FileEncryption fe = null;

	static {
		CommonUtil.CreateDirectory(defaultEncryptedFileDirectory);
		CommonUtil.CreateDirectory(defaultDecryptedFileDirectory);
		String aseKeyFileName = defaultEncryptedFileDirectory + aESKeyFileName;
		
	}
	
	public static boolean  makeSureAESKeyExist()
	{
		String aseKeyFileName = defaultEncryptedFileDirectory + aESKeyFileName;
		
		
		File f = new File(aseKeyFileName);
		if (!f.exists()) {
			FileEncryption fileEnc = null;

			try {
				fileEnc = new FileEncryption();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			try {
				fileEnc.makeAESKey();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fileEnc.saveAESKey(new File(aseKeyFileName), new File(
						defaultEncryptedFileDirectory + "public.key"));
			} catch (IOException | GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return true;
	}

	public PkiEncryption() {
		try {
			fe = new FileEncryption();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private KeyPair keyPair = null;

	public File encryptFile(File planFile)

	{
		 makeSureAESKeyExist();
		String encryptedFilePath = planFile.getAbsoluteFile() + ".enc";
		File encryptedFile = new File(encryptedFilePath);

		try {
			fe.loadAESKey(new File(defaultEncryptedFileDirectory
					+ aESKeyFileName), new File(defaultEncryptedFileDirectory
					+ "private.key"));
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fe.encrypt(planFile, encryptedFile);
		} catch (InvalidKeyException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			return encryptedFile;
		}

	}

	public File decryptFile(File encFile)

	{ 
		makeSureAESKeyExist();

		String plainFilePath = PkiEncryption.defaultDecryptedFileDirectory +  encFile.getName();
		File planFile = new File(plainFilePath);

		if (!planFile.exists()) {
			try {
				planFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			try {
				fe.loadAESKey(new File(defaultEncryptedFileDirectory
						+ aESKeyFileName), new File(
						defaultEncryptedFileDirectory + "private.key"));
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fe.decrypt(encFile, planFile);
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InvalidKeyException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planFile;

	}

	private String getEncryptionMethod() {
		// TODO Auto-generated method stub
		return "RSA";
	}

	public static void main(String[] unused) throws Exception {
		
		PkiEncryption p = new PkiEncryption();
		File plainFile = new File(defaultEncryptedFileDirectory + "sample.txt");
		byte[] originalByte = IOUtils.toByteArray(CommonUtil
				.reteriveByteArrayInputStream(plainFile));

		File encyptedFile = p.encryptFile(plainFile);

		byte[] encBytes = IOUtils.toByteArray(CommonUtil
				.reteriveByteArrayInputStream(encyptedFile));

		byte[] plainBytes = IOUtils.toByteArray(CommonUtil
				.reteriveByteArrayInputStream(p.decryptFile(encyptedFile)));

		System.out.println("the plain text is " + new String(originalByte));
		System.out.println("the encrypted text is " + new String(encBytes));

		boolean expected = java.util.Arrays.equals(plainBytes, originalByte);
		System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));
	}

	public KeyPair getKeyPair() {
		try {
			keyPair = (new KeyPairUtil()).LoadKeyPair(
					defaultEncryptedFileDirectory, "RSA");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException
				| IOException e) {
		
			e.printStackTrace();
		}
		return keyPair;
	}

}
