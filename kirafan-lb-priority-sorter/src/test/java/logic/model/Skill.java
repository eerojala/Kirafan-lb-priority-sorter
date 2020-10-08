package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "skills", schemaVersion = "1.0")
public class Skill {
    @Id
    private String id;

    public Skill() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
