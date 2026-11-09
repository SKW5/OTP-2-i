import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationResourcesTest {
    private static final String FXML_NAMESPACE = "http://javafx.com/fxml/1";
    private static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.US,
            Locale.forLanguageTag("fi-FI"), Locale.JAPAN, Locale.forLanguageTag("sv-SE"), Locale.forLanguageTag("ar"));
    private static final Set<String> REQUIRED_MESSAGE_KEYS = Set.of(
            "title", "Temp", "invalidNumber", "inCelcius", "inFahrenheit",
                "extreme", "normal", "convert", "to", "result", "history");

    @Test
    void everySupportedLocaleHasAllMessagesUsedByTheView() {
        for (Locale locale : SUPPORTED_LOCALES) {
            ResourceBundle bundle = ResourceBundle.getBundle("messageBundle", locale);
            assertEquals(locale, bundle.getLocale(), "Missing exact bundle for " + locale);
            for (String key : REQUIRED_MESSAGE_KEYS) {
                assertTrue(bundle.containsKey(key), locale + " is missing " + key);
                assertFalse(bundle.getString(key).isBlank(), locale + " has a blank " + key);
            }
        }
    }

    @Test
    void fxmlLoadsAsWellFormedXmlAndReferencesControllerFields() throws Exception {
        Document document;
        try (InputStream input = getClass().getResourceAsStream("/temperature-converter.fxml")) {
            assertNotNull(input, "FXML resource is missing");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            document = factory.newDocumentBuilder().parse(input);
        }

        Element root = document.getDocumentElement();
        assertEquals("TemperatureConverterController", root.getAttributeNS(FXML_NAMESPACE, "controller"));

        Set<String> fieldIds = IntStream.range(0, document.getElementsByTagName("*").getLength())
                .mapToObj(index -> (Element) document.getElementsByTagName("*").item(index))
                .map(element -> element.getAttributeNS(FXML_NAMESPACE, "id"))
                .filter(id -> !id.isBlank())
                .collect(Collectors.toSet());
        assertTrue(fieldIds.containsAll(Set.of("languageChoice", "temperatureInput", "fromUnitChoice", "toUnitChoice",
                "celsiusResult", "fahrenheitResult", "statusMessage", "historyList")));

        NodeList actionNodes = document.getElementsByTagName("*");
        long convertActions = IntStream.range(0, actionNodes.getLength())
                .mapToObj(index -> (Element) actionNodes.item(index))
                .filter(element -> "#convert".equals(element.getAttribute("onAction")))
                .count();
        assertEquals(2, convertActions, "Both Enter and the Convert button should invoke convert");
    }
}
