import java.sql.*;
import java.util.*;

public class UnitDao {
    public record Unit(String code, String symbol, String name, String description) {
        @Override public String toString() { return name + " (" + symbol + ")"; }
    }
    public List<Unit> findAll(String language) throws SQLException {
        String lang = language == null ? "en" : Locale.forLanguageTag(language).getLanguage();
        String sql = "SELECT u.code,u.symbol,COALESCE(l.unit_name,e.unit_name),COALESCE(l.description,e.description) " +
                "FROM measurement_unit u LEFT JOIN unit_localization l ON l.unit_code=u.code AND l.language_tag=? " +
                "LEFT JOIN unit_localization e ON e.unit_code=u.code AND e.language_tag='en' ORDER BY u.code";
        try (Connection c = Database.connection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lang);
            try (ResultSet rs = ps.executeQuery()) {
                List<Unit> result = new ArrayList<>();
                while (rs.next()) result.add(new Unit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                return result;
            }
        }
    }
    public void save(double input, String from, String to, double result, String language) throws SQLException {
        try (Connection c = Database.connection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO conversion_history(input_value,from_unit,to_unit,result_value,language_tag,converted_at) VALUES(?,?,?,?,?,CURRENT_TIMESTAMP)")) {
            ps.setDouble(1,input); ps.setString(2,from); ps.setString(3,to); ps.setDouble(4,result);
            ps.setString(5, Locale.forLanguageTag(language).getLanguage()); ps.executeUpdate();
        }
    }
    public List<String> lastFive(String language) throws SQLException {
        Map<String,String> names = new HashMap<>();
        for (Unit u : findAll(language)) names.put(u.code(), u.name());
        try (Connection c = Database.connection(); PreparedStatement ps = c.prepareStatement(
                "SELECT input_value,from_unit,to_unit,result_value,converted_at FROM conversion_history ORDER BY id DESC LIMIT 5");
             ResultSet rs = ps.executeQuery()) {
            List<String> rows = new ArrayList<>();
            while (rs.next()) rows.add(String.format(Locale.ROOT, "%.2f %s → %.2f %s (%s)", rs.getDouble(1),
                    names.getOrDefault(rs.getString(2), rs.getString(2)), rs.getDouble(4),
                    names.getOrDefault(rs.getString(3), rs.getString(3)), rs.getTimestamp(5).toLocalDateTime()));
            return rows;
        }
    }
}
