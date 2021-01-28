import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.Database;
import logic.DatabaseHandler;
import logic.controllers.Controller;
import logic.controllers.MainWindowController;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        String dbFilesLocation = System.getProperty("user.dir") + "/json";
        String modelPackageName = "domain.model";

        Database<GameCharacter> characterDatabase = new Database<>(dbFilesLocation, modelPackageName, "characters");
        Database<GameEvent> eventDatabase = new Database<>(dbFilesLocation, modelPackageName, "events");
        Database<Series> seriesDatabase = new Database<>(dbFilesLocation, modelPackageName, "series");
        Database<Weapon> weaponDatabase = new Database<>(dbFilesLocation, modelPackageName, "weapons");
        DatabaseHandler databaseHandler = new DatabaseHandler(characterDatabase, eventDatabase, seriesDatabase, weaponDatabase);
        databaseHandler.initializeCollections();
        databaseHandler.initializeEvent();

        URL url = getClass().getClassLoader().getResource("fxml/main.fxml");
        MainWindowController controller = new MainWindowController();
        controller.setDatabaseHandler(databaseHandler);
        String windowTitle = "Kirafan limit break priority sorter";
        Controller.openWindow(url, controller, windowTitle);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
