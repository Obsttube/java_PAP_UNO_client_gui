import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
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

    public void setImages(){  // set proper images to ImageViews
        Card card = SocketThread.lastSelectedCard;
        // load proper card type and color
        BLUE.setImage(new Image("PNGs/large/BLUE_" + card.type +".png"));
        RED.setImage(new Image("PNGs/large/RED_" + card.type +".png"));
        YELLOW.setImage(new Image("PNGs/large/YELLOW_" + card.type +".png"));
        GREEN.setImage(new Image("PNGs/large/GREEN_" + card.type +".png"));
    }

    public void chooseCard(MouseEvent actionevent) {  // When user is required to choose card color. It is used when user wants to play a black card.
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
            Main.objectOutputStream.writeObject(clientRequest);  // send selected color to the server
        } catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();  // close the ChooseCard window
    }
}
