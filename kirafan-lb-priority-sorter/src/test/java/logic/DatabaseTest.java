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
    private static final String character1Entry = String.format("{\"id\":\"1\",\"name\":\"character1\",\"series\":%s}", series1Entry);
    private static final String character2Entry = String.format("{\"id\":\"2\",\"name\":\"character2\",\"series\":%s}", series2Entry);
    private static final String character3Entry = String.format("{\"id\":\"3\",\"name\":\"character3\",\"series\":%s}", series1Entry);

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

        series1 = new mock_Series("1", "series1", mock_Series.Status.COMPLETE);
        series2 = new mock_Series("2", "series2", mock_Series.Status.INCOMPLETE);
        character1 = new mock_Character("1", "character1", series1);
        character2 = new mock_Character("2", "character2", series2);
        character3 = new mock_Character("3", "character3", series1);
        skill1 = new mock_Skill("1");
    }

    @AfterEach
    public void tearDown() {
        Util.delete(dbFilesFolder);
    }

    @Test
    public void testCreateCollection_createsNewJson() {
        assertFalse(skillsJson.exists()); // file named skills.json should not exist yet
        assertEquals(4, dbFilesFolder.list().length); // lock folder and the json files series, characters and events

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
        defaultSeriesJsonAsserts(); // series.json should already exist

        boolean result = seriesDatabase.createCollection();
        assertFalse(result); // Function should return false if json already exists
        defaultSeriesJsonAsserts(); // series.json should not be modified in any way
    }

    @Test
    public void testInsert_insertsObjectIntoJson() throws Exception {
        defaultSeriesJsonAsserts(); // series.json should already exist

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
        // The existing json files should not be modified
        defaultSeriesJsonAsserts();
        defaultCharactersJsonAsserts();
        defaultEventsJsonAsserts();
    }

    @Test
    public void testInsert_failsIfObjectAlreadyExistsInJson() throws Exception {
        boolean result = seriesDatabase.insert(series1);
        assertFalse(result); // function should return false on unsuccessful insert
        defaultSeriesJsonAsserts(); // series 1 should not be modified or inserted again in series.json
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
        defaultCharactersJsonAsserts(); // characters.json should already exist

        List<mock_Character> allCharacters = characterDatabase.findAll();
        // The returned list should have all the characters
        assertEquals(3, allCharacters.size());
        assertEquals(character1, allCharacters.get(0));
        assertEquals(character2, allCharacters.get(1));
        assertEquals(character3, allCharacters.get(2));
    }

    @Test
    public void testFindAll_returnsEmptyListIfJsonEmpty() throws Exception {
        defaultEventsJsonAsserts(); // The file events.json exists and should only have the schema version header

        List<mock_Event> fetchedEvents = eventDatabase.findAll();
        assertEquals(0, fetchedEvents.size()); // The function should return an empty list
    }

    @Test
    public void testFindAll_returnsNullIfNoJson() {
        assertFalse(skillsJson.exists()); // File skills.json should not exist

        List<mock_Skill> fetchedSkills = skillDatabasee.findAll();
        assertNull(fetchedSkills);
    }

    @Test
    public void update_updatesGivenObjectSuccessfully() throws Exception {
        defaultCharactersJsonAsserts(); // characters.json should already exist with correct content

        character2.setName("new_name");
        boolean result = characterDatabase.update(character2);
        String updatedCharacter2Entry = String.format("{\"id\":\"2\",\"name\":\"new_name\",\"series\":%s}", series2Entry);
        assertTrue(result); // Function should return true on a successful update
        assertEquals(updatedCharacter2Entry, getLineFromFile(charactersJson, 3)); // character2 should have the new name and the other variables should be unaffected
        // character1 and character3 should be unaffected and character2 should not have a duplicate entry
        assertEquals(character1Entry, getLineFromFile(charactersJson, 2));
        assertEquals(character3Entry, getLineFromFile(charactersJson, 4));
        assertFalse(lineExistsInFile(charactersJson, 5));
    }

    @Test
    public void update_doesNotCreateAnEntryForANewObject() throws Exception {
        defaultSeriesJsonAsserts(); // series.json should already exist with correct content

        boolean result = seriesDatabase.update(series2);
        assertFalse(result); // function should return false if attempting to update an object which is not in the json file yet
        defaultSeriesJsonAsserts(); // series.json should not be modified in any way
    }

    @Test
    public void remove_removesGivenObjectSuccessfully() throws Exception {
        defaultCharactersJsonAsserts(); // characters.json should already exist with correct content

        boolean result = characterDatabase.remove(character2);
        assertTrue(result); // Function should return true on successful removal
        assertEquals(schemaVersion, getLineFromFile(charactersJson, 1));
        assertEquals(character1Entry, getLineFromFile(charactersJson, 2));
        assertEquals(character3Entry, getLineFromFile(charactersJson, 3));
    }

    @Test
    public void remove_doesNotAffectJsonIfTryingToRemoveNonExistentObject() throws Exception {
        defaultSeriesJsonAsserts(); // series.json should already exist with correct content

        boolean result = seriesDatabase.remove(series2);
        assertFalse(result); // Function should return false if trying to remove an object which is not in the Json
        defaultSeriesJsonAsserts(); // series.json should be unaffected after failed remove operation
    }

    @Test
    public void find_returnsAListOfMatchingObjects() throws Exception {
        defaultCharactersJsonAsserts();

        String jxQuery = "/.[series/name='series1']";
        List<mock_Character> results = characterDatabase.find(jxQuery);
        // Function should find the two characters (character1 and character3) which have series1 as their series
        assertEquals(2, results.size());
        assertTrue(results.contains(character1));
        assertTrue(results.contains(character3));
    }

    @Test
    public void find_returnsAnEmptyListIfNoMatchingObjectsFound() throws Exception {
        defaultSeriesJsonAsserts();

        String jxQuery = "/.[name='series2']";
        List<mock_Series> results = seriesDatabase.find(jxQuery);
        assertTrue(results.isEmpty()); // Function should return an empty list if no object found matching query
    }

    @Test
    public void find_returnsNullIfNoJson() {
        assertFalse(skillsJson.exists());

        String jxQuery = "/.[id='1']";
        List<mock_Skill> results = skillDatabasee.find(jxQuery);
        assertNull(results);
    }

    private void writeLineIntoFile(File file, String line) throws Exception {
        FileWriter writer = new FileWriter(file, true);
        writer.write(line + "\n");
        writer.close();
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

    private void defaultSeriesJsonAsserts() throws Exception {
        assertTrue(seriesJson.exists()); // series.json should exist
        assertEquals(schemaVersion, getLineFromFile(seriesJson, 1)); // First line should be the schema version
        assertEquals(series1Entry, getLineFromFile(seriesJson, 2)); // Second line should be series1
        assertFalse(lineExistsInFile(seriesJson, 3)); // There should be no lines after line 2
    }

    private void defaultCharactersJsonAsserts() throws Exception {
        assertTrue(charactersJson.exists()); // characters.json should exist
        assertEquals(schemaVersion, getLineFromFile(charactersJson, 1)); // First line should be the schema version
        assertEquals(character1Entry, getLineFromFile(charactersJson, 2)); // Second line should be character1
        assertEquals(character2Entry, getLineFromFile(charactersJson, 3)); // Third line should be character2
        assertEquals(character3Entry, getLineFromFile(charactersJson, 4)); // Fourth lien should be character3
        assertFalse(lineExistsInFile(charactersJson, 5)); // There should be no lines after line 4
    }

    private void defaultEventsJsonAsserts() throws Exception {
        assertTrue(eventsJson.exists()); // events.json should exist
        assertEquals(schemaVersion, getLineFromFile(eventsJson, 1)); // First line should be the schema version
        assertFalse(lineExistsInFile(eventsJson, 2)); // There should be no lines after line 1
    }
 }