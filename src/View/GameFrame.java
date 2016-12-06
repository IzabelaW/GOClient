package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Kasia on 2016-12-02.
 */
public class GameFrame extends JFrame {

    ImageIcon[][] freeFieldsImg = new ImageIcon[19][19];
    ImageIcon[][] blackFieldsImg = new ImageIcon[19][19];
    ImageIcon[][] whiteFieldsImg = new ImageIcon[19][19];

    FieldLabel[][] fields = new FieldLabel[19][19];

    JPanel panelForBoard;

    public GameFrame()  {
        makeListsOfImages();
        makeBoard();
        makeFinalFrame();

    }


    private void makeBoard(){
        panelForBoard = new JPanel();
        panelForBoard.setLayout(new GridLayout(19,19));

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                fields[j][i] = new FieldLabel();
                fields[j][i].setIcon(freeFieldsImg[j][i]);
                fields[j][i].setX(j);
                fields[j][i].setY(i);
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        MyPresenter myPresenter = MyPresenter.INSTANCE;
                        JLabel label = (JLabel) e.getSource();
                        myPresenter.moveMade(Integer.toString(label.getX()) +" "+ Integer.toString(label.getY()));
                    }
                });
                panelForBoard.add(fields[j][i]);
            }
        }

    }

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


    private void makeFinalFrame(){
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, panelForBoard);
        setTitle("Go Game");
        setSize(750,750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private class FieldLabel extends JLabel{
        int x;
        int y;

        public void setX(int x){
            this.x = x;
        }

        public void setY(int y){
            this.y = y;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }
    }
}
