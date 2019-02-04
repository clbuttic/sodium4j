package org.github.clbuttic.sodium4j;


import jdk.nashorn.internal.runtime.ConsString;
import org.github.clbuttic.sodium4j.Helpers.Base64;
import org.github.clbuttic.sodium4j.Constants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;

/**
 * Test Helpers.Base64 encoding and decoding
 */
public class HelpersBase64Test {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
    }

    //codecs.c#L82 Simple encode, original variant
    @Test
    public void Test01() {
        String input = "\u00fb\u00f0\u00f10123456789ABCDEFab";
        String expected = "+/DxMDEyMzQ1Njc4OUFCQ0RFRmFi";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
        assertEquals(expected, output);
    }

    //codecs.c#L85 Simple encode, no padding, original variant
    @Test
    public void Test02() {
        String input = "\u00fb\u00f0\u00f10123456789ABCDEFabc";
        String expected = "+/DxMDEyMzQ1Njc4OUFCQ0RFRmFiYw";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
        assertEquals(expected, output);
    }

    //codecs.c#L88 Simple encode, url safe variant
    @Test
    public void Test03() {
        String input = "\u00fb\u00f0\u00f10123456789ABCDEFab";
        String expected = "-_DxMDEyMzQ1Njc4OUFCQ0RFRmFi";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_URLSAFE);
        assertEquals(expected, output);
    }

    //codecs.c#L91 Simple encode, no padding, url safe variant
    @Test
    public void Test04() {
        String input = "\u00fb\u00f0\u00f10123456789ABCDEFabc";
        String expected = "-_DxMDEyMzQ1Njc4OUFCQ0RFRmFiYw";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_URLSAFE_NO_PADDING);
        assertEquals(expected, output);
    }

    //codecs.c#L94 0 length non-null input, original variant
    @Ignore("Can't do this without having to go direct to the interface.")
    @Test
    public void Test05() {}

    //codecs.c#L97 Single character encode, original variant
    @Test
    public void Test06() {
        String input = "a";
        String expected = "YQ==";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
        assertEquals(expected, output);
    }

    //codecs.c#L100 double character encode, original variant
    @Test
    public void Test07() {
        String input = "ab";
        String expected = "YWI=";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
        assertEquals(expected, output);
    }

    //codecs.c#L103 triple character encode, original variant
    @Test
    public void Test08() {
        String input = "abc";
        String expected = "YWJj";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
    }

    //codecs.c#L106 0 length non-null input, no padding, original variant
    @Ignore("Can't do this without having to go direct to the interface.")
    @Test
    public void Test09() {}

    //codecs.c#L109 Single character encode, no padding, original variant
    @Test
    public void Test10() {
        String input = "a";
        String expected = "YQ";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
        assertEquals(expected, output);
    }

    //codecs.c#L112 double character encode, no padding, original variant
    @Test
    public void Test11() {
        String input = "ab";
        String expected = "YWI";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
        assertEquals(expected, output);
    }

    //codecs.c#L115 triple character encode, no padding, original variant
    @Test
    public void Test12() {
        String input = "abc";
        String expected = "YWJj";
        String output = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
        assertEquals(expected, output);
    }

    //codecs.c#L119 Short output decoding
    @Ignore("Can't do this without having to go direct to the interface.")
    @Test
    public void Test13() {

    }

    //codecs.c#L127 Simple decoding.
    @Test
    public void Test14() {
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = "\r\n ";
        String expected = "This is a journey into sound";

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
        assertEquals(expected, output);
    }

    //codecs.c#L134 Invalid character detection. Full test
    @Ignore("Can't test that the invalid characters are identified correctly.")
    @Test
    public void Test15() {
    }

    //codecs.c#L141 Invalid character detection
    @Test
    public void Test16() {
        thrown.expect(IllegalArgumentException.class);
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = null;

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_ORIGINAL);
    }

    //codecs.c#L143 Invalid character detection, original variant, no padding
    @Test
    public void Test17() {
        thrown.expect(IllegalArgumentException.class);
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = null;

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
    }

    //codecs.c#L145 Invalid character detection, original variant, no padding, ignore chars
    @Test
    public void Test18() {
        thrown.expect(IllegalArgumentException.class);
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = " \r\n";

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
    }

    //codecs.c#L147 Invalid character detection, original variant, no padding,
    @Test
    public void Test19() {
        thrown.expect(IllegalArgumentException.class);
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = null;

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_ORIGINAL_NO_PADDING);
    }

    //codecs.c#L149 Invalid character detection, original variant, no padding,
    @Test
    public void Test21() {
        thrown.expect(IllegalArgumentException.class);
        String input = "VGhpcyBpcyBhIGpvdXJu\nZXkgaW50by Bzb3VuZA==";
        String ignore = " \r\n";

        String output = Base64.decode(input, ignore, Constants.SODIUM_BASE64_VARIANT_URLSAFE_NO_PADDING);
    }

    //codecs.c#L152
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test22() {}

    //codecs.c#L154
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test23() {}

    //codecs.c#L156
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test24() {}

    //codecs.c#L158
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test25() {}

    //codecs.c#L160
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test26() {}


    //codecs.c#L163
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test27() {}

    //codecs.c#L165
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test28() {}

    //codecs.c#L167
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test29() {}

    //codecs.c#L169
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test30() {}

    //codecs.c#L171
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test31() {}

    //codecs.c#L173
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test32() {}

    //codecs.c#L175
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test33() {}

    //codecs.c#L177
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test34() {}


    //codecs.c#L180
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test35() {}

    //codecs.c#L182
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test36() {}

    //codecs.c#L184
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test37() {}

    //codecs.c#L186
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test38() {}

    //codecs.c#L188
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test39() {}


    //codecs.c#L191
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test40() {}

    //codecs.c#L193
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test41() {}

    //codecs.c#L195
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test42() {}

    //codecs.c#L197
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test43() {}

    //codecs.c#L199
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test44() {}

    //codecs.c#L201
    @Test
    @Ignore("Can't do this without having to go direct to the interface.")
    public void Test45() {}

    //codecs.c#L204 Memory safety
    //Since this test's job isn't to test whether RNG is working, we use
    //Java's RNG
    @Test
    public void Test46() {
        Random random = new Random();
        for(int x=0; x < 1000; x++) {
            byte[] input = new byte[100];
            random.nextBytes(input);
            int encodedLength = (input.length + 2) / 3 * 4; //not +1, since null trailer is stripped
            assertEquals(encodedLength, Base64.encodedLength(input.length, Constants.SODIUM_BASE64_VARIANT_URLSAFE));

            byte[] encoded = Base64.encode(input, Constants.SODIUM_BASE64_VARIANT_URLSAFE);
            byte[] decoded = Base64.decode(encoded, Constants.SODIUM_BASE64_VARIANT_URLSAFE);
            assert(Arrays.equals(input, decoded));
        }
    }

}
