package View;

import javax.swing.*;

/**
 * Created by Kasia on 2016-12-02.
 */
public class ChooseRoomFrame extends JFrame {

    public ChooseRoomFrame() {

        JLabel chooseRoomLabel = new JLabel();
        chooseRoomLabel.setText("Do you want to create a new room or choose an existing one?");

        JButton newRoomButton = new JButton();
        newRoomButton.setText("New");

        JButton existingRoomButton = new JButton();
        existingRoomButton.setText("Existing");

        ButtonGroup group = new ButtonGroup();

      //  JRadioButton humanButton = new JRadioButton("HUMAN");
      //  humanButton.setSelected(true);
      //  JRadioButton botButton = new JRadioButton("BOT");
      //  group.add(humanButton);
      //  group.add(botButton);






        setTitle("Go Game");
        setSize(300, 125);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }
}
