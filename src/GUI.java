import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class GUI extends JFrame implements ActionListener,MouseListener,KeyListener{

	private BoardView board = new BoardView();              //view for board
	private  String colour1 = "red", colour2 = "blue";      //colour names
	private int colour1Code = 16711680, colour2Code= 255;  //hexcodes for colours
	private JLabel turnIndicator = new JLabel("",JLabel.CENTER);   //label for whose turn
	private JPanel footer = new JPanel();                   //footer panel
	private int turnNumber;                                 //keeps track of whose turn it is
	private String gameState="";                                    //game state
	private JPanel header = new JPanel(), left=new JPanel(),right=new JPanel(); //Panels
	private Player [] players = new Player[2];              //player model
	private PlayerView [] playerViews=new PlayerView[2];    //player view
	private Node [] nodes = new Node [16];
	private int selectedNumber;
    private int previousnode;
	private int blueTokens=0;
	private int redTokens=0;
	private int errorcatch=1;
	private int Contra=0;



	public GUI (){
		initializeNodes();

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
        getContentPane().addKeyListener(this);
        getContentPane().setFocusable(true);
	}

	//these are now relative to the center of the board panel due to the fact that the board image is centered with the
	//panel, this is the easier way for tracking,  although is does cause some more complicated math
	private void initializeNodes(){
        Scanner fin;
        String [] tempString;
        double [] tempDouble;
        ArrayList<double []> nodeInfo = new ArrayList<>();
        try{
            fin = new Scanner (new File("board.smm"));
            while (fin.hasNextLine()){
                tempString=fin.nextLine().split("/");
                tempDouble=new double[tempString.length];
                for (int i = 0;i<tempDouble.length;i++){
                    tempDouble[i]=Double.parseDouble(tempString[i]);
                }
                nodeInfo.add(tempDouble);
            }
            for (int i = 0; i<nodes.length;i++){
                nodes[i]=new Node(nodeInfo.get(i)[0],nodeInfo.get(i)[1],i);
            }
            for (int i = 0; i<nodes.length; i++){
                Node [] nodeArray = new Node[4];
                for (int j=0; j<nodeArray.length; j++){
                    if (nodeInfo.get(i)[j+2]!=-1){nodeArray[j]=nodes[(int)nodeInfo.get(i)[j+2]];}
                    else {nodeArray[j]=null;}
                }
                nodes[i].setConnectedNodes(nodeArray);
            }
        }catch (FileNotFoundException e1){
            System.out.println("CANNOT READ FILE");
        }catch (IndexOutOfBoundsException e2){
            System.out.println("FILE CONTENT ERROR");
        }
	}

	//sets up side and bottom panels
	private void initSidePanels(){
		if (gameState.equals("New")){ //if new game, sets up labels

			JButton check= new JButton("Check Board Setup");   // add check button to run check on gamestate
			check.setActionCommand("check");
			check.setAlignmentX(Component.CENTER_ALIGNMENT);
			check.addActionListener(this);
			footer.add(check);

			JLabel player1Label = new JLabel(colour1,JLabel.CENTER), player2Label = new JLabel(colour2,JLabel.CENTER);
			player1Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			player2Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
			player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
			left.add(player1Label);
			right.add(player2Label);
		}else if (gameState.equals("Old")) { //if placing tokens, sets up buttons

			JButton player1Button = new JButton(colour1), player2Button = new JButton(colour2), check= new JButton("Check/Begin Game");
			player1Button.setActionCommand("player1");
			player2Button.setActionCommand("player2");
			check.setActionCommand("check");
			player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
			player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
			check.setAlignmentX(Component.CENTER_ALIGNMENT);
			player1Button.addActionListener(this);
			player2Button.addActionListener(this);
			check.addActionListener(this);
			left.add(player1Button);
			right.add(player2Button);
			footer.add(check);
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
        turnNumber++;
		//sets font colour and changes JLabel's text to reflect turn
		String text="<html><font color='";
		text += (turnNumber%2==0)?colour1+"'>"+colour1+"'s</font> ":colour2+"'>"+colour2+"'s</font> ";
		text+="turn.</html>";
		turnIndicator.setText(text);
		turnIndicator.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		turnIndicator.revalidate();
		revalidate();
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
		}else if (e.getActionCommand().equals("player2")) {
			turnNumber = 1;
			updateTurn();
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
        int nodeNumber = findNodeNumber(x, y);

        if (e.getButton()==1&&!gameState.equals("")&&nodeNumber!=-1){           //if the correct mouse button is pressed and pieces are

            System.out.println("Board Width: "+board.getWidth()+" x: "+x+"\tBoard Height: "+board.getHeight()+" y: "+y+ "" +
                    "   \tTurn Number: "+turnNumber+"\tGame State: "+gameState+"\tBlue: "+blueTokens+"\tRed: "+redTokens);

            if (gameState.equals("New")) {
                //being placed
                placePiece(turnNumber % 2, nodeNumber); //piece placed

                if (players[0].getNumTokens() == 0 && players[1].getNumTokens() == 0) {
                    gameState = "Move";//checks if all pieces placed
                    for (int i=0; i<nodes.length-1; i++) {
                        if (nodes[i].getNumberTokens() >0 && nodes[i].getTokencolour() == colour1Code) {
                            redTokens++;
                        } else if (nodes[i].getNumberTokens() >0 && nodes[i].getTokencolour() == colour2Code)
                            blueTokens++;
                    }
                }

                if (nodes[nodeNumber].inMill() && errorcatch!=1){
                    previousnode=nodeNumber;
                    gameState="MillMade";
                    showRemove();
                }else{
                    updateTurn();
                }
                errorcatch=0; // used to catch error if place pieced so you dont end up removing 2 pieces

            }else if (gameState.equals("Move")){ //highlights piece to be moved

                if (nolegalmoves(colour1Code)){
                    JOptionPane.showMessageDialog(null, "Blue Wins by default", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                if(nolegalmoves(colour2Code)){
                    JOptionPane.showMessageDialog(null, "Red Wins by default", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }

                if (canSelect(nodeNumber)){
                    selectedNumber=nodeNumber;
                    board.highlightNode(nodes[nodeNumber]);
                    gameState="Place";
                }

            }else if (gameState.equals("Place")){ //allows moving of highlighted piece to another place and updates the turn so other player can move
                if (nodes[selectedNumber].isConnectedTo(nodeNumber)&&nodes[nodeNumber].getNumberTokens()==0){ // if node is connected and node want to move to has no token
                    nodes[selectedNumber].moveToken(nodes[nodeNumber]);
                    board.moveToken(nodes[selectedNumber], nodes[nodeNumber]);

                    // determines after placing token at new location there is a mill
                    if (nodes[nodeNumber].inMill()){
                        previousnode=nodeNumber;
                        gameState="MillMade";
                        showRemove();
                    }else{
                        gameState="Move";
                        updateTurn();
                    }
                }else if (nodes[selectedNumber]==nodes[nodeNumber]){ // removes highlighted node, allows to click and move another piece
                    selectedNumber=-1;
                    board.clearHighlight();
                    gameState="Move";
                }else{
                    JOptionPane.showMessageDialog(null, "Cannot move piece to selected node", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
                }
            }else if (gameState.equals("MillMade")){
                removePiece(nodeNumber);

            }
        }
    }

    private void showRemove(){
        String text="<html><font color='";
        text += (turnNumber%2==0)?colour1+"'>"+colour1+"</font> ":colour2+"'>"+colour2+"</font> ";
        text+="to remove one of <font color='";
        text += (turnNumber%2== 1) ? colour1+"'>"+colour1+"'s</font> " : colour2 + "'>" + colour2 + "'s</font> ";
        text+="tokens turn.</html>";
        turnIndicator.setText(text);
        turnIndicator.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        turnIndicator.revalidate();
        revalidate();
    }


	private boolean nolegalmoves (int colour){
		int tokenswithcolour= 0;
		int cantmove=0;

        for (Node node : nodes) {
            // if node has token and colour of token is same as designated colour
            if (node.getNumberTokens() == 1 && node.getTokencolour() == colour) {
                tokenswithcolour++;
                //setting up checking every node around chosen if node exists or has token
                boolean checkleft = (node.getLeft() == null) || (node.getLeft().getNumberTokens() == 1);
                boolean checkright = (node.getRight() == null) || (node.getRight().getNumberTokens() == 1);
                boolean checkup = (node.getUp() == null) || (node.getUp().getNumberTokens() == 1);
                boolean checkdown = (node.getDown() == null) || (node.getDown().getNumberTokens() == 1);

                //check left exists or has token
                if (checkleft && checkright && checkup && checkdown) {
                    cantmove++;
                }

            }
        }
        return tokenswithcolour == cantmove;

	}

	private int numbColourToken (int colour) {
		int numberOfColour=0;

        for (Node node : nodes) {
            if (node.getNumberTokens() == 1 && node.getTokencolour() == colour) {
                numberOfColour++;
            }
        }
		return numberOfColour;
	}


	private boolean canSelect (int nodeNumber) {
		if (nodes [nodeNumber].getNumberTokens()<1)return false;
		if (turnNumber%2==0&&nodes[nodeNumber].getTokencolour()==colour2Code)return false;
		else return !(turnNumber % 2 == 1 && nodes[nodeNumber].getTokencolour() == colour1Code);
	}


	private void placePiece (int pNum, int NN){
		int colour = (pNum==0)?colour1Code:colour2Code;
		if (players[pNum].removeToken()&&nodes[NN].getNumberTokens()==0) {
			nodes[NN].addToken(colour);
			board.placeToken(nodes[NN]);
			playerViews[pNum].removeToken();
        }else{
			players[pNum].addToken();
			if (nodes[NN].getNumberTokens()>0){
				errorcatch=1;
				JOptionPane.showMessageDialog(null, "Piece is already placed at selected node","Six Men's Morris",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void removePiece (int NN ){ //get rid of pNum later

        if (nodes[NN].getNumberTokens()!=0){
			int millcolour=nodes[previousnode].getTokencolour();
			int chosencolour=nodes[NN].getTokencolour();

            System.out.println("Colour: "+(millcolour!=chosencolour)+" !inMill:"+(!nodes[NN].inMill())+" " +
                    "onlyMills:"+onlyMills(chosencolour)+" number:"+(numbColourToken(chosencolour)<=3));

			if (millcolour!=chosencolour && ((!nodes[NN].inMill()||onlyMills(chosencolour)) || numbColourToken(chosencolour)<=3)){

				board.removeToken(nodes[NN].getRelX(), nodes[NN].getRelY());
				nodes[NN].removeTopToken();

				if (chosencolour==colour1Code && redTokens!=0){ // if chosencolour red
					redTokens--;
				}else if (chosencolour==colour2Code && redTokens != 0){// if chosencolour blue
					blueTokens--;
				}


				if (numbColourToken(colour1Code)==2 && redTokens!=0 ){
					JOptionPane.showMessageDialog(null, "Blue Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}else if(numbColourToken(colour2Code)==2 && blueTokens!=0){
					JOptionPane.showMessageDialog(null, "Red Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
                }
                if (blueTokens==0||redTokens==0){
					gameState="New";
				}else{
                    gameState="Move";
                }
                updateTurn();
            }else{
				JOptionPane.showMessageDialog(null, "Need to Choose other player Piece to remove", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null, "Remove opponent piece", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);

		}

	}

	private int findNodeNumber (int x, int y){ // finds the node number where u have selected
		int nodeNumber=-1;
		double relX = (x-(board.getWidth()/2.0))/board.getDimension();
		for (int i = 0; i<nodes.length;i++){
			if (nodes[i].inRange(relX,((double)y/board.getDimension())))nodeNumber=i;
		}
		return nodeNumber;
	}

	private void saveGame(String filename){
		try{
			PrintWriter fout = new PrintWriter(new File(filename+".sav"));
			fout.println(turnNumber % 2);
			fout.println(gameState);
			for (Node node: nodes){
				while (node.getNumberTokens()>0){
					fout.println(node.getNodeNumber() + "|" + node.getTokencolour());
					node.removeTopToken();
				}
			}
			fout.close();
		}catch (IOException e){
			System.out.println("Error writing to save file!");
		}
	}

	private void loadGame (String filename){
		String [] line= new String[2];
		int nodeNumber;
		try{
			Scanner fin = new Scanner(new File (filename+".txt"));
			if (fin.hasNextLine()){
				line[0] = fin.nextLine();
				turnNumber=Integer.parseInt(line[0]);
			}else {
				throw new IllegalStateException("STOP TAMPERING");
			}if (fin.hasNextLine()){
				gameState = fin.nextLine();
			}else{
				throw new IllegalStateException("STOP TAMPERING");
			}while (fin.hasNextLine()){
				line=fin.nextLine().split("|");
				nodeNumber=Integer.parseInt(line[0]);
				nodes[nodeNumber].addToken(Integer.parseInt(line[1]));
			}
            if (!gameState.equals("New")){
                for (int i=0; i<nodes.length-1; i++) {
                    if (nodes[i].getNumberTokens() >0 && nodes[i].getTokencolour() == colour1Code) {
                        redTokens++;
                    } else if (nodes[i].getNumberTokens() >0 && nodes[i].getTokencolour() == colour2Code)
                        blueTokens++;
                }
            }
		}catch (FileNotFoundException e){
			System.out.println("Error reading from file!");
		}catch (NumberFormatException e){
			System.out.println("Save file corrupted.  Cannot load fle.  (Error code: 314159265");
		}catch (IllegalStateException e){
			System.out.println("Save file corrupted.  Cannot load file.  (Error code: 11235813");
		}catch (IndexOutOfBoundsException e){
			System.out.println("Save file not compatible with game set-up (board.smm has been modified).");
		}catch (Exception e){
			System.out.print("Program has encountered an unknown error: ");
			System.out.println(e);
		}
	}



    private boolean onlyMills (int colour){
        int numMills=0;
        int [] locations = {1, 4, 6, 9, 11, 14};
        for (int i: locations){
            if (nodes[i].getTokencolour()==colour&&nodes[i].inMill()){numMills++;}
        }
        return (numMills==2);
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

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		boolean badKey=false;
		switch (Contra){
		case 0:case 1:  if (keyCode==KeyEvent.VK_UP) {Contra++;}
		else {badKey=true;}
		break;
		case 2:case 3:  if (keyCode==KeyEvent.VK_DOWN) {Contra++;}
		else {badKey=true;}
		break;
		case 4:case 6:  if (keyCode==KeyEvent.VK_LEFT) {Contra++;}
		else {badKey=true;}
		break;
		case 5:case 7:  if (keyCode==KeyEvent.VK_RIGHT) {Contra++;}
		else {badKey=true;}
		break;
		case 8:  if (keyCode==KeyEvent.VK_B) {Contra++;}
		else {badKey=true;}
		break;
		case 9:  if (keyCode==KeyEvent.VK_A) {Contra++;}
		else {badKey=true;}
		break;
		case 10:  if (keyCode==KeyEvent.VK_ENTER) {changeBoard(); Contra=0;}
		else {badKey=true;}
		break;
		}
		if (badKey){badKey=false; Contra=0;}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public void changeBoard (){
		try {
			File folder = new File("."+File.separator+"Contra");
			File [] listOfFiles = folder.listFiles();
			Random rng = new Random();
			int image = rng.nextInt(listOfFiles.length);
			board.changeBoardImage(listOfFiles[image]);
		}catch (Exception ignored){}
	}
}
