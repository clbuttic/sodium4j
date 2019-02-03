package org.github.clbuttic.sodium4j;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Test the raw NaCl.Sodium interface.
 *
 * This is an implementation of a subset of libsodium's tests. It is assumed that libsodium is sane, so tests in this
 * class are only intended to confirm the interface is working correctly.
 */
public class InterfaceTest {
    @Before
    public void init() {
        Sodium4J.getInstance();
    }

    @Test
    public void sanityCheck() {
        Sodium4J instance = Sodium4J.getInstance();
        assert(instance != null);
    }

//    @Test
//    public void memcmp1() {
//        assertEquals(0, NaCl.sodium().sodium_memcmp(Constants.MEMCMP_1_IN, Constants.MEMCMP_1_IN,
//                Constants.MEMCMP_1_IN.length));
//    }
//
//    @Test
//    public void bin2hex1() {
//        byte[] out = new byte[Constants.BIN2HEX_1_IN.length * 2 + 1];
//
//        NaCl.sodium().sodium_bin2hex(out, out.length, Constants.BIN2HEX_1_IN, Constants.BIN2HEX_1_IN.length);
//        assertTrue(Arrays.equals(out, Constants.BIN2HEX_1_OUT));
//    }
//
//    @Test
//    public void hex2bin() {
//        byte[] out = new byte[Constants.BIN2HEX_1_IN.length * 2 + 1];
//    }
}
