import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardView extends JPanel {
    private BufferedImage image;    //board's image
    private ArrayList<double[]> tokens = new ArrayList<>(); //array of tokens, may replace with nodes, idk
    private double [] highlight=new double[2];
    private final double nullVal = -100; //used to set double values to "null"
    private final double highlightSize=0.15;

    public BoardView (){ //loads board
        try {
            image = ImageIO.read(new File("board.png"));
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        highlight[0]=nullVal;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void highlightNode (Node node){
        highlight[0]=node.getRelX();
        highlight[1]=node.getRelY();
        repaint();
    }

    public void clearHighlight(){
        highlight[0]=nullVal;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) { //draws board and tokens
        super.paintComponent(g);
        g.drawImage(image, getWidth() / 2 - getDimension() / 2, 0, getDimension(), getDimension(), null); // see javadoc for more info on the parameters
        if (highlight[0]!=nullVal){
            g.setColor(new Color(0x00BC0E));
            g.fillOval((int)(highlight[0]*getDimension()+getWidth()/2.0)-offset(highlightSize,20),(int)(highlight[1]*
                    getDimension())-offset(highlightSize,20), offset(highlightSize,10), offset(highlightSize,10));
        }
        for (double[] t:tokens) {
            g.setColor(new Color((int)t[0]));
            g.fillOval((int)(t[1]*getDimension()+getWidth()/2.0)-offset(0,20),(int)(t[2]*getDimension())-
                    offset(0, 20),offset(0,10),offset(0,10));
        }
    }

    private int offset (double percent, int divisor){
        return (int)((getDimension()+getDimension()*percent)/divisor);
    }

    public int getDimension (){
        return Math.min(getHeight(),getWidth());
    }


    public void placeToken(Node node){ //place new token
        double [] tokenInfo= {node.getTopToken().colour,node.getRelX(),node.getRelY()};
        tokens.add(tokenInfo);
        repaint();
    }

    public void moveToken (Node oldNode, Node newNode){
        int i=0;
        double oldX=oldNode.getRelX(), oldY=oldNode.getRelY(), newX=newNode.getRelX(), newY=newNode.getRelY();
        while (i<tokens.size()){
            if (tokens.get(i)[1]==oldX&&tokens.get(i)[2]==oldY){
                tokens.get(i)[1]=newX; tokens.get(i)[2]=newY;
                clearHighlight();
                repaint();
                break;
            }
            i++;
        }
    }

    public void changeBoardImage (File newFile){
        try {
            image = ImageIO.read(newFile);
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        repaint();
    }


}

