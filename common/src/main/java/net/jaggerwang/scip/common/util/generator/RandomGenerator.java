package net.jaggerwang.scip.common.util.generator;

import java.security.SecureRandom;
import java.util.Optional;

public class RandomGenerator {
    static final String upperLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String lowerLetters = "abcdefghijklmnopqrstuvwxyz";
    static final String numbers = "0123456789";
    static final String upperHexChars = "0123456789ABCDEF";
    static final String lowerHexChars = "0123456789abcdef";

    static final SecureRandom random = new SecureRandom();

    public String randomString(Integer len, String chars) {
        var sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String letterString(Integer len, Optional<Boolean> upper) {
        var chars = upperLetters + lowerLetters;
        if (upper.isPresent()) {
            chars = upper.get() ? upperLetters : lowerLetters;
        }
        return randomString(len, chars);
    }

    public String letterNumberString(Integer len, Optional<Boolean> upper) {
        var chars = upperLetters + lowerLetters;
        if (upper.isPresent()) {
            chars = upper.get() ? upperLetters : lowerLetters;
        }
        chars += numbers;
        return randomString(len, chars);
    }

    public String numberString(Integer len) {
        return randomString(len, numbers);
    }

    public String hexString(Integer len, Boolean upper) {
        var chars = upper ? upperHexChars : lowerHexChars;
        return randomString(len, chars);
    }
}
