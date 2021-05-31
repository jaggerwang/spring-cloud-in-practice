package net.jaggerwang.scip.common.util.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestEncoder {
    public String sha512(String content, String salt) {
        try {
            var md = MessageDigest.getInstance("SHA-512");
            return md.digest((content + salt).getBytes()).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
