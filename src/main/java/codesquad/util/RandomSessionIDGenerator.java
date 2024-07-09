package codesquad.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomSessionIDGenerator {

    private static MessageDigest digest;
    private static SecureRandom secureRandom;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException ignore) {
        }

    }

    private RandomSessionIDGenerator() {
    }

    public static String generate() {
        String temp = String.valueOf(secureRandom.nextLong());
        digest.update(temp.getBytes());
        return HexEncoder.byteToHex(digest.digest());
    }
}
