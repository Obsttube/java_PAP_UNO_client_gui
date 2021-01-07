import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class SocketThread extends Thread {

    static List<Card> playerCards = null;
    static Card tableCard = null; 
    static boolean gameStarted = false;
    static Card.Color currentWildColor = null;
    static Card lastSelectedCard = null;

    public void run(){
        gameStarted = false;
        try{
            PlayerViewController pvc;

            while(true){
                if(Main.bufferedInputStream.available() > 0){
                    ServerRequest serverRequest = (ServerRequest)Main.objectInputStream.readObject();
                    System.out.println(serverRequest.requestType);
                    switch(serverRequest.requestType){
                        case YOUR_TURN: {
                            pvc = Main.playerViewController;
                            if (pvc !=null)
                                pvc.setButtonsState(true);
                            break;
                        }
                        case CHOOSE_COLOR: {
                            Platform.runLater(() -> {
                                PlayerViewController pvc2 = Main.playerViewController;
                                if (pvc2 !=null){
                                    pvc2.chooseCard();
                                    pvc2.setButtonsState(false);
                                }
                            });
                            break;
                        }
                        case ILLEGAL_MOVE:
                            System.out.println("Illegal move! (It wasn't accepted)");
                            pvc = Main.playerViewController;
                            if (pvc !=null)
                                pvc.setButtonsState(true);
                            break;
                        case LIST_OF_PLAYERS:
                            /*List<Player> players2 = serverRequest.players;
                            System.out.print("{ ");
                            for (Player player : players2) {
                                System.out.print(player.name + " ");
                            }
                            System.out.println("}");
                            System.out.println("Write START to begin.");*/
                            break;
                        case YOUR_CARDS:
                            tableCard = serverRequest.cardOnTable;
                            playerCards = serverRequest.cardsOnHand;
                            currentWildColor = serverRequest.currentWildColor;
                            pvc = Main.playerViewController;
                            if (pvc !=null){
                                pvc.renderPlayerCards();
                                pvc.setButtonsState(false);
                            }
                            break;
                        default:
                            break;
                    }
                }
                Thread.sleep(100);
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (InterruptedException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
