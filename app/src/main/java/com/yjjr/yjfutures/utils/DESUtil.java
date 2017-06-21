package com.yjjr.yjfutures.utils;

import android.util.Base64;


import com.yjjr.yjfutures.contants.Config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {


    public static String decrypt(String decryptString, String decryptKey)
            throws Exception {
        IvParameterSpec iv = new IvParameterSpec(Config.DES_IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decode(decryptString.getBytes(), Base64.DEFAULT)));
    }

    public static String encrypt(String encryptString) {
        try {
            IvParameterSpec iv = new IvParameterSpec(Config.DES_IV.getBytes());
            DESKeySpec dks = null;

            dks = new DESKeySpec(Config.DES_KEY.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            return new String(
                    Base64.encode(cipher.doFinal(encryptString.getBytes()), Base64.NO_WRAP));
//            return new String(cipher.doFinal(encryptString.getBytes()));
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }
}
