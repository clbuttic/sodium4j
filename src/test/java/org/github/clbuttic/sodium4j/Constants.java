package org.github.clbuttic.sodium4j;

/**
 * Constants used for unit tests.
 */
public class Constants {
    public static byte[] MEMCMP_1_IN =  "\u0001\u0002\u0003\u0004\u0005".getBytes();
    public static byte[] BIN2HEX_1_IN = "\u0001\u0002\u0003\u0004\u0005".getBytes();
    public static byte[] BIN2HEX_1_OUT = "0102030405\u0000".getBytes();

    public static String HELPERS_HEX_01_DECODED = "\u0001\u0002\u0003\u0004\u0005";
    public static String HELPERS_HEX_01_ENCODED = "0102030405";

    public static String HELPERS_HEX_02_DECODED = "\u0043\u003c\u0037\u004f";
    public static String HELPERS_HEX_02_ENCODED = "43:3c:37:4f";
}
