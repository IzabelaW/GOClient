package Usecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class which communicates with server.
 */
public class Game {

    /**
     * Input stream.
     */
    private BufferedReader in;

    /**
     * Output stream.
     */
    private PrintWriter out;

    private Socket socket;


    public Game() throws IOException {

        InetAddress addr = InetAddress.getByName("78.8.246.0");
        socket = new Socket(addr, 8901);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    /**
     * Sends response from player. It's used in MyPresenter class.
     * @param response - response from player
     */
    public void sendResponse(String response){
        out.println(response);
    }

    /**
     * Receives response from server. It's used in MyPresenter class.
     * @return response from server
     */
    public String receiveResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
