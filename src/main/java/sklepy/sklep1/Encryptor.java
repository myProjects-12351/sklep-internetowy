package sklepy.sklep1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public String encryptString(String input) {
        MessageDigest md = null;
        try {
            // algoritms: MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] messageDiggest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDiggest);
        return bigInt.toString(16);
    }
}
