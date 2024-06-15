package edu.jcourse.util;

import org.hashids.Hashids;

public class CryptoUtil {
    private final Hashids hashids;

    public CryptoUtil(String salt) {
        int minHashLength = 10;
        hashids = new Hashids(salt, minHashLength);
    }

    public String encrypt(Long value) {
        return hashids.encode(value);
    }

    public Long decrypt(String hash) {
        long[] decode = hashids.decode(hash);
        return decode.length > 0 ? decode[0] : null;
    }
}