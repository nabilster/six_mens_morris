package six_mens_morris_step2;

import javax.swing.*;
import java.awt.*;

public class PlayerView extends JPanel{
    private int numTokens; //number of tokens they have
    private int colour; //colour they have

    public PlayerView(int colour,int numTokens){
        this.numTokens=numTokens;
        this.colour=colour;
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) { //draws the number of tokens left
        super.paintComponent(g);
        g.setColor(new Color(colour));

        for (int i = 0; i<numTokens;i++) {
            g.fillOval(getWidth()/2-25,(getHeight()-50)/numTokens*(i+1)-25,50,50);
        }
    }

    public void removeToken (){
        numTokens--;
        repaint();
    }

}