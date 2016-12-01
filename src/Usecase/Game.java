package Usecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Izabela on 2016-12-01.
 */
public class Game {

    private BufferedReader in;
    private PrintWriter out;


    public Game() throws IOException {

        Socket socket = new Socket(InetAddress.getLocalHost(), 9001);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public void sendResponse(String response){
        out.println(response);
    }

}
