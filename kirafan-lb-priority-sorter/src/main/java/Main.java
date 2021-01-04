import domain.CreaStatus;
import domain.model.Series;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Database;
import logic.controllers.MainWindowController;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        Database<Series> seriesDatabase = new Database<>("kirafan-lb-priority-sorter/src/main/resources/json",
                "domain.model", "series");

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        MainWindowController controller = new MainWindowController();
        controller.setSeriesDatabase(seriesDatabase);
        loader.setController(controller);

        Parent root = (Parent)loader.load();
        window.setTitle("Kirafan limit break priority sorter");
        window.setScene(new Scene(root));
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
