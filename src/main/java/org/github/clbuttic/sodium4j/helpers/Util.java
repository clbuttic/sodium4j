package org.github.clbuttic.sodium4j.helpers;

import org.github.clbuttic.sodium4j.SecureMemory;
import org.github.clbuttic.sodium4j.Sodium4J;

public class Util {

    /**
     * Clear out len bytes of memory above the current stack pointer.
     * @param len
     */
    public static void stackZero(int len) {
        Sodium4J.getLibrary().sodium_stackzero(len);
    }

}
