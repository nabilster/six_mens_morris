import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying a player's remaining tokens and colour (view for player)
 *
 * @version 1.0
 */
public class PlayerView extends JPanel{
    private int numTokens; //number of tokens they have
    private int colour; //colour they have

    /**
     * Initialises player with colour and number tokens to draw
     *
     * @param colour    colour of a player, taken as the hexadecimal colour code converted to an integer
     * @param numTokens number of tokens a player starts with
     */
    public PlayerView(int colour,int numTokens){
        this.numTokens=numTokens;
        this.colour=colour;
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Draws shapes, images, etc. in panel behind any elements (eg. labels, other panels, etc.)
     *
     * {@inheritDoc}
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) { //draws the number of tokens left
        super.paintComponent(g);
        g.setColor(new Color(colour));

        for (int i = 0; i<numTokens;i++) {
            g.fillOval(getWidth()/2-25,(getHeight()-50)/numTokens*(i+1)-25,50,50);
        }
    }

    /**
     * specifies that one less token should be drawn
     */
    public void removeToken (){
        numTokens--;
        repaint();
    }

}