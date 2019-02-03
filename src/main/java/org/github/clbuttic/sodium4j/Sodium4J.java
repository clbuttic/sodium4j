package org.github.clbuttic.sodium4j;

import jnr.ffi.LibraryLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sodium4J {

    /**
     * The minimum version of libsodium that we are willing to work with.
     *
     * Expressed as an ordinal calculated by: Major * 1E6 + Minor * 1E3 + Patch, so 1.7.3 is 1007003
     */

    public static final int LIBSODIUM_MINIMUM_VERSION = versionToOrdinal(1, 2,12);

    public static int LIBSODIUM_CURRENT_VERSION;

    private static Sodium4J ourInstance;

    private static Sodium libSodium;

    private static List<String> searchPaths = new ArrayList<>(Arrays.asList(
            "lib",
            "/lib",
            "/usr/lib",
            "/usr/local/lib"
    ));

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
        LibraryLoader libraryLoader = LibraryLoader.create(Sodium.class);
        for (String path : searchPaths)
            libraryLoader.search(path);

        libraryLoader.library("sodium").library("libsodium");

        libSodium = (Sodium) libraryLoader.load();

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
            throw new RuntimeException(String.format("Unsupported libsodium version. %d found, but %d required.",
                    LIBSODIUM_CURRENT_VERSION, LIBSODIUM_MINIMUM_VERSION
                    ));

        int ret = libSodium.sodium_init();

        switch(ret) {
            case -1:
                throw new RuntimeException("An error occurred while initializing libsodium.");
            case 0:
            case 1:
                //NOOP
                break;
            default:
                throw new RuntimeException(String.format("Unknown return code during libsodium initialization: %d ",
                        ret));

        }
    }

    /**
     * Libsodium uses a simole major.minor.patch version scheme. We exploit this to simplify version comparison.
     * @param major
     * @param minor
     * @param patch
     * @return
     */
    private static int versionToOrdinal(int major, int minor, int patch) {
        return (int) (major * 1E6 + minor * 1E3 + patch);
    }

    /**
     * Overwrite the libsodium search paths with ones you provide.
     *
     *
     * @param paths
     */
    public static void setSearchPath(List<String> paths) {
        if (isInstantiated())
            throw new IllegalStateException("Search paths cannot be modified after cass is instantiated.");
        searchPaths.clear();
        appendSearchPaths(paths);
    }

    /**
     * Append some paths to our default search paths to search for libsodium.
     * @param paths
     */
    public static void appendSearchPaths(List<String> paths) {
        if (isInstantiated())
            throw new IllegalStateException("Search paths cannot be modified after cass is instantiated.");
        searchPaths.addAll(paths);
    }

    private static boolean isInstantiated() {
        return ourInstance != null;
    }

    public static Sodium getInterface() {
        return libSodium;
    }
}
