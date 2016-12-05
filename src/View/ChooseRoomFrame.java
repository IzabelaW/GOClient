package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kasia on 2016-12-02.
 */
public class ChooseRoomFrame extends JFrame {

    public ChooseRoomFrame() {

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

        JPanel panelForButtons = new JPanel();

        panelForButtons.add(newRoomButton);
        panelForButtons.add(existingRoomButton);


        setLayout(new BorderLayout());
        add(BorderLayout.NORTH,chooseRoomLabel);
        add(BorderLayout.CENTER, panelForButtons);

        setTitle("Go Game");
        setSize(300, 125);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void makeViewOfRooms(){
        MyPresenter myPresenter = MyPresenter.INSTANCE;
        String syf = myPresenter.receiveListOfRooms();
        System.out.println(syf);
    }
}
