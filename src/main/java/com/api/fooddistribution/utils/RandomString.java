package com.api.fooddistribution.utils;


import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class RandomString {
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String digits = "0123456789";
    public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    public String nextString() {
        for (int idx = 0; idx < this.buf.length; ++idx) {
            this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
        }
        return new String(this.buf);
    }

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    public RandomString(int length) {
        this(length, (Random)new SecureRandom());
    }

    public RandomString() {
        this(21);
    }

    public static String randomCode() {
        KeyGenerator keyGenerator=    new KeyGenerator(4, ThreadLocalRandom.current());
        return (keyGenerator.nextString().toUpperCase());
    }

    public static String randUserPassword() {
        KeyGenerator keyGenerator=    new KeyGenerator(8, ThreadLocalRandom.current());
        return (keyGenerator.nextString().toUpperCase());
    }

    public static String referenceCode(){
        KeyGenerator keyGenerator=    new KeyGenerator(4, ThreadLocalRandom.current());
        return (keyGenerator.nextString().toUpperCase() + "-" + keyGenerator.nextString().toUpperCase()+"-"+ ConvertDate.formatDate(new Date(), "YYYYMMdd"));
    }

}

