package org.github.clbuttic.sodium4j;

/**
 * Constants for use for normal operations
 */
public class Constants {
    /**
     * Private constructor, since we don't ever want this instantiated.
     */
    private Constants() {}

    /**
     * Base64 encoding using + / with padding.
     */
    public static final int SODIUM_BASE64_VARIANT_ORIGINAL = 1;

    /**
     * Base64 encoding using + / without padding.
     */
    public static final int SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING = 3;

    /**
     * Base64 encoding using - _ with padding.
     */
    public static final int SODIUM_BASE64_VARIANT_URLSAFE = 5;

    /**
     * Base64 encoding using - _ without padding.
     */
    public static final int SODIUM_BASE64_VARIANT_URLSAFE_NO_PADDING = 7;
}
