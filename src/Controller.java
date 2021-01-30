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
    @FXML Text textErrorLogin;  // wrong user or password
    @FXML Text textErrorRegister;  // unable to register
    @FXML Text textSuccessfulRegister;  // registered successfully
    @FXML Text textWrongGameName;  // wrong game name
    @FXML Text textSelectLobby;  // lobby wasn't selected, please select one

    static List<Lobby> lobbyList = null;

    @FXML
    private void handleLogin(ActionEvent event) {  // login button pressed
        event.consume();
        System.out.println("Logging in...");

        boolean successfulLogin = true;
        String login;
        String password;
        if(textFieldLogin.getText() != null && !textFieldLogin.getText().isEmpty() &&
                textFieldPassword.getText() != null && !textFieldPassword.getText().isEmpty()) {  // check if login and password fields aren't empty
            login = new String(textFieldLogin.getText());  // load login from gui
            password = new String(textFieldPassword.getText());
        }
        else{  // show error message
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
            Main.socket = new Socket("127.0.0.1", 25566);  // connect to server at localhost:25566
            Main.socket.setSoTimeout(10*1000);  // set 10s timeout

            Thread.sleep(100);

            Main.bufferedInputStream = new BufferedInputStream(Main.socket.getInputStream());  // socket input (not the actual ObjectInputStream, because ObjectOutputStream has to be initialised first)
            OutputStream outputStream = Main.socket.getOutputStream();
            Main.objectOutputStream = new ObjectOutputStream(outputStream);  // output to socket

            // prepare request
            clientRequest = new ClientRequest(ClientRequest.RequestType.LOGIN);
            clientRequest.playerName = login;
            Main.objectOutputStream.writeObject(clientRequest);  // send it to the server

            Main.objectInputStream = new ObjectInputStream(Main.bufferedInputStream);  // socket input initialised
            serverRequest = (ServerRequest)Main.objectInputStream.readObject();  // read server response
            System.out.println(serverRequest.requestType); // LOGIN_SUCCESSFUL or LOGIN_UNSUCCESSFUL
            if (serverRequest.requestType == ServerRequest.RequestType.LOGIN_UNSUCCESSFUL)
                successfulLogin = false;

            if (successfulLogin){
                clientRequest = new ClientRequest(ClientRequest.RequestType.GET_LOBBY_LIST);
                Main.objectOutputStream.writeObject(clientRequest);  // send request to server

                serverRequest = (ServerRequest)Main.objectInputStream.readObject();
                System.out.println(serverRequest.requestType); // LOBBY_LIST
                lobbyList = serverRequest.lobbyList;

                try {  // change gui scene to lobby
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
                    // load new lobby list into ListView
                    for (Lobby lobby : lobbyList) {
                        System.out.println(lobby.id + " " + lobby.name);
                        list.getItems().add(lobby.name);
                    }

                    stage.show();  // show lobby scene
                    Main.stg.close();  // close last scene
                    Main.stg = stage;  // save reference to current (lobby) scene in Main.stage
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

        if(!successfulLogin) {  // show login error if login wasn't successful
            textErrorLogin.setVisible(true);
            textErrorRegister.setVisible(false);
            textSuccessfulRegister.setVisible(false);
        }else{
            textErrorLogin.setVisible(false);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {  // register button pressed
        event.consume();

        boolean successfulRegister = true;
        String login;
        String password;
        if(textFieldLogin.getText() != null && !textFieldLogin.getText().isEmpty() &&
                textFieldPassword.getText() != null && !textFieldPassword.getText().isEmpty()) {  // check if login and password fields aren't empty
            login = new String(textFieldLogin.getText());  // load login from gui
            password = new String(textFieldPassword.getText());
        } else{  // show error message
            textErrorRegister.setVisible(true);
            textSuccessfulRegister.setVisible(false);
            textErrorLogin.setVisible(false);
            return;
        }

        if (Main.test_start) {
            Main.test_login = login;
            Main.test_password = password;
        }

        if(!successfulRegister) {  // if registration wasn't successful
            textErrorRegister.setVisible(true);
            textSuccessfulRegister.setVisible(false);
            textErrorLogin.setVisible(false);
            return;
        } else{
            textSuccessfulRegister.setVisible(true);
            textErrorRegister.setVisible(false);
            textErrorLogin.setVisible(false);
        }
    }

    @FXML
    private void handleConnect(ActionEvent event) {  // connect button pressed
        event.consume();
        System.out.println("Connecting...");
        Node node = (Node) event.getSource();  // Node needed to get Scene
        Scene scene = node.getScene();
        @SuppressWarnings("unchecked")
        ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");  // get ListView
        int selectedItem = list.getSelectionModel().getSelectedIndex();  // get selected item index
        if (selectedItem == -1){  // if no item was selected
            System.out.println("Please select the lobby");
            textSelectLobby.setVisible(true);
            return;
        }
        else
            textSelectLobby.setVisible(false);
        
        System.out.println(selectedItem);

        if (Main.test_start) {
            Main.test_list_id = selectedItem;
        }

        try{
            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.JOIN_LOBBY);
            clientRequest.lobbyId = lobbyList.get(selectedItem).id;
            Main.objectOutputStream.writeObject(clientRequest);  // send JOIN_LOBBY request to server

            try {  // switch current stage into the actual game
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("playerView.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Main.playerViewController = (PlayerViewController) fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setTitle("UNO");
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();  // show game scene
                Main.stg.close();  // close last scene
                Main.stg = stage;  // save reference to current (game) scene in Main.stage
            } catch(Exception e) {
                e.printStackTrace();
            }

            SocketThread socketThread = new SocketThread();
            socketThread.start();  // start a second thread for handling communication with the server

        } catch (UnknownHostException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void refreshItems(Scene scene){  // get new list of lobbies
        @SuppressWarnings("unchecked")
        ListView<String> list = (ListView<String>) scene.lookup("#lobbyList");  // get ListView

        try{
            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.GET_LOBBY_LIST);
            Main.objectOutputStream.writeObject(clientRequest);  // send GET_LOBBY_LIST request to server

            ServerRequest serverRequest = (ServerRequest)Main.objectInputStream.readObject();  // get LOBBY_LIST from server
            System.out.println(serverRequest.requestType); // LOBBY_LIST
            lobbyList = serverRequest.lobbyList;

            list.getItems().clear();
            // load lobbies into the ListView
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
    private void handleRefresh(ActionEvent event) {  // refresh button pressed
        event.consume();
        System.out.println("Refreshing...");

        Node node = (Node) event.getSource();  // Node required to get Scene
        Scene scene = node.getScene();

        refreshItems(scene);  // get new list of lobbies

    }

    @FXML
    private void handleCreateGame(ActionEvent event) {  // create game/lobby button pressed
        event.consume();
        System.out.println("Creating game...");

        String name;
        if(textFieldGameName.getText() != null && !textFieldGameName.getText().isEmpty()) {  // check if game name field is empty
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
        // check if there is a game with the same name already
        for(Lobby lobby : lobbyList){
            if(name.equals(lobby.name)){
                textWrongGameName.setVisible(true);
                return;
            }
        }
        
        try{
            ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.CREATE_LOBBY);
            clientRequest.lobbyName = name;
            Main.objectOutputStream.writeObject(clientRequest);  // send CREATE_LOBBY request to the server
        } catch (IOException e){
            e.printStackTrace();
        }

        Scene scene = Main.stg.getScene();  // get current Scene from Main

        refreshItems(scene);  // get new list of lobbies

    }
}
