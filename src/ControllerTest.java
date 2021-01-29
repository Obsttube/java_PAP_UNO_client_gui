import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    static Main app = new Main();

    @BeforeAll
    static void setScene(){
        app.main_test();
    }

    @Test
    void loginTesting(){
        assertEquals(Main.test_login, "test", "Unexpected login");
        assertEquals(Main.test_password, "test", "Unexpected password");
    }

    @Test
    void registerTest(){
        assertEquals(Main.test_login, "test", "Unexpected login");
        assertEquals(Main.test_password, "test", "Unexpected password");
    }

    @Test
    void createGameTest(){
        assertEquals(Main.test_game_name, "test", "Unexpected game name");
    }

    @Test
    void getListId(){
        assertEquals(Main.test_list_id, 1, "Unexpected index");
    }
}