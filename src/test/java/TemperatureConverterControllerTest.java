import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureConverterControllerTest {
    @Test
    void convertsInputAndFormatsResultsToTwoDecimalPlaces() {
        TemperatureConverterController.ConversionOutput output =
                TemperatureConverterController.convertInput(" 32 ", Locale.US);

        assertEquals("Fahrenheit to Celsius: 0.00", output.celsiusResult());
        assertEquals("Celcius to Fahrenheit: 89.60", output.fahrenheitResult());
        assertEquals("Temperature is normal.", output.statusMessage());
    }

    @Test
    void translatesResultsAndStatusUsingSelectedLocale() {
        TemperatureConverterController.ConversionOutput finnish =
                TemperatureConverterController.convertInput("60", Locale.forLanguageTag("fi-FI"));

        assertEquals("Fahrenheit-asteet Celsiuksiksi: 15.56", finnish.celsiusResult());
        assertEquals("Celsius-asteet Fahrenheit-asteiksi: 140.00", finnish.fahrenheitResult());
        assertEquals("Lämpötila on ääriarvoinen.", finnish.statusMessage());

        TemperatureConverterController.ConversionOutput japanese =
                TemperatureConverterController.convertInput("32", Locale.JAPAN);
        assertTrue(japanese.celsiusResult().startsWith("華氏から摂氏への変換:"));
        assertEquals("通常の温度です。", japanese.statusMessage());

        TemperatureConverterController.ConversionOutput swedish =
                TemperatureConverterController.convertInput("not-number", Locale.forLanguageTag("sv-SE"));
        assertEquals("Ange ett giltigt tal.", swedish.statusMessage());
    }

    @Test
    void acceptsBothInclusiveNormalTemperatureLimits() {
        assertEquals("Temperature is normal.",
                TemperatureConverterController.convertInput("-30", Locale.US).statusMessage());
        assertEquals("Temperature is normal.",
                TemperatureConverterController.convertInput("50", Locale.US).statusMessage());
    }

    @Test
    void marksTemperaturesOutsideNormalLimitsAsExtreme() {
        assertEquals("Temperature is extreme.",
                TemperatureConverterController.convertInput("-30.01", Locale.US).statusMessage());
        assertEquals("Temperature is extreme.",
                TemperatureConverterController.convertInput("50.01", Locale.US).statusMessage());
    }

    @Test
    void invalidInputClearsResultsAndShowsLocalizedError() {
        TemperatureConverterController.ConversionOutput output =
                TemperatureConverterController.convertInput("not a number", Locale.forLanguageTag("sv-SE"));

        assertEquals("", output.celsiusResult());
        assertEquals("", output.fahrenheitResult());
        assertEquals("Ange ett giltigt tal.", output.statusMessage());
    }

    @Test
    void blankAndNonFiniteInputAreRejected() {
        for (String input : new String[]{"", "   ", "NaN", "Infinity", "-Infinity"}) {
            TemperatureConverterController.ConversionOutput output =
                    TemperatureConverterController.convertInput(input, Locale.US);
            assertEquals("", output.celsiusResult(), input);
            assertEquals("", output.fahrenheitResult(), input);
            assertEquals("Please enter a valid number.", output.statusMessage(), input);
        }
    }

    @Test
    void mapsLanguageChoicesToLocalesAndBack() {
        Locale[] expected = {Locale.US, Locale.forLanguageTag("fi-FI"), Locale.JAPAN,
                Locale.forLanguageTag("sv-SE")};

        assertEquals(4, TemperatureConverterController.languageNames().size());
        assertEquals(java.util.List.of("English", "Suomi", "日本語", "Svenska"),
                TemperatureConverterController.languageNames());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], TemperatureConverterController.localeForLanguageIndex(i));
            assertEquals(i, TemperatureConverterController.languageIndex(expected[i]));
        }
        assertEquals(Locale.US, TemperatureConverterController.localeForLanguageIndex(-1));
        assertEquals(0, TemperatureConverterController.languageIndex(Locale.CANADA));
    }

    @Test
    void formatsNumbersInLocaleIndependentDecimalNotation() {
        Locale originalDefault = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);
            assertEquals("1.50", TemperatureConverterController.format(1.5));
            assertEquals("-1.50", TemperatureConverterController.format(-1.5));
        } finally {
            Locale.setDefault(originalDefault);
        }
    }
}
