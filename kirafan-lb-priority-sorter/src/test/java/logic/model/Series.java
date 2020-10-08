package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "series", schemaVersion = "1.0")
public class Series {
    public enum Status {
        COMPLETE, INCOMPLETE, NONE
    }

    @Id
    private String id;
    private String name;
    private Status status;

    public Series() {
    }

    public Series(String id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return id.equals(series.id) &&
                name.equals(series.name) &&
                status == series.status;
    }
}
