import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TemperatureConverterController {
    private static final List<String> LANGUAGE_NAMES = List.of("English", "Suomi", "日本語", "Svenska");

    @FXML private ComboBox<String> languageChoice;
    @FXML private TextField temperatureInput;
    @FXML private Label celsiusResult;
    @FXML private Label fahrenheitResult;
    @FXML private Label statusMessage;

    private final Locale locale;

    public TemperatureConverterController(Locale locale) {
        this.locale = locale;
    }

    @FXML
    private void initialize() {
        languageChoice.getItems().setAll(LANGUAGE_NAMES);
        languageChoice.getSelectionModel().select(languageIndex(locale));
        languageChoice.setOnAction(event -> {
            Locale selected = localeForLanguageIndex(languageChoice.getSelectionModel().getSelectedIndex());
            try {
                new Main().show((Stage) languageChoice.getScene().getWindow(), selected);
            } catch (IOException exception) {
                throw new IllegalStateException("Could not load localized view", exception);
            }
        });
    }

    @FXML
    private void convert() {
        ConversionOutput output = convertInput(temperatureInput.getText(), locale);
        celsiusResult.setText(output.celsiusResult());
        fahrenheitResult.setText(output.fahrenheitResult());
        statusMessage.setText(output.statusMessage());
    }

    static ConversionOutput convertInput(String input, Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle("messageBundle", locale);
        double temperature;
        try {
            temperature = Double.parseDouble(input.trim());
            if (!Double.isFinite(temperature)) {
                throw new NumberFormatException("Temperature must be finite");
            }
        } catch (NumberFormatException exception) {
            return new ConversionOutput("", "", messages.getString("invalidNumber"));
        }

        String celsius = messages.getString("inCelcius") + " " +
                format(TemperatureConverter.fahrenheitToCelsius(temperature));
        String fahrenheit = messages.getString("inFahrenheit") + " " +
                format(TemperatureConverter.celsiusToFahrenheit(temperature));
        String status = TemperatureConverter.isExtremeTemperature(temperature)
                ? messages.getString("extreme") : messages.getString("normal");
        return new ConversionOutput(celsius, fahrenheit, status);
    }

    static Locale localeForLanguageIndex(int index) {
        return switch (index) {
            case 1 -> Locale.forLanguageTag("fi-FI");
            case 2 -> Locale.JAPAN;
            case 3 -> Locale.forLanguageTag("sv-SE");
            default -> Locale.US;
        };
    }

    static List<String> languageNames() {
        return LANGUAGE_NAMES;
    }

    static String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    static int languageIndex(Locale locale) {
        return switch (locale.getLanguage()) {
            case "fi" -> 1;
            case "ja" -> 2;
            case "sv" -> 3;
            default -> 0;
        };
    }

    record ConversionOutput(String celsiusResult, String fahrenheitResult, String statusMessage) { }
}
