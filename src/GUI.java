import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;


public class GUI extends JFrame implements ActionListener,MouseListener{

    private BoardView board = new BoardView();              //view for board
    private  String colour1 = "red", colour2 = "blue";      //colour names
    private int colour1Code = 16711680, colour2Code= 255;   //hexcodes for colours
    private JLabel turnIndicator = new JLabel("",JLabel.CENTER);   //label for whose turn
    private JPanel footer = new JPanel();                   //footer panel
    private int turnNumber;                                 //keeps track of whose turn it is
    String gameState="";                                    //game state
    private JPanel header = new JPanel(), left=new JPanel(),right=new JPanel(); //Panels
    private Player [] players = new Player[2];              //player model
    private PlayerView [] playerViews=new PlayerView[2];    //player view


    public GUI (){
        //initializes stuff, adds listeners, sets first player
        header.setLayout(new FlowLayout());
        JButton newGame = new JButton("New Game");
        newGame.setActionCommand("NewG");
        JButton loadGame = new JButton("Load Game");
        loadGame.setActionCommand("OldG");
        newGame.addActionListener(this);
        loadGame.addActionListener(this);
        header.add(newGame);
        header.add(loadGame);
        Random rng = new Random();
        turnNumber= rng.nextInt(2);
        updateTurn();

        //sets up some panel spacing and layout
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        left.add(Box.createRigidArea(new Dimension(100, 0)));
        right.add(Box.createRigidArea(new Dimension(100, 0)));
        footer.add(Box.createRigidArea(new Dimension(0,50)));

        //adds panels
        setTitle("Six Men's Morris");
        board.addMouseListener(this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(board, BorderLayout.CENTER);
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(left,BorderLayout.WEST);
        getContentPane().add(right,BorderLayout.EAST);
        getContentPane().add(footer,BorderLayout.SOUTH);
    }

    //sets up side panels
    private void initSidePanels(){
        if (gameState.equals("New")){ //if new game, sets up labels
            JLabel player1Lable = new JLabel(colour1,JLabel.CENTER), player2Lable = new JLabel(colour2,JLabel.CENTER);
            player1Lable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            player2Lable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            player1Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
            player2Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
            left.add(player1Lable);
            right.add(player2Lable);
        }else if (gameState.equals("Old")) { //if placing tokens, sets up buttons
            JButton player1Button = new JButton(colour1), player2Button = new JButton(colour2);
            player1Button.setActionCommand("player1");
            player2Button.setActionCommand("player2");
            player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
            player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
            player1Button.addActionListener(this);
            player2Button.addActionListener(this);
            left.add(player1Button);
            right.add(player2Button);
        }
        //initializes player view and model
        players[0]=new Player(colour1Code,6);
        players[1]=new Player(colour2Code,6);
        playerViews[0]=new PlayerView(colour1Code,6);
        playerViews[1]=new PlayerView(colour2Code,6);
        left.add(playerViews[0]);
        right.add(playerViews[1]);
    }

    private void updateTurn(){
        //sets font colour and changes JLabel's text to reflect turn
        String text="<html><font color='";
        text += (turnNumber%2==0)?colour1+"'>"+colour1+"'s</font> ":colour2+"'>"+colour2+"'s</font> ";
        text+="turn.</html>";
        //System.out.println(text);
        turnIndicator.setText(text);
        turnIndicator.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    }

    public void actionPerformed (ActionEvent e){ //if buttons are pressed
        if (e.getActionCommand().equals("NewG")||e.getActionCommand().equals("OldG")){
            //updates which mode to start
            gameState=(e.getActionCommand().equals("NewG"))?"New":"Old";
            header.removeAll();
            revalidate();
            header.add(turnIndicator, BorderLayout.NORTH);
            header.revalidate();
            initSidePanels();
            revalidate();
        }else if (e.getActionCommand().equals("player1")){//for placing pieces
            turnNumber=0;
            updateTurn();
            revalidate();
        }else if (e.getActionCommand().equals("player2")){
            turnNumber=1;
            updateTurn();
            revalidate();
        }
    }

    //the following are for mouse location.  Only matters where mouse is released
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if (e.getButton()==1&&!gameState.equals("")){           //if the correct mouse button is pressed and pieces are
                                                                    //being placed
            //TODO send coordinates to model to check for node and such
            if (turnNumber%2==0){                   //if "red" player's turn
                if (players[0].removeToken()){
                    board.placeToken(new Token(colour1Code));
                    playerViews[0].removeToken();
                    //System.out.print("\tplaced red\t");
                }
            }else{                                  //if "blue" player's turn
                if (players[1].removeToken()){
                    board.placeToken(new Token(colour2Code));
                    playerViews[1].removeToken();
                    //System.out.print("\tplaced blue\t");
                }
            }
            if (!gameState.equals("Old"))turnNumber++; //if the next player goes
            if (players[0].getNumTokens()==0&&players[1].getNumTokens()==0)gameState="Move";//checks if all pieces placed
            //System.out.println(gameState);
            updateTurn();
            turnIndicator.revalidate();
            revalidate();
        }
        //System.out.println("Board Width: "+board.getWidth()+" x: "+x+"\tBoard Height: "+board.getHeight()+" y: "+y);
    }

    public static void main (String [] args){
        //sets the GUI look based on OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to apply OS theme to GUI.  Switching to default Java.");
        }
        JFrame gui = new GUI();
        Dimension defaultSize = new Dimension(800,700);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(defaultSize);
        gui.setMinimumSize(defaultSize);
        gui.setVisible(true);

    }
}
