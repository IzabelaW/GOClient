package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kasia on 2016-12-02.
 */
public class ChooseOpponentFrame extends JFrame {

    public ChooseOpponentFrame(){
       makeFinalFrame();
    }

    private void makeFinalFrame(){
        JLabel askForOpponentLabel = new JLabel();
        askForOpponentLabel.setText("Choose your opponent:");

        ButtonGroup group = new ButtonGroup();
        JRadioButton humanButton = new JRadioButton("HUMAN");
        humanButton.setSelected(true);
        JRadioButton botButton = new JRadioButton("BOT");
        group.add(humanButton);
        group.add(botButton);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));

        panel.add(humanButton);
        panel.add(botButton);

        JButton acceptButton = new JButton("OK");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                if(humanButton.isSelected()){
                    myPresenter.humanChosen();
                    ChooseRoomFrame chooseRoomFrame = new ChooseRoomFrame();
                }
                else if (botButton.isSelected()){
                    myPresenter.botChosen();
                    GameFrame gameFrame = new GameFrame();
                }

                dispose();
            }
        });

        add(askForOpponentLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(acceptButton, BorderLayout.SOUTH);


        setTitle("Go Game");
        setSize(300,125);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
