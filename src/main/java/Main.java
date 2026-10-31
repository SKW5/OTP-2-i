import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        show(stage, supportedLocale(Locale.getDefault()));
        stage.show();
    }

    static Locale supportedLocale(Locale locale) {
        return switch (locale.getLanguage()) {
            case "fi" -> Locale.forLanguageTag("fi-FI");
            case "ja" -> Locale.JAPAN;
            case "sv" -> Locale.forLanguageTag("sv-SE");
            default -> Locale.US;
        };
    }

    void show(Stage stage, Locale locale) throws IOException {
        ResourceBundle messages = ResourceBundle.getBundle("messageBundle", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/temperature-converter.fxml"), messages);
        loader.setControllerFactory(type -> new TemperatureConverterController(locale));
        stage.setTitle(messages.getString("title"));
        stage.setScene(new Scene(loader.load()));
        stage.setMinWidth(420);
        stage.setMinHeight(300);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
