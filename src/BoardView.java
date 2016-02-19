import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardView extends JPanel {
    private BufferedImage image;    //board's image
    private ArrayList<Token> tokens = new ArrayList<>(); //array of tokens, may replace with nodes, idk

    public BoardView (){ //loads board
        try {
            image = ImageIO.read(new File("board.png"));
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    protected void paintComponent(Graphics g) { //draws board and tokens
        super.paintComponent(g);
        int dimension=(getWidth()<getHeight())?getWidth():getHeight();
        g.drawImage(image, getWidth()/2-dimension/2, 0, dimension,dimension,null); // see javadoc for more info on the parameters
        for (Token t:tokens) {
            g.setColor(new Color(t.getColour()));
            g.fillOval(t.getX()-dimension/20,t.getY()-dimension/20,dimension/10,dimension/10);
        }

    }


    public void placeToken(Token newToken){ //place new token
        tokens.add(newToken);
        repaint();
    }



}

