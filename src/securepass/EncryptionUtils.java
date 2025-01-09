package securepass;

import java.util.Base64;

public class EncryptionUtils {

    public static String encrypt(String plainText, String key) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            char k = key.charAt(i % key.length());
            encryptedText.append((char) (c ^ k));
        }
        return Base64.getEncoder().encodeToString(encryptedText.toString().getBytes());
    }

    public static String decrypt(String encryptedText, String key) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < decodedBytes.length; i++) {
            char c = (char) decodedBytes[i];
            char k = key.charAt(i % key.length());
            decryptedText.append((char) (c ^ k));
        }
        return decryptedText.toString();
    }
}
