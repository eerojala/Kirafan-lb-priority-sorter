package logic;

import com.google.common.io.Files;
import io.jsondb.Util;
import logic.model.Series;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private String dbFilesLocation;
    private File dbFilesFolder;
    private File seriesJson;
    private Database database;

    private Series series1;
    private Series series2;
    private Series series3;

    public DatabaseTest() {
        dbFilesLocation = "src/test/resources/db test files";
        dbFilesFolder = new File(dbFilesLocation);
        seriesJson = new File(dbFilesFolder, "series.json");

        series1 = new Series("1", "Series1", Series.Status.COMPLETE);
        series2 = new Series("2", "Series2", Series.Status.INCOMPLETE);
        series3 = new Series("3", "Series3", Series.Status.NONE);
    }

    @BeforeEach
    void setUp() throws Exception {
        dbFilesFolder.mkdir();
//        Files.copy(new File("src/test/resources/db test files/series.json"), seriesJson);
        database = new Database(dbFilesLocation, "logic.model", "series");
    }

    @AfterEach
    void tearDown() {
        Util.delete(dbFilesFolder);
    }

    @Test
    void testCreateCollection_failsIfInvalidCollection() {
        assertTrue(dbFilesFolder.exists()); // the folder with the test jsons should exist

        Database db = new Database(dbFilesLocation, "logic.model", "nonexistant");
        boolean result = db.createCollection();

        assertFalse(result); // Function should return false if json creation failed
        assertEquals(1, dbFilesFolder.list().length); // a folder named lock is created regardless if json creation succeeded or not
    }

    @Test
    void testCreateCollection_createsNewJson() {
        File seriesJson = new File(dbFilesFolder, "series.json");
        assertFalse(seriesJson.exists()); // file named series.json should not exist yet

        boolean result = database.createCollection();
        seriesJson = new File(dbFilesFolder, "series.json");

        assertTrue(result);
        assertTrue(seriesJson.exists()); // file named series should now exist after calling the function
        assertEquals(2, dbFilesFolder.list().length); // the other file is the lock folder which is created along with the json
    }
}