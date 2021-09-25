package com.api.fooddistribution.utils;

import java.util.Random;

public class RandomGenerator {
    //old private int start = 15574;
    private int start = 10000;
    private int end = 900000;
    //Testing private int start = 1;
    //Testing private int end = 4;

    // note a single Random object is reused here
    Random randomGenerator = new Random();
    int randomInt = showRandomInteger(start, end, randomGenerator);

    private static int showRandomInteger(int aStart, int aEnd, Random aRandom){
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;
    }

    public int getRandomInt() {
        return randomInt;
    }
    public void setRandomInt(int randomInt) {
        this.randomInt = randomInt;
    }



}
