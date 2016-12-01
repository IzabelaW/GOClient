package Presenter;

import Usecase.Game;

import java.io.IOException;

/**
 * Created by Izabela on 2016-12-01.
 */
public class MyPresenter {

    private Game game;

    public MyPresenter(){

        try {
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendLogin(String login){
        game.sendResponse(login);
    }

    public void botChosen(){
        game.sendResponse("BOT");
    }

    public void humanChosen(){
        game.sendResponse("HUMAN");
    }

    public void newRoomChosen(){
        game.sendResponse("NEW");
    }

    public void existingRoomChosen(){
        game.sendResponse("EXISTING");
    }

    public void indexOfRoomChosen(String index){
        game.sendResponse(index);
    }

    public void moveMade(String move){
        game.sendResponse(move);
    }

}
