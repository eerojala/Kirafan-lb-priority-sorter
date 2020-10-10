package logic;

import io.jsondb.Util;
import logic.model.mock_Character;
import logic.model.mock_Series;
import logic.model.mock_Skill;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static final String dbFilesLocation = "src/test/resources/db test files";
    private static final String lockFilesLocation = dbFilesLocation + "/lock";
    private static final String modelPackage = "logic.model";
    private static final String schemaVersion = "{\"schemaVersion\":\"1.0\"}";
    private static final String series1Entry = "{\"id\":\"1\",\"name\":\"series1\",\"status\":\"COMPLETE\"}";
    private static final String series2Entry = "{\"id\":\"2\",\"name\":\"series2\",\"status\":\"INCOMPLETE\"}";
    private static final String character1Entry ="{\"id\":\"1\",\"name\":\"character1\"}";

    private File dbFilesFolder;
    private File skillsJson;
    private File seriesJson;
    private File seriesJsonLock;
    private File charactersJson;
    private File charactersJsonLock;
    private Database<mock_Series> seriesDatabase;
    private Database<mock_Skill> skillDatabasee;
    private Database<mock_Character> characterDatabase;

    private mock_Series series1;
    private mock_Series series2;
    private mock_Character character1;


    public DatabaseTest() {
        dbFilesFolder = new File(dbFilesLocation);
        skillsJson = new File(dbFilesFolder, "skills.json");
        seriesJson = new File(dbFilesFolder, "series.json");
        seriesJsonLock = new File(lockFilesLocation, "series.json.lock");
        charactersJson = new File(dbFilesFolder, "characters.json");
        charactersJsonLock = new File(lockFilesLocation, "characters.json.lock");

        series1 = new mock_Series("1", "series1", mock_Series.Status.COMPLETE);
        series2 = new mock_Series("2", "series2", mock_Series.Status.INCOMPLETE);
        character1 = new mock_Character("1", "character1");
    }

    @BeforeEach
    void setUp() throws Exception {
        dbFilesFolder.mkdir();

        seriesJson.createNewFile();
        writeLineIntoJson(seriesJson, schemaVersion);
        writeLineIntoJson(seriesJson, series1Entry);

//        charactersJson.createNewFile();
//        charactersJsonLock.createNewFile();

        seriesDatabase = new Database<>(dbFilesLocation, modelPackage, "series");
        skillDatabasee = new Database<>(dbFilesLocation, modelPackage, "skills");
        characterDatabase = new Database<>(dbFilesLocation, modelPackage, "characters");
    }

    @AfterEach
    void tearDown() {
        Util.delete(dbFilesFolder);
    }

    @Test
    void testCreateCollection_createsNewJson() {
        File seriesJson = new File(dbFilesFolder, "skills.json");
        assertFalse(seriesJson.exists()); // file named skills.json should not exist yet

        boolean result = skillDatabasee.createCollection();
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

        boolean result = seriesDatabase.createCollection();
        assertFalse(result); // Function should return false if json already exists

        String firstEntry = getLineFromJson(seriesJson, 2);  // First entry in the json is on the seoncd line
        assertEquals(series1Entry, firstEntry); // series.json should not be overwritten
    }

    @Test
    void testInsert_insertsObjectIntoJson() throws Exception {
        assertTrue(seriesJson.exists()); // series.json should already exist

        assertEquals(schemaVersion, getLineFromJson(seriesJson, 1)); // the first line should be the schema version
        assertEquals(series1Entry, getLineFromJson(seriesJson, 2)); // the second line should be the first entry


        boolean result = seriesDatabase.insert(series2);
        assertTrue(result); // function should return true on successful insert

        assertEquals(series1Entry, getLineFromJson(seriesJson, 2)); // first entry should not be overwritten
        assertEquals(series2Entry, getLineFromJson(seriesJson, 3)); // the new entry is successfully inserted
    }


    private String getLineFromJson(File file, int lineNumber) throws Exception {
        Scanner scanner = new Scanner(file);
        String line = null;


        for (int i = 1; i <= lineNumber; i++) {
            line = scanner.nextLine();
        }

        scanner.close();

        return line;
    }

    private void writeLineIntoJson(File file, String line) throws Exception {
        FileWriter writer = new FileWriter(file, true);
        writer.write(line + "\n");
        writer.close();
    }
 }