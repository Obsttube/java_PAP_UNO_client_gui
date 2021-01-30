import java.io.Serializable;
import java.util.List;

public class ServerRequest implements Serializable{  // serializable used for sending this object via sockets
    private static final long serialVersionUID = 3L;  // required for Serializable, incremented with every change of this class

    public final RequestType requestType;  // type of request sent to the client

    public enum RequestType {
        LOGIN_SUCCESSFUL,
        LOGIN_UNSUCCESSFUL,
        LOBBY_LIST,
        LIST_OF_PLAYERS,
        YOUR_CARDS,
        YOUR_TURN,
        ILLEGAL_MOVE, // tells the client to repeat last move, which was illegal
        CHOOSE_COLOR
    }

    /* Request fields */
    public List<Lobby> lobbyList;
    public List<Player> players;
    public List<Card> cardsOnHand;
    public Card cardOnTable;
    public Card.Color currentWildColor;  // color of current Wild card, if there is one on the table

    public ServerRequest(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
