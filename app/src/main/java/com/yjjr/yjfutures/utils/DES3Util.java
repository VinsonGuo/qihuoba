package com.yjjr.yjfutures.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * 3DES加密工具类
 *
 * @author 感谢原始作者liufeng
 * @date 2014-7-9
 */
public class DES3Util {
    // 密钥
    private final static String secretKey = "pR3t8Bp16BeDAMErGwnditpFhf9ZY4a1";
    // 向量
    private final static String iv = "12345678";
    // 加解密统一使用的编码方式
    private final static String encoding = "UTF-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) {
        try {
            return android.util.Base64.encodeToString(encode(plainText, secretKey).getBytes(), android.util.Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encode(String plainText, String secretKey)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return new String(Base64.encodeBase64(encryptData));
    }

}