import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Database;
import logic.DatabaseHandler;
import logic.controllers.Controller;
import logic.controllers.MainWindowController;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        String dbFilesLocation = "kirafan-lb-priority-sorter/src/main/resources/json";
        String modelPackageName = "domain.model";

        Database<GameCharacter> characterDatabase = new Database<>(dbFilesLocation, modelPackageName, "characters");
        Database<Series> seriesDatabase = new Database<>(dbFilesLocation, modelPackageName, "series");
        Database<Weapon> weaponDatabase = new Database<>(dbFilesLocation, modelPackageName, "weapons");
        DatabaseHandler databaseHandler = new DatabaseHandler(characterDatabase, seriesDatabase, weaponDatabase);

        URL url = getClass().getClassLoader().getResource("fxml/main.fxml");
        MainWindowController controller = new MainWindowController();
        controller.setDatabaseHandler(databaseHandler);
        String windowTitle = "Kirafan limit break priority sorter";
        Controller.openWindow(url, controller, windowTitle);


//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
//        loader.setController(controller);
//        Controller.openWindow(getClass().getClassLoader().getResource("fxml/main.fxml"), controller, "Kirafan limit");
//        Parent root = (Parent)loader.load();
//        window.setTitle("Kirafan limit break priority sorter");
//        window.setScene(new Scene(root));
//        window.show();
    }

    public static void main(String[] args) {
//        Database<Series> seriesDatabase = new Database("kirafan-lb-priority-sorter/src/main/resources", "domain.model", "series");
//        Series series = new Series("Hanayamata","ハナヤマタ", CreaStatus.INCOMPLETE);
//        seriesDatabase.createCollection();
//        seriesDatabase.insert(series);
        launch(args);
    }
}
