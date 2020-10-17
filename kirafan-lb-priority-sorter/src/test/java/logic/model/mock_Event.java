package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "events", schemaVersion = "1.0")
public class mock_Event {
    @Id
    private String id;

    public mock_Event() {
    }

    public mock_Event(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
