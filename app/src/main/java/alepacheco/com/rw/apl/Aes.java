package alepacheco.com.rw.apl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes {


    public static void encryptLarge(String password, File in, File out) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(password), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        FileInputStream inputStream = new FileInputStream(in);
        FileOutputStream fileOutputStream = new FileOutputStream(out);

        int read;
        byte[] buffer = new byte[4096];

        CipherInputStream cis = new CipherInputStream(inputStream, cipher);

        while ((read = cis.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, read);
        }
        fileOutputStream.close();
        cis.close();
        in.delete();
    }
    public static void decryptLarge(String password, File in, File out) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(password), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        FileInputStream inputStream = new FileInputStream(in);
        FileOutputStream fileOutputStream = new FileOutputStream(out);

        int read;
        byte[] buffer = new byte[4096];

        CipherInputStream cis = new CipherInputStream(inputStream, cipher);

        while ((read = cis.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, read);
        }
        fileOutputStream.close();
        cis.close();
        in.delete();
    }


    /*
     * getRawKey does key expansion using a more secure algorithm and uses a salt to generate
     * the Key
     */
    private static byte[] getRawKey(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "AndroRW".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return secret.getEncoded();
    }

    public static byte[] encrypt(String password, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(password), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    public static byte[] decrypt(String password, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(password), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }
}