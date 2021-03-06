import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Main extends Application {

    static Stage stg;  // JavaFX stages

    static PlayerViewController playerViewController = null;  // reference to playerViewController used for making calls from another thread

    static Socket socket = null;
    static BufferedInputStream bufferedInputStream;  // for sockets
    static ObjectInputStream objectInputStream;  // for sockets
    static ObjectOutputStream objectOutputStream;  // for sockets

    static boolean test_start = false;
    static String test_login, test_password, test_game_name;
    static int test_list_id;

    @Override
    public void start(Stage primaryStage){
        stg = primaryStage;

        try{
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setTitle("UNO");
            primaryStage.setScene(new Scene(root));
            primaryStage.setMaximized(true);
            primaryStage.show();  // show login screen
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void main_test() { test_start = true; launch(); }  // for unit testing
}
