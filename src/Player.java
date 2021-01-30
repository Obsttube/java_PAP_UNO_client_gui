import java.io.Serializable;

public class Player implements Serializable {  // serializable used for sending this object via sockets
    private static final long serialVersionUID = 2L;  // required for Serializable, incremented with every change of this class
    public String name;
    public int numberOfCards = 0;
    public Player(String name) {
        this.name = name;
    }
    public Player(String name, int numberOfCards) {
        this(name);
        this.numberOfCards = numberOfCards;
    }
}
