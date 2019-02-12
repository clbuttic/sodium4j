package org.github.clbuttic.sodium4j.types;

/**
 * Holds an associated secret and public keypair.
 *
 * Keys are kept locked.
 */
public abstract class KeyPair {
    /**
     * The public key, shared with others.
     */
    SecureMemory publicKey;
    /**
     * The secret key, kept secret.
     */
    SecureMemory secretKey;

    /**
     * Generate a new keypair.
     */
    public abstract void generate();

    /**
     * Get the length of a public key of the implementing type.
     * @return The length of a public key of this type.
     */
    public abstract int getPublicKeyLength();

    /**
     * Get the length of a secret key of the implementing type.
     * @return The lenght of a secret key of this type.
     */
    public abstract int getSecretKeyLength();

    /**
     * Retrieve the public key
     * @return
     */
    public SecureMemory getPublicKey() {
        return publicKey;
    }

    /**
     * Set the public key. Returns this to chain statements.
     * @param publicKey The public key to set.
     * @return this
     */
    public KeyPair setPublicKey(SecureMemory publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Retrieve the secret key
     * @return
     */
    public SecureMemory getSecretKey() {
        return secretKey;
    }

    /**
     * Set the secret key. Returns this to chain statements.
     * @param secretKey
     * @return
     */
    public KeyPair setSecretKey(SecureMemory secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
