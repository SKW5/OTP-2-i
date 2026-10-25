import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Japanese");
        System.out.println("4. Swedish");
        System.out.print("Choose language: ");

        String choice = scanner.nextLine().trim();

        Locale locale = switch (choice) {
            case "1" -> Locale.US;
            case "2" -> Locale.forLanguageTag("fi-FI");
            case "3" -> Locale.forLanguageTag("ja-JP");
            case "4" -> Locale.forLanguageTag("sv-SE");
            default -> {
                System.out.println("Invalid defaulting to english");
                yield Locale.US;
            }
        };

        ResourceBundle messages =
                ResourceBundle.getBundle("messageBundle", locale);

        System.out.print(messages.getString("Temp") + " ");
        double temp;
        try {
            temp = scanner.nextDouble();
        } catch (InputMismatchException exception) {
            System.out.println(messages.getString("invalidNumber"));
            return;
        }
        System.out.println(messages.getString("inCelcius") + " " + TemperatureConverter.fahrenheitToCelsius(temp));
        System.out.println(messages.getString("inFahrenheit") + " " + TemperatureConverter.celsiusToFahrenheit(temp));
        if (TemperatureConverter.isExtremeTemperature(temp)) {
            System.out.println(messages.getString("extreme"));
        } else {
            System.out.println(messages.getString("normal"));
        }
    }
}
