public class TemperatureConverter {
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
    public static boolean isExtremeTemperature(double celcius) {
        if (celcius < -30 || celcius > 50) {
            return true;
        } else {
            return false;
        }
    }
}
