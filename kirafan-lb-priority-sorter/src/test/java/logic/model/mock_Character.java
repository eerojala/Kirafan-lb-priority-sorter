package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "characters", schemaVersion = "1.0")

public class mock_Character {
    @Id
    private String id;
    private String name;

    public mock_Character() {
    }

    public mock_Character(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
