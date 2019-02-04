package org.github.clbuttic.sodium4j.Helpers;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import org.github.clbuttic.sodium4j.Sodium4J;
import org.github.clbuttic.sodium4j.SodiumLibrary;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Hex {

    /**
     * Encode a string to a hex string.
     * @param bin A string to encode
     * @return An encoded hex string.
     */

    public static String encode(String bin) {
        //Hex encoded strings are only ISO_8859_1, so we don't offer any options.
        return new String(encode(bin.getBytes()), StandardCharsets.ISO_8859_1);
    }

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
        Sodium4J.getLibrary().sodium_bin2hex(hex, hex.length, bin, bin.length);
        //Remove the null terminator.
        return Arrays.copyOfRange(hex, 0, hex.length - 1);
    }

    /**
     * Decode a hex encoded string.
     * @param hex The hex encoded string.
     * @return The decoded string.
     */

    public static String decode(String hex) {
        return new String(
                decode(hex.getBytes(StandardCharsets.ISO_8859_1), null),
                StandardCharsets.ISO_8859_1);
    }

    /**
     * Decode a hex encoded string, encoding to a specific character set.
     * @param hex A hex encoded string.
     * @param charset The character set to encode the result.
     * @return The decoded string.
     */

    public static String decode(String hex, Charset charset){
        return new String(decode(hex.getBytes(charset), null), charset);
    }

    /**
     * Decode a hex encoded string, with characters to ignore.
     * @param hex A hex encoded string.
     * @param ignoreChars Characters in hex to ignore.
     * @return The decoded string.
     */

    public static String decode(String hex, String ignoreChars) {
        return new String(
                decode(hex.getBytes(StandardCharsets.ISO_8859_1), ignoreChars.getBytes(StandardCharsets.ISO_8859_1)),
                StandardCharsets.ISO_8859_1);
    }

    /**
     * Decode a hex encoded string, with characters to ignore and a character set to decode to.
     * @param hex A hex encoded string.
     * @param ignoreChars Characters in hex to ignore.
     * @param charset The character set to decode to.
     * @return The decoded string.
     */
    public static String decode(String hex, String ignoreChars, Charset charset) {
        return new String(decode(hex.getBytes(charset), ignoreChars.getBytes(charset)), charset);
    }

    /**
     * Decode a hex encoded byte array.
     * @param hex A hex encoded byte array.
     * @return A decoded byte array.
     */
    public static byte[] decode(byte[] hex) {
        return decode(hex, null);
    }

    /**
     * Decode a hex encoded byte array, with a character set specifier.
     * @param hex A hex encoded byte array.
     * @param ignoreChars Characters to ignore in hex
     * @param charset The character ignoreChars is in.
     * @return A decoded byte array
     */

    public static byte[] decode(byte[] hex, String ignoreChars, Charset charset) {
        return decode(hex, ignoreChars.getBytes(charset));
    }

    /**
     * Decode a hex encoded byte array, with characters to ignore.
     *
     * The decoding is tolerent to unexpected characters. Any character in ignoreChars is ignored.
     * Any other unexpected character results in an IllegalArgumentException.
     *
     * The behaviour is exactly the same as you would expect for sodium_bin2hex, with hex_end set to null.
     * @param hex A hex encoded byte array.
     * @param ignoreChars Characters in hex to ignore.
     * @throws IllegalArgumentException If input is invalid.
     * @return The decoded byte array.
     */

    public static byte[] decode(byte[] hex, byte[] ignoreChars) {
        byte[] bin = new byte[hex.length/2 + 1];

        LongByReference outputLength = new LongByReference();
        Pointer hexPointer = new Memory(hex.length);

        hexPointer.write(0, hex, 0, hex.length);

        PointerByReference hex_end = new PointerByReference();

        int result = Sodium4J.getLibrary().sodium_hex2bin(
                bin, bin.length,
                hexPointer, hex.length,
                ignoreChars, outputLength,
                hex_end
        );

        if (result != 0) {
            //Result is not 0 if something went wrong, usually due to a weird input.
            throw new IllegalArgumentException("Cannot decode supplied input.");
        }

        long offset = Pointer.nativeValue(hex_end.getValue()) - Pointer.nativeValue(hexPointer);

        if (offset < hex.length) {
            //System.out.println("Offset is too short for input " + new String(hex, StandardCharsets.ISO_8859_1));
            byte[] extraChars = Arrays.copyOfRange(hex, (int)offset, hex.length);

            throw new IllegalArgumentException("Unexpected characters in hex decode at position " + offset);
        }

        return Arrays.copyOfRange(bin, 0, (int)outputLength.getValue());
    }
}
