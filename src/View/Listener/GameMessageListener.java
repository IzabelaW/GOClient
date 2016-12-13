package View.Listener;

/**
 * Created by Izabela on 2016-12-07.
 */
public interface GameMessageListener {

    void playerReceivedPermissionToMove();

    void playerMadeLegalMove();

    void playerMadeIllegalMoveKO();

    void playerMadeIllegalMoveSuicide();

    void playerMadeIllegalMoveOccupiedField();

    void opponentPassed();

    void opponentGaveUp();

    void updateBoard(String[][] updatedBoard);

}
