import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;



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
    private Button play11;

    @FXML
    private Button play12;

    @FXML
    private Button play13;

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
    private ImageView card11;

    @FXML
    private ImageView card12;


    //static String[] playerCards = {"BLUE_THREE", "BLACK_WILD", "YELLOW_REVERSE", "RED_ZERO", "RED_ADD_TWO"};//todo player cards from server

    public void playCard(ActionEvent actionevent) { //returns string representing index of card or -1 if draw has been chosen
        final Node source = (Node) actionevent.getSource();
        String id = source.getId();
        String idNumber = "";
        for (int i=4; i<id.length(); i++)
            idNumber = idNumber + id.charAt(i);
        if (idNumber.equals("13") )
            idNumber = "-1";
        //todo player.playcard if index < player.numberofcards
        System.out.println(idNumber);

        ClientRequest clientRequest;

        if (SocketThread.gameStarted){
            int selectedCard = Integer.parseInt(idNumber);
            clientRequest = new ClientRequest(ClientRequest.RequestType.CHOOSE_CARD);
            clientRequest.choosenCardIndex = selectedCard;
            try{
                Main.objectOutputStream.writeObject(clientRequest);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else{
            SocketThread.gameStarted = true;
            clientRequest = new ClientRequest(ClientRequest.RequestType.CLICK_START);
            try{
                Main.objectOutputStream.writeObject(clientRequest);
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        // TODO deactivate cards

        renderPlayerCards();
    }

    public void setTopCard(Card card) {
        if (card == null){
            topCard.setImage(new Image("PNGs/large/card_back.png"));
            return;
        }
        String url = "PNGs/large/" + card.color + "_" + card.type +".png";
        topCard.setImage(new Image(url));
    }

    public void setButtonsState(boolean enabled){
        Button[] buttonsList = {play0, play1, play2, play3, play4, play5, play6, play7, play8, play9, play10,
            play11, play12};
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
            play13.setDisable(false);
        }else{
            for (Button button : buttonsList){
                button.setDisable(true);
            }
            play13.setDisable(true);
        }
    }

    public void renderPlayerCards () {
        ImageView[] cardsList = {card0, card1, card2, card3, card4, card5, card6, card7, card8, card9, card10,
            card11, card12};
        int i=0;
        
        List<Card> playerCards = SocketThread.playerCards;

        for (ImageView image : cardsList) {
            if (playerCards != null && i<playerCards.size() && i<13) {
                image.setImage(new Image("PNGs/large/" + playerCards.get(i).color + "_" + playerCards.get(i).type + ".png"));
            }else
                image.setImage(new Image("PNGs/large/card_back.png"));
            i++;
        }

        Card tableCard = SocketThread.tableCard;
        setTopCard(tableCard);//todo top card from server

    }

}
