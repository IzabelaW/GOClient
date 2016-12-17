package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class of frame, which gives player opportunity to choose room for the game.
 */
public class ChooseRoomFrame extends JFrame {

    /**
     * ArrayList of rooms' indexes.
     */
    private ArrayList<String> indexes = new ArrayList<>();

    /**
     * ArrayList of initiators' logins.
     */
    private ArrayList<String> initiators = new ArrayList<>();

    /**
     * ArrayList of joiners' logins.
     */
    private ArrayList<String> joiners = new ArrayList<>();

    /**
     * ArrayList of buttons for choosing room.
     */
    private ArrayList<ChooseButton> chooseButtons = new ArrayList<>();

    /**
     * JPanel which contains list of existing rooms.
     */
    private JPanel panelForExistingRooms = new JPanel();

    /**
     * JPanel which contains buttons for choosing room.
     */
    private JPanel panelForChooseRoomButtons = new JPanel();

    /**
     * JPanel which contains header (New or existing?) and two buttons to choose.
     */
    private JPanel header = new JPanel();


    public ChooseRoomFrame() {

        makeLists();
        makeViewOfRooms();
        makeHeader();
        makeFinalFrame();

    }

    /**
     * Makes 3 ArrayLists:
     * 1) indexes of rooms
     * 2) logins of players (player1)
     * 3) logins of opponents (player2)
     */
    private void makeLists(){

        MyPresenter myPresenter = MyPresenter.INSTANCE;
        indexes = myPresenter.receiveListOfRoomsInfo();
        initiators = myPresenter.receiveListOfRoomsInfo();
        joiners = myPresenter.receiveListOfRoomsInfo();

    }

    /**
     * Creates labels with indexes and logins.
     * If room contains only one player, it creates button to give another player opportunity to choose this room.
     * After all it puts everything on two JPanels (panelForExistingRooms and panelForChooseRoomButtons).
     */
    private void makeViewOfRooms(){

        panelForExistingRooms.setLayout(new GridLayout(indexes.size(),3));
        panelForChooseRoomButtons.setLayout(new GridLayout(indexes.size(),1));


        for (int i=0; i<indexes.size(); i++){

            JLabel indexLabel = new JLabel(indexes.get(i));
            JLabel player1Label = new JLabel(initiators.get(i));
            JLabel player2Label = new JLabel(joiners.get(i));

            panelForExistingRooms.add(indexLabel);
            panelForExistingRooms.add(player1Label);
            panelForExistingRooms.add(player2Label);

            if (joiners.get(i).equals("-")) {
                indexLabel.setForeground(new Color(0x78DD64));
                player1Label.setForeground(new Color(0x78DD64));
                player2Label.setForeground(new Color(0x78DD64));
                ChooseButton chooseButton = new ChooseButton();
                chooseButton.setIndex(i);
                chooseButton.setEnabled(false);
                chooseButtons.add(chooseButton);
                chooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        MyPresenter myPresenter = MyPresenter.INSTANCE;
                        ChooseButton exactChooseButton = (ChooseButton) e.getSource();
                        myPresenter.indexOfRoomChosen(Integer.toString(exactChooseButton.getIndex()));
                        GameFrame gameFrame = new GameFrame();
                        gameFrame.joinToRoom(initiators.get(exactChooseButton.getIndex()));
                        myPresenter.receiveGameMessage(gameFrame);
                        dispose();

                    }
                });
                panelForChooseRoomButtons.add(chooseButton);

            }
            else{
                indexLabel.setForeground(new Color(0xDD101F));
                player1Label.setForeground(new Color(0xDD101F));
                player2Label.setForeground(new Color(0xDD101F));
                JLabel label = new JLabel();
                panelForChooseRoomButtons.add(label);
            }
        }
    }

    /**
     * Makes header and two buttons:
     * 1) to create new room
     * 2) to choose existing room.
     * Puts also everything on JPanel (panelForNewExistingButtons).
     */
    private void makeHeader(){

        JLabel chooseRoomLabel = new JLabel();
        chooseRoomLabel.setText("Do you want to create a new room or choose an existing one?");

        JButton newRoomButton = new JButton();
        newRoomButton.setText("New");
        JButton existingRoomButton = new JButton();
        existingRoomButton.setText("Existing");

        newRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.newRoomChosen();
                GameFrame gameFrame = new GameFrame();
                gameFrame.waitingForOpponent();
                myPresenter.receiveGameMessage(gameFrame);
                dispose();

            }
        });


        existingRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.existingRoomChosen();
                newRoomButton.setEnabled(false);
                existingRoomButton.setEnabled(false);
                for(ChooseButton chooseButton: chooseButtons)
                    chooseButton.setEnabled(true);
            }
        });

        header.setLayout(new BorderLayout());

        JPanel panelForNewExistingButtons = new JPanel();
        panelForNewExistingButtons.add(newRoomButton);
        panelForNewExistingButtons.add(existingRoomButton);

        header.add(BorderLayout.NORTH,chooseRoomLabel);
        header.add(BorderLayout.CENTER, panelForNewExistingButtons);

    }

    /**
     * Sets frame visible.
     */
    private void makeFinalFrame(){

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH,header);
        add(BorderLayout.WEST,panelForChooseRoomButtons);
        add(BorderLayout.CENTER,panelForExistingRooms);

        setTitle("Go Game");
        setSize(400, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    /**
     * Class of button which enables player to choose concrete room.
     */
    private class ChooseButton extends JButton{

        /**
         * Index of chosen room.
         */
        private int index;

        private ChooseButton (){
            super("Choose");
        }

        /**
         * Index setter.
         * @param index - index of chosen room.
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * Index getter.
         * @return index of chosen room.
         */
        public int getIndex() {
            return index;
        }
    }
}
