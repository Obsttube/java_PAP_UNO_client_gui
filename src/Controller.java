import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    private void handleLogin(ActionEvent event) {
        event.consume();
        System.out.println("Logging in...");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        event.consume();
        System.out.println("Registered.");
    }

}