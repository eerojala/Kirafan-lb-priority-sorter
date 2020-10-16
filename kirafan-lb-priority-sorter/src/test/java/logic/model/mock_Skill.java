package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "skills", schemaVersion = "1.0")
public class mock_Skill {
    @Id
    private String id;

    public mock_Skill() {
    }

    public mock_Skill(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
