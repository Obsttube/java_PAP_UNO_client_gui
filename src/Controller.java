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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    static Socket socket = null;
    static BufferedInputStream bufferedInputStream;
    static OutputStream outputStream;
    static ObjectInputStream objectInputStream;
    static ObjectOutputStream objectOutputStream;
    static ClientRequest clientRequest;
    static ServerRequest serverRequest;

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

        try{
            socket = new Socket("127.0.0.1", 25566);
            socket.setSoTimeout(10*1000);
            
            Thread.sleep(100); // TODO

            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream); // TODO should be created before objectInputStream at both sides https://stackoverflow.com/a/60361496

            clientRequest = new ClientRequest(ClientRequest.RequestType.LOGIN);
            clientRequest.playerName = login;
            objectOutputStream.writeObject(clientRequest);

            objectInputStream = new ObjectInputStream(bufferedInputStream);
            serverRequest = (ServerRequest)objectInputStream.readObject();
            System.out.println(serverRequest.requestType); // LOGIN_SUCCESSFUL
            if (serverRequest.requestType == ServerRequest.RequestType.LOGIN_UNSUCCESSFUL)
                successfulLogin = false;

            if (successfulLogin){
                clientRequest = new ClientRequest(ClientRequest.RequestType.GET_LOBBY_LIST);
                objectOutputStream.writeObject(clientRequest);

                serverRequest = (ServerRequest)objectInputStream.readObject();
                System.out.println(serverRequest.requestType); // LOBBY_LIST
                List<Lobby> lobbyList = serverRequest.lobbyList;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lobby.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("UNO");
                    stage.setScene(new Scene(root));
        
                    Scene scene = stage.getScene();
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
            // TODO
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (InterruptedException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } finally {
            /*if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Logger.getLogger(class.getName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }*/
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
        // TODO Mateusz pobierz id wybranego lobby i ustal jego ID
        try{
            
            clientRequest = new ClientRequest(ClientRequest.RequestType.JOIN_LOBBY);
            clientRequest.lobbyId = "1";
            objectOutputStream.writeObject(clientRequest);

            // TODO będzie to inaczej zrealizowane, ale na razie zrobię tak, aby połączyć obie części

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("playerView.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("UNO");
                stage.setScene(new Scene(root));
    
                stage.show();
                Main.stg.close();
                Main.stg = stage;
            } catch(Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            // TODO
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        event.consume();
        System.out.println("Refreshing...");
        // jeszcze nie zaimplementowane po stronie serwera

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
        // jeszcze nie zaimplementowane po stronie serwera

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