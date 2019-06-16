package com.tipdm.framework.common.utils;

import org.apache.commons.io.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 文件工具类
 *
 * @author zhoulong E-mail:zhoulong8513@gmail.com
 * @version 创建时间：2016年9月6日 下午4:29:34
 *          类说明
 */
public class FileKit extends FileUtils {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "abcdefghijklmnopqrstuvwxyz1234567890";

    public static boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }

    /**
     * AES方式加密文件
     * @param sourceFile
     * @param encryptFile
     * @return
     * @throws IOException
     */
    public static File encryptFile(File sourceFile, File encryptFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(sourceFile)) {
            try (OutputStream outputStream = new FileOutputStream(encryptFile)) {
                Cipher cipher = initAESCipher(Cipher.ENCRYPT_MODE);
                // 以加密流写入文件
                try (CipherInputStream cipherInputStream = new CipherInputStream(
                        inputStream, cipher)) {
                    byte[] cache = new byte[1024];
                    int nRead = 0;
                    while ((nRead = cipherInputStream.read(cache)) != -1) {
                        outputStream.write(cache, 0, nRead);
                        outputStream.flush();
                    }
                }
            }
        }
        return encryptFile;
    }


    /**
     * AES方式解密文件
     *
     * @param sourceFile  要解密的文件路径
     * @param decryptFile 解密后的文件路径
     * @return
     */
    public static File decryptFile(File sourceFile, File decryptFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(sourceFile);) {
            Cipher cipher = initAESCipher(Cipher.DECRYPT_MODE);
            try (OutputStream outputStream = new FileOutputStream(decryptFile)) {
                try (CipherOutputStream cipherOutputStream = new CipherOutputStream(
                        outputStream, cipher)) {
                    byte[] buffer = new byte[1024];
                    int r;
                    while ((r = inputStream.read(buffer)) >= 0) {
                        cipherOutputStream.write(buffer, 0, r);
                    }
                }
            }
        }
        return decryptFile;
    }

    /**
     * 初始化 AES Cipher
     *
     * @param cipherMode
     * @return
     */
    private static Cipher initAESCipher(int cipherMode) {
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
//            keyGenerator.init(128, new SecureRandom(KEY.getBytes()));
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(KEY.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] codeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(codeFormat, ALGORITHM);
            cipher = Cipher.getInstance(ALGORITHM);
            // 初始化
            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }
}
