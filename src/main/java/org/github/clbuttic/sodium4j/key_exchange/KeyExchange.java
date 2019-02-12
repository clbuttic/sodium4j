package org.github.clbuttic.sodium4j.key_exchange;

import org.github.clbuttic.sodium4j.Sodium4J;
import org.github.clbuttic.sodium4j.types.ExchangeKeyPair;
import org.github.clbuttic.sodium4j.types.SecureMemory;

import static org.github.clbuttic.sodium4j.Constants.SODIUM_KEY_EXCHANGE_SESSION_KEY;

/**
 * The super class for key exchange.
 */
abstract class KeyExchange {
    private ExchangeKeyPair clientKeyPair;
    private ExchangeKeyPair serverKeyPair;

    public KeyExchange() {

    }

    /**
     * Generate a session key pair. Messages are encrypted with txKey and decrypted with rxKey.
     *
     * Earlier versions generated the same shared secret for both the client and server. Since 1.0.12, two keys are
     * derived from the key exchange: A receiving key and a transmission key. The server's rxKey is the same as the
     * client's txKey, and the server's txKey is the same as the client's rxKey.
     */
    public void generateSessionKeys(SecureMemory txKey, SecureMemory rxKey) {
        //Wipe out the keys, if they exist.
        if (txKey != null)
            txKey.dispose();
        if (rxKey != null)
            rxKey.dispose();

        txKey = new SecureMemory(SODIUM_KEY_EXCHANGE_SESSION_KEY);
        rxKey = new SecureMemory(SODIUM_KEY_EXCHANGE_SESSION_KEY);
        
        if (this instanceof ClientKeyExchange) {
            clientKeyPair.getSecretKey().setAccessReadOnly();
            Sodium4J.getLibrary().crypto_kx_client_session_keys(
                    rxKey.getPointer(), txKey.getPointer(),
                    clientKeyPair.getPublicKey().getPointer(), clientKeyPair.getSecretKey().getPointer(),
                    serverKeyPair.getPublicKey().getPointer());
            clientKeyPair.getSecretKey().setAccessNone();
        } else if (this instanceof ServerKeyExchange){
            serverKeyPair.getSecretKey().setAccessReadOnly();
            Sodium4J.getLibrary().crypto_kx_server_session_keys(
                    rxKey.getPointer(), txKey.getPointer(),
                    serverKeyPair.getPublicKey().getPointer(), serverKeyPair.getSecretKey().getPointer(),
                    clientKeyPair.getPublicKey().getPointer());
            serverKeyPair.getSecretKey().setAccessNone();
        } else {
            throw new RuntimeException("Unknown KeyExchange child class");
        }

        txKey.setAccessNone();
        rxKey.setAccessNone();
    }

    public ExchangeKeyPair getClientKeyPair() {
        return clientKeyPair;
    }

    public void setClientKeyPair(ExchangeKeyPair clientKeyPair) {
        this.clientKeyPair = clientKeyPair;
    }

    public void setClientKeyPair(SecureMemory publicKey, SecureMemory privateKey) {
        this.clientKeyPair = new ExchangeKeyPair();
    }

    public ExchangeKeyPair getServerKeyPair() {
        return serverKeyPair;
    }

    public void setServerKeyPair(ExchangeKeyPair serverKeyPair) {
        this.serverKeyPair = serverKeyPair;
    }
}
