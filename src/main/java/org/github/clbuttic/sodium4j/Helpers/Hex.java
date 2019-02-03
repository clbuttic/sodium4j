package org.github.clbuttic.sodium4j.Helpers;

import jnr.ffi.Address;
import jnr.ffi.Pointer;
import jnr.ffi.byref.ByteByReference;
import jnr.ffi.byref.LongLongByReference;
import jnr.ffi.byref.PointerByReference;
import org.github.clbuttic.sodium4j.Sodium;
import org.github.clbuttic.sodium4j.Sodium4J;

import java.util.Arrays;

public class Hex {
    /**
     * Encode a byte array into a hexadecimal byte array.
     *
     * The returned byte array has its null terminator removed.
     * @param bin The byte array to encode.
     * @return A hexadecimal byte array
     */
    public static byte[] encode(byte[] bin) {
        //Double length, plus a null terminator
        byte[] hex = new byte[bin.length * 2 + 1];
        Sodium4J.getInterface().sodium_bin2hex(hex, hex.length, bin, bin.length);
        //Remove the null terminator.
        return Arrays.copyOfRange(hex, 0, hex.length - 1);
    }

    /**
     * Encode a string into a hexadecimal string.
     *
     * The returned string has its null terminator removed.
     * @param message The String to encode.
     * @return A hexadecimal String.
     */
    public static String encode(String message) {
        return new String(encode(message.getBytes()));
    }

    /**
     * Decode a hexadecimal byte array into a byte array.
     * @param message A hexadecimal encoded byte array.
     * @return A decoded byte array.
     */
    public static byte[] decode(byte[] message) {
        return decode(message, null);
    }

    /**
     * Decode a hexadecimal String into a String.
     * @param message A hexadecimal encoded String
     * @return A decoded String.
     */
    public static String decode (String message) {
        return new String(decode(message.getBytes(), null));
    }

    /**
     * Decode a hexadecimal String into a String with characters to ignore.
     * @param message A hexadecimal encoded String.
     * @param ignoreChars A list of characters to ignore in message.
     * @return A decoded String
     */
    public static String decode (String message, String ignoreChars) {
        return new String(decode(message.getBytes(), ignoreChars.getBytes()));
    }

    /**
     * Decode a hexadecimal byte array into a byte array with characters to ignore
     * @param hex A hexadecimal encoded byte array
     * @param ignoreChars A list of characters to ignore in message
     * @return A decoded byte array.
     */

    public static byte[] decode (byte[] hex, byte[] ignoreChars) {
        byte[] bin = new byte[hex.length*2];
        LongLongByReference outputLength = new LongLongByReference();
        ByteByReference hex_end = new ByteByReference();

        int result = Sodium4J.getInterface().sodium_hex2bin(
                bin, bin.length,
                hex, hex.length,
                ignoreChars, outputLength,
                hex_end
        );
        //TODO: Handle hex_end and result
        int length = outputLength.intValue();
        return Arrays.copyOfRange(bin, 0, length);
    }
}
