package org.github.clbuttic.sodium4j;

import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sodium4J {


    /**
     * The minimum version of libsodium that we are willing to work with.
     *
     * Expressed as an ordinal calculated by: Major * 1E6 + Minor * 1E3 + Patch, so 1.7.3 is 1007003
     */

    public static final int LIBSODIUM_MINIMUM_VERSION = versionToOrdinal(1, 0,12);

    public static int LIBSODIUM_CURRENT_VERSION;

    private static Sodium4J ourInstance;

    private static SodiumLibrary libSodium;

    /**
     * Get an instance of Sodium4J.
     *
     * Only one instance is allowed. On first call, the object is instantiated.
     * @return An instance of Sodium4J
     */
    public static Sodium4J getInstance() {
        if (!isInstantiated())
            ourInstance = new Sodium4J();
        return ourInstance;
    }

    private Sodium4J() {
        libSodium  = Native.load(
                (Platform.isWindows() ? "libsodium" : "sodium"),
                SodiumLibrary.class
        );

        int ret = libSodium.sodium_init();

        switch(ret) {
            case -1:
                throw new RuntimeException("An error occurred while initializing libsodium.");
            case 0:
                //NOOP
                break;
            case 1:
                //The library is reporting that it has already been initialized. This is a bit odd.
                break;
            default:
                throw new RuntimeException(String.format("Unknown return code during libsodium initialization: %d ",
                        ret));

        }

        String version = libSodium.sodium_version_string();

        String[] versionTokens = version.split("\\.");
        if (versionTokens.length != 3)
            throw new RuntimeException("Format of libsodium version had changed.");

        LIBSODIUM_CURRENT_VERSION = versionToOrdinal(
                Integer.parseInt(versionTokens[0]),
                Integer.parseInt(versionTokens[1]),
                Integer.parseInt(versionTokens[2])
                );

        if (LIBSODIUM_MINIMUM_VERSION > LIBSODIUM_CURRENT_VERSION)
            throw new RuntimeException(String.format("Unsupported libsodium version. %s found, but %s required.",
                    ordinalToVersion(LIBSODIUM_CURRENT_VERSION), ordinalToVersion(LIBSODIUM_MINIMUM_VERSION)
                    ));
    }

    /**
     * Libsodium uses a simple major.minor.patch version scheme. We exploit this to simplify version comparison.
     * @param major
     * @param minor
     * @param patch
     * @return
     */
    private static int versionToOrdinal(int major, int minor, int patch) {
        return (int) ((major * 1E6) + (minor * 1E3) + patch);
    }

    private static String ordinalToVersion(int ordinal) {
        int major = (int)(ordinal / 1E6);
        int minor = (int)((ordinal - major * 1E6) / 1E3);
        int patch = (int)(ordinal - major * 1E6 - minor * 1E3);

        return String.format("%d.%d.%d", major, minor, patch);
    }

    private static boolean isInstantiated() {
        return ourInstance != null;
    }

    /**
     * A shortcut to getting the library. Should really only be used internally.
     * @return
     */
    public static SodiumLibrary getLibrary() {

        Sodium4J.getInstance();
        return libSodium;
    }
}
