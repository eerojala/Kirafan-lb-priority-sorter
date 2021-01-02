import domain.CreaStatus;
import domain.model.Series;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Database;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout.fxml"));
        window.setTitle("Hello world");
        window.setScene(new Scene(root, 300, 275));
        window.show();
    }

    public static void main(String[] args) {
//        Database<Series> seriesDatabase = new Database("kirafan-lb-priority-sorter/src/main/resources", "domain.model", "series");
//        Series series = new Series("Hanayamata","ハナヤマタ", CreaStatus.INCOMPLETE);
//        seriesDatabase.createCollection();
//        seriesDatabase.insert(series);
        launch(args);
    }
}
