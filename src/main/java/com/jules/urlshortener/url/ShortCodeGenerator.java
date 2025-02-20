package com.jules.urlshortener.url;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Service
public class ShortCodeGenerator {
    private static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62_ALPHABET.length();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new SecureRandom();

    public String generateShortCode(String longUrl) {
        var randomizedUrl = randomizeUrl(longUrl);
        var md5 = getMd5(randomizedUrl);
        return base64Encode(md5.substring(0, 5));
    }

    private String randomizeUrl(String longUrl) {
        StringBuilder randomString = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            randomString.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        randomString.append(longUrl);
        return randomString.toString();
    }

    private String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}