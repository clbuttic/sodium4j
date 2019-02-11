package org.github.clbuttic.sodium4j;

import org.github.clbuttic.sodium4j.helpers.Hex;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Test helpers.Hex encoding and decoding.
 *
 * Not all tests from codec.c are implemented, yet.
 *
 * TODO: Implement all tests, even if it means direct calls to SodiumLibrary
 */
public class HelpersHexTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
    }

    //codecs.c#L20 Simple encoding
    @Test
    public void Test01() {
        String input = "0123456789ABCDEF";
        String expected = "30313233343536373839414243444546";
        String encoded = Hex.encode(input);
        assertEquals(expected, encoded);
    }

    //codecs.c#L23 Decoding to extended ascii
    @Test
    public void Test02a() {
        byte[] input = "Cafe : 6942".getBytes();
        byte[] expected = {(byte)0xca, (byte)0xfe, 0x69, 0x42};
        byte[] decoded = Hex.decode(input, ": ".getBytes());
        assertTrue(Arrays.equals(expected, decoded));
    }

    /*
    Duplicate Test02, but use Strings instead of byte[].
    Anything in extended ascii beyond 0x7f needs to be forced to ISO_8859_1, or else it'll be assumed to be UTF and
    be translated. One byte > 127 is increased to 2 or 3 bytes, depending on which UTF charset is in place.

    We have to make sure this doesn't happen.
    */
    @Test
    public void Test02b() {
        String input = "Cafe : 6942";
        String expected = "\u00ca\u00fe\u0069\u0042";
        String decoded = Hex.decode(input, ": ");
        assertEquals(expected, decoded);
    }

    //codecs.c#L30 Tests decoding without hex_end
    //We always have hex_end
    @Ignore
    @Test
    public void Test03() {}

    //codecs.c#L35 Tests overflow detection
    //Not possible without changing the implementer.
    @Ignore
    @Test
    public void Test04() {}

    //codecs.c#L41 Test a strange input length, and short output
    //Output will always be long enough for us.
    @Ignore
    @Test
    public void Test05() {}

    //codecs.c#L49 Test a strange input length

    @Test
    public void Test06() {
        thrown.expect(IllegalArgumentException.class);
        String input = "de:ad:be:eff";
        String expected = "\u00de\u00ad\u00be\u00ef";
        String output = Hex.decode(input, ":");
        assertEquals(expected, output);
    }

    //codecs.c#L56 Test a strange input length, including \0
    //A runtimeException is thrown if the decode return is not 0.
    //Having a \0 in the input should trigger an invalid character.
    //hex_end is not null,
    @Test
    public void Test07() {
        thrown.expect(IllegalArgumentException.class);
        String input = "de:ad:be:eff\u0000";
        String expected = "\u00de\u00ad\u00be\u00ef";
        String output = Hex.decode(input, ":");
        assertEquals(expected, output);
    }

    //codecs.c#L63 Strange input length and no hex_end
    //We always have hex_end
    @Ignore
    @Test
    public void Test08() {}

    //codecs.c#L69 An unexpected character
    @Test
    public void Test09() {
        thrown.expect(IllegalArgumentException.class);
        String input = "de:ad:be:ef*";
        String expected = "\u00de\u00ad\u00be\u00ef";
        String output = Hex.decode(input, ":");
        assertEquals(expected, output);
    }

    //codecs.c#L76 An unexpected character with no end pointer.
    //We always have an end pointer.
    @Ignore
    @Test
    public void Test10() {}


}

