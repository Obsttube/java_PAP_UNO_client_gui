import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class PlayerViewController {

    @FXML
    private Button play0;

    @FXML
    private Button play1;

    @FXML
    private Button play2;

    @FXML
    private Button play3;

    @FXML
    private Button play4;

    @FXML
    private Button play5;

    @FXML
    private Button play6;

    @FXML
    private Button play7;

    @FXML
    private Button play8;

    @FXML
    private Button play9;

    @FXML
    private Button play10;

    @FXML
    private Button drawCard;

    @FXML
    private Button forward;

    @FXML
    private Button backward;

    @FXML
    private ImageView topCard;

    @FXML
    private ImageView card0;

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView card3;

    @FXML
    private ImageView card4;

    @FXML
    private ImageView card5;

    @FXML
    private ImageView card6;

    @FXML
    private ImageView card7;

    @FXML
    private ImageView card8;

    @FXML
    private ImageView card9;

    @FXML
    private ImageView card10;

    @FXML
    private Text invalidMove;


    public void invalidMove(boolean show){  // set invailidMove visibility
        invalidMove.setVisible(show);
    }

    public void playCard(ActionEvent actionevent) { //returns string representing index of card or -1 if draw has been chosen
        final Node source = (Node) actionevent.getSource();
        String id = source.getId();

        ClientRequest clientRequest;

        if (SocketThread.gameStarted){
            int selectedCard = -1;
            if (!id.equals("drawCard"))  // if actual card was selected, not the drawCard button
                selectedCard = Integer.parseInt(id.substring(4));
            clientRequest = new ClientRequest(ClientRequest.RequestType.CHOOSE_CARD);  // create a CHOOSE_CARD request
            clientRequest.choosenCardIndex = selectedCard;  // set selected card
            SocketThread.lastSelectedCard = SocketThread.playerCards.get(selectedCard);
            try{
                Main.objectOutputStream.writeObject(clientRequest);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else{
            SocketThread.gameStarted = true;
            clientRequest = new ClientRequest(ClientRequest.RequestType.CLICK_START);  // send start request to the server
            try{
                Main.objectOutputStream.writeObject(clientRequest);
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        invalidMove(false);
        renderPlayerCards();
    }

    public void setTopCard(Card card) {  // set image of the top card at the table
        if (card == null){
            topCard.setImage(new Image("PNGs/large/card_back.png"));
            return;
        }
        Card.Color color = card.color;
        if (SocketThread.currentWildColor != null && (card.type == Card.Type.WILD || card.type == Card.Type.WILD_DRAW_FOUR))  // if it is a wild card
            color = SocketThread.currentWildColor;
        String url = "PNGs/large/" + color + "_" + card.type +".png";
        topCard.setImage(new Image(url));
    }

    public void setButtonsState(boolean enabled){  // disable or enable all available cards
        Button[] buttonsList = {play0, play1, play2, play3, play4, play5, play6, play7, play8, play9, play10};
        if (enabled){
            int i = 0;
            List<Card> playerCards = SocketThread.playerCards;
            for (Button button : buttonsList) {
                if (playerCards != null && i<playerCards.size()) {
                    button.setDisable(false);
                }else
                    button.setDisable(true);
                i++;
            }
            drawCard.setDisable(false);
        }else{
            for (Button button : buttonsList){
                button.setDisable(true);
            }
            drawCard.setDisable(true);
        }
    }

    public void renderPlayerCards () {  // show player cards on the screen
        ImageView[] cardsList = {card0, card1, card2, card3, card4, card5, card6, card7, card8, card9, card10,};
        int i=0;
        
        List<Card> playerCards = SocketThread.playerCards;

        for (ImageView image : cardsList) {
            if (playerCards != null && i<playerCards.size() && i<13) {
                Card currentCard = playerCards.get(i);
                image.setImage(new Image("PNGs/large/" + currentCard.color + "_" + currentCard.type + ".png"));
            }else
                image.setImage(new Image("PNGs/large/card_back.png"));
            i++;
        }

        Card tableCard = SocketThread.tableCard;
        setTopCard(tableCard);

    }

    public void chooseCard(){  // if it is a wild card and user has to choose card color
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chooseCard.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("UNO");
            stage.setScene(new Scene(root));
            ((ChooseCard) fxmlLoader.getController()).setImages();
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
