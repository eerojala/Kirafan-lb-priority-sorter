package logic;

import com.google.common.io.Files;
import io.jsondb.Util;
import logic.model.Series;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static final String seriesJsonSecondLine = "{\"id\":\"1\",\"name\":\"series1\",\"status\":\"COMPLETE\"}";
    private String dbFilesLocation;
    private File dbFilesFolder;
    private File skillsJson;
    private File seriesJson;
    private Database seriesDatabase;

    private Series series1;
    private Series series2;
    private Series series3;

    public DatabaseTest() {
        dbFilesLocation = "src/test/resources/db test files";
        dbFilesFolder = new File(dbFilesLocation);
        skillsJson = new File(dbFilesFolder, "skills.json");
        seriesJson = new File(dbFilesFolder, "series.json");

        series1 = new Series("1", "Series1", Series.Status.COMPLETE);
        series2 = new Series("2", "Series2", Series.Status.INCOMPLETE);
        series3 = new Series("3", "Series3", Series.Status.NONE);
    }

    @BeforeEach
    void setUp() throws Exception {
//        dbFilesFolder.mkdir();
//        Files.copy(new File("src/test/resources/db test files/series.json"), seriesJson);
        seriesJson.createNewFile();
        seriesDatabase = new Database(dbFilesLocation, "logic.model", "series");
    }

    @AfterEach
    void tearDown() {
        skillsJson.delete();
    }

    @Test
    void testCreateCollection_createsNewJson() {
        File seriesJson = new File(dbFilesFolder, "skills.json");
        assertFalse(seriesJson.exists()); // file named skills.json should not exist yet

        Database db = new Database(dbFilesLocation, "logic.model", "skills");
        boolean result = db.createCollection();
        seriesJson = new File(dbFilesFolder, "skills.json");

        assertTrue(result); // Function should return true if json creation succeeded
        assertTrue(seriesJson.exists()); // file named skills should now exist after calling the function
        assertEquals(3, dbFilesFolder.list().length); // lock, series.json and skills.json
    }

    @Test
    void testCreateCollection_failsIfInvalidCollection() {
        assertTrue(dbFilesFolder.exists()); // the folder with the test jsons should exist

        Database db = new Database(dbFilesLocation, "logic.model", "nonexistant");
        boolean result = db.createCollection();

        assertFalse(result); // Function should return false if json creation failed
        assertEquals(2, dbFilesFolder.list().length); // directory should only contain the lock folder and series.json
    }

    @Test
    void testCreateCollection_doesNotOverwriteExistingJson() throws Exception {
        assertTrue(seriesJson.exists()); // series.json should already exist
        long sizeBeforeOperation = seriesJson.length();

        boolean result = seriesDatabase.createCollection();

        assertFalse(result); // Function should return false if json already exists

        Scanner scanner = new Scanner(seriesJson);
        scanner.nextLine();
        String secondLine = scanner.nextLine();

        assertEquals(seriesJsonSecondLine, secondLine); // series.json should not be overwritten
    }
}