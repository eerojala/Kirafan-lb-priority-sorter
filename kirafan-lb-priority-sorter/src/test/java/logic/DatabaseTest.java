package logic;

import io.jsondb.Util;
import logic.model.mock_Character;
import logic.model.mock_Event;
import logic.model.mock_Series;
import logic.model.mock_Skill;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static final String dbFilesLocation = "src/test/resources/db test files";
    private static final String modelPackage = "logic.model";
    private static final String schemaVersion = "{\"schemaVersion\":\"1.0\"}";
    private static final String series1Entry = "{\"id\":\"1\",\"name\":\"series1\",\"status\":\"COMPLETE\"}";
    private static final String series2Entry = "{\"id\":\"2\",\"name\":\"series2\",\"status\":\"INCOMPLETE\"}";
    private static final String character1Entry = "{\"id\":\"1\",\"name\":\"character1\",\"series\":" + series1Entry + "}";
    private static final String character2Entry = "{\"id\":\"2\",\"name\":\"character2\",\"series\":" + series1Entry + "}";
    private static final String character3Entry = "{\"id\":\"3\",\"name\":\"character3\",\"series\":" + series2Entry + "}";

    private File dbFilesFolder;
    private File skillsJson;
    private File seriesJson;
    private File charactersJson;
    private File eventsJson;

    private Database<mock_Series> seriesDatabase;
    private Database<mock_Skill> skillDatabasee;
    private Database<mock_Character> characterDatabase;
    private Database<mock_Event> eventDatabase;

    private mock_Series series1;
    private mock_Series series2;
    private mock_Character character1;
    private mock_Character character2;
    private mock_Character character3;
    private mock_Skill skill1;


    public DatabaseTest() {
        dbFilesFolder = new File(dbFilesLocation);
        skillsJson = new File(dbFilesFolder, "skills.json");
        seriesJson = new File(dbFilesFolder, "series.json");
        charactersJson = new File(dbFilesFolder, "characters.json");
        eventsJson = new File(dbFilesFolder, "events.json");

        series1 = new mock_Series("1", "series1", mock_Series.Status.COMPLETE);
        series2 = new mock_Series("2", "series2", mock_Series.Status.INCOMPLETE);
        character1 = new mock_Character("1", "character1", series1);
        character2 = new mock_Character("2", "character2", series1);
        character3 = new mock_Character("3", "character3", series2);
        skill1 = new mock_Skill("1");
    }

    @BeforeEach
    public void setUp() throws Exception {
        dbFilesFolder.mkdir();

        seriesJson.createNewFile();
        writeLineIntoFile(seriesJson, schemaVersion);
        writeLineIntoFile(seriesJson, series1Entry);

        charactersJson.createNewFile();
        writeLineIntoFile(charactersJson, schemaVersion);
        writeLineIntoFile(charactersJson, character1Entry);
        writeLineIntoFile(charactersJson, character2Entry);
        writeLineIntoFile(charactersJson, character3Entry);

        eventsJson.createNewFile();
        writeLineIntoFile(eventsJson, schemaVersion);

        seriesDatabase = new Database<>(dbFilesLocation, modelPackage, "series");
        skillDatabasee = new Database<>(dbFilesLocation, modelPackage, "skills");
        characterDatabase = new Database<>(dbFilesLocation, modelPackage, "characters");
        eventDatabase = new Database<>(dbFilesLocation, modelPackage, "events");
    }

    @AfterEach
    public void tearDown() {
        Util.delete(dbFilesFolder);
    }

    @Test
    public void testCreateCollection_createsNewJson() {
        assertFalse(skillsJson.exists()); // file named skills.json should not exist yet

        boolean result = skillDatabasee.createCollection();

        assertTrue(result); // Function should return true if json creation succeeded
        assertTrue(skillsJson.exists()); // file named skills should now exist after calling the function
        assertEquals(5, dbFilesFolder.list().length); // lock folder and the json files series, skills, characters and events
    }

    @Test
    public void testCreateCollection_failsIfInvalidCollection() {
        assertTrue(dbFilesFolder.exists()); // the folder with the test jsons should exist

        Database db = new Database(dbFilesLocation, "logic.model", "nonexistant");
        boolean result = db.createCollection();
        assertFalse(result); // Function should return false if json creation failed
        assertEquals(4, dbFilesFolder.list().length); // directory should only contain the lock folder and the json files series, characters and events
    }

    @Test
    public void testCreateCollection_doesNotOverwriteExistingJson() throws Exception {
        assertTrue(seriesJson.exists()); // series.json should already exist

        boolean result = seriesDatabase.createCollection();
        assertFalse(result); // Function should return false if json already exists

        // series.json should not be modified in any way
        assertEquals(schemaVersion, getLineFromFile(seriesJson, 1));
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2));
        assertFalse(lineExistsInFile(seriesJson, 3));
    }

    @Test
    public void testInsert_insertsObjectIntoJson() throws Exception {
        assertTrue(seriesJson.exists()); // series.json should already exist
        assertEquals(schemaVersion, getLineFromFile(seriesJson, 1)); // the first line should be the schema version
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2)); // the second line should be the first entry

        boolean result = seriesDatabase.insert(series2);
        assertTrue(result); // function should return true on successful insert
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2)); // first entry should not be overwritten
        assertEquals(series2Entry, getLineFromFile(seriesJson, 3)); // the new entry is successfully inserted
        assertFalse(lineExistsInFile(seriesJson, 4)); // the new entry is not inserted multiple times
    }

    @Test
    public void testInsert_failsIfUncreatedCollection() throws Exception {
        assertFalse(skillsJson.exists()); // file named skills.json should not exist yet

        boolean result = skillDatabasee.insert(skill1);
        assertFalse(result); // function should return false on unsuccessful insert
        assertFalse(skillsJson.exists()); // skills.json should not be created with function insert

        // The existing json series.json should not be modified
        assertEquals(schemaVersion, getLineFromFile(seriesJson, 1));  // the first line should be the schema version
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2)); // the second line should be the first entry
        assertFalse(lineExistsInFile(seriesJson, 3)); // skill1 should not be inserted into series.json
    }

    @Test
    public void testInsert_failsIfObjectAlreadyExistsInJson() throws Exception {
        boolean result = seriesDatabase.insert(series1);
        assertFalse(result); // function should return false on unsuccessful insert
        assertEquals(schemaVersion, getLineFromFile(seriesJson, 1));  // the first line should be the schema version
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2)); // the second line should be the first entry
        assertFalse(lineExistsInFile(seriesJson, 3)); // series1 should not be inserted into series.json
    }

    @Test
    public void testFindById_returnsDesiredObjectWithCorrectId() {
        mock_Series fetchedSeries = seriesDatabase.findById("1");
        assertEquals(series1, fetchedSeries); // function returns correct object
    }

    @Test
    public void testFindById_returnsNullIfNoObjectFoundMatchingId() {
        mock_Series fetchedSeries = seriesDatabase.findById("asdf");
        assertNull(fetchedSeries);
    }

    @Test
    public void testFindAll_returnsAllObjects() throws Exception {
        // The file characters.json should already exist and contain the schema version and all the characters
        assertTrue(charactersJson.exists());
        assertEquals(schemaVersion, getLineFromFile(charactersJson, 1));
        assertEquals(character1Entry, getLineFromFile(charactersJson, 2));
        assertEquals(character2Entry, getLineFromFile(charactersJson, 3));
        assertEquals(character3Entry, getLineFromFile(charactersJson, 4));
        assertFalse(lineExistsInFile(charactersJson, 5));

        List<mock_Character> allCharacters = characterDatabase.findAll();
        // The returned list should have all the characters
        assertEquals(3, allCharacters.size());
        assertEquals(character1, allCharacters.get(0));
        assertEquals(character2, allCharacters.get(1));
        assertEquals(character3, allCharacters.get(2));
    }

    @Test
    public void testFindAll_returnsEmptyListIfJsonEmpty() throws Exception {
        // The file events.json exists and should only have the schema version header
        assertTrue(eventsJson.exists());
        assertEquals(schemaVersion, getLineFromFile(eventsJson, 1));
        assertFalse(lineExistsInFile(eventsJson, 2));

        List<mock_Event> fetchedEvents = eventDatabase.findAll();
        assertEquals(0, fetchedEvents.size()); // The function should return an empty list
    }

    @Test
    public void testFindAll_returnsNullIfNoJson() {
        assertFalse(skillsJson.exists()); // File skills.json should not exist

        List<mock_Skill> fetchedSkills = skillDatabasee.findAll();
        assertEquals(null, fetchedSkills); // The function should return null
    }

    private String getLineFromFile(File file, int lineNumber) throws Exception {
        Scanner scanner = new Scanner(file);
        String line = null;


        for (int i = 1; i <= lineNumber; i++) {
            line = scanner.nextLine();
        }

        scanner.close();

        return line;
    }

    private void writeLineIntoFile(File file, String line) throws Exception {
        FileWriter writer = new FileWriter(file, true);
        writer.write(line + "\n");
        writer.close();
    }

    private boolean lineExistsInFile(File file, int lineNumber) throws Exception {
        Scanner scanner = new Scanner(file);

        for (int i = 1; i <= lineNumber; i++) {
            if (scanner.hasNext()) {
                scanner.nextLine();
            } else {
                scanner.close();

                return false;
            }
        }

        scanner.close();

        return true;
    }
 }