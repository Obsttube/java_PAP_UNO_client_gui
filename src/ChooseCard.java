import java.io.IOException;
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
        ClientRequest clientRequest = new ClientRequest(ClientRequest.RequestType.CHOOSE_COLOR);
        Card.Color color = null;
        switch(id){
            case "RED":
                color = Card.Color.RED;
                break;
            case "YELLOW":
                color = Card.Color.YELLOW;
                break;
            case "GREEN":
                color = Card.Color.GREEN;
                break;
            case "BLUE":
                color = Card.Color.BLUE;
                break;
            default:
        }
        clientRequest.choosenColor = color;
        try{
            Main.objectOutputStream.writeObject(clientRequest);
        } catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
