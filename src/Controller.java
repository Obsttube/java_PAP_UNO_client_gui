import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.net.Socket;
import java.net.UnknownHostException;
import javafx.scene.Node;

public class Controller {
    @FXML TextField textFieldLogin;
    @FXML TextField textFieldPassword;
    @FXML TextField textFieldGameName;
    @FXML Text textErrorLogin;
    @FXML Text textErrorRegister;
    @FXML Text textSuccessfulRegister;
    @FXML Text textWrongGameName;
    @FXML Text textSelectLobby;

    static List<Lobby> lobbyList = null;

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

        if (Main.test_start) {
            Main.test_login = login;
            Main.test_password = password;
        }

        ClientRequest clientRequest;
        ServerRequest serverRequest;

        try{
            Main.socket = new Socket("127.0.0.1", 25566);
            Main.socket.setSoTimeout(10*1000);

            Thread.sleep(100);

            Main.bufferedInputStream = new BufferedInputStream(Main.socket.getInputStream());
            OutputStream outputStream = Main.socket.getOutputStream();
            Main.objectOutputStream = new ObjectOutputStream(outputStream);

            clientRequest = new ClientRequest(ClientRequest.RequestType.LOGIN);
            clientRequest.playerName = login;
            Main.objectOutputStream.writeObject(clientRequest);

            Main.objectInputStream = new ObjectInputStream(Main.bufferedInputStream);
            serverRequest = (ServerRequest)Main.objectInputStream.readObject();
            System.out.println(serverRequest.requestType); // LOGIN_SUCCESSFUL
            if (serverRequest.requestType == ServerRequest.RequestType.LOGIN_UNSUCCESSFUL)
                successfulLogin = false;

            if (successfulLogin){
                clientRequest = new ClientRequest(ClientRequest.RequestType.GET_LOBBY_LIST);
                Main.objectOutputStream.writeObject(clientRequest);

                serverRequest = (ServerRequest)Main.objectInputStream.readObject();
                System.out.println(serverRequest.requestType); // LOBBY_LIST
                lobbyList = serverRequest.lobbyList;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lobby.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("UNO");
                    stage.setMaximized(true);
                    stage.setScene(new Scene(root));

                    Scene scene = stage.getScene();

                    @SuppressWarnings("unchecked")
                    ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");

                    list.getItems().clear();
                    for (Lobby lobby : lobbyList) {
                        System.out.println(lobby.id + " " + lobby.name);
                        list.getItems().add(lobby.name);
                    }

                    stage.show();
                    Main.stg.close();
                    Main.stg = stage;
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (UnknownHostException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (InterruptedException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if (Main.socket != null) {
                try {
                    Main.socket.close();
                } catch (IOException e) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        if(!successfulLogin) {
            textErrorLogin.setVisible(true);
            textErrorRegister.setVisible(false);
            textSuccessfulRegister.setVisible(false);
        }else{
            textErrorLogin.setVisible(false);
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

        if (Main.test_start) {
            Main.test_login = login;
            Main.test_password = password;
        }

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
        Node node = (Node) event.getSource();
        Scene scene = node.getScene();
        @SuppressWarnings("unchecked")
        ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");
        int selectedItem = list.getSelectionModel().getSelectedIndex();
        if (selectedItem == -1){
            System.out.println("Please select the lobby");
            textSelectLobby.setVisible(true);
            return;
        }
        else{
            textSelectLobby.setVisible(false);
        }
        System.out.println(selectedItem);

        if (Main.test_start) {
            Main.test_list_id = selectedItem;
        }

        try{

            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.JOIN_LOBBY);
            clientRequest.lobbyId = lobbyList.get(selectedItem).id;
            Main.objectOutputStream.writeObject(clientRequest);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("playerView.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Main.playerViewController = (PlayerViewController) fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setTitle("UNO");
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();
                Main.stg.close();
                Main.stg = stage;
            } catch(Exception e) {
                e.printStackTrace();
            }

            SocketThread socketThread = new SocketThread();
            socketThread.start();

        } catch (UnknownHostException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            // TODO
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void refreshItems(Scene scene){
        @SuppressWarnings("unchecked")
        ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");

        try{
            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.GET_LOBBY_LIST);
            Main.objectOutputStream.writeObject(clientRequest);

            ServerRequest serverRequest = (ServerRequest)Main.objectInputStream.readObject();
            System.out.println(serverRequest.requestType); // LOBBY_LIST
            lobbyList = serverRequest.lobbyList;

            list.getItems().clear();
            for (Lobby lobby : lobbyList) {
                System.out.println(lobby.id + " " + lobby.name);
                list.getItems().add(lobby.name);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        event.consume();
        System.out.println("Refreshing...");

        Node node = (Node) event.getSource();
        Scene scene = node.getScene();

        refreshItems(scene);

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

        if (Main.test_start) {
            Main.test_game_name = name;
        }

        ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();
        for(Lobby lobby : lobbyList){
            if(name.equals(lobby.name)){
                textWrongGameName.setVisible(true);
                return;
            }
        }

        try{
            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.CREATE_LOBBY);
            clientRequest.lobbyName = name;
            Main.objectOutputStream.writeObject(clientRequest);
        } catch (IOException e){
            e.printStackTrace();
        }

        Scene scene = Main.stg.getScene();

        refreshItems(scene);

    }

}