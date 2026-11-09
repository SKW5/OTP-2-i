import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class TemperatureConverterController {
    private static final List<String> LANGUAGE_NAMES = List.of("English", "Suomi", "Svenska", "日本語", "العربية");
    @FXML private ComboBox<String> languageChoice;
    @FXML private TextField temperatureInput;
    @FXML private ComboBox<UnitDao.Unit> fromUnitChoice, toUnitChoice;
    @FXML private Label celsiusResult, fahrenheitResult, statusMessage, historyTitle;
    @FXML private ListView<String> historyList;
    @FXML private Node root;
    private final Locale locale;
    private final UnitDao dao = new UnitDao();
    public TemperatureConverterController(Locale locale) { this.locale = locale; }

    @FXML private void initialize() {
        languageChoice.getItems().setAll(LANGUAGE_NAMES);
        languageChoice.getSelectionModel().select(languageIndex(locale));
        languageChoice.setOnAction(e -> {
            if (languageChoice.getScene() == null || languageChoice.getScene().getWindow() == null) return;
            try { new Main().show((Stage) languageChoice.getScene().getWindow(), localeForLanguageIndex(languageChoice.getSelectionModel().getSelectedIndex())); }
            catch (IOException ex) { throw new IllegalStateException("Could not load localized view", ex); }
        });
        root.setNodeOrientation("ar".equals(locale.getLanguage()) ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
        try {
            List<UnitDao.Unit> units = dao.findAll(locale.getLanguage());
            fromUnitChoice.setItems(FXCollections.observableArrayList(units));
            toUnitChoice.setItems(FXCollections.observableArrayList(units));
            fromUnitChoice.getSelectionModel().select(0); toUnitChoice.getSelectionModel().select(1);
            refreshHistory();
        } catch (SQLException ex) { statusMessage.setText(ex.getMessage()); }
    }

    @FXML private void convert() {
        ResourceBundle messages = ResourceBundle.getBundle("messageBundle", locale);
        UnitDao.Unit from = fromUnitChoice.getValue(), to = toUnitChoice.getValue();
        try {
            double input = Double.parseDouble(temperatureInput.getText().trim());
            if (!Double.isFinite(input)) throw new NumberFormatException();
            double value = TemperatureConverter.convert(input, from.code(), to.code());
            String result = format(value) + " " + to.name() + " (" + to.symbol() + ")";
            celsiusResult.setText(messages.getString("result") + ": " + result);
            fahrenheitResult.setText("");
            double celsius = "CELSIUS".equals(to.code()) ? value : TemperatureConverter.fahrenheitToCelsius(value);
            statusMessage.setText(TemperatureConverter.isExtremeTemperature(celsius) ? messages.getString("extreme") : messages.getString("normal"));
            dao.save(input, from.code(), to.code(), value, locale.getLanguage()); refreshHistory();
        } catch (NumberFormatException ex) { celsiusResult.setText(""); fahrenheitResult.setText(""); statusMessage.setText(messages.getString("invalidNumber")); }
        catch (SQLException ex) { statusMessage.setText(ex.getMessage()); }
    }
    private void refreshHistory() throws SQLException { historyList.getItems().setAll(dao.lastFive(locale.getLanguage())); }
    static ConversionOutput convertInput(String input, Locale locale) {
        ResourceBundle m = ResourceBundle.getBundle("messageBundle", locale);
        try { double t = Double.parseDouble(input.trim()); if (!Double.isFinite(t)) throw new NumberFormatException();
            return new ConversionOutput(m.getString("inCelcius") + " " + format(TemperatureConverter.fahrenheitToCelsius(t)), m.getString("inFahrenheit") + " " + format(TemperatureConverter.celsiusToFahrenheit(t)), TemperatureConverter.isExtremeTemperature(t) ? m.getString("extreme") : m.getString("normal"));
        } catch (NumberFormatException e) { return new ConversionOutput("", "", m.getString("invalidNumber")); }
    }
    static Locale localeForLanguageIndex(int i) { return switch(i) { case 1 -> Locale.forLanguageTag("fi-FI"); case 2 -> Locale.forLanguageTag("sv-SE"); case 3 -> Locale.JAPAN; case 4 -> Locale.forLanguageTag("ar"); default -> Locale.US; }; }
    static List<String> languageNames() { return LANGUAGE_NAMES; }
    static String format(double value) { return String.format(Locale.ROOT, "%.2f", value); }
    static int languageIndex(Locale l) { return switch(l.getLanguage()) { case "fi" -> 1; case "sv" -> 2; case "ja" -> 3; case "ar" -> 4; default -> 0; }; }
    record ConversionOutput(String celsiusResult, String fahrenheitResult, String statusMessage) { }
}
