# Temperature converter

## Localization

The interface uses JavaFX FXML with `ResourceBundle` property files (`messageBundle_<language>_<region>.properties`, plus `messageBundle_ar.properties`). This keeps presentation strings outside the controller and lets JavaFX resolve translated labels while loading the view. Selecting a language creates a new localized FXML scene at runtime. The root node switches to right-to-left orientation for Arabic. Property files and SQL seed data are UTF-8.

Unit names and descriptions are stored in `unit_localization`, keyed by unit and ISO language code. `UnitDao` joins the requested translation and falls back to English when it is absent. On startup, opening the embedded H2 JDBC URL creates the database file if needed; `src/main/resources/init.sql` then creates and seeds its tables. H2 embedded mode creates databases on connection rather than through a `CREATE DATABASE` SQL statement. Set `-Dtemperature.db.url=...` to override the default file database URL.
