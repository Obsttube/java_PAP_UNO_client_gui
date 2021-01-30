import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class SocketThread extends Thread {  // thread for asynchronous communication with the server

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
                if(Main.bufferedInputStream.available() > 0){  // if we received something from the server
                    ServerRequest serverRequest = (ServerRequest)Main.objectInputStream.readObject();  // read the received message
                    System.out.println(serverRequest.requestType);
                    switch(serverRequest.requestType){  // and interpret it
                        case YOUR_TURN: {  // it is our turn
                            pvc = Main.playerViewController;
                            if (pvc !=null)
                                pvc.setButtonsState(true);  // enable card buttons
                            break;
                        }
                        case CHOOSE_COLOR: {  // we have to choose card color (required when we play a black (wild) card)
                            Platform.runLater(() -> {
                                PlayerViewController pvc2 = Main.playerViewController;
                                if (pvc2 !=null){
                                    pvc2.chooseCard();
                                    pvc2.setButtonsState(false);  // disable card buttons
                                }
                            });
                            break;
                        }
                        case ILLEGAL_MOVE:  // this move wasn't legal
                            pvc = Main.playerViewController;
                            if (pvc !=null){
                                pvc.setButtonsState(true);  // enable card buttons
                                pvc.invalidMove(true);
                            }
                            break;
                        case LIST_OF_PLAYERS:
                            break;
                        case YOUR_CARDS:  // we received our set of cards from the servers (and cards that are on the table too)
                            tableCard = serverRequest.cardOnTable;
                            playerCards = serverRequest.cardsOnHand;
                            currentWildColor = serverRequest.currentWildColor;  // if the card on the table is wild
                            pvc = Main.playerViewController;
                            if (pvc !=null){
                                pvc.renderPlayerCards();
                                pvc.setButtonsState(false);  // disable card buttons
                            }
                            break;
                        default:
                            break;
                    }
                }
                Thread.sleep(100);  // wait 100ms untill next check if we received something
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
