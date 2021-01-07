import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChooseCard {
    @FXML
    private ImageView BLUE;

    @FXML
    private ImageView RED;

    @FXML
    private ImageView YELLOW;

    @FXML
    private ImageView GREEN;

    public void chooseCard(MouseEvent actionevent) {
        final Node source = (Node) actionevent.getSource();
        String id = source.getId();
        System.out.println(id);
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }
}
