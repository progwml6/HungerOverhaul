package iguanaman.hungeroverhaul.common;

import java.util.Random;

public class RandomHelper
{
    public static final Random random = new Random();

    public static int getRandomIntFromRange(int min, int max)
    {
        return min + (max > min ? random.nextInt(1 + max - min) : 0);
    }

    /**
     * Like {@link Random#nextInt(int)}, but for floats.
     * 
     * Can be used with a {@code >= 1} check to emulate {@code nextInt(bound) != 0}.
     * 
     * @return float with bounds {@code 0f} (inclusive) and {@code bound} (exclusive)
     */
    public static float nextFloat(Random random, float bound)
    {
        return random.nextFloat() * bound;
    }
}
