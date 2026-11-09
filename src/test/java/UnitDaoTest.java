import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UnitDaoTest {
    @Test void returnsLocalizedUnitsAndFallsBackToEnglish() throws Exception {
        System.setProperty("temperature.db.url", "jdbc:h2:mem:units;DB_CLOSE_DELAY=-1");
        UnitDao dao = new UnitDao();
        assertEquals("摂氏", dao.findAll("ja").get(0).name());
        List<UnitDao.Unit> fallback = dao.findAll("fr");
        assertEquals("Celsius", fallback.get(0).name());
        assertEquals("Degrees Celsius", fallback.get(0).description());
        dao.save(32, "FAHRENHEIT", "CELSIUS", 0, "ja");
        dao.save(0, "CELSIUS", "FAHRENHEIT", 32, "ja");
        assertEquals(2, dao.lastFive("ja").size());
        assertTrue(dao.lastFive("ja").get(0).contains("華氏"));
    }
}
