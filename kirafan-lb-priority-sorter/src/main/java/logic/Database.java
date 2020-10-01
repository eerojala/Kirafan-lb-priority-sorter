package logic;

import io.jsondb.JsonDBTemplate;

import java.util.Collections;
import java.util.List;

public class Database<T> {
    private JsonDBTemplate database;
    private String collectionName;

    public Database(String dbFilesLocation, String modelPackageName, String collectionName) {
        database = new JsonDBTemplate(dbFilesLocation, modelPackageName);
        this.collectionName = collectionName;
    }

    public boolean insert(T type) {
        try {
            database.insert(type);
            return true;
        } catch (Exception e) {
            if (!database.collectionExists(collectionName)) {
                database.createCollection(collectionName);
                return insert(type);
            }
            System.out.println(e);

            return false;
        }
    }

}
