package co.nilin.tosanboomsample.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Navid on 9/18/16.
 */
public class TextUtils {
    public static String pad(long num) {
        return (num < 10 ? "0" + num : String.valueOf(num));
    }

    public static String encryptMd5(String inStr) {
        try {
            byte[] bytesOfMessage = inStr.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);

            return new BigInteger(1, thedigest).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
