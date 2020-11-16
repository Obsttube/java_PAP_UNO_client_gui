import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private void handleLogin(ActionEvent event) {
        event.consume();
        System.out.println("Logging in...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lobby.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("UNO");
            stage.setScene(new Scene(root, 1200, 800));

            Scene scene = stage.getScene();
            ListView list = (ListView) scene.lookup("#lobbyList");
            list.getItems().add("Lobby 1");
            list.getItems().add("Lobby 2");
            list.getItems().add("Lobby 2");

            stage.show();
            Main.stg.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        event.consume();
        System.out.println("Registered.");
    }

}