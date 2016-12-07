package View;

import Presenter.MyPresenter;
import View.Listener.GameMessageListener;

import javax.swing.*;
import java.awt.*;
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
     * Table with labels treated like fields.
     */
    private FieldLabel[][] fields = new FieldLabel[19][19];

    /**
     * Panel with game board.
     */
    private JPanel panelForBoard;

    /**
     * Statement whether this is the player's turn now.
     */
    private Boolean myTurn = false;

    public GameFrame()  {

        makeListsOfImages();
        makeBoard();
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
                        if(myTurn) {
                            MyPresenter myPresenter = MyPresenter.INSTANCE;
                            FieldLabel label = (FieldLabel) e.getSource();
                            myPresenter.moveMade("TURN " + Integer.toString(label.getClickedX()) + " " + Integer.toString(label.getClickedY()));
                            myTurn = false; // w przypadku gdy będzie nielegalny ruch, kolejna możliwość ruchu
                        }
                    }
                });
                panelForBoard.add(fields[j][i]);
            }
        }

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
        ImageIcon blackBottoRightCorner = new ImageIcon("img/prawydolnyCzarny.png");
        ImageIcon topRightCorner = new ImageIcon("img/prawygórny.png");
        ImageIcon whiteTopRightCorner = new ImageIcon("img/prawygórnyBiały.png");
        ImageIcon blackTopRightCorner = new ImageIcon("img/prawygórnyCzarny.png");
        ImageIcon center = new ImageIcon("img/środek.png");
        ImageIcon whiteCenter = new ImageIcon("img/środekBiały.png");
        ImageIcon blackCenter= new ImageIcon("img/środekCzarny.png");

        freeFieldsImg[0][0] = topLeftCorner;
        whiteFieldsImg[0][0] = whiteTopLeftCorner;
        blackFieldsImg[0][0] = blackTopLeftCorner;

        freeFieldsImg[18][0] = topRightCorner;
        whiteFieldsImg[18][0] = whiteTopRightCorner;
        blackFieldsImg[18][0] = blackTopRightCorner;

        freeFieldsImg[0][18] = bottomLeftCorner;
        whiteFieldsImg[0][18] = whiteBottomLeftCorner;
        blackFieldsImg[0][18] = blackBottomLeftCorner;

        freeFieldsImg[18][18] = bottomRightCorner;
        whiteFieldsImg[18][18] = whiteBottomRightCorner;
        blackFieldsImg[18][18] = blackBottoRightCorner;

        for(int i = 1; i < 18; i++){
            freeFieldsImg[i][0] = top;
            whiteFieldsImg[i][0] = whiteTop;
            blackFieldsImg[i][0] = blackTop;

            freeFieldsImg[i][18] = bottom;
            whiteFieldsImg[i][18] = whiteBottom;
            blackFieldsImg[i][18] = blackBottom;

            freeFieldsImg[0][i] = leftSide;
            whiteFieldsImg[0][i] = whiteLeftSide;
            blackFieldsImg[0][i] = blackLeftSide;

            freeFieldsImg[18][i] = rightSide;
            whiteFieldsImg[18][i] = whiteRightSide;
            blackFieldsImg[18][i] = blackRightSide;
        }

        for(int i = 1; i < 18; i++){
            for(int j = 1; j < 18; j++){
                freeFieldsImg[i][j] = center;
                whiteFieldsImg[i][j]= whiteCenter;
                blackFieldsImg[i][j] = blackCenter;
            }
        }


    }

    /**
     * Sets frame visible.
     */
    private void makeFinalFrame(){
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, panelForBoard);
        setTitle("Go Game");
        setSize(1000,850);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void playerReceivedPermissionToMove() {
        myTurn = true;
        //wyswietlenie napisu "YOUR TURN"
    }

    @Override
    public void opponentPassed() {
        myTurn = true;
        JOptionPane.showMessageDialog(null, "Opponent passed. Your turn!");
    }

    @Override
    public void opponentGaveUp() {
        JOptionPane.showMessageDialog(null, "Congrats! Opponent gave up. You won!");
    }

    @Override
    public void updateBoard(String[][] updatedBoard) {
        for(int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){

                if (updatedBoard[i][j].equals("null")){
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

    /**
     * Class of label treated like field.
     */
    private class FieldLabel extends JLabel{
        int x;
        int y;

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
