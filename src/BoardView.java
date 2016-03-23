import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * View for the board.  Displays tokens and board
 *
 * @version 2.0
 */
public class BoardView extends JPanel {
    private BufferedImage image;    //board's image
    private ArrayList<double[]> tokens = new ArrayList<>(); //array of tokens, may replace with nodes, idk
    private double [] highlight=new double[2];
    private final double nullVal = -100; //used to set double values to "null"
    private final double highlightSize=0.15;

    /**
     * initializes panel.  Loads board's image, and creates a boarder around it
     */
    public BoardView (){ //loads board
        try {
            image = ImageIO.read(new File("boards"+File.separator+"board.png"));
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        highlight[0]=nullVal;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /**
     * Highlights a specified token at the given node
     * @param node node to highlight
     */
    public void highlightNode (Node node){
        highlight[0]=node.getRelX();
        highlight[1]=node.getRelY();
        repaint();
    }


    /**
     * Clears highlight from the highlighted token.
     */
    public void clearHighlight(){
        highlight[0]=nullVal;
        repaint();
    }

    /**
     * Used to display the board, display any tokens, add a highlight on a token, and scale everything when the window
     * is resized
     *
     * {@inheritDoc}
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) { //draws board and tokens
        super.paintComponent(g);
        g.drawImage(image, getWidth() / 2 - getDimension() / 2, 0, getDimension(), getDimension(), null); // see javadoc for more info on the parameters
        if (highlight[0]!=nullVal){             //check if there is a highlight
            g.setColor(new Color(0x00BC0E));    //highlight colour is green
            //draws a circle big enough to be seen from behind a node.
            g.fillOval((int)(highlight[0]*getDimension()+getWidth()/2.0)-offset(highlightSize,20),(int)(highlight[1]*
                    getDimension())-offset(highlightSize,20), offset(highlightSize,10), offset(highlightSize,10));
        }
        for (double[] t:tokens) { //draws each token
            g.setColor(new Color((int)t[0]));
            g.fillOval((int)(t[1]*getDimension()+getWidth()/2.0)-offset(0,20),(int)(t[2]*getDimension())-
                    offset(0, 20),offset(0,10),offset(0,10));
        }
    }

     // calculates the offset to display an element properly
    private int offset (double percent, int divisor){
        return (int)((getDimension()+getDimension()*percent)/divisor);
    }

    /**
     * In order to keep the board square, the minimum of the width and height is used for most calculations
     *
     * @return the smaller value of height and width
     */
    public int getDimension (){
        return Math.min(getHeight(),getWidth());
    }

    /**
     * Used to specify where to place a token, and what colour it should be.
     *
     * @param node node piece was placed on
     */
    public void placeToken(Node node){ //place new token
        double [] tokenInfo= {node.getTopToken().colour,node.getRelX(),node.getRelY()};
        tokens.add(tokenInfo);
        repaint();
    }

    /**
     * Changes to location to draw a token at
     * @param oldNode old location of the token
     * @param newNode location to move token to
     */
    public void moveToken (Node oldNode, Node newNode){
        //extracts coordinates
        double oldX=oldNode.getRelX(), oldY=oldNode.getRelY(), newX=newNode.getRelX(), newY=newNode.getRelY();
        int node = findNode(oldX, oldY); //finds array index
        tokens.get(node)[1]=newX; tokens.get(node)[2]=newY; //sets new coordinates
        clearHighlight();
        repaint();
    }

    /**
     * removes visual token from board
     *
     * @param relX x-coordinate of token
     * @param relY y-coordinate of token
     */
    public void removeToken (double relX, double relY){
        //TODO change parameters to accept a node
        int node = findNode(relX, relY);
        tokens.remove(node);
        repaint();
    }

    //finds a stored node's index based on its x and y coordinate
    private int findNode (double relX, double relY){
        int i=0;
        while (i<tokens.size()){
            if (tokens.get(i)[1]==relX&&tokens.get(i)[2]==relY){
                return i;
            }
            i++;
        }
        return -1;
    }


    public void changeBoardImage (File newFile){
        try {
            image = ImageIO.read(newFile);
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        repaint();
    }

    public void reset(){
        highlight[0]=nullVal;
        tokens.clear();
    }



}

