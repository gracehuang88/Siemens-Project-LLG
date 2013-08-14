package com.simens.contest.gll;

import java.io.*;
import java.security.*;
import java.security.spec.*;
/**
 * 
 * @author Logan L
 */
public class KeyPairUtil {

	public static void main(String args[]) {
		KeyPairUtil keyPairUtil = new KeyPairUtil();
		try {
			String path = PkiEncryption.defaultEncryptedFileDirectory;
			CommonUtil.CreateDirectory(path);

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

			keyGen.initialize(1024);
			KeyPair generatedKeyPair = keyGen.genKeyPair();

			System.out.println("Generated Key Pair");
			keyPairUtil.viewKeyPairGivenKeyPair(generatedKeyPair);
			keyPairUtil.SaveKeyPair(path, generatedKeyPair);

			KeyPair loadedKeyPair = keyPairUtil.LoadKeyPair(path, "RSA");
			System.out.println("Loaded Key Pair");
			keyPairUtil.viewKeyPairGivenKeyPair(loadedKeyPair);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private String viewKeyPairGivenKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		String result ="Pulic Key is /n";
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
		result = result +   getHexString(pub.getEncoded()); 

		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
		result = result +   "private key is /n" + getHexString(priv.getEncoded()); 
		return result;
	}
	
	
	public String viewDefaultKeyPair() {
		
		KeyPair loadedKeyPair =null;
		try {
			loadedKeyPair = this.LoadKeyPair(PkiEncryption.defaultEncryptedFileDirectory, "RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PublicKey pub = loadedKeyPair.getPublic();
		String result ="Pulic Key is /n";
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
		result = result +   getHexString(pub.getEncoded()); 

		PrivateKey priv = loadedKeyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
		result = result +   "private key is /n" + getHexString(priv.getEncoded()); 
		return result;
	}

	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	public KeyPair generateKeyPair(){
		
		KeyPairUtil keyPairUtil = new KeyPairUtil();
		KeyPair generatedKeyPair= null;
		try {
			String path = PkiEncryption.defaultEncryptedFileDirectory;
			CommonUtil.CreateDirectory(path);

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

			keyGen.initialize(1024);
			 generatedKeyPair = keyGen.genKeyPair();

			//System.out.println("Generated Key Pair");
			//keyPairUtil.dumpKeyPair(generatedKeyPair);
			keyPairUtil.SaveKeyPair(path, generatedKeyPair);

			//KeyPair loadedKeyPair = keyPairUtil.LoadKeyPair(path, "RSA");
			//System.out.println("Loaded Key Pair");
			// keyPairUtil.dumpKeyPair(loadedKeyPair);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return generatedKeyPair;	
	}
	

	public void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
		CommonUtil.CreateDirectory(path);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + "/public.key");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(path + "/private.key");
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	

	public KeyPair LoadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Public Key.
		CommonUtil.CreateDirectory(path);
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(path + "/public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		// Read Private Key.
		File filePrivateKey = new File(path + "/private.key");
		fis = new FileInputStream(path + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return new KeyPair(publicKey, privateKey);
	}
}
