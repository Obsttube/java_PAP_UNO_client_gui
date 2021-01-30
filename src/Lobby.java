import java.io.Serializable;

public class Lobby implements Serializable {  // serializable used for sending this object via sockets
    private static final long serialVersionUID = 2L;  // required for Serializable, incremented with every change of this class
    private static long next_available_id = 0;  // Next available id when creating a new lobby. Incremented every time a new lobby is created.
    public String id;
    public String name;
    public Lobby(String name) {
        this.id = "" + next_available_id;
        next_available_id++;
        this.name = name;
    }
}
