package Presenter;

import Usecase.Game;

import java.io.IOException;

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
     * Receives info about existing rooms from server.
     * @return - info about existing rooms ([indexOfRoom, player1Login, player2Login])
     */
    public String receiveListOfRooms() { return game.receiveResponse(); }

}
