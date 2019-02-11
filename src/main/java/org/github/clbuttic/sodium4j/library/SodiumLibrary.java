package org.github.clbuttic.sodium4j.library;



import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import org.github.clbuttic.sodium4j.annotation.Since;


/**
 * An interface into libsodium. It's not advised to use this interface directly.
 *
 * If there is a feature or behaviousr missing from Sodium4J that requires you to use this interface, please log a new
 * issue at https://github.com/clbuttic/sodium4j/issues so we can help you.
 *
 * Much of the documentation was copied or modified from https://jedisct1.gitbooks.io/libsodium/.
 *
 */
public interface SodiumLibrary extends Library {

    /**
     * Initilizes the underlying libsodium library.
     * @return 0 if successful, 1 if the library has already been initialized, -1 if an error has occurred.
     */

    int sodium_init();

    String sodium_version_string();

    // helpers

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
     * Hexadecimal encoding, using a pointer.
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
    void sodium_bin2hex(Pointer hex, long hex_maxlen,
                        Pointer bin, long bin_len);

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

    int sodium_hex2bin(Pointer bin, long bin_maxlen,
                       Pointer hex, long hex_len,
                       byte[] ignore, LongByReference bin_len,
                       PointerByReference hex_end);

    int sodium_hex2bin(Pointer bin, long bin_maxlen,
                       byte[] hex, long hex_len,
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

    void sodium_bin2base64(Pointer b64, long b64_maxlen,
                           Pointer bin, long bin_len,
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
    int sodium_base642bin(Pointer bin, long bin_maxlen,
                          Pointer b64, long b64_len,
                          byte[] ignore, LongByReference bin_len,
                          PointerByReference b64_end, int variant);

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
    int sodium_memcmp(Pointer b1_, Pointer b2_, long len);

    /**
     * Increment an arbitrary length unsigned number.
     *
     * It runs in constant-time for a given length, and considers the number to be encoded in little-endian format
     *
     * This can be used to increment nonces in constant time.
     * @param n The number to increment.
     * @param nlen The length of n
     */
    void sodium_increment(Pointer n, long nlen);

    /**
     * Add two arbitrary length little endian numbers together.
     *
     * Computes (a + b) mod 2^(8*len) in constant time for a given length, and overwrites a with the result.
     * @param a The first number, overwritten with the result.
     * @param b The second number.
     * @param len The length of both numbers.
     */
    void sodium_add(Pointer a, Pointer b, long len);

    /**
     * Subtract an arbitrary length little endian number from another.
     *
     * Computes (a - b) mod 2^(8*len) in constant time for a given length, and overwrites a with the result.
     * @param a
     * @param b
     * @param len
     */
    @Since("1.0.17")
    void sodium_sub(Pointer a, Pointer b, long len);

    /**
     * Compare two arbitrary length little endian numbers.
     *
     * Return -1 if b1_ < b2_
     * Return 0 if b1_ == b2_
     * Return 1 if b1_ > b2_
     *
     * The comparison is done in constant time for a given length. This function can be used with nonces, in order to prevent replay attacks.
     * @param b1_ The first number to compare
     * @param b2_ The second number to compare
     * @param len The length of both numbers.
     * @return -1, 0 or 1.
     */
    int sodium_compare(Pointer b1_, Pointer b2_, long len);

    /**
     * Tests if arbitrary length is 0.
     *
     * Returns 1 is the nlen bytes vector pointed by n contains only zeros. It returns 0 if non-zero bits are found.
     * @param n The number to test.
     * @param nlen The length of the number.
     * @return 1 if n is 0.
     */
    int sodium_is_zero(Pointer n, long nlen);

    /**
     * clears len bytes above the current stack pointer, to overwrite sensitive values that may have been temporarily
     * stored on the stack.
     *
     * Note that these values can still be present in registers.
     * @param len
     */
    @Since("1.0.16")
    void sodium_stackzero(long len);

    /**
     *
     * @param pnt
     * @param len
     */
    void sodium_memzero(Pointer pnt, long len);



}
