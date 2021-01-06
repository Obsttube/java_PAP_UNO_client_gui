import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Controller {
    @FXML TextField textFieldLogin;
    @FXML TextField textFieldPassword;
    @FXML TextField textFieldGameName;
    @FXML Text textErrorLogin;
    @FXML Text textErrorRegister;
    @FXML Text textSuccessfulRegister;
    @FXML Text textWrongGameName;

    @FXML
    private void handleLogin(ActionEvent event) {
        event.consume();
        System.out.println("Logging in...");

        boolean successfulLogin = true;
        String login;
        String password;
        if(textFieldLogin.getText() != null && !textFieldLogin.getText().isEmpty() &&
                textFieldPassword.getText() != null && !textFieldPassword.getText().isEmpty()) {
            login = new String(textFieldLogin.getText());
            password = new String(textFieldPassword.getText());
        }
        else{
            textErrorLogin.setVisible(true);
            textErrorRegister.setVisible(false);
            textSuccessfulRegister.setVisible(false);
            return;
        }

        // successfulLogin = login(login, password);

        if(!successfulLogin) {
            textErrorLogin.setVisible(true);
            textErrorRegister.setVisible(false);
            textSuccessfulRegister.setVisible(false);
            return;
        }
        else{
            textErrorLogin.setVisible(false);
        }


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lobby.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("UNO");
            stage.setScene(new Scene(root));

            Scene scene = stage.getScene();
            ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");
            ArrayList<String> lobbyList = new ArrayList<String>();

            // lobbyList = getLobbyList();

            list.getItems().clear();
            for(String lobby : lobbyList){
                list.getItems().add(lobby);
            }
            list.getItems().add("test lobby");
            stage.show();
            Main.stg.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        event.consume();

        boolean successfulRegister = true;
        String login;
        String password;
        if(textFieldLogin.getText() != null && !textFieldLogin.getText().isEmpty() &&
                textFieldPassword.getText() != null && !textFieldPassword.getText().isEmpty()) {
            login = new String(textFieldLogin.getText());
            password = new String(textFieldPassword.getText());
        }
        else{
            textErrorRegister.setVisible(true);
            textSuccessfulRegister.setVisible(false);
            textErrorLogin.setVisible(false);
            return;
        }

        // successfulRegister = register(login, password);

        if(!successfulRegister) {
            textErrorRegister.setVisible(true);
            textSuccessfulRegister.setVisible(false);
            textErrorLogin.setVisible(false);
            return;
        }
        else{
            textSuccessfulRegister.setVisible(true);
            textErrorRegister.setVisible(false);
            textErrorLogin.setVisible(false);
        }
    }

    @FXML
    private void handleConnect(ActionEvent event) {
        event.consume();
        System.out.println("Connecting...");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        event.consume();
        System.out.println("Refreshing...");

        Node node = (Node) event.getSource();
        Scene scene = node.getScene();
        ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");
        ArrayList<String> lobbyList = new ArrayList<String>();

        // lobbyList = getLobbyList();

        list.getItems().clear();
        for(String lobby : lobbyList){
            list.getItems().add(lobby);
        }
    }

    @FXML
    private void handleCreateGame(ActionEvent event) {
        event.consume();
        System.out.println("Creating game...");

        String name;
        if(textFieldGameName.getText() != null && !textFieldGameName.getText().isEmpty()) {
            name = new String(textFieldGameName.getText());
            textWrongGameName.setVisible(false);
        }
        else{
            textWrongGameName.setVisible(true);
            return;
        }

        // createLobby(name);
        // TODO load game scrren
    }

}