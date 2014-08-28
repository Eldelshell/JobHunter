/*
 * Copyright (C) 2014 Alejandro Ayuso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jobhunter.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	
	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String KEY_ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final Integer iterationCount = 128;
	private static final Integer keyLength = 128;
	private final static int SALT_LEN = 8;
	private final static int IV_LEN = 16;
	private static final SecureRandom random = new SecureRandom();
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private static char[] pwd = null;
	private static SecretKeyFactory factory = null;
	
	public static void init(final String password){
		if(pwd == null){
			pwd = password.toCharArray();
			try {
				factory = SecretKeyFactory.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] encrypt(byte[] bytes) throws Exception{
		final byte[] salt = salt();
		final KeySpec spec = new PBEKeySpec(pwd, salt, iterationCount, keyLength);
		final SecretKey tmp = factory.generateSecret(spec);
		final SecretKey secret = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
		final Cipher cipher = Cipher.getInstance(TRANSFORMATION);

//		Fails with NPE because of unfixed bug. https://code.google.com/p/android/issues/detail?id=58191
//		cipher.init(Cipher.ENCRYPT_MODE, secret);
//		final byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
//		final byte[] ciphertext = cipher.doFinal(bytes);
		
		final byte[] iv = iv();
		final IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
		final byte[] ciphertext = cipher.doFinal(bytes);
		
		return concatAll(iv, salt, ciphertext);
	}
	
	public static byte[] decryptBytes(byte[] bytes) throws Exception {
		final byte [] iv = Arrays.copyOfRange(bytes, 0, IV_LEN);
		final byte [] salt = Arrays.copyOfRange(bytes, IV_LEN, IV_LEN + SALT_LEN);
		final byte [] secret = Arrays.copyOfRange(bytes, IV_LEN + SALT_LEN, bytes.length);
		
		KeySpec spec = new PBEKeySpec(pwd, salt, iterationCount, keyLength);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey key = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		return cipher.doFinal(secret);
	}
	
	public static byte[] encrypt(String secret){
		try {
			return encrypt(secret.getBytes(UTF8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] encrypt(CharSequence secret){
		try {
			return encrypt(secret.toString().getBytes(UTF8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(byte[] bytes){
		try {
			return new String(decryptBytes(bytes), UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] salt(){
		byte[] salt = new byte [SALT_LEN];
		random.nextBytes(salt);
		return salt;
	}
	
	private static byte[] iv(){
		byte[] iv = new byte [IV_LEN];
		random.nextBytes(iv);
		return iv;
	}
	
	public static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	/**
	 * Pretty cool class to handle encrypted data and its serialization to byte array.
	 * ORMLite doesn't like it very much tough.
	 * @author eldelshell
	 *
	 */
	@Deprecated
	public static class Data {
		
		private final byte[] encrypted;
		private final byte[] iv;
		private final byte[] salt;
		
		private Data(byte[] encrypted, byte[] iv, byte[] salt) {
			super();
			this.encrypted = encrypted;
			this.iv = iv;
			this.salt = salt;
		}
		
		public byte[] getEncrypted() {
			return encrypted;
		}
		
		public byte[] getIv() {
			return iv;
		}

		public byte[] getSalt() {
			return salt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(encrypted);
			result = prime * result + Arrays.hashCode(iv);
			result = prime * result + Arrays.hashCode(salt);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Data other = (Data) obj;
			if (!Arrays.equals(encrypted, other.encrypted))
				return false;
			if (!Arrays.equals(iv, other.iv))
				return false;
			if (!Arrays.equals(salt, other.salt))
				return false;
			return true;
		}
		
		public byte[] getBytes(){
			final byte[] encryptedLength = intToByteArray(this.encrypted.length);
			final byte[] ivLength = intToByteArray(this.iv.length);
			byte[] res = concatAll(encryptedLength, this.encrypted, ivLength, this.iv, this.salt);
			return res;
		}
		
		public static Data valueOf(final byte[] bytes){
			final int full = bytes.length;
			final int il = 4;
			final int el = byteArrayToInt(Arrays.copyOfRange(bytes, 0, il));
			final int ivl = byteArrayToInt(Arrays.copyOfRange(bytes, el+il, el+(il*2)));
			final byte[] encrypted = Arrays.copyOfRange(bytes, il, il+el);
			final byte[] iv = Arrays.copyOfRange(bytes, el+(il*2), el+ivl+(il*2));
			final byte[] salt = Arrays.copyOfRange(bytes, el+ivl+(il*2), full);
			return new Data(encrypted, iv, salt);
		}
		
		public static byte[] concatAll(byte[] first, byte[]... rest) {
			int totalLength = first.length;
			for (byte[] array : rest) {
				totalLength += array.length;
			}
			byte[] result = Arrays.copyOf(first, totalLength);
			int offset = first.length;
			for (byte[] array : rest) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
			return result;
		}
		
		public static  byte[] intToByteArray(int myInteger){
		    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
		}

		public static int byteArrayToInt(byte [] byteBarray){
			return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Data [encrypted=")
					.append(Arrays.toString(encrypted)).append(", iv=")
					.append(Arrays.toString(iv)).append(", salt=")
					.append(Arrays.toString(salt)).append("]");
			return builder.toString();
		}
		
	}

}