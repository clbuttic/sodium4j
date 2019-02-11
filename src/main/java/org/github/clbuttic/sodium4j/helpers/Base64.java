package org.github.clbuttic.sodium4j.helpers;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import org.github.clbuttic.sodium4j.SecureMemory;
import org.github.clbuttic.sodium4j.Sodium4J;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A class to encode or decode Base64 strings
 */
public class Base64 {

    /**
     * Returns the length of the encoded string of binLength were to be encoded using variant.
     * @param binLength The length of the input binary data to calculated in bytes.
     * @param variant The variant to calculate.
     * @return The length of an encoded string.
     */
    public static long encodedLength(long binLength, int variant) {
        //Subtract 1 from the return, since sodium assumes a null terminator.
        return Sodium4J.getLibrary().sodium_base64_encoded_len(binLength, variant) - 1;
    }

    /**
     * Base64 encode a string using a specified base64 variant.
     * @param bin A string to encode.
     * @param variant The base64 variant to encode with.
     * @return A base64 encoded string.
     */
    public static String encode(String bin, int variant) {
        return new String(encode(bin.getBytes(StandardCharsets.ISO_8859_1), variant), StandardCharsets.ISO_8859_1);
    }

    /**
     * Base64 encode a binary string using a specified Base64 variant.
     * @param bin A byte array to encode
     * @param variant The base64 variant to encode to.
     * @return An encoded byte array.
     */
    public static byte[] encode(byte[] bin, int variant) {
        byte[] b64 = new byte[Sodium4J.getLibrary().sodium_base64_encoded_len(bin.length, variant)];

        Sodium4J.getLibrary().sodium_bin2base64(b64, b64.length, bin, bin.length, variant);
        //Remove null terminator
        return Arrays.copyOfRange(b64, 0, b64.length - 1);
    }

    public static SecureMemory encode(SecureMemory bin, int variant) {
        SecureMemory b64 = new SecureMemory(Sodium4J.getLibrary().sodium_base64_encoded_len(bin.getLength(), variant));

        Sodium4J.getLibrary().sodium_bin2base64(
                b64.getPointer(), b64.getLength(),
                bin.getPointer(), bin.getLength(),
                variant);

        //Remove the null terminator
        return b64.copyOfRange(0, b64.getLength() - 1);
    }

    /**
     * Decode a base64 encoded string into a binary string, from a specific base64 variant.
     * @param b64 A base64 encoded string.
     * @param variant A base64 variant that b64 was encoded with.
     * @return A decoded binary string.
     */
    public static String decode(String b64, int variant) {
        return decode(b64, variant, StandardCharsets.ISO_8859_1);
    }

    /**
     * Decode a Base64 encoded string into a binary string, from a specific base64 variant, and a specific character
     * set.
     * @param b64 A base64 encoded string.
     * @param variant A base64 variant that b64 was encoded with.
     * @param charset The character set to override for string encoding.
     * @return A decoded binary string
     */
    public static String decode(String b64, int variant, Charset charset) {
        return decode(b64, null, variant, charset);
    }

    /**
     * Decode a base64 encoded string into a binary string, from a specific base64 variant, with characters to ignore.
     * @param b64 A base64 encoded string.
     * @param ignoreChars Characters in b64 to ignore.
     * @param variant The base64 variant that b64 was encoded with
     * @return A decoded binary string
     */
    public static String decode(String b64, String ignoreChars, int variant) {
        return decode(b64, ignoreChars, variant, StandardCharsets.ISO_8859_1);
    }

    /**
     * Decode a base64 encoded String into a binary string, from a specific base64 variant, with characters to ignore
     * and a specified character ste.
     * @param b64 A base64 encoded String.
     * @param ignoreChars Characters in b64 to ignore.
     * @param variant The base64 variants that b64 was encoded with
     * @param charset The character set to override for string encoding.
     * @return A decoded binary string.
     */
    public static String decode(String b64, String ignoreChars, int variant, Charset charset) {
        return new String(decode(b64.getBytes(charset), ignoreChars == null ? null : ignoreChars.getBytes(charset),
                variant), charset);
    }

    /**
     * Decode a base64 encoded byte array into a binary byte array.
     * @param b64 A base64 encoded byte array
     * @param variant The base64 variant that b64 was encoded with.
     * @return A decoded byte array.
     */
    public static byte[] decode(byte[] b64, int variant) {
        return decode(b64, null, variant);
    }

    /**
     * Decode a base64 encoded byte array into a binary byte array
     *
     * The decoding is not tolerent to unexpected characters. Any character in ignoreChars is ignored.
     * Any other unexpected character results in an IllegalArgumentException.
     *
     * The behaviour is exactly the same as you would expect for sodium_base642bin, with h64_end set to null, but throws
     * an exception when -1 is returned.
     *
     * @param b64 The base64 encoded byte array
     * @param ignoreChars characters in b64 to ignore (newlines and other whitespace)
     * @param variant The base64 variant to decode from.
     * @throws RuntimeException If an error during decoding is detected without fault of the input parameters.
     * @throws IllegalArgumentException If an input parameter is invalid.
     * @return A decoded byte array.
     */
    public static byte[] decode(byte[] b64, byte[] ignoreChars, int variant) {
        byte[] bin = new byte[b64.length];

        LongByReference outputLength = new LongByReference();
        Pointer b64Pointer = new Memory(b64.length);

        /*
        Even though JNA can read (or copy) from a byte array, we need to do some pointer arithmetic later on to check
        if invalid characters were detected.
         */
        b64Pointer.write(0, b64, 0, b64.length);

        /*
         * This will point to somwhere in the memory region of b64Pointer. If after decoding, it isn't at the end,
         * then there was some junk in the input.
         */
        PointerByReference b64_end = new PointerByReference();

        int result = Sodium4J.getLibrary().sodium_base642bin(
                bin, bin.length,
                b64Pointer, b64.length,
                ignoreChars, outputLength,
                b64_end, variant
        );

        //Pointer arithmetic in Java!
        long offset = Pointer.nativeValue(b64_end.getValue()) - Pointer.nativeValue(b64Pointer);

        if (result != 0) {
            //Result is not 0 if something went wrong.
            //If b64_end is set, when return == -1, there is some illegal input
            if (offset < b64.length)
                throw new IllegalArgumentException("Illegal characters from position " + offset);
            else
                throw new RuntimeException("Cannot decode supplied input.");
        }

        if (offset < b64.length) {
            throw new IllegalArgumentException("Unexpected characters at position " + offset);
        }

        return Arrays.copyOfRange(bin, 0, (int)outputLength.getValue());
    }
}
