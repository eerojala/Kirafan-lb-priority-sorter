package domain.model;

import domain.CreaStatus;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.Date;
import java.util.Objects;

@Document(collection = "series", schemaVersion = "1.0")
public class Series {
    @Id
    private String id;
    private String name;
    private CreaStatus creaStatus;

    // Jackson requires a public constructor with no parameters
    public Series() {}

    public Series(String name, CreaStatus creaStatus, String id) {
        this.id = id;
        this.name = name;
        this.creaStatus = creaStatus;
    }

    public Series(String name, CreaStatus creaStatus) {
        this(name, creaStatus, new Date().toString());
    }

    public String getId() {
        return this.id;
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

    public CreaStatus getCreaStatus() {
        return creaStatus;
    }

    public void setCreaStatus(CreaStatus creaStatus) {
        this.creaStatus = creaStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(getId(), series.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(name);
        sb.append(" (Crea status: ").append(creaStatus).append(")");

        return sb.toString();
    }
}
