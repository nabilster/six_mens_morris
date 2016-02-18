import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardView extends JPanel {
    private BufferedImage image;

    public BoardView (){
        try {
            image = ImageIO.read(new File("board.png"));
        }catch (IOException e){
            add (new JLabel("Board could not be loaded"));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int dimension=(getWidth()<getHeight())?getWidth():getHeight();
        Graphics2D util = (Graphics2D)g;
        util.translate(this.getWidth()/2, this.getHeight()/2);
        util.translate(-dimension/2, -dimension/2);
        g.drawImage(image, 0, 0, dimension,dimension,null); // see javadoc for more info on the parameters
    }

}

