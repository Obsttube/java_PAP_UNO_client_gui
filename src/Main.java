import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    static Stage stg;

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
        /*Parent root = FXMLLoader.load(getClass().getResource("playerView.fxml"));
        primaryStage.setTitle("Uno");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
