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
        g.drawImage(image, getWidth() / 2 - getDimension() / 2, 0, getDimension(), getDimension(), null); // see javadoc for more info on the parameters
        for (double[] t:tokens) {
            g.setColor(new Color((int)t[0]));
            g.fillOval((int)(t[1]*getDimension()+getWidth()/2.0)-getDimension()/20,(int)(t[2]*getDimension())-getDimension()/20,getDimension()/10,getDimension()/10);
        }

    }

    public int getDimension (){
        return Math.min(getHeight(),getWidth());
    }


    public void placeToken(Node node){ //place new token
        double [] tokenInfo= {node.getTopToken().colour,node.getRelX(),node.getRelY()};
        tokens.add(tokenInfo);
        repaint();
    }


}

