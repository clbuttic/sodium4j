package org.github.clbuttic.sodium4j.types;

import org.github.clbuttic.sodium4j.Sodium4J;
import org.github.clbuttic.sodium4j.types.KeyPair;
import org.github.clbuttic.sodium4j.types.SecureMemory;

import static org.github.clbuttic.sodium4j.Constants.SODIUM_KEY_EXCHANGE_PUBLIC_KEY;
import static org.github.clbuttic.sodium4j.Constants.SODIUM_KEY_EXCHANGE_SECRET_KEY;

public class ExchangeKeyPair extends KeyPair {

    @Override
    public void generate() {
        publicKey = new SecureMemory(getPublicKeyLength());
        secretKey = new SecureMemory(getSecretKeyLength());
        Sodium4J.getLibrary().crypto_kx_keypair(publicKey.getPointer(), secretKey.getPointer());
        secretKey.setAccessNone();
        secretKey.setAccessNone();
    }

    @Override
    public int getPublicKeyLength() {
        return SODIUM_KEY_EXCHANGE_PUBLIC_KEY;
    }

    @Override
    public int getSecretKeyLength() {
        return SODIUM_KEY_EXCHANGE_SECRET_KEY;
    }
}
