package org.github.clbuttic.sodium4j;

import org.github.clbuttic.sodium4j.Helpers.Hex;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.github.clbuttic.sodium4j.Constants.*;

public class HelpersHexTest {
    @Before
    public void init() {
        //Sodium4J.getInstance();
    }

    @Test
    public void Test01() {
        String input = "0123456789ABCDEF";
        String expected = "30313233343536373839414243444546";
        String encoded = Hex.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void Test02a() {
        byte[] input = "Cafe : 6942".getBytes();
        byte[] expected = {(byte)0xca, (byte)0xfe, 0x69, 0x42};
        byte[] decoded = Hex.decode(input, ": ".getBytes());
        assertTrue(Arrays.equals(expected, decoded));
    }

    //Duplicate Test02, but use Strings instead of byte[].
    //Anything in extended ascii beyond 0x7f needs to be forced to ISO_8859_1, or else it'll be assumed to be UTF.
    //We have to make sure this doesn't happen.
    @Test
    public void Test02b() {
        String input = "Cafe : 6942";
        byte[] expectedBytes = {(byte)0xca, (byte)0xfe, 0x69, 0x42};
        String expected = new String(expectedBytes, StandardCharsets.ISO_8859_1);
        String decoded = Hex.decode(input, ": ");
        assertEquals(expected, decoded);
    }

    @Test
    public void Test04() {
        assertEquals("", Hex.decode(""));
    }



}

