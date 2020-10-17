package logic.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.Objects;

@Document(collection = "characters", schemaVersion = "1.0")

public class mock_Character {
    @Id
    private String id;
    private String name;
    private mock_Series series;

    public mock_Character() {
    }

    public mock_Character(String id, String name, mock_Series series) {
        this.id = id;
        this.name = name;
        this.series = series;
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

    public mock_Series getSeries() {
        return series;
    }

    public void setSeries(mock_Series series) {
        this.series = series;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        mock_Character that = (mock_Character) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                series.equals(that.series);
    }
}
