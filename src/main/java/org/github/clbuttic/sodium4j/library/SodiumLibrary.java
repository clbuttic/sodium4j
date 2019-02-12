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

    /**
     * Get the version of sodium as a string. It will return as MAJOR.MINOR.PATCH.
     * @return A string containing the version.
     */

    String sodium_version_string();

    // helpers

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

    int sodium_hex2bin(Pointer bin, long bin_maxlen,
                       Pointer hex, long hex_len,
                       byte[] ignore, LongByReference bin_len,
                       PointerByReference hex_end);

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

    /**
     * Constant-time test for equality.
     *
     * When a comparison involves secret data (e.g. key, authentication tag), is it critical to use a constant-time
     * comparison function. This property does not relate to computational complexity: it expresses the fact that
     * the time needed to perform the comparison is the same for all data of the same size. The goal is to mitigate
     * side-channel attacks.
     *
     * The sodium_memcmp() function can be used for this purpose.
     * @param b1_ The first region to compare.
     * @param b2_ The second region to compare.
     * @param len The length of both regions to compare.
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


    // Memory stuff

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
     * @param len The length above the stack pointer to overwrite.
     */
    @Since("1.0.16")
    void sodium_stackzero(long len);

    /**
     * After use, sensitive data should be overwritten, but memset() and hand-written code can be silently stripped out
     * by an optimizing compiler or by the linker.
     *
     * The sodium_memzero() function tries to effectively zero len bytes starting at pnt, even if optimizations are
     * being applied to the code.
     * @param pnt A pointer to the start of the memory region.
     * @param len The length of the memory region in bytes.
     */
    void sodium_memzero(Pointer pnt, long len);

    /**
     * The sodium_mlock() function locks at least len bytes of memory starting at addr. This can help avoid swapping
     * sensitive data to disk.
     *
     * In addition, it is recommended to totally disable swap partitions on machines processing sensitive data, or, as
     * a second choice, use encrypted swap partitions.
     *
     * For similar reasons, on Unix systems, one should also disable core dumps when running crypto code outside a
     * development environment. This can be achieved using a shell built-in such as ulimit or programatically using
     * setrlimit(RLIMIT_CORE, &(struct rlimit) {0, 0}). On operating systems where this feature is implemented, kernel
     * crash dumps should also be disabled.
     *
     * sodium_mlock() wraps mlock() and VirtualLock(). Note: Many systems place limits on the amount of memory that may
     * be locked by a process. Care should be taken to raise those limits (e.g. Unix ulimits) where neccessary.
     * sodium_mlock() will return -1 when any limit is reached.
     * @param pnt The start of the memory region.
     * @param len The length of the memory region.
     * @return
     */
    int sodium_mlock(Pointer pnt, long len);

    /**
     * The sodium_munlock() function should be called after locked memory is not being used any more. It will zero len
     * bytes starting at addr before actually flagging the pages as swappable again. Calling sodium_memzero() prior to
     * sodium_munlock() is thus not required.
     *
     * On systems where it is supported, sodium_mlock() also wraps madvise() and advises the kernel not to include the
     * locked memory in core dumps. sodium_munlock() also undoes this additional protection.
     * @param pnt
     * @param len
     * @return
     */
    int sodium_munlock(Pointer pnt, long len);

    /**
     * The sodium_malloc() function returns a pointer from which exactly size contiguous bytes of memory can be
     * accessed. Like normal malloc, NULL may be returned and errno set if it is not possible to allocate enough memory.
     *
     * The allocated region is placed at the end of a page boundary, immediately followed by a guard page. As a result,
     * accessing memory past the end of the region will immediately terminate the application.
     *
     * A canary is also placed right before the returned pointer. Modifications of this canary are detected when trying
     * to free the allocated region with sodium_free(), and also cause the application to immediately terminate.
     *
     * An additional guard page is placed before this canary to make it less likely for sensitive data to be accessible
     * when reading past the end of an unrelated region.
     *
     * The allocated region is filled with 0xdb bytes in order to help catch bugs due to uninitialized data.
     *
     * In addition, sodium_mlock() is called on the region to help avoid it being swapped to disk. On operating systems
     * supporting MAP_NOCORE or MADV_DONTDUMP, memory allocated this way will also not be part of core dumps.
     *
     * The returned address will not be aligned if the allocation size is not a multiple of the required alignment.
     *
     * For this reason, sodium_malloc() should not be used with packed or variable-length structures, unless the size
     * given to sodium_malloc() is rounded up in order to ensure proper alignment.
     *
     * All the structures used by libsodium can safely be allocated using sodium_malloc().
     *
     * Allocating 0 bytes is a valid operation. It returns a pointer that can be successfully passed to sodium_free().
     * @param size
     * @return
     */
    Pointer sodium_malloc(long size);

    /**
     * The sodium_allocarray() function returns a pointer from which count objects that are size bytes of memory each
     * can be accessed.
     *
     * It provides the same guarantees as sodium_malloc() but also protects against arithmetic overflows when
     * count * size exceeds SIZE_MAX.
     * @param count
     * @param size
     * @return
     */
    Pointer sodium_allocarray(long count, long size);

    /**
     * The sodium_free() function unlocks and deallocates memory allocated using sodium_malloc() or sodium_allocarray().
     *
     * Prior to this, the canary is checked in order to detect possible buffer underflows and terminate the process if
     * required.
     *
     * sodium_free() also fills the memory region with zeros before the deallocation.
     *
     * This function can be called even if the region was previously protected using sodium_mprotect_readonly(); the
     * protection will automatically be changed as needed.
     *
     * ptr can be NULL, in which case no operation is performed.
     * @param ptr
     */
    void sodium_free(Pointer ptr);

    /**
     * The sodium_mprotect_noaccess() function makes a region allocated using sodium_malloc() or sodium_allocarray()
     * inaccessible. It cannot be read or written, but the data are preserved.
     *
     * This function can be used to make confidential data inaccessible except when actually needed for a specific
     * operation.
     * @param ptr
     * @return
     */
    int sodium_mprotect_noaccess(Pointer ptr);

    /**
     * The sodium_mprotect_readonly() function marks a region allocated using sodium_malloc() or sodium_allocarray() as
     * read-only.
     *
     * Attempting to modify the data will cause the process to terminate.
     * @param ptr
     * @return
     */
    int sodium_mprotect_readonly(Pointer ptr);

    /**
     * The sodium_mprotect_readwrite() function marks a region allocated using sodium_malloc() or sodium_allocarray()
     * as readable and writable, after having been protected using sodium_mprotect_readonly() or
     * sodium_mprotect_noaccess().
     * @param ptr
     * @return
     */
    int sodium_mprotect_readwrite(Pointer ptr);

    // Padding

    /**
     * The sodium_pad() function adds padding data to a buffer buf whose original size is unpadded_buflen in order to
     * extend its total length to a multiple of blocksize.
     *
     * The new length is put into padded_buflen_p.
     *
     * The function returns -1 if the padded buffer length would exceed max_buflen, or if the block size is 0. It
     * returns 0 on success.
     *
     * @param padded_buflen_p The new length of buf, with padding. A multiple of blocksize
     * @param buf The buffer to pad
     * @param unpadded_buflen The length of the unpadded data in buf.
     * @param blocksize The blocksize to pad to.
     * @param max_buflen The maximum length of buf.
     * @return 0 on success, or -1 if the added padding would exceed max_buflen
     */
    int sodium_pad(LongByReference padded_buflen_p, Pointer buf,
                   long unpadded_buflen, long blocksize, long max_buflen);

    /**
     * The sodium_unpad() function computes the original, unpadded length of a message previously padded using
     * sodium_pad(). The original length is put into unpadded_buflen_p.
     *
     * @param unpadded_buflen_p The new length of buf, minus the padding.
     * @param buf The buffer to unpad.
     * @param padded_buflen The length of the padded data in buf.
     * @param blocksize The blocksize that was padded to.
     * @return 0 on success
     */
    int sodium_unpad(LongByReference unpadded_buflen_p, Pointer buf,
                     long padded_buflen, long blocksize);

    /**
     * The randombytes_random() function returns an unpredictable value between 0 and 0xffffffff (included).
     * @return a random number.
     */
    int randombytes_random();

    /**
     * The randombytes_uniform() function returns an unpredictable value between 0 and upper_bound (excluded). Unlike
     * randombytes_random() % upper_bound, it guarantees a uniform distribution of the possible output values even when
     * upper_bound is not a power of 2. Note that an upper_bound < 2 leaves only a single element to be chosen, namely
     * 0.
     * @param upper_bound The upper bound, one more than the highest expected number.
     * @return A random number 0 <= n < upper_bound
     */
    int randombytes_uniform(int upper_bound);

    /**
     * The randombytes_buf() function fills size bytes starting at buf with an unpredictable sequence of bytes.
     * @param buf A buffer to fill
     * @param size The number of bytes to fill.
     */
    void randombytes_buf(Pointer buf, long size);

    /**
     * The randombytes_buf_deterministic function stores size bytes into buf indistinguishable from random bytes without
     * knowing seed.
     *
     * For a given seed, this function will always output the same sequence. size can be up to 2^38 (256 GB).
     *
     * seed is randombytes_SEEDBYTES bytes long.
     *
     * This function is mainly useful for writing tests, and was introduced in libsodium 1.0.12. Under the hood, it
     * uses the ChaCha20 stream cipher.
     *
     * Up to 256 GB can be produced with a single seed.
     * @param buf A buffer to fill
     * @param size The number of bytes to fill
     * @param seed The seed to use.
     */
    @Since("1.0.12")
    void randombytes_buf_deterministic(Pointer buf, long size,
                                       Pointer seed);

    /**
     * This deallocates the global resources used by the pseudo-random number generator. More specifically, when the
     * /dev/urandom device is used, it closes the descriptor. Explicitly calling this function is almost never required.
     * @return
     */
    int randombytes_close();

    /**
     * The randombytes_stir() function reseeds the pseudo-random number generator, if it supports this operation.
     *
     * Calling this function is not required with the default generator, even after a fork() call, unless the descriptor
     * for /dev/urandom was closed using randombytes_close().
     *
     * If a non-default implementation is being used (see randombytes_set_implementation()), randombytes_stir() must be
     * called by the child after a fork() call.
     */
    void randombytes_stir();

    // Key Exchange

    /**
     * The crypto_kx_keypair() function creates a new key pair. It puts the public key into pk and the secret key into
     * sk.
     * @param pk The new public key.
     * @param sk The new secret key.
     * @return
     */
    int crypto_kx_keypair(Pointer pk, Pointer sk);

    /**
     * The crypto_kx_seed_keypair() function computes a deterministic key pair from the seed seed
     * (crypto_kx_SEEDBYTES bytes).
     * @param pk The new public key.
     * @param sk The new secret key.
     * @param seed The seed to use.
     * @return
     */
    int crypto_kx_seed_keypair(Pointer pk, Pointer sk, Pointer seed);

    /**
     * The crypto_kx_client_session_keys() function computes a pair of shared keys (rx and tx) using the client's
     * public key client_pk, the client's secret key client_sk and the server's public key server_pk.
     *
     * It returns 0 on success, or -1 if the server's public key is not acceptable.
     *
     * These keys can be used by any functions requiring secret keys up to crypto_kx_SESSIONKEYBYTES bytes, including
     * crypto_secretbox_*() and crypto_aead_*().
     *
     * The shared secret key rx should be used by the client to receive data from the server, whereas tx should be used
     * for data flowing in the opposite direction.
     *
     * rx and tx are both crypto_kx_SESSIONKEYBYTES bytes long. If only one session key is required, either rx or tx
     * can be set to NULL.
     * @param rx
     * @param tx
     * @param client_pk
     * @param client_sk
     * @param server_pk
     * @return
     */
    int crypto_kx_client_session_keys(Pointer rx, Pointer tx, Pointer client_pk, Pointer client_sk, Pointer server_pk);

    /**
     * The crypto_kx_server_session_keys() function computes a pair of shared keys (rx and tx) using the server's
     * public key server_pk, the server's secret key server_sk and the client's public key client_pk.
     *
     * It returns 0 on success, or -1 if the client's public key is not acceptable.
     *
     * The shared secret key rx should be used by the server to receive data from the client, whereas tx should be used
     * for data flowing in the opposite direction.
     *
     * rx and tx are both crypto_kx_SESSIONKEYBYTES bytes long. If only one session key is required, either rx or tx
     * can be set to NULL.
     * @param rx
     * @param tx
     * @param server_pk
     * @param server_sk
     * @param client_pk
     * @return
     */
    int crypto_kx_server_session_keys(Pointer rx, Pointer tx, Pointer server_pk, Pointer server_sk, Pointer client_pk);

}
