package com.simens.contest.gll;

import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *  
 * @author Lesia L.
 *
 * Utility class for encrypting/decrypting files.
 * 
 * you  need Unlimited Strength Java(TM) Cryptography Extension Policy Files
 *  for the Java(TM) Platform, Standard Edition Development Kit, v7 to run this
 *  you will need to download it from Oracle and unzip the zip file and put the two policy jar
 *  files under your %JRE_Home%/lib/security folder. In windows with JRE7, it is at
 *  C:\Program Files\Java\jre7\lib\security
 */

public class FileEncryption {

	public static final int AES_Key_Size = 256;
	public static final String  AES_Algorith = "AES/CBC/PKCS5Padding";

	Cipher pkCipher, aesCipher;
	byte[] aesKey;
	SecretKeySpec aeskeySpec;
	IvParameterSpec ips;

	/**
	 * Constructor: creates ciphers and initialize vector
	 */
	public FileEncryption() throws GeneralSecurityException {
		// create RSA public key cipher
		pkCipher = Cipher.getInstance("RSA");
		// create AES shared key cipher
		aesCipher = Cipher.getInstance(AES_Algorith,
				new BouncyCastleProvider());
		// Initialization Vector
		// Required for CBC
		byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		ips = new IvParameterSpec(iv);

	}
	
	public static void main(String[] unused) throws Exception {
		
		FileEncryption secure = new FileEncryption();
		File aESKeyFile = new File("C:\\encryption\\aes.txt"); 
		File publicKeyFile =new File("c:\\encryption\\public.key");
		File privateKeyFile = new File("c:\\encryption\\private.key");
		File plainOriginalFile = new File("c:\\encryption\\sample.txt"); 
		File encFile = new File("c:\\encryption\\sample.txt.enc"); 
		File deCryptedFile =new File("c:\\encryption\\sample.txt.enc.plain");
	// make and save AES key if AES key file does not exist
		if (!aESKeyFile.exists()){
		secure.makeAESKey();
		secure.saveAESKey(aESKeyFile, publicKeyFile);
		}
		// to encrypt a file
		secure.loadAESKey(aESKeyFile, privateKeyFile);
		secure.encrypt(plainOriginalFile, encFile);

		// to decrypt it again
		secure.loadAESKey(aESKeyFile, privateKeyFile);
		secure.decrypt(encFile, deCryptedFile);
		
		
		byte [] originalByte = IOUtils.toByteArray(CommonUtil.reteriveByteArrayInputStream(plainOriginalFile));
		System.out.println("the original text is " + new String( originalByte));
		
		
		byte [] encByte = IOUtils.toByteArray(CommonUtil.reteriveByteArrayInputStream(encFile));
		System.out.println("the encrypted text is " + new String( encByte));
		
		byte [] decryptedByte = IOUtils.toByteArray(CommonUtil.reteriveByteArrayInputStream(deCryptedFile));
		System.out.println("the decrypted text is " + new String( decryptedByte));
		
	}

	/**
	 * Creates a new AES key
	 */
	public void makeAESKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(AES_Key_Size);
		SecretKey key = kgen.generateKey();
		aesKey = key.getEncoded();
		aeskeySpec = new SecretKeySpec(aesKey, AES_Algorith);
	}

	/**
	 * Decrypt an AES key from a file using an RSA private key
	 * 
	 * File
	 */
	public void loadAESKey(File in, File privateKeyFile)
			throws GeneralSecurityException, IOException {
		// read private key to be used to decrypt the AES key
		byte[] encodedKey = new byte[(int) privateKeyFile.length()];
		new FileInputStream(privateKeyFile).read(encodedKey);

		// create private key
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey pk = kf.generatePrivate(privateKeySpec);

		// read AES key
		pkCipher.init(Cipher.DECRYPT_MODE, pk);
		aesKey = new byte[AES_Key_Size / 8];
		CipherInputStream is = new CipherInputStream(new FileInputStream(in),
				pkCipher);
		is.read(aesKey);
		aeskeySpec = new SecretKeySpec(aesKey, AES_Algorith);
	}

	/**
	 * Encrypts the AES key to a file using an RSA public key File out is the
	 * ASEKeyFile
	 */
	public void saveAESKey(File out, File publicKeyFile) throws IOException,
			GeneralSecurityException {
		// read public key to be used to encrypt the AES key
		byte[] encodedKey = new byte[(int) publicKeyFile.length()];
		new FileInputStream(publicKeyFile).read(encodedKey);

		// create public key
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pk = kf.generatePublic(publicKeySpec);

		// write AES key
		pkCipher.init(Cipher.ENCRYPT_MODE, pk);
		CipherOutputStream os = new CipherOutputStream(
				new FileOutputStream(out), pkCipher);
		os.write(aesKey);
		os.close();
	}

	/**
	 * Encrypts and then copies the contents of a given file. in :
	 * filetoEncrypt, out: EncrytedFile
	 * @throws InvalidAlgorithmParameterException 
	 */
	public void encrypt(File in, File out) throws IOException,
			InvalidKeyException, InvalidAlgorithmParameterException {
		aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec, ips);

		// opening the inputstream for the file to be encrypted
		FileInputStream fis = new FileInputStream(in);
		//open output stream for the encrypted file to be writen to desk with extention .enc
		out = new File(in.getAbsolutePath() + ".enc");
		FileOutputStream fos = new FileOutputStream(out);
		//openning the cipheroutputstream on this new file 
		CipherOutputStream os = new CipherOutputStream(fos, aesCipher);
		// the following copy will do the copy of encrypted stream to CipherOutputStream
		copy(fis, os);
		os.flush();
		os.close();
	}

	/**
	 * Decrypts and then copies the contents of a given file.
	 * @throws InvalidAlgorithmParameterException 
	 */
	public void decrypt(File in, File out) throws IOException,
			InvalidKeyException, InvalidAlgorithmParameterException {

		aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec, ips);

		CipherInputStream is = new CipherInputStream(new FileInputStream(in),
				aesCipher);
		if (!out.exists()) {
			out.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(out);

		copy(is, os);

		os.flush();

		is.close();
		os.close();
	}

	/**
	 * write the byte to outpurstream
	 */
	private void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[1024];
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
		}
		// os.flush();
	}
}