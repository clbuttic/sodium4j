package org.github.clbuttic.sodium4j.helpers;

import org.github.clbuttic.sodium4j.SecureMemory;
import org.github.clbuttic.sodium4j.Sodium4J;

/**
 * A source of high quality random data.
 *
 * On Windows systems, the RtlGenRandom() function is used
 * On OpenBSD and Bitrig, the arc4random() function is used
 * On recent Linux kernels, the getrandom system call is used
 * On other Unices, the /dev/urandom device is used
 * If none of these options can safely be used, custom implementations can easily be hooked.
 *
 * The quality of random data is better than Java's Random or SecureRandom classes, but is slower and should not be
 * used for general purpose RNG.
 */
public class Random {
    /**
     * Return a random integer between -2^31-1 and 2^31.
     *
     * The original libsodium implementation returns an unsigned integer. Java doesn't support unsigned integers.
     * @return A random number
     */
    public static int nextInt() {
        return Sodium4J.getLibrary().randombytes_random();
    }

    /**
     * Return a random integer between 0 and upper_bound.
     *
     * Since Java doesn't support unsigned integers, an upper_bound greater than 0x7fffffff will yield negative numbers.
     *
     * @param upper_bound The highest value to return
     * @return A random integer, 0 < upper_bound
     */
    public static int nextInt(int upper_bound) {
        return Sodium4J.getLibrary().randombytes_uniform(upper_bound);
    }

    /**
     * Fill an array with random data.
     *
     * This is good enough for long term secret keys.
     * @param buffer The buffer to fill.
     */
    public static void fill(byte[] buffer) {
        SecureMemory tmp = new SecureMemory(buffer.length);
        tmp.setAccessReadWrite();
        tmp.fillRandom();
        tmp.copyTo(buffer);
    }

    /**
     * Fill a SecureMemory region with random data.
     *
     * This is an alias for SecureMemory.randomFill
     * @param buffer The buffer to fill.
     */
    public static void fill(SecureMemory buffer) {
        buffer.fillRandom();
    }

    /**
     * Get a random byte.
     * @return A random byte.
     */
    public static byte nextByte() {
        SecureMemory tmp = new SecureMemory(Byte.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getByte(0);
    }

    /**
     * Get a random char.
     * @return A random char.
     */
    public static char nextChar() {
        SecureMemory tmp = new SecureMemory(Character.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getChar(0);
    }

    /**
     * Get a random short.
     * @return A random short.
     */
    public static short nextShort() {
        SecureMemory tmp = new SecureMemory(Short.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getShort(0);
    }

    /**
     * Get a random long.
     * @return A random long.
     */
    public static long nextLong() {
        SecureMemory tmp = new SecureMemory(Long.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getLong(0);
    }

    /**
     * Get a random float.
     * @return A random float.
     */
    public static float nextFloat() {
        SecureMemory tmp = new SecureMemory(Float.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getFloat(0);
    }

    /**
     * Get a random double.
     * @return A random double.
     */
    public static double nextDouble() {
        SecureMemory tmp = new SecureMemory(Double.SIZE/8);
        tmp.fillRandom();
        return tmp.getPointer().getDouble(0);
    }

}
