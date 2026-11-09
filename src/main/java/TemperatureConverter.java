public class TemperatureConverter {
    public static double celsiusToFahrenheit(double celsius) { return celsius * 9 / 5 + 32; }
    public static double fahrenheitToCelsius(double fahrenheit) { return (fahrenheit - 32) * 5 / 9; }
    public static double convert(double value, String from, String to) {
        if (from.equals(to)) return value;
        if ("CELSIUS".equals(from) && "FAHRENHEIT".equals(to)) return celsiusToFahrenheit(value);
        if ("FAHRENHEIT".equals(from) && "CELSIUS".equals(to)) return fahrenheitToCelsius(value);
        throw new IllegalArgumentException("Unsupported temperature unit");
    }
    public static boolean isExtremeTemperature(double celsius) { return celsius < -30 || celsius > 50; }
}
