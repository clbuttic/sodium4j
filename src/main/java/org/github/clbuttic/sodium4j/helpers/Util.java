package org.github.clbuttic.sodium4j.helpers;

import org.github.clbuttic.sodium4j.SecureMemory;
import org.github.clbuttic.sodium4j.Sodium4J;

public class Util {
    /**
     * Returns true if a equals b.
     *
     * This comparison is performed in constant time, independent of contents of a or b.
     * @param a An array to compare
     * @param b An array to compare
     * @return True if a equals b, false otherwise.
     */
    public static boolean Equals(SecureMemory a, SecureMemory b) {
        if (a.getLength() != b.getLength())
            return false;

        int ret = Sodium4J.getLibrary().sodium_memcmp(a.getPointer(), b.getPointer(), a.getLength());

        switch (ret) {
            case -1:
                return false;
            case 0:
                return true;
            default:
                throw new IllegalStateException("Unknown return from sodium_memcmp: " + ret);
        }
    }

    /**
     * Increment an arbitrary length little endian number
     * @param n A number to increment.
     */
    public static void Increment(SecureMemory n) {
        Sodium4J.getLibrary().sodium_increment(n.getPointer(), n.getLength());
    }

    /**
     * Add two arbitrary length little endian numbers together.
     * @param a The first value.
     * @param b The second value.
     * @return The result, a + b
     * @throws IllegalArgumentException if the arrays are not the same length
     */
    public static void Add(SecureMemory a, SecureMemory b) {
        if (a.getLength() != b.getLength())
            throw new IllegalArgumentException("Memory regions are not the same length");

        Sodium4J.getLibrary().sodium_add(a.getPointer(), b.getPointer(), a.getLength());
    }

    /**
     * Subtracts two arbitrary length little endian numbers.
     * @param a The first value
     * @param b The second value
     * @return The result, a - b
     * @throws IllegalArgumentException if the arrays are not the same length
     */
    public static void Subtract(SecureMemory a, SecureMemory b) {
        if (a.getLength() != b.getLength())
            throw new IllegalArgumentException("Memory regions are not the same length");


        Sodium4J.getLibrary().sodium_sub(a.getPointer(), b.getPointer(), a.getLength());

    }

    /**
     * Compare two arbitrary length little endian numbers
     * @param a The first number
     * @param b The second number
     * @return -1 if a < b, 0 if a == b, 1 if a > b
     */
    public static int Compare(SecureMemory a, SecureMemory b) {
        if (a.getLength() != b.getLength())
            throw new IllegalArgumentException("Arrays are not the same length");

        return Sodium4J.getLibrary().sodium_compare(a.getPointer(), b.getPointer(), a.getLength());
    }

    /**
     * Tests if an array is all zero.
     * @param n An array to test
     * @return true if n contains all zeros, false if otherwise.
     */
    public static boolean isZero(SecureMemory n) {
        int ret = Sodium4J.getLibrary().sodium_is_zero(n.getPointer(), n.getLength());

        switch(ret) {
            case 1:
                return true;
            case 0:
                return false;
            default:
                throw new IllegalStateException("Unknown return from sodium_is_zero: " + ret);
        }
    }

    /**
     * Clear out len bytes of memory above the current stack pointer.
     * @param len
     */
    public static void stackZero(int len) {
        Sodium4J.getLibrary().sodium_stackzero(len);
    }
}
