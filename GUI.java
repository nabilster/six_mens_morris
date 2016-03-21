package six_mens_morris_step2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//TODO check if no moves are possible (Pieces are Trapped!)

public class GUI extends JFrame implements ActionListener,MouseListener,KeyListener{

	private BoardView board = new BoardView();              //view for board
	private  String colour1 = "red", colour2 = "blue";      //colour names
	private int colour1Code = 16711680, colour2Code= 255, colour3Code= 179255179;  //hexcodes for colours
	private JLabel turnIndicator = new JLabel("",JLabel.CENTER);   //label for whose turn
	private JPanel footer = new JPanel();                   //footer panel
	private int turnNumber;                                 //keeps track of whose turn it is
	String gameState="";                                    //game state
	private JPanel header = new JPanel(), left=new JPanel(),right=new JPanel(); //Panels
	private Player [] players = new Player[2];              //player model
	private PlayerView [] playerViews=new PlayerView[2];    //player view
	private Node [] nodes = new Node [16];
	private int selectedNumber;
	private int selectedNumber2;
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
			//TODO Add can't play game
		}catch (IndexOutOfBoundsException e2){
			//TODO Error message
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

			JButton player1Button = new JButton(colour1), player2Button = new JButton(colour2), check= new JButton("Check/Begin Game");;
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
		else if (e.getActionCommand().equals("check")){   //when clicked run check 
			if(gameState.equals("New")){ // if new game
				Check.checkScreen("New",nodes,players);
				Check.tokenoverflowcheck(nodes,colour3Code,board);//check for tokenstack

			}
			else{//if old game
				Check.checkScreen("Old",nodes,players);
				Check.tokenoverflowcheck(nodes,colour3Code,board);//check for tokenstack
			}

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
					"   \tTurn Number: "+turnNumber+"\tGame State: "+gameState);

			if (gameState.equals("New")) {
				//being placed
				placePiece(turnNumber % 2, nodeNumber); //piece placed

				//if (!gameState.equals("Old"))turnNumber++; //if the next player goes
				if (players[0].getNumTokens() == 0 && players[1].getNumTokens() == 0)
					gameState = "Move";//checks if all pieces placed

				if (checkmills(nodeNumber) && errorcatch!=1){ 
					previousnode=nodeNumber;
					gameState="MillMade";
				}
				errorcatch=0; // used to catch error if place pieced so you dont end up removing 2 pieces
				updateTurn();

			}else if (gameState.equals("Move")){ //highlights piece to be moved

				//TODO insert here check for if any legal move
				if (nolegalmoves(colour1Code)){
					JOptionPane.showMessageDialog(null, "Blue Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
				if(nolegalmoves(colour2Code)){
					JOptionPane.showMessageDialog(null, "Red Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}


				int numberOfRedTokens=0;
				int numberOfBlueTokens=0;
				for (int i=0; i<nodes.length-1; i++){
					if (nodes[i].getNumberTokens()==1 && nodes[i].getTokencolour()==colour1Code){
						numberOfRedTokens++;
					}if(nodes[i].getNumberTokens()==1 && nodes[i].getTokencolour()==colour2Code)
						numberOfBlueTokens++;
				}
				redTokens=numberOfRedTokens;
				blueTokens=numberOfBlueTokens;
			
				if (canSelect(nodeNumber)){
					selectedNumber=nodeNumber;
					board.highlightNode(nodes[nodeNumber]);
					gameState="Place";	
				}

			}else if (gameState.equals("Place")){ //allows moving of highlighted piece to another place and updates the turn so other player can move
				if (nodes[selectedNumber].isConnectedTo(nodeNumber)&&nodes[nodeNumber].getNumberTokens()==0){ // if node is connected and node want to move to has no token
					nodes[selectedNumber].moveToken(nodes[nodeNumber]);
					board.moveToken(nodes[selectedNumber].getRelX(), nodes[selectedNumber].getRelY(),
							nodes[nodeNumber].getRelX(), nodes[nodeNumber].getRelY());

					// determines after placing token at new location there is a mill
					if (checkmills(nodeNumber)){
						previousnode=nodeNumber;
						gameState="MillMade";

					}else{
						turnNumber++;
						updateTurn();
						gameState="Move";
					}
				}else if (nodes[selectedNumber]==nodes[nodeNumber]){ // removes highlighted node, allows to click and move another piece
					selectedNumber=-1;
					board.clearHighlight();
					gameState="Move";
				}
			}else if (gameState.equals("MillMade")){
				selectedNumber2=nodeNumber;
				removePiece(turnNumber % 2,selectedNumber2);

			}
		}
	}

	private boolean nolegalmoves (int colour){
		int tokenswithcolour= 0;
		int cantmove=0;

		for (int i=0; i<nodes.length; i++){
			// if node has token and colour of token is same as designated colour
			if (nodes[i].getNumberTokens()==1 && nodes[i].getTokencolour()==colour){
				tokenswithcolour++;
				//setting up checking every node around chosen if node exists or has token
				boolean checkleft=(nodes[i].getLeft()==-1)||(nodes[i].getLeft()!=-1 && nodes[nodes[i].getLeft()].getNumberTokens()==1);
				boolean checkright=(nodes[i].getRight()==-1)||(nodes[i].getRight()!=-1 && nodes[nodes[i].getRight()].getNumberTokens()==1);
				boolean checkup=(nodes[i].getUp()==-1)||(nodes[i].getUp()!=-1 &&nodes[nodes[i].getUp()].getNumberTokens()==1);
				boolean checkdown=(nodes[i].getDown()==-1)||(nodes[i].getDown()!=-1 && nodes[nodes[i].getDown()].getNumberTokens()==1);

				//check left exists or has token
				if (checkleft && checkright && checkup && checkdown){
					cantmove++;
				}

			}
		}
		if (tokenswithcolour==cantmove){
			return true;
		}else{
			return false;
		}

	}

	private int numbColourToken (int colour) {
		int numberOfColour=0;

		for (int i=0; i<nodes.length-1; i++){
			if (nodes[i].getNumberTokens()==1 && nodes[i].getTokencolour()==colour){
				numberOfColour++;
			}
		}
		return numberOfColour;
	}


	private boolean canSelect (int nodeNumber) {
		if (nodes [nodeNumber].getNumberTokens()<1)return false;
		if (turnNumber%2==0&&nodes[nodeNumber].getTokencolour()==colour2Code)return false;
		else if (turnNumber%2==1&&nodes[nodeNumber].getTokencolour()==colour1Code)return false;
		else return true;
	}


	private void placePiece (int pNum, int NN){
		int colour = (pNum==0)?colour1Code:colour2Code;
		if (players[pNum].removeToken()&&nodes[NN].getNumberTokens()==0) {
			nodes[NN].addToken(colour);
			board.placeToken(nodes[NN]);
			playerViews[pNum].removeToken();
			turnNumber++;
		}else{
			players[pNum].addToken();
			if (nodes[NN].getNumberTokens()>0){
				errorcatch=1;
				JOptionPane.showMessageDialog(null, "Piece is already placed at selected node","Six Men's Morris",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void removePiece (int pNum, int NN ){

		if (nodes[NN].getNumberTokens()!=0){
			int millcolour=nodes[previousnode].getTokencolour();
			int chosencolour=nodes[NN].getTokencolour();

			if (millcolour!=chosencolour && (checkmills(NN)!=true || numbColourToken(chosencolour)<=3)){

				board.removeToken(nodes[NN].getRelX(), nodes[NN].getRelY());
				nodes[NN].removeTopToken();

				if (chosencolour==colour1Code){ // if chosencolour red
					redTokens--;
					
				}
				else if (chosencolour==colour2Code){// if chosencolour blue
					blueTokens--;
					
				}


				if (numbColourToken(colour1Code)==2 && redTokens!=0 ){
					JOptionPane.showMessageDialog(null, "Blue Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}else if(numbColourToken(colour2Code)==2 && blueTokens!=0){
					JOptionPane.showMessageDialog(null, "Red Wins", "Six Men's Morris", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);;
				}
				else if (players[0].getNumTokens() == 0 && players[1].getNumTokens() == 0){
					turnNumber++;
					updateTurn();
					gameState="Move";
				}
				else{
					gameState="New";
				}
			}
			else{
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
				throw new IllegalStateException("STOP TAMPERING");//TODO change to custom exception?
			}if (fin.hasNextLine()){
				gameState = fin.nextLine();
			}else{
				throw new IllegalStateException("STOP TAMPERING");
			}while (fin.hasNextLine()){
				line=fin.nextLine().split("|");
				nodeNumber=Integer.parseInt(line[0]);
				nodes[nodeNumber].addToken(Integer.parseInt(line[1]));
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

	// checks if a mill exists, if it does it returns true
	private boolean checkmills(int NN){
		//	if (nodes[selectedNumber].isConnectedTo(nodeNumber)&&nodes[nodeNumber].getNumberTokens()==0){ // if node is connected and node want to move to has no token
		// seeing if nodes exist around current node	

		int left=nodes[NN].getLeft();
		int right=nodes[NN].getRight();
		int up=nodes[NN].getUp();
		int down=nodes[NN].getDown();
		int currentcolour=nodes[NN].getTokencolour();

		if ((up!=-1 && nodes[up].getNumberTokens()==1)){ // if node above exists and has token
			if(down!=-1 && nodes[down].getNumberTokens()==1){ // if node below exists and has token
				int upcolour=nodes[up].getTokencolour();
				int downcolour=nodes[down].getTokencolour();
				if(currentcolour==upcolour && currentcolour==downcolour){
					System.out.println("vertical mill start mid");

					return true;
				}
			}
			else if(down==-1){ // if node below doesnt exist check up
				int nextup=nodes[up].getUp();
				if(nodes[up].getUp()!=-1&&nodes[nextup].getNumberTokens()==1){
					int upcolour= nodes[up].getTokencolour();
					int nextupcolour= nodes[nextup].getTokencolour();
					if(currentcolour==upcolour && currentcolour==nextupcolour){
						System.out.println("vertical mill start bottom");

						return true;
					}
				}
			}
		}

		else if (up==-1){ //if node above doesnt exist
			if (down!=-1){
				int nextdown=nodes[down].getDown();
				if(nodes[down].getDown()!=-1 &&nodes[down].getNumberTokens()==1 && nodes[nextdown].getNumberTokens()==1){
					int downcolour= nodes[down].getTokencolour();
					int nextdowncolour= nodes[nextdown].getTokencolour();
					if(currentcolour==downcolour && currentcolour==nextdowncolour){
						System.out.println("vertical mill start top");

						return true;
					}
				}
			}
		}

		if ((left!=-1 && nodes[left].getNumberTokens()==1)){
			if(right!=-1 && nodes[right].getNumberTokens()==1){
				int leftcolour=nodes[left].getTokencolour();
				int rightcolour=nodes[right].getTokencolour();
				if(currentcolour==leftcolour && currentcolour==rightcolour){
					System.out.println("horizontal mill start mid");

					return true;
				}

			}
			else if(right==-1){
				int nextleft=nodes[left].getLeft();
				if(nodes[left].getLeft()!=-1 && nodes[nextleft].getNumberTokens()==1){
					int leftcolour= nodes[left].getTokencolour();
					int nextleftcolour= nodes[nextleft].getTokencolour();
					if(currentcolour==leftcolour && currentcolour==nextleftcolour){
						System.out.println("horizontal mill start right");

						return true;
					}
				}

			}
		}
		else if (left==-1){
			if (right!=-1){
				int nextright=nodes[right].getRight();
				if(nodes[right].getRight()!=-1 && nodes[right].getNumberTokens()==1 && nodes[nextright].getNumberTokens()==1){
					int rightcolour= nodes[right].getTokencolour();
					int nextrightcolour= nodes[nextright].getTokencolour();
					if(currentcolour==rightcolour && currentcolour==nextrightcolour){
						System.out.println("horizontal mill start left");

						return true;
					}
				}
			}
		}

		return false;
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
