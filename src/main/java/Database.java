import org.h2.jdbcx.JdbcDataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

final class Database {
    private static final JdbcDataSource SOURCE = createDatabase();
    private Database() { }
    /** Opening the first embedded H2 connection creates the database file when absent. */
    private static JdbcDataSource createDatabase() {
        JdbcDataSource ds = new JdbcDataSource();
        String url = System.getProperty("temperature.db.url", "jdbc:h2:file:./data/temperature;AUTO_SERVER=TRUE");
        if (url.startsWith("jdbc:h2:file:")) {
            String file = url.substring("jdbc:h2:file:".length()).split(";", 2)[0];
            Path parent = Path.of(file).toAbsolutePath().getParent();
            try { if (parent != null) Files.createDirectories(parent); }
            catch (Exception e) { throw new IllegalStateException("Could not create database directory", e); }
        }
        ds.setURL(url);
        ds.setUser("sa"); ds.setPassword("");
        // H2's embedded mode creates the named database on connect; init.sql then creates its schema.
        try (Connection c = ds.getConnection(); InputStream in = Database.class.getResourceAsStream("/init.sql")) {
            if (in == null) throw new IllegalStateException("init.sql is missing");
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String statement : sql.split(";")) if (!statement.isBlank()) c.createStatement().execute(statement);
        } catch (Exception e) { throw new IllegalStateException("Could not initialize database", e); }
        return ds;
    }
    static Connection connection() throws SQLException { return SOURCE.getConnection(); }
}
