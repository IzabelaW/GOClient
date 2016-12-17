package View;

import Presenter.MyPresenter;
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
    private ImageIcon[][] freeFieldsImg = new ImageIcon[19][19];

    /**
     * Table with images of fields occupied by black stones.
     */
    private ImageIcon[][] blackFieldsImg = new ImageIcon[19][19];

    /**
     * Table with images of fields occupied by white stones.
     */
    private ImageIcon[][] whiteFieldsImg = new ImageIcon[19][19];

    /**
     * Table with images of white fields marked as dead.
     */
    private ImageIcon[][] deadWhiteFieldsImg = new ImageIcon[19][19];

    /**
     * Table with images of black fields marked as dead.
     */
    private ImageIcon[][] deadBlackFieldsImg = new ImageIcon[19][19];

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
    private Boolean markDeadStones;

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
                fields[i][j].setColor("FREE");
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                            if (myTurn) {
                                MyPresenter myPresenter = MyPresenter.INSTANCE;
                                FieldLabel label = (FieldLabel) e.getSource();
                                myPresenter.moveMade("TURN " + Integer.toString(label.getClickedX()) + " " + Integer.toString(label.getClickedY()));
                            }
                            else if(markDeadStones){
                                FieldLabel label = (FieldLabel) e.getSource();
                                if(label.getColor().equals("BLACK") && playerColor.equals("WHITE")) {
                                    label.setIcon(deadBlackFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
                                }
                                else if(label.getColor().equals("WHITE") && playerColor.equals("BLACK")){
                                    label.setIcon(deadWhiteFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
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
        JLabel myEmptyLabel = new JLabel("                                                    ");
        JLabel opponentEmptyLabel = new JLabel("                                                    ");


        leftPanel.add(opponentLoginLabel);
        leftPanel.add(opponentCapturedLabel);
        leftPanel.add(opponentNumberOfCaptured);
        leftPanel.add(opponentEmptyLabel);

        rightPanel.add(myLoginLabel);
        rightPanel.add(myCapturedLabel);
        rightPanel.add(myNumberOfCaptured);
        rightPanel.add(infoLabel);
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
                infoLabel.setText("OPPONENT'S TURN!");
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
        infoLabel.setText("Waiting for opponent...");
    }

    public void opponentJoined(String login){
        opponentLoginLabel.setText(login);
        infoLabel.setText("OPPONENT'S TURN!");
        JOptionPane.showMessageDialog(null, "Opponent joined. Let's start the game!");
    }

    public void joinToRoom(String initiatorLogin){
        playerColor = "BLACK";
        opponentLoginLabel.setText(initiatorLogin);
        infoLabel.setText("YOUR TURN!");
        passButton.setEnabled(true);

    }

    /**
     * Makes 3 Tables:
     * 1) images of free fields
     * 2) images of fields occupied by white stones
     * 3) images of fields occupied by black stones.
     */
    private void makeListsOfImages(){
        ImageIcon leftSide = new ImageIcon("img/boklewy.png");
        ImageIcon whiteLeftSide = new ImageIcon("img/boklewyBiały.png");
        ImageIcon blackLeftSide = new ImageIcon("img/boklewyCzarny.png");
        ImageIcon rightSide = new ImageIcon("img/bokprawy.png");
        ImageIcon whiteRightSide = new ImageIcon("img/bokprawyBiały.png");
        ImageIcon blackRightSide = new ImageIcon("img/bokprawyCzarny.png");
        ImageIcon bottom = new ImageIcon("img/dół.png");
        ImageIcon whiteBottom = new ImageIcon("img/dółBiały.png");
        ImageIcon blackBottom = new ImageIcon("img/dółCzarny.png");
        ImageIcon top = new ImageIcon("img/góra.png");
        ImageIcon whiteTop = new ImageIcon("img/góraBiała.png");
        ImageIcon blackTop = new ImageIcon("img/góraCzarna.png");
        ImageIcon bottomLeftCorner = new ImageIcon("img/lewydolny.png");
        ImageIcon whiteBottomLeftCorner = new ImageIcon("img/lewydolnyBiały.png");
        ImageIcon blackBottomLeftCorner = new ImageIcon("img/lewydolnyCzarny.png");
        ImageIcon topLeftCorner = new ImageIcon("img/lewygórny.png");
        ImageIcon whiteTopLeftCorner = new ImageIcon("img/lewygórnyBiały.png");
        ImageIcon blackTopLeftCorner = new ImageIcon("img/lewygórnyCzarny.png");
        ImageIcon bottomRightCorner = new ImageIcon("img/prawydolny.png");
        ImageIcon whiteBottomRightCorner = new ImageIcon("img/prawydolnyBiały.png");
        ImageIcon blackBottomRightCorner = new ImageIcon("img/prawydolnyCzarny.png");
        ImageIcon topRightCorner = new ImageIcon("img/prawygórny.png");
        ImageIcon whiteTopRightCorner = new ImageIcon("img/prawygórnyBiały.png");
        ImageIcon blackTopRightCorner = new ImageIcon("img/prawygórnyCzarny.png");
        ImageIcon center = new ImageIcon("img/środek.png");
        ImageIcon whiteCenter = new ImageIcon("img/środekBiały.png");
        ImageIcon blackCenter= new ImageIcon("img/środekCzarny.png");
        ImageIcon deadWhiteTopLeftCorner = new ImageIcon ("img/lewygórnyBiałyMartwy");
        ImageIcon deadBlackTopLeftCorner = new ImageIcon("img/lewygórnyCzarnyMartwy");
        ImageIcon deadWhiteTopRightCorner = new ImageIcon("img/prawygórnyBiałyMartwy");
        ImageIcon deadBlackTopRightCorner = new ImageIcon("img/prawygórnyCzarnyMartwy");
        ImageIcon deadWhiteBottomLeftCorner = new ImageIcon("img/lewydolnyBiałyMartwy");
        ImageIcon deadBlackBottomLeftCorner = new ImageIcon("img/lewydolnyCzarnyMartwy");
        ImageIcon deadWhiteBottomRightCorner = new ImageIcon("img/prawydolnyBiałyMartwy");
        ImageIcon deadBlackBottomRightCorner = new ImageIcon("img/prawydolnyCzarnyMartwy");
        ImageIcon deadWhiteTop = new ImageIcon("img/góraBiałyMartwy");
        ImageIcon deadBlackTop = new ImageIcon("img/góraCzarnyMartwy");
        ImageIcon deadWhiteBottom = new ImageIcon("img/dółBiałyMartwy");
        ImageIcon deadBlackBottom = new ImageIcon("img/dółCzarnyMartwy");
        ImageIcon deadWhiteCenter = new ImageIcon("img/środekBiałyMartwy");
        ImageIcon deadBlackCenter = new ImageIcon("img/środekCzarnyMartwy");
        ImageIcon deadWhiteLeftSide = new ImageIcon("img/boklewyBiałyMartwy");
        ImageIcon deadBlackLeftSide = new ImageIcon("img/boklewyCzarnyMartwy");
        ImageIcon deadWhiteRightSide = new ImageIcon("img/bokprawyBiałyMartwy");
        ImageIcon deadBlackRightSide = new ImageIcon("img/bokprawyCzarnyMartwy");

        freeFieldsImg[0][0] = topLeftCorner;
        whiteFieldsImg[0][0] = whiteTopLeftCorner;
        blackFieldsImg[0][0] = blackTopLeftCorner;
        deadBlackFieldsImg[0][0] = deadBlackTopLeftCorner;
        deadWhiteFieldsImg[0][0] = deadWhiteTopLeftCorner;

        freeFieldsImg[18][0] = topRightCorner;
        whiteFieldsImg[18][0] = whiteTopRightCorner;
        blackFieldsImg[18][0] = blackTopRightCorner;
        deadBlackFieldsImg[18][0] = deadBlackTopRightCorner;
        deadWhiteFieldsImg[18][0] = deadWhiteTopRightCorner;

        freeFieldsImg[0][18] = bottomLeftCorner;
        whiteFieldsImg[0][18] = whiteBottomLeftCorner;
        blackFieldsImg[0][18] = blackBottomLeftCorner;
        deadBlackFieldsImg[0][18] = deadBlackBottomLeftCorner;
        deadWhiteFieldsImg[0][18] = deadWhiteBottomLeftCorner;

        freeFieldsImg[18][18] = bottomRightCorner;
        whiteFieldsImg[18][18] = whiteBottomRightCorner;
        blackFieldsImg[18][18] = blackBottomRightCorner;
        deadBlackFieldsImg[18][18] = deadBlackBottomRightCorner;
        deadWhiteFieldsImg[18][18] = deadWhiteBottomRightCorner;

        for(int i = 1; i < 18; i++){
            freeFieldsImg[i][0] = top;
            whiteFieldsImg[i][0] = whiteTop;
            blackFieldsImg[i][0] = blackTop;
            deadBlackFieldsImg[i][0] = deadBlackTop;
            deadWhiteFieldsImg[i][0] = deadWhiteTop;

            freeFieldsImg[i][18] = bottom;
            whiteFieldsImg[i][18] = whiteBottom;
            blackFieldsImg[i][18] = blackBottom;
            deadBlackFieldsImg[i][18] = deadBlackBottom;
            deadWhiteFieldsImg[i][18] = deadWhiteBottom;

            freeFieldsImg[0][i] = leftSide;
            whiteFieldsImg[0][i] = whiteLeftSide;
            blackFieldsImg[0][i] = blackLeftSide;
            deadBlackFieldsImg[0][i] = deadBlackLeftSide;
            deadWhiteFieldsImg[0][i] = deadWhiteLeftSide;

            freeFieldsImg[18][i] = rightSide;
            whiteFieldsImg[18][i] = whiteRightSide;
            blackFieldsImg[18][i] = blackRightSide;
            deadBlackFieldsImg[18][i] = deadBlackRightSide;
            deadWhiteFieldsImg[18][i] = deadWhiteRightSide;
        }

        for(int i = 1; i < 18; i++){
            for(int j = 1; j < 18; j++){
                freeFieldsImg[i][j] = center;
                whiteFieldsImg[i][j]= whiteCenter;
                blackFieldsImg[i][j] = blackCenter;
                deadBlackFieldsImg[i][j] = deadBlackCenter;
                deadWhiteFieldsImg[i][j] = deadWhiteCenter;
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
        setSize(950,690);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void playerReceivedPermissionToMove() {
        myTurn = true;
        passButton.setEnabled(true);
        infoLabel.setText("YOUR TURN!");
    }

    @Override
    public void playerMadeLegalMove() {
        myTurn = false;
        passButton.setEnabled(false);
        infoLabel.setText("OPPONENT'S TURN!");
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
        JOptionPane.showMessageDialog(null, "Opponent passed. Your turn!");
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
                    fields[i][j].setColor("FREE");
                }
                else if (updatedBoard[i][j].equals("BLACK")){
                    fields[i][j].setIcon(blackFieldsImg[i][j]);
                    fields[i][j].setColor("BLACK");
                }
                else if (updatedBoard[i][j].equals("WHITE")){
                    fields[i][j].setIcon(whiteFieldsImg[i][j]);
                    fields[i][j].setColor("WHITE");
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

    public void waitForOpponentToMarkDeadStones(){
        infoLabel.setText("<html>Wait for opponent <br/>to mark dead stones!</html>");
        passButton.setEnabled(false);
        markDeadStones=false;
    }

    public void markDeadStones(){
        JOptionPane.showMessageDialog(null, "Opponent has also passed. Mark his dead stones.");
        passButton.setEnabled(false);
        markDeadStones=true;
        suggestButton.setVisible(true);

    }

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
    }

    /**
     * Class of label treated like field.
     */
    private class FieldLabel extends JLabel{
        String color;
        int x;
        int y;

        public void setColor(String color){
            this.color = color;
        }

        public String getColor(){
            return color;
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
