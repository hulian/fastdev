package com.fastdev.core.util;


import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AESUtil {
	
	static String IV = "z35al_@0}@5y~QJP";
	
	public static byte[] encryptBytes(String plainText, String encryptionKey) throws Exception {
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		 SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
		 cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
		 return cipher.doFinal(plainText.getBytes("UTF-8"));
	 }
	  
	 public static String decryptBytes(byte[] cipherText, String encryptionKey) throws Exception{
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		 SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
		 cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
		 return new String(cipher.doFinal(cipherText),"UTF-8");
	 }
	 
	 public static String encrypt(String plainText, String encryptionKey) throws Exception {
		return new String(Base64.getEncoder().encode(encryptBytes( plainText,  encryptionKey)));
	 }
	 
	 public static String decrypt(String plainText, String encryptionKey) throws Exception {
		return decryptBytes( Base64.getDecoder().decode(plainText),  encryptionKey);
	 }
	 
}