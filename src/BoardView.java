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
 * @version 1.0
 */
public class BoardView extends JPanel {
    private BufferedImage image;    //board's image
    private ArrayList<double[]> tokens = new ArrayList<>(); //array of tokens, may replace with nodes, idk

    /**
     * initializes panel.  Loads board's image, and creates a boarder around it
     */
    public BoardView (){
        try {
            image = ImageIO.read(new File("board.png"));
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /**
     * Used to display the board, display any tokens, and scale everything when the window is resized
     *
     * {@inheritDoc}
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) { //draws board and tokens
        super.paintComponent(g);
        g.drawImage(image, getWidth() / 2 - getDimension() / 2, 0, getDimension(), getDimension(), null); // see javadoc for more info on the parameters
        for (double[] t:tokens) {
            g.setColor(new Color((int)t[0]));
            g.fillOval((int)(t[1]*getDimension()+getWidth()/2.0)-getDimension()/20,(int)(t[2]*getDimension())-getDimension()/20,getDimension()/10,getDimension()/10);
        }

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
        double [] tokenInfo= {node.getTopToken().getColour(),node.getRelX(),node.getRelY()};//get's the node's token's colour, as well as the node's coordinates
        tokens.add(tokenInfo);
        repaint();
    }


}

