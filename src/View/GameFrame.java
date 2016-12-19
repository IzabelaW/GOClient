package View;

import Presenter.MyPresenter;
import View.ImageIcon.*;
import View.Listener.GameMessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class of frame with game board.
 */
public class GameFrame extends JFrame implements GameMessageListener{

    /**
     * Table with images of free fields.
     */
    private FreeFieldsImg[][] freeFieldsImg = new FreeFieldsImg[19][19];

    /**
     * Table with images of fields occupied by black stones.
     */
    private BlackFieldsImg[][] blackFieldsImg = new BlackFieldsImg[19][19];

    /**
     * Table with images of fields occupied by white stones.
     */
    private WhiteFieldsImg[][] whiteFieldsImg = new WhiteFieldsImg[19][19];

    /**
     * Table with images of white fields marked as dead.
     */
    private DeadWhiteFieldsImg[][] deadWhiteFieldsImg = new DeadWhiteFieldsImg[19][19];

    /**
     * Table with images of black fields marked as dead.
     */
    private DeadBlackFieldsImg[][] deadBlackFieldsImg = new DeadBlackFieldsImg[19][19];

    /**
     * Table with images of marked fields as player's area.
     */
    private MarkedFieldsImg[][] markedFieldsImg = new MarkedFieldsImg[19][19];

    /**
     * Table with labels treated like fields.
     */
    private FieldLabel[][] fields = new FieldLabel[19][19];

    /**
     * Table with fields marked as dead which is going to be send to the server.
     */
    private boolean[][] markedAsDead = new boolean[19][19];

    /**
     * Panel with game board.
     */
    private JPanel panelForBoard;

    /**
     * Left panel with info about player.
     */
    private JPanel leftPanel;

    /**
     * Right panel with info about player.
     */
    private JPanel rightPanel;

    /**
     * Panel with buttons.
     */
    private JPanel bottomPanel;

    /**
     * Label with info for me.
     */
    private JLabel infoLabel;

    /**
     * Label with opponent's login.
     */
    JLabel opponentLoginLabel;

    /**
     * TextField with number of my captured.
     */
    JTextField myNumberOfCaptured;

    /**
     * TextField with number of opponent's captured.
     */
    JTextField opponentNumberOfCaptured;
    /**
     * Pass button.
     */
    JButton passButton;

    /**
     * Button which accepts suggested dead stones.
     */
    JButton acceptButton;

    /**
     * Button which doesn't accept suggested dead stones.
     */
    JButton notAcceptButton;

    /**
     * Suggest button.
     */
    JButton suggestButton;

    /**
     * Player color.
     */
    String playerColor;

    /**
     * Statement whether this is the player's turn now.
     */
    private Boolean myTurn = false;

    /**
     * If marking stones is enabled for the player.
     */
    private Boolean ifMarkDeadStones = false;

    /**
     * If this player accepted his opponent suggestion.
     */
    private Boolean ifAccepted = false;

    /**
     * If opponent accepted this player's suggestion.
     */
    private Boolean ifOpponentAccepted = false;

    /**
     * If marking area is enabled for the player.
     */
    private Boolean ifMarkArea = false;


    public GameFrame()  {

        makeListsOfImages();
        makeBoard();
        makeSidePanels();
        makeBottomPanel();
        makeFinalFrame();


    }

    /**
     * Creates game board.
     */
    private void makeBoard(){
        panelForBoard = new JPanel();
        panelForBoard.setLayout(new GridLayout(19,19));

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                fields[j][i] = new FieldLabel();
                fields[j][i].setIcon(freeFieldsImg[j][i]);
                fields[j][i].setClickedX(j);
                fields[j][i].setClickedY(i);
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                            if (myTurn) {
                                MyPresenter myPresenter = MyPresenter.INSTANCE;
                                FieldLabel label = (FieldLabel) e.getSource();
                                myPresenter.moveMade("TURN " + label.getClickedX() + " " + label.getClickedY());
                            } else if(ifMarkDeadStones){
                                FieldLabel label = (FieldLabel) e.getSource();
                                if(label.getIcon() instanceof BlackFieldsImg && playerColor.equals("WHITE")) {
                                    fields[label.getClickedX()][label.getClickedY()].setIcon(deadBlackFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
                                }
                                else if(label.getIcon() instanceof WhiteFieldsImg && playerColor.equals("BLACK")){
                                    fields[label.getClickedX()][label.getClickedY()].setIcon(deadWhiteFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
                                }
                            } else if(ifMarkArea){
                                MyPresenter myPresenter = MyPresenter.INSTANCE;
                                FieldLabel label = (FieldLabel) e.getSource();
                                if(label.getIcon() instanceof FreeFieldsImg) {
                                    System.out.println("klik");
                                    myPresenter.sendInfo("AREA: " + label.getClickedX() + " " + label.getClickedY());
                                }
                            }
                    }
                });
                panelForBoard.add(fields[j][i]);
            }
        }

    }

    private void makeSidePanels(){
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(10,1));
        rightPanel.setLayout(new GridLayout(10,1));

        JLabel myLoginLabel = new JLabel("Me");
        opponentLoginLabel = new JLabel();
        JLabel myCapturedLabel = new JLabel("Captured:");
        JLabel opponentCapturedLabel = new JLabel("Captured:");
        myNumberOfCaptured = new JTextField("0");
        opponentNumberOfCaptured = new JTextField("0");
        myNumberOfCaptured.setEditable(false);
        opponentNumberOfCaptured.setEditable(false);
        infoLabel = new JLabel();

        JPanel acceptDeadStonesPanel = new JPanel();
        acceptButton = new JButton("YES");
        notAcceptButton = new JButton("NO");
        acceptDeadStonesPanel.add(acceptButton);
        acceptDeadStonesPanel.add(notAcceptButton);
        acceptButton.setVisible(false);
        notAcceptButton.setVisible(false);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifAccepted = true;
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                infoLabel.setText("Mark dead stones!");
                deleteAcceptedDeadStones();
                myPresenter.sendInfoDeadStonesAccepted();
                acceptButton.setVisible(false);
                notAcceptButton.setVisible(false);

            }
        });

        notAcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.sendInfoDeadStonesNotAccepted();
                deleteNotAcceptedDeadStones();
                notAcceptButton.setEnabled(false);
                acceptButton.setEnabled(false);
            }
        });

        JLabel myEmptyLabel = new JLabel("                                                                         ");
        JLabel opponentEmptyLabel = new JLabel("                                                                         ");


        leftPanel.add(opponentLoginLabel);
        leftPanel.add(opponentCapturedLabel);
        leftPanel.add(opponentNumberOfCaptured);
        leftPanel.add(opponentEmptyLabel);

        rightPanel.add(myLoginLabel);
        rightPanel.add(myCapturedLabel);
        rightPanel.add(myNumberOfCaptured);
        rightPanel.add(infoLabel);
        rightPanel.add(acceptDeadStonesPanel);
        rightPanel.add(myEmptyLabel);

    }

    private void makeBottomPanel(){
        bottomPanel = new JPanel();

        passButton = new JButton("PASS");
        suggestButton = new JButton("SUGGEST");

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.playerPassed();
                passButton.setEnabled(false);
                infoLabel.setText("Opponent's turn!");
                myTurn=false;
            }
        });

        suggestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.sendFieldsMarkedAsDead(markedAsDead);
                for(int i = 0; i < 19; i++){
                    for(int j = 0; j < 19; j++){
                        markedAsDead[i][j] = false;
                    }
                }
                infoLabel.setText("<html>Wait for your opponent<br/>to accept it.</html>.");
                ifMarkDeadStones = false;
                suggestButton.setEnabled(false);
            }
        });

        passButton.setEnabled(false);
        suggestButton.setVisible(false);

        bottomPanel.add(passButton);
        bottomPanel.add(suggestButton);
    }

    public void waitingForOpponent(){
        playerColor = "WHITE";
        opponentLoginLabel.setText("-");
        infoLabel.setText("Waiting for the opponent...");
    }

    public void opponentJoined(String login){
        opponentLoginLabel.setText(login);
        infoLabel.setText("Opponent's turn!");
        JOptionPane.showMessageDialog(null, "Opponent joined. Let's start the game!");
    }

    public void joinToRoom(String initiatorLogin){
        playerColor = "BLACK";
        opponentLoginLabel.setText(initiatorLogin);
        infoLabel.setText("Your turn!");
        passButton.setEnabled(true);

    }

    /**
     * Makes 3 Tables:
     * 1) images of free fields
     * 2) images of fields occupied by white stones
     * 3) images of fields occupied by black stones.
     */
    private void makeListsOfImages(){
        FreeFieldsImg leftSide = new FreeFieldsImg("img/boklewy.png");
        WhiteFieldsImg whiteLeftSide = new WhiteFieldsImg("img/boklewyBiały.png");
        BlackFieldsImg blackLeftSide = new BlackFieldsImg("img/boklewyCzarny.png");
        FreeFieldsImg rightSide = new FreeFieldsImg("img/bokprawy.png");
        WhiteFieldsImg whiteRightSide = new WhiteFieldsImg("img/bokprawyBiały.png");
        BlackFieldsImg blackRightSide = new BlackFieldsImg("img/bokprawyCzarny.png");
        FreeFieldsImg bottom = new FreeFieldsImg("img/dół.png");
        WhiteFieldsImg whiteBottom = new WhiteFieldsImg("img/dółBiały.png");
        BlackFieldsImg blackBottom = new BlackFieldsImg("img/dółCzarny.png");
        FreeFieldsImg top = new FreeFieldsImg("img/góra.png");
        WhiteFieldsImg whiteTop = new WhiteFieldsImg("img/góraBiała.png");
        BlackFieldsImg blackTop = new BlackFieldsImg("img/góraCzarna.png");
        FreeFieldsImg bottomLeftCorner = new FreeFieldsImg("img/lewydolny.png");
        WhiteFieldsImg whiteBottomLeftCorner = new WhiteFieldsImg("img/lewydolnyBiały.png");
        BlackFieldsImg blackBottomLeftCorner = new BlackFieldsImg("img/lewydolnyCzarny.png");
        FreeFieldsImg topLeftCorner = new FreeFieldsImg("img/lewygórny.png");
        WhiteFieldsImg whiteTopLeftCorner = new WhiteFieldsImg("img/lewygórnyBiały.png");
        BlackFieldsImg blackTopLeftCorner = new BlackFieldsImg("img/lewygórnyCzarny.png");
        FreeFieldsImg bottomRightCorner = new FreeFieldsImg("img/prawydolny.png");
        WhiteFieldsImg whiteBottomRightCorner = new WhiteFieldsImg("img/prawydolnyBiały.png");
        BlackFieldsImg blackBottomRightCorner = new BlackFieldsImg("img/prawydolnyCzarny.png");
        FreeFieldsImg topRightCorner = new FreeFieldsImg("img/prawygórny.png");
        WhiteFieldsImg whiteTopRightCorner = new WhiteFieldsImg("img/prawygórnyBiały.png");
        BlackFieldsImg blackTopRightCorner = new BlackFieldsImg("img/prawygórnyCzarny.png");
        FreeFieldsImg center = new FreeFieldsImg("img/środek.png");
        WhiteFieldsImg whiteCenter = new WhiteFieldsImg("img/środekBiały.png");
        BlackFieldsImg blackCenter= new BlackFieldsImg("img/środekCzarny.png");
        DeadWhiteFieldsImg deadWhiteTopLeftCorner = new DeadWhiteFieldsImg ("img/lewygórnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTopLeftCorner = new DeadBlackFieldsImg("img/lewygórnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteTopRightCorner = new DeadWhiteFieldsImg("img/prawygórnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTopRightCorner = new DeadBlackFieldsImg("img/prawygórnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottomLeftCorner = new DeadWhiteFieldsImg("img/lewydolnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottomLeftCorner = new DeadBlackFieldsImg("img/lewydolnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottomRightCorner = new DeadWhiteFieldsImg("img/prawydolnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottomRightCorner = new DeadBlackFieldsImg("img/prawydolnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteTop = new DeadWhiteFieldsImg("img/góraBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTop = new DeadBlackFieldsImg("img/góraCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottom = new DeadWhiteFieldsImg("img/dółBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottom = new DeadBlackFieldsImg("img/dółCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteCenter = new DeadWhiteFieldsImg("img/środekBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackCenter = new DeadBlackFieldsImg("img/środekCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteLeftSide = new DeadWhiteFieldsImg("img/boklewyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackLeftSide = new DeadBlackFieldsImg("img/boklewyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteRightSide = new DeadWhiteFieldsImg("img/bokprawyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackRightSide = new DeadBlackFieldsImg("img/bokprawyCzarnyMartwy.png");
        MarkedFieldsImg markedLeftSide = new MarkedFieldsImg("img/boklewyzielony.png");
        MarkedFieldsImg markedRightSide = new MarkedFieldsImg("img/bokprawyzielony.png");
        MarkedFieldsImg markedBottom = new MarkedFieldsImg("img/dółzielony.png");
        MarkedFieldsImg markedTop = new MarkedFieldsImg("img/górazielony.png");
        MarkedFieldsImg markedBottomLeftCorner = new MarkedFieldsImg("img/lewydolnyzielony.png");
        MarkedFieldsImg markedTopLeftCorner = new MarkedFieldsImg("img/lewygórnyzielony.png");
        MarkedFieldsImg markedBottomRightCorner = new MarkedFieldsImg("img/prawydolnyzielony.png");
        MarkedFieldsImg markedTopRightCorner = new MarkedFieldsImg("img/prawygórnyzielony.png");
        MarkedFieldsImg markedCenter = new MarkedFieldsImg("img/środekzielony.png");

        freeFieldsImg[0][0] = topLeftCorner;
        whiteFieldsImg[0][0] = whiteTopLeftCorner;
        blackFieldsImg[0][0] = blackTopLeftCorner;
        deadBlackFieldsImg[0][0] = deadBlackTopLeftCorner;
        deadWhiteFieldsImg[0][0] = deadWhiteTopLeftCorner;
        markedFieldsImg[0][0] = markedTopLeftCorner;

        freeFieldsImg[18][0] = topRightCorner;
        whiteFieldsImg[18][0] = whiteTopRightCorner;
        blackFieldsImg[18][0] = blackTopRightCorner;
        deadBlackFieldsImg[18][0] = deadBlackTopRightCorner;
        deadWhiteFieldsImg[18][0] = deadWhiteTopRightCorner;
        markedFieldsImg[18][0] = markedTopRightCorner;

        freeFieldsImg[0][18] = bottomLeftCorner;
        whiteFieldsImg[0][18] = whiteBottomLeftCorner;
        blackFieldsImg[0][18] = blackBottomLeftCorner;
        deadBlackFieldsImg[0][18] = deadBlackBottomLeftCorner;
        deadWhiteFieldsImg[0][18] = deadWhiteBottomLeftCorner;
        markedFieldsImg[0][18] = markedBottomLeftCorner;

        freeFieldsImg[18][18] = bottomRightCorner;
        whiteFieldsImg[18][18] = whiteBottomRightCorner;
        blackFieldsImg[18][18] = blackBottomRightCorner;
        deadBlackFieldsImg[18][18] = deadBlackBottomRightCorner;
        deadWhiteFieldsImg[18][18] = deadWhiteBottomRightCorner;
        markedFieldsImg[18][18] = markedBottomRightCorner;

        for(int i = 1; i < 18; i++){
            freeFieldsImg[i][0] = top;
            whiteFieldsImg[i][0] = whiteTop;
            blackFieldsImg[i][0] = blackTop;
            deadBlackFieldsImg[i][0] = deadBlackTop;
            deadWhiteFieldsImg[i][0] = deadWhiteTop;
            markedFieldsImg[i][0] = markedTop;

            freeFieldsImg[i][18] = bottom;
            whiteFieldsImg[i][18] = whiteBottom;
            blackFieldsImg[i][18] = blackBottom;
            deadBlackFieldsImg[i][18] = deadBlackBottom;
            deadWhiteFieldsImg[i][18] = deadWhiteBottom;
            markedFieldsImg[i][18] = markedBottom;

            freeFieldsImg[0][i] = leftSide;
            whiteFieldsImg[0][i] = whiteLeftSide;
            blackFieldsImg[0][i] = blackLeftSide;
            deadBlackFieldsImg[0][i] = deadBlackLeftSide;
            deadWhiteFieldsImg[0][i] = deadWhiteLeftSide;
            markedFieldsImg[0][i] = markedLeftSide;

            freeFieldsImg[18][i] = rightSide;
            whiteFieldsImg[18][i] = whiteRightSide;
            blackFieldsImg[18][i] = blackRightSide;
            deadBlackFieldsImg[18][i] = deadBlackRightSide;
            deadWhiteFieldsImg[18][i] = deadWhiteRightSide;
            markedFieldsImg[18][i] = markedRightSide;
        }

        for(int i = 1; i < 18; i++){
            for(int j = 1; j < 18; j++){
                freeFieldsImg[i][j] = center;
                whiteFieldsImg[i][j]= whiteCenter;
                blackFieldsImg[i][j] = blackCenter;
                deadBlackFieldsImg[i][j] = deadBlackCenter;
                deadWhiteFieldsImg[i][j] = deadWhiteCenter;
                markedFieldsImg[i][j] = markedCenter;
            }
        }

    }

    /**
     * Sets frame visible.
     */
    private void makeFinalFrame(){
        setLayout(new BorderLayout());
        panelForBoard.setPreferredSize(new Dimension(625,625));
        add(BorderLayout.CENTER, panelForBoard);
        add(BorderLayout.WEST, leftPanel);
        add(BorderLayout.EAST, rightPanel);
        add(BorderLayout.SOUTH, bottomPanel);
        setTitle("Go Game");
        setSize(1080,695);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void playerReceivedPermissionToMove() {
        myTurn = true;
        passButton.setEnabled(true);
        infoLabel.setText("Your turn!");
    }

    @Override
    public void playerMadeLegalMove() {
        myTurn = false;
        passButton.setEnabled(false);
        infoLabel.setText("Opponent's turn!");
    }

    @Override
    public void playerMadeIllegalMoveKO() {
        JOptionPane.showMessageDialog(null, "Illegal move: KO. Try again!");
    }

    @Override
    public void playerMadeIllegalMoveSuicide() {
        JOptionPane.showMessageDialog(null, "Illegal move: suicide. Try again!");
    }

    @Override
    public void playerMadeIllegalMoveOccupiedField() {
        JOptionPane.showMessageDialog(null, "Illegal move: occupied field. Try again!");
    }

    @Override
    public void opponentPassed() {
        myTurn = true;
        passButton.setEnabled(true);
        infoLabel.setText("Opponent passed. Your turn!");
    }

    @Override
    public void opponentGaveUp() {
        JOptionPane.showMessageDialog(null, "Congrats! Opponent gave up. You won!");
        passButton.setEnabled(false);
    }

    @Override
    public void updateBoard(String[][] updatedBoard) {
        for(int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){

                if (updatedBoard[i][j].equals("FREE")){
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                }
                else if (updatedBoard[i][j].equals("BLACK")){
                    fields[i][j].setIcon(blackFieldsImg[i][j]);
                }
                else if (updatedBoard[i][j].equals("WHITE")){
                    fields[i][j].setIcon(whiteFieldsImg[i][j]);
                }
            }
        }
    }

    @Override
    public void updateCaptured(String capturedForWhite, String capturedForBlack){
        if(playerColor.equals("WHITE")){
            myNumberOfCaptured.setText(capturedForWhite);
            opponentNumberOfCaptured.setText(capturedForBlack);
        }
        else if(playerColor.equals("BLACK")){
            myNumberOfCaptured.setText(capturedForBlack);
            opponentNumberOfCaptured.setText(capturedForWhite);
        }
    }

    @Override
    public void waitForOpponentToMarkDeadStones(){
        infoLabel.setText("<html>Wait for opponent <br/>to mark dead stones!</html>");
        passButton.setEnabled(false);
        ifMarkDeadStones =false;
    }

    @Override
    public void markDeadStones(){
        passButton.setEnabled(false);
        ifMarkDeadStones = true;
        suggestButton.setVisible(true);
        infoLabel.setText("Mark dead stones of your opponent.");

    }

    @Override
    public void showMarkedAsDead(String[][] markedAsDead){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(markedAsDead[i][j].equals("true")){
                    if(playerColor.equals("WHITE"))
                        fields[i][j].setIcon(deadWhiteFieldsImg[i][j]);
                    else if(playerColor.equals("BLACK"))
                            fields[i][j].setIcon(deadBlackFieldsImg[i][j]);
                }
            }
        }
        infoLabel.setText("Do you accept?");
        acceptButton.setVisible(true);
        notAcceptButton.setVisible(true);
        acceptButton.setEnabled(true);
        notAcceptButton.setEnabled(true);
    }

    @Override
    public void deadStonesAccepted(){
        ifOpponentAccepted = true;
        MyPresenter myPresenter = MyPresenter.INSTANCE;
        if (ifAccepted && ifOpponentAccepted){
            myPresenter.sendUpdatedBoard(fields);
            deleteAcceptedDeadStones();
            infoLabel.setText("<html>Wait for the opponent<br/>to mark his area.</html>");
        }
        else {
            myPresenter.sendInfo("MARK_DEAD");
            deleteAcceptedDeadStones();
            infoLabel.setText("<html>Opponent accepted your suggestion!<br/>Wait for him to mark dead stones.</html>");
        }

    }

    @Override
    public void deadStonesNotAccepted(){
        deleteNotAcceptedDeadStones();
        ifMarkDeadStones = true;
        infoLabel.setText("<html>Opponent didn't accept your suggestion.<br/>Mark his dead stones again!.</html>");
        suggestButton.setEnabled(true);

    }

    private void deleteNotAcceptedDeadStones(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                if (fields[i][j].getIcon() instanceof DeadBlackFieldsImg)
                    fields[i][j].setIcon(blackFieldsImg[i][j]);
                else if (fields[i][j].getIcon() instanceof DeadWhiteFieldsImg)
                    fields[i][j].setIcon(whiteFieldsImg[i][j]);
            }
        }
    }

    private void deleteAcceptedDeadStones(){
        int myCaptured = Integer.parseInt(myNumberOfCaptured.getText());
        int opponentCaptured = Integer.parseInt(opponentNumberOfCaptured.getText());
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(fields[i][j].getIcon() instanceof DeadWhiteFieldsImg) {
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    if (playerColor.equals("BLACK"))
                        myCaptured++;
                    else if (playerColor.equals("WHITE"))
                        opponentCaptured++;
                } else if (fields[i][j].getIcon() instanceof DeadBlackFieldsImg){
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    if (playerColor.equals("BLACK"))
                        opponentCaptured++;
                    else if (playerColor.equals("WHITE"))
                        myCaptured++;
                }
            }
        }
        myNumberOfCaptured.setText(Integer.toString(myCaptured));
        opponentNumberOfCaptured.setText(Integer.toString(opponentCaptured));
    }

    @Override
    public void markArea() {
        ifMarkArea = true;
        infoLabel.setText("Mark your area.");
    }

    @Override
    public void showMarkedArea(String[][] markedArea){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(!markedArea[i][j].equals("0"))
                    fields[i][j].setIcon(markedFieldsImg[i][j]);
            }
        }
    }

    /**
     * Class of label treated like field.
     */
    public class FieldLabel extends JLabel{
        ImageIcon icon;
        int x;
        int y;

        public void setIcon(ImageIcon icon){
            super.setIcon(icon);
            this.icon = icon;
        }

        public ImageIcon getIcon(){
            return icon;
        }

        public void setClickedX(int x){
            this.x = x;
        }

        public void setClickedY(int y){
            this.y = y;
        }

        public int getClickedX(){
            return x;
        }

        public int getClickedY(){
            return y;
        }
    }
}
