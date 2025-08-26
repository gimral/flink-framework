package com.gimral.streaming.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryption {
    private static final int IV_LENGTH_GCM = 12; // Recommended for GCM
    private static final int IV_LENGTH_CBC = 16; // Required for CBC
    private static final int GCM_TAG_LENGTH = 128; // 128-bit auth tag for GCM
    private static final int[] VALID_KEY_LENGTHS = {16, 24, 32}; // 128, 192, 256 bits

    public enum CipherMode {
        CBC, GCM
    }

    /**
     * Encrypts the given plaintext using AES in the specified mode (CBC or GCM) with the provided key.
     * Supports key lengths of 128, 192, or 256 bits. Generates a random IV and prepends it to the ciphertext.
     *
     * @param plainText The text to encrypt
     * @param key The encryption key (16, 24, or 32 bytes for AES-128, AES-192, or AES-256)
     * @param mode The encryption mode (CBC or GCM)
     * @return Base64-encoded string containing IV + ciphertext (and tag for GCM)
     * @throws Exception If encryption fails or key is invalid
     */
    public static String encrypt(String plainText, String key, CipherMode mode) throws Exception {
        // Validate inputs
        if (plainText == null || key == null) {
            throw new IllegalArgumentException("Plaintext and key must not be null");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Cipher mode must not be null");
        }

        // Validate key length
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        boolean validKeyLength = false;
        for (int length : VALID_KEY_LENGTHS) {
            if (keyBytes.length == length) {
                validKeyLength = true;
                break;
            }
        }
        if (!validKeyLength) {
            throw new IllegalArgumentException("Key must be 16, 24, or 32 bytes (AES-128, AES-192, or AES-256)");
        }

        // Determine IV length based on mode
        int ivLength = (mode == CipherMode.GCM) ? IV_LENGTH_GCM : IV_LENGTH_CBC;

        // Generate secure random IV
        byte[] iv = new byte[ivLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // Initialize cipher
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher;
        if (mode == CipherMode.GCM) {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        } else {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        }

        // Encrypt plaintext
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Combine IV and ciphertext
        byte[] combined = new byte[ivLength + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, ivLength);
        System.arraycopy(encrypted, 0, combined, ivLength, encrypted.length);

        // Return Base64-encoded result
        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * Decrypts the given Base64-encoded ciphertext using AES in the specified mode (CBC or GCM).
     * Supports key lengths of 128, 192, or 256 bits. Expects the ciphertext to include the IV.
     *
     * @param cipherText The Base64-encoded ciphertext (IV + encrypted data)
     * @param key The decryption key (16, 24, or 32 bytes for AES-128, AES-192, or AES-256)
     * @param mode The decryption mode (CBC or GCM)
     * @return The decrypted plaintext
     * @throws Exception If decryption fails, key is invalid, or ciphertext is malformed
     */
    public static String decrypt(String cipherText, String key, CipherMode mode) throws Exception {
        // Validate inputs
        if (cipherText == null || key == null) {
            throw new IllegalArgumentException("Ciphertext and key must not be null");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Cipher mode must not be null");
        }

        // Decode Base64 ciphertext
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(cipherText);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64-encoded ciphertext");
        }

        // Validate key length
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        boolean validKeyLength = false;
        for (int length : VALID_KEY_LENGTHS) {
            if (keyBytes.length == length) {
                validKeyLength = true;
                break;
            }
        }
        if (!validKeyLength) {
            throw new IllegalArgumentException("Key must be 16, 24, or 32 bytes (AES-128, AES-192, or AES-256)");
        }

        // Determine IV length based on mode
        int ivLength = (mode == CipherMode.GCM) ? IV_LENGTH_GCM : IV_LENGTH_CBC;

        // Validate ciphertext length
        if (decoded.length < ivLength) {
            throw new IllegalArgumentException("Ciphertext too short to contain IV");
        }

        // Extract IV and ciphertext
        byte[] iv = new byte[ivLength];
        byte[] encrypted = new byte[decoded.length - ivLength];
        System.arraycopy(decoded, 0, iv, 0, ivLength);
        System.arraycopy(decoded, ivLength, encrypted, 0, decoded.length - ivLength);

        // Initialize cipher
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher;
        if (mode == CipherMode.GCM) {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        } else {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        }

        // Decrypt ciphertext
        byte[] decrypted = cipher.doFinal(encrypted);

        // Return plaintext
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
