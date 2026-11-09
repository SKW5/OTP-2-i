import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemperatureConverterTest {
    @Test
    void convertsCelsiusToFahrenheit() {
        assertEquals(32.0, TemperatureConverter.celsiusToFahrenheit(0), 1e-9);
        assertEquals(212.0, TemperatureConverter.celsiusToFahrenheit(100), 1e-9);
        assertEquals(-40.0, TemperatureConverter.celsiusToFahrenheit(-40), 1e-9);
        assertEquals(68.0, TemperatureConverter.celsiusToFahrenheit(20), 1e-9);
        assertEquals(-58.0, TemperatureConverter.celsiusToFahrenheit(-50), 1e-9);
    }

    @Test void convertsBetweenSelectedUnitsAndAllowsSameUnit() {
        assertEquals(212, TemperatureConverter.convert(100, "CELSIUS", "FAHRENHEIT"), 1e-9);
        assertEquals(0, TemperatureConverter.convert(32, "FAHRENHEIT", "CELSIUS"), 1e-9);
        assertEquals(12, TemperatureConverter.convert(12, "CELSIUS", "CELSIUS"), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> TemperatureConverter.convert(1, "KELVIN", "CELSIUS"));
    }

    @Test
    void convertsFahrenheitToCelsius() {
        assertEquals(0.0, TemperatureConverter.fahrenheitToCelsius(32), 1e-9);
        assertEquals(100.0, TemperatureConverter.fahrenheitToCelsius(212), 1e-9);
        assertEquals(-40.0, TemperatureConverter.fahrenheitToCelsius(-40), 1e-9);
        assertEquals(20.0, TemperatureConverter.fahrenheitToCelsius(68), 1e-9);
        assertEquals(-40.0, TemperatureConverter.fahrenheitToCelsius(-40), 1e-9);
    }

    @Test
    void recognizesTemperaturesOutsideInclusiveNormalRangeAsExtreme() {
        assertTrue(TemperatureConverter.isExtremeTemperature(-30.1));
        assertTrue(TemperatureConverter.isExtremeTemperature(50.1));
        assertFalse(TemperatureConverter.isExtremeTemperature(-30));
        assertFalse(TemperatureConverter.isExtremeTemperature(50));
        assertFalse(TemperatureConverter.isExtremeTemperature(20));
        assertTrue(TemperatureConverter.isExtremeTemperature(Double.POSITIVE_INFINITY));
        assertTrue(TemperatureConverter.isExtremeTemperature(Double.NEGATIVE_INFINITY));
    }
}
