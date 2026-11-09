import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainLocaleTest {
    @Test
    void mapsSystemLanguagesToAvailableBundles() {
        assertEquals(Locale.US, Main.supportedLocale(Locale.forLanguageTag("en-GB")));
        assertEquals(Locale.forLanguageTag("fi-FI"), Main.supportedLocale(Locale.forLanguageTag("fi")));
        assertEquals(Locale.JAPAN, Main.supportedLocale(Locale.JAPAN));
        assertEquals(Locale.forLanguageTag("sv-SE"), Main.supportedLocale(Locale.forLanguageTag("sv")));
        assertEquals(Locale.forLanguageTag("ar"), Main.supportedLocale(Locale.forLanguageTag("ar")));
        assertEquals(Locale.US, Main.supportedLocale(Locale.ROOT));
    }
}
