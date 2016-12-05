package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Kasia on 2016-12-02.
 */
public class ChooseRoomFrame extends JFrame {

    private ArrayList<String> index = new ArrayList<>();
    private ArrayList<String> player1 = new ArrayList<>();
    private ArrayList<String> player2 = new ArrayList<>();

    private JPanel panelForExistingRooms = new JPanel();
    private JPanel panelForChooseRoomButtons = new JPanel();
    private JPanel header = new JPanel();


    public ChooseRoomFrame() {

        makeLists();
        makeViewOfRooms();
        makeHeader();
        makeFinalFrame();

    }

    /* Function which makes 3 Arrays with index of room, login of player 1 and login of player 2 */
    private void makeLists(){
        ArrayList<String> rooms = new ArrayList<>();

        MyPresenter myPresenter = MyPresenter.INSTANCE;
        String listOfRooms = myPresenter.receiveListOfRooms();

        String list = listOfRooms.replace("[","");
        String readyList = list.replace("]","");

        if (!readyList.equals("")) {

            for (String room : readyList.split(", ")) {
                rooms.add(room);
            }

            for (String room : rooms) {
                String[] info = room.split(" ");
                index.add(info[0]);
                player1.add(info[1]);
                player2.add(info[2]);
            }
        }
    }

    private void makeViewOfRooms(){

        panelForExistingRooms.setLayout(new GridLayout(index.size(),3));
        panelForChooseRoomButtons.setLayout(new GridLayout(index.size(),1));


        for (int i=0; i<index.size(); i++){

            JLabel indexLabel = new JLabel(index.get(i));
            JLabel player1Label = new JLabel(player1.get(i));
            JLabel player2Label = new JLabel(player2.get(i));

            panelForExistingRooms.add(indexLabel);
            panelForExistingRooms.add(player1Label);
            panelForExistingRooms.add(player2Label);

            if (player2.get(i).equals("-")) {
                ChooseButton chooseButton = new ChooseButton();
                chooseButton.setIndex(i);
                chooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        MyPresenter presenter = MyPresenter.INSTANCE;
                        ChooseButton exactChooseButton = (ChooseButton) e.getSource();
                        presenter.indexOfRoomChosen(Integer.toString(exactChooseButton.getIndex()));
                    }
                });
                panelForChooseRoomButtons.add(chooseButton);

            }
        }
    }

    private void makeHeader(){

        JLabel chooseRoomLabel = new JLabel();
        chooseRoomLabel.setText("Do you want to create a new room or choose an existing one?");

        JButton newRoomButton = new JButton();
        newRoomButton.setText("New");
        newRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.newRoomChosen();
            }
        });

        JButton existingRoomButton = new JButton();
        existingRoomButton.setText("Existing");
        existingRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.existingRoomChosen();
            }
        });

        header.setLayout(new BorderLayout());

        JPanel panelForNewExistingButtons = new JPanel();
        panelForNewExistingButtons.add(newRoomButton);
        panelForNewExistingButtons.add(existingRoomButton);

        header.add(BorderLayout.NORTH,chooseRoomLabel);
        header.add(BorderLayout.CENTER, panelForNewExistingButtons);

    }

    private void makeFinalFrame(){

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH,header);
        add(BorderLayout.WEST,panelForChooseRoomButtons);
        add(BorderLayout.CENTER,panelForExistingRooms);


        setTitle("Go Game");
        setSize(300, 125);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private class ChooseButton extends JButton{

        private int index;

        private ChooseButton (){
            super("Choose");
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
