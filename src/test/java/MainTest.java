import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void printsConversionsAndNormalStatusForEnglishInput() {
        runMain("1\n20\n");

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Fahrenheit to Celsius:"));
        assertTrue(result.contains("Celcius to Fahrenheit:"));
        assertTrue(result.contains("Temperature is normal."));
    }

    @Test
    void usesSelectedFinnishBundleForExtremeTemperature() {
        runMain("2\n60\n");

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Syötä muunnettava lämpötila:"));
        assertTrue(result.contains("Lämpötila on ääriarvoinen."));
    }

    @Test
    void defaultsToEnglishForUnknownLanguageChoice() {
        runMain("unknown\n20\n");

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Invalid defaulting to english"));
        assertTrue(result.contains("Temperature is normal."));
    }

    @Test
    void showsLocalizedErrorAndStopsForNonNumericTemperature() {
        runMain("1\nnot-a-number\n");

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Please enter a valid number."));
        assertFalse(result.contains("Fahrenheit to Celsius:"));
    }

    private void runMain(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Main.main(new String[0]);
    }
}
