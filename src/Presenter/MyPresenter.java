package Presenter;

import Usecase.Game;
import View.Listener.GameMessageListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Enum Singleton class.
 * It Mediates between View(GUI) and Usecase(Game - concrete communication with server class).
 */
public enum MyPresenter {

    /**
     *  Instance of MyPresenter.
     */
    INSTANCE;

    /**
     *  Field of Game class, needed to communication with server (to use it's methods).
     */
    private Game game;

    private MyPresenter(){

        try {
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends player's login to server.
     * @param login - player's login to send
     */
    public void sendLogin(String login){
        game.sendResponse(login);
    }

    /**
     * Sends info to server, that BOT was chosen.
     */
    public void botChosen(){
        game.sendResponse("BOT");
    }

    /**
     * Sends info to server, that HUMAN was chosen.
     */
    public void humanChosen(){
        game.sendResponse("HUMAN");
    }

    /**
     * Sends info to server, that NEW ROOM was chosen.
     */
    public void newRoomChosen(){
        game.sendResponse("NEW");
    }

    /**
     * Sends info to server, that EXISTING ROOM was chosen.
     */
    public void existingRoomChosen(){
        game.sendResponse("EXISTING");
    }

    /**
     * Sends index of chosen room to server.
     * @param index - index of chosen room
     */
    public void indexOfRoomChosen(String index){
        game.sendResponse(index);
    }

    /**
     * Sends move made by player to server.
     * @param move - player's turn
     */
    public void moveMade(String move){
        game.sendResponse(move);
    }

    /**
     * Receives info about indexes of existing rooms from server.
     * @return - info about indexes of existing rooms
     */
    public ArrayList<String> receiveListOfRoomsInfo() {

        ArrayList<String> roomsInfo = new ArrayList<>();
        String response = game.receiveResponse();

        String list = response.replace("[","");
        String readyList = list.replace("]","");

        if (!readyList.equals("")) {

            for (String info : readyList.split(", ")) {
                roomsInfo.add(info);
            }
        }
        return roomsInfo;

    }

    public String[][] receiveUpdatedBoard(String response){
        String[][] updatedBoard = new String[19][19];
        String[] responseBoard;

        String response1 = response.replace("UPDATED_BOARD [","");
        String response2 = response1.replace("]","");

        responseBoard = response2.split(", ");

        if(!response2.equals("")){
            for(int i = 0; i < 19; i++){
                for(int j = 0; j < 19; j++){
                    updatedBoard[i][j] = responseBoard[(i*18)+i+j];
                }
            }
        }
        return updatedBoard;
    }


    /**
     * Receives response from server during game.
     * @return response from server.
     */
    public void receiveGameMessage (GameMessageListener listener){

        new Thread(() -> {

            while (true) {
                String response = game.receiveResponse();
                if (response.equals("MAKE_TURN")) {
                    listener.playerReceivedPermissionToMove();
                } else if (response.equals("LEGAL_MOVE")) {
                    listener.playerMadeLegalMove();
                } else if (response.equals("KO")) {
                    listener.playerMadeIllegalMoveKO();
                } else if (response.equals("SUICIDE")) {
                    listener.playerMadeIllegalMoveSuicide();
                } else if (response.equals("OCCUPIED_FIELD")) {
                    listener.playerMadeIllegalMoveOccupiedField();
                } else if (response.equals("OPPONENT_PASSED")) {
                    listener.opponentPassed();
                } else if (response.equals("OPPONENT_GAVE_UP")) {
                    listener.opponentGaveUp();
                } else if (response.startsWith("UPDATED_BOARD ")) {
                    String[][] updatedBoard = receiveUpdatedBoard(response);
                    listener.updateBoard(updatedBoard);
                }
            }

        }).start();
    }
}