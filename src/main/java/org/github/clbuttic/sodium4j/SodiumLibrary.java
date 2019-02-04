package org.github.clbuttic.sodium4j;



import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;


/**
 * An interface into libsodium. It's not advised to use this interface directly.
 *
 * If there is a feature or behaviousr missing from Sodium4J that requires you to use this interface, please log a new
 * issue at https://github.com/clbuttic/sodium4j/issues so we can help you.
 *
 * Much of the documentation was copied or modified from https://jedisct1.gitbooks.io/libsodium/.
 */
public interface SodiumLibrary extends Library {

    /**
     * Initilizes the underlying libsodium library.
     * @return 0 if successful, 1 if the library has already been initialized, -1 if an error has occurred.
     */

    int sodium_init();

    String sodium_version_string();

    // Helpers

    /**
     * Constant-time test for equality.
     *
     * When a comparison involves secret data (e.g. key, authentication tag), is it critical to use a constant-time
     * comparison function. This property does not relate to computational complexity: it expresses the fact that
     * the time needed to perform the comparison is the same for all data of the same size. The goal is to mitigate
     * side-channel attacks.
     *
     * The sodium_memcmp() function can be used for this purpose.
     * @param b1_
     * @param b2_
     * @param len
     * @return 0 if the len bytes pointed to by b1_ match the len bytes pointed to by b2_. Otherwise, it returns -1.
     */
    int sodium_memcmp(byte[] b1_, byte[] b2_, int len);

    /**
     * Hexadecimal encoding.
     *
     * The sodium_bin2hex() function converts bin_len bytes stored at bin into a hexadecimal string.
     * The string is stored into hex and includes a nul byte (\0) terminator.
     * hex_maxlen is the maximum number of bytes that the function is allowed to write starting at hex. It must be
     * at least bin_len * 2 + 1 bytes.
     * @param hex
     * @param hex_maxlen
     * @param bin
     * @param bin_len
     * @return
     */

    void sodium_bin2hex(byte[] hex, long hex_maxlen,
                        byte[] bin, long bin_len);


    /**
     * Hexadecimal decoding.
     *
     * The sodium_hex2bin() function parses a hexadecimal string hex and converts it to a byte sequence.
     * hex does not have to be nul terminated, as the number of characters to parse is supplied via the hex_len
     * parameter.
     * ignore is a string of characters to skip. For example, the string ": " allows colons and spaces to be
     * present at any locations in the hexadecimal string. These characters will just be ignored. As a result,
     * "69:FC", "69 FC", "69 : FC" and "69FC" will be valid inputs, and will produce the same output.
     * ignore can be set to NULL in order to disallow any non-hexadecimal character.
     * bin_maxlen is the maximum number of bytes to put into bin.
     * The parser stops when a non-hexadecimal, non-ignored character is found or when bin_maxlen bytes have been
     * written.
     * If hex_end is not NULL, it will be set to the address of the first byte after the last valid parsed
     * character.
     * The function returns 0 on success.
     * It returns -1 if more than bin_maxlen bytes would be required to store the parsed string, or if the string
     * couldn't be fully parsed, but a valid pointer for hex_end was not provided.
     *
     * It evaluates in constant time for a given length and format.
     * @param bin
     * @param bin_maxlen
     * @param hex
     * @param hex_len
     * @param ignore
     * @param bin_len
     * @param hex_end
     * @return
     */

    int sodium_hex2bin(byte[] bin, long bin_maxlen,
                       Pointer hex, long hex_len,
                       byte[] ignore, LongByReference bin_len,
                       PointerByReference hex_end);

    /**
     * The sodium_bin2base64() function encodes bin as a Base64 string. variant must be one of:
     *
     * - SODIUM_BASE64_VARIANT_ORIGINAL
     * - SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING
     * - SODIUM_BASE64_VARIANT_URLSAFE
     * - SODIUM_BASE64_VARIANT_URLSAFE_NO_PADDING
     *
     * None of these Base64 variants provides any form of encryption; just like hex encoding, anyone can decode them.
     *
     * Computing a correct size for b64_maxlen is not straightforward and depends on the chosen variant.
     *
     * The sodium_base64_encoded_len(size_t bin_len, int variant) function is also available for the same purpose.
     *
     * b64 does not have its lines trimmed.
     * @param b64 The byte array that the encoded string will be placed into
     * @param b64_maxlen The maximum length that can be written out
     * @param bin The binary string to encode
     * @param bin_len The length of the binary string.
     * @param variant One of Constants.SODIUM_BASE64_VARIANT_* indicating the Base64 variant to encode with.
     */
    void sodium_bin2base64(byte[] b64, long b64_maxlen,
                           byte[] bin, long bin_len,
                           int variant);

    /**
     *The sodium_base642bin() function decodes a Base64 string using the given variant, and an optional set of
     * characters to ignore (typically: whitespaces and newlines).
     *
     * If b64_end is not NULL, it will be set to the address of the first byte after the last valid parsed character.
     *
     * The function returns 0 on success.
     *
     * It returns -1 if more than bin_maxlen bytes would be required to store the parsed string, or if the string
     * couldn't be fully parsed, but a valid pointer for b64_end was not provided.
     *
     * @param bin The binary string to decode to
     * @param bin_maxlen The maximum length that can be written to bin
     * @param b64 The encoded base64 string to decode
     * @param b64_len The length of the base64 input string
     * @param ignore Characters in b64 to ignore. Typically newlines and other white space.
     * @param bin_len The actual number of bytes written to bin
     * @param b64_end A pointer to the first unexpected character, or if succesful, the end of b64.
     * @param variant One of Constants.SODIUM_BASE64_VARIANT_* indicating the Base64 variant to decode with.
     * @return 0 on success, -1 if more than bin_maxlen bytes would be required to store the parsed string, or if the
     * string couldn't be fully parsed, but a valid pointer for b64_end was not provided.
     */
    int sodium_base642bin(byte[] bin, long bin_maxlen,
                          Pointer b64, long b64_len,
                          byte[] ignore, LongByReference bin_len,
                          PointerByReference b64_end, int variant);

    /**
     * Calculate the length of a base64 encoded string if a variant was used to encode a bin_len string.
     *
     * The returned length includes a trailing \0 byte.
     * @param bin_len The length of the string to encode.
     * @param variant The variant to encode.
     * @return The length of the encoded string.
     */
    int sodium_base64_encoded_len(long bin_len, int variant);


}
