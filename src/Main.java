import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Main extends Application {

    static Stage stg;

    static PlayerViewController playerViewController = null;

    static Socket socket = null;
    static BufferedInputStream bufferedInputStream;
    static ObjectInputStream objectInputStream;
    static ObjectOutputStream objectOutputStream;

    @Override
    public void start(Stage primaryStage){
        this.stg = primaryStage;

        try{
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setTitle("UNO");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
