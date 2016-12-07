package View.Listener;

/**
 * Created by Izabela on 2016-12-07.
 */
public interface GameMessageListener {

    void playerReceivedPermissionToMove();

    void opponentPassed();

    void opponentGaveUp();

    void updateBoard(String[][] updatedBoard);

}
