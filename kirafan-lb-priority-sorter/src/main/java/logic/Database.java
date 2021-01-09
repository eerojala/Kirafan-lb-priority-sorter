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

    public boolean createCollection() {
        try {
            database.createCollection(collectionName);

            return true;
        } catch (Exception e) {
            System.out.println(e);

            return false;
        }
    }

    public List<T> find (String jxQuery) {
        try {
            return database.find(jxQuery, collectionName);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public T findById(String id) {
        try {
            // returns null if no object found matching id
            return database.findById(id, collectionName);
        } catch (Exception e) {
            System.out.println(e);

            return null;
        }
    }

    public List<T> findAll() {
        try {
            return database.findAll(collectionName);
        } catch (Exception e) {
            System.out.println(e);

            return null;
        }
    }

    public boolean insert(T t) {
        try {
            database.insert(t);

            return true;
        } catch (Exception e) {
            System.out.println(e);

            return false;
        }
    }

    public boolean update(T t) {
        try {
            database.save(t, this.collectionName);

            return true;
        } catch (Exception e) {
            System.out.println(e);

            return false;
        }
    }

    public boolean remove(T t) {
        try {
            database.remove(t, this.collectionName);

            return true;
        } catch (Exception e) {
            System.out.println(e);

            return false;
        }
    }

}
