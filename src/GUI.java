import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame{

    //private JPanel colours = new JPanel();
    private JPanel board = new BoardView();

    public GUI (){
        setTitle("Six Men's Morris");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(board, BorderLayout.CENTER);
        getContentPane().add(new JLabel("test"),BorderLayout.NORTH);
        revalidate();
    }




    public static void main (String [] args){
        JFrame gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(new Dimension(800, 600));
        gui.setVisible(true);
    }
}
