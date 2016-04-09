import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

//TODO FIX LOAD
public class GUI extends JFrame implements ActionListener, MouseListener, KeyListener {

    private int colour1Code = 16711680, colour2Code = 255; // hexcodes for colours
    private int turnNumber; // keeps track of whose turn it is
    private int selectedNumber;
    private int previousNode;
    private int blueTokens = 0;
    private int redTokens = 0;
    private int Contra = 0;
    private String colour1 = "red", colour2 = "blue"; // colour names
    private PlayerView[] playerViews = new PlayerView[2]; // player view
    private Player[] players = new Player[2]; // player model
    private Node[] nodes = new Node[16];
    private BoardView board = new BoardView(); // view for board
    private JPanel footer = new JPanel(); // footer panel
    private JPanel header = new JPanel(), left = new JPanel(), right = new JPanel(); // Panels
    private JLabel turnIndicator = new JLabel("", JLabel.CENTER); // label for whose turn
    private JButton saveGame, loadGame, save1, save2,save3, vsHuman, vsAI;                      //vsHuman and vsAI*
    private GameStates gameState = GameStates.NONE; // game state
    private int aiTurnNumber = -1;   //-1 = off, 0 or 1 otherwise                               //*
    private Random rng = new Random();

    //used as constants for what state the game is in
    private enum GameStates{
        NONE("NONE"), START("START"), MOVE("MOVE"), PLACE("PLACE"), MILLMADE("MILLMADE");
        private String state;
        GameStates (String state){this.state=state;}
        public String toString (){return state;}
    }

    /**
     * Initializes all the swing components
     */
    public GUI() {
        initializeNodes();
        // initializes stuff, adds listeners, sets first player
        header.setLayout(new FlowLayout());
        JButton newGame = new JButton("New Game");
        loadGame = new JButton("Load Game");
        save1 = new JButton("Game 1");
        save2 = new JButton("Game 2");
        save3 = new JButton("Game 3");
        vsHuman = new JButton("VS Human");
        vsAI = new JButton("VS Computer");
        newGame.setActionCommand("NewG");
        loadGame.setActionCommand("loadgame");
        vsHuman.setActionCommand("human");
        vsAI.setActionCommand("ai");
        newGame.addActionListener(this);
        loadGame.addActionListener(this);
        save1.addActionListener(this);
        save2.addActionListener(this);
        save3.addActionListener(this);
        vsHuman.addActionListener(this);
        vsAI.addActionListener(this);
        header.add(newGame);
        header.add(loadGame);
        turnNumber = rng.nextInt(2);
        updateTurn();
        // sets up some panel spacing and layout
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        left.add(Box.createRigidArea(new Dimension(100, 0)));
        right.add(Box.createRigidArea(new Dimension(100, 0)));
        footer.add(Box.createRigidArea(new Dimension(0, 50)));
        // adds panels
        setTitle("Six Men's Morris");
        board.addMouseListener(this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(board, BorderLayout.CENTER);
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(right, BorderLayout.EAST);
        getContentPane().add(footer, BorderLayout.SOUTH);
        getContentPane().addKeyListener(this);
        getContentPane().setFocusable(true);
    }

    //sets up nodes on board
    private void initializeNodes() {
        Scanner fin;
        String[] tempString;
        double[] tempDouble;
        ArrayList<double[]> nodeInfo = new ArrayList<>();
        try {
            fin = new Scanner(new File("board.smm"));
            while (fin.hasNextLine()) {
                tempString = fin.nextLine().split("/");
                tempDouble = new double[tempString.length];
                for (int i = 0; i < tempDouble.length; i++) {
                    tempDouble[i] = Double.parseDouble(tempString[i]);
                }
                nodeInfo.add(tempDouble);
            }
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new Node(nodeInfo.get(i)[0], nodeInfo.get(i)[1], i);
            }
            for (int i = 0; i < nodes.length; i++) {
                Node[] nodeArray = new Node[4];
                for (int j = 0; j < nodeArray.length; j++) {
                    if (nodeInfo.get(i)[j + 2] != -1) {
                        nodeArray[j] = nodes[(int) nodeInfo.get(i)[j + 2]];
                    } else {
                        nodeArray[j] = null;
                    }
                }
                nodes[i].setConnectedNodes(nodeArray);
            }
        } catch (FileNotFoundException e1) {
            System.out.println("CANNOT READ FILE");
        } catch (IndexOutOfBoundsException e2) {
            System.out.println("FILE CONTENT ERROR");
        }
    }

    // sets up side and bottom panels
    private void initExtraPanels() {
        JLabel player1Label = new JLabel(colour1, JLabel.CENTER), player2Label = new JLabel(colour2, JLabel.CENTER);
        player1Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        player2Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(player1Label);
        right.add(player2Label);
        saveGame = new JButton("Save");
        saveGame.setActionCommand("savegame");
        saveGame.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadGame.setAlignmentX(Component.RIGHT_ALIGNMENT);
        saveGame.addActionListener(this);
        footer.add(saveGame);
        footer.add(loadGame);

        // initializes player view and model
        players[0] = new Player(colour1Code, 6);
        players[1] = new Player(colour2Code, 6);
        playerViews[0] = new PlayerView(colour1Code, 6);
        playerViews[1] = new PlayerView(colour2Code, 6);
        left.add(playerViews[0]);
        right.add(playerViews[1]);
    }

    //goes to next player's turn
    private void updateTurn() {
        turnNumber++;
        // sets font colour and changes JLabel's text to reflect turn
        String text = "<html><font color='";
        text += (turnNumber % 2 == 0) ? colour1 + "'>" + colour1 + "'s</font> "
                : colour2 + "'>" + colour2 + "'s</font> ";
        text += "turn.</html>";
        turnIndicator.setText(text);        //updates what the user sees
        turnIndicator.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        turnIndicator.revalidate();
        revalidate();
        if (aiTurnNumber==turnNumber%2){ //will activate the AI if enabled and turned on
            AI();
        }
    }

    //brings up save files
    private void open_Save_Menu(String lorS) {
        // Buttons
        footer.remove(saveGame);
        footer.remove(loadGame);
        footer.add(save1);
        footer.add(save2);
        footer.add(save3);
        if (lorS.equals("save")){
            save1.setActionCommand("s1");
            save2.setActionCommand("s2");
            save3.setActionCommand("s3");
        }else {
            save1.setActionCommand("l1");
            save2.setActionCommand("l2");
            save3.setActionCommand("l3");
        }
        footer.updateUI();
        footer.revalidate();
        revalidate();
    }

    //closes save files
    private void closeSaveMenu() {
        footer.remove(save1);
        footer.remove(save2);
        footer.remove(save3);
        footer.add(saveGame);
        footer.add(loadGame);
        footer.updateUI();
        footer.revalidate();
        revalidate();
        getContentPane();
    }

    /**
     * does an action based on what button was pressed
     *
     * @param e info on the object interacted with by the user
     */
    public void actionPerformed(ActionEvent e) { // if buttons are pressed
        String cmd = e.getActionCommand();
        if (cmd.equals("NewG")){
            header.removeAll();
            revalidate();
            header.add(vsHuman);
            header.add(vsAI);
            revalidate();
        }else if (cmd.equals("human")||cmd.equals("ai")||gameState.equals(GameStates.NONE)&&cmd.equals("loadgame")) {
            // updates which mode to start
            gameState = GameStates.START;
            header.removeAll();
            revalidate();
            header.add(turnIndicator, BorderLayout.NORTH);
            header.revalidate();
            initExtraPanels();
            if (cmd.equals("loadgame")){
                open_Save_Menu("load");
                turnIndicator.setText("");
            }else if (cmd.equals("ai")){
                aiTurnNumber=rng.nextInt(2);
                if (aiTurnNumber==turnNumber%2){
                    AI();
                }
            }
            revalidate();
        } else if (cmd.equals("savegame")) {//save is pressed
            open_Save_Menu("save");//show buttons
        }else if (cmd.equals("s1")||cmd.equals("s2")||cmd.equals("s3")) {//save game one is pressed
            saveGame(cmd);
            closeSaveMenu();
        } else if (cmd.equals("loadgame")) {//load is pressed same as above but with load
            open_Save_Menu("load");
        }else if (cmd.equals("l1")||cmd.equals("l2")||cmd.equals("l3")) {
            loadGame("s" + cmd.charAt(1),false);
            closeSaveMenu();
        }
    }

    // the following are for mouse location. Only matters where mouse is
    // released
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Activates when mouse button is released by user.  Takes the coordinates of the mouse as input, and decides
     * what happens next in the game based on the current state of the game.
     *
     * (@inheritDoc)
     * @param e information on the mouse
     *
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int nodeNumber = findNodeNumber(x, y); //gets node

        if (e.getButton() == 1 && !gameState.equals(GameStates.NONE) && nodeNumber != -1) { // if the correct mouse button is pressed and pieces are

            if (gameState.equals(GameStates.START)) {
                placeNewPiece(nodeNumber);
            } else if (gameState.equals(GameStates.MOVE)) { // highlights piece to be moved
                selectPieceToMove(nodeNumber);
            } else if (gameState.equals(GameStates.PLACE)) { // allows moving of highlighted piece to another place and updates
                moveToken(nodeNumber);
            } else if (gameState.equals(GameStates.MILLMADE)) {
                removePiece(nodeNumber);

            }
        }
    }

    //places new piece on board
    private void placeNewPiece(int nodeNumber){
        // being placed
        boolean piecePlaced = placePiece(turnNumber % 2, nodeNumber); // piece placed

        if (players[0].getNumTokens() == 0 && players[1].getNumTokens() == 0) {
            gameState = GameStates.MOVE;// checks if all pieces placed
            for (int i = 0; i < nodes.length - 1; i++) {
                if (nodes[i].getNumberTokens() > 0 && nodes[i].getTokencolour() == colour1Code) {
                    redTokens++;
                } else if (nodes[i].getNumberTokens() > 0 && nodes[i].getTokencolour() == colour2Code)
                    blueTokens++;
            }
        }
        if (nodes[nodeNumber].inMill()&&piecePlaced) {
            previousNode = nodeNumber;
            gameState = GameStates.MILLMADE;
            showRemove();
        } else if (piecePlaced) {
            updateTurn();
        }
    }

    //selects a piece on board to be moved
    private void selectPieceToMove (int nodeNumber){

        if (noLegalMoves(colour1Code)) {
            JOptionPane.showMessageDialog(null, "Blue Wins by default", "Six Men's Morris",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if (noLegalMoves(colour2Code)) {
            JOptionPane.showMessageDialog(null, "Red Wins by default", "Six Men's Morris",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        if (canSelect(nodeNumber)) {
            selectedNumber = nodeNumber;
            board.highlightNode(nodes[nodeNumber]);
            gameState = GameStates.PLACE;
        }
    }

    //moves a selected token
    private void moveToken(int nodeNumber){
        // the turn so other player can move
        if (nodes[selectedNumber].isConnectedTo(nodeNumber) && nodes[nodeNumber].getNumberTokens() == 0) {
            // if node is connected and node want to move to has no token
            nodes[selectedNumber].moveToken(nodes[nodeNumber]);
            board.moveToken(nodes[selectedNumber], nodes[nodeNumber]);

            // determines after placing token at new location there is a mill
            if (nodes[nodeNumber].inMill()) {
                previousNode = nodeNumber;
                gameState = GameStates.MILLMADE;
                showRemove();
            } else {
                gameState = GameStates.MOVE;
                updateTurn();
            }
        } else if (nodes[selectedNumber] == nodes[nodeNumber]) {
            // removes highlighted node, allows to click and move another piece
            selectedNumber = -1;
            board.clearHighlight();
            gameState = GameStates.MOVE;
        } else {
            JOptionPane.showMessageDialog(null, "Cannot move piece to selected node", "Six Men's Morris",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //shows dialog to prompt player to remove opponent's piece
    private void showRemove() {
        String text = "<html><font color='";
        text += (turnNumber % 2 == 0) ? colour1 + "'>" + colour1 + "</font> " : colour2 + "'>" + colour2 + "</font> ";
        text += "to remove one of <font color='";
        text += (turnNumber % 2 == 1) ? colour1 + "'>" + colour1 + "'s</font> "
                : colour2 + "'>" + colour2 + "'s</font> ";
        text += "tokens turn.</html>";
        turnIndicator.setText(text);
        turnIndicator.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        turnIndicator.revalidate();
        revalidate();
        if (turnNumber%2==aiTurnNumber)AI();
    }

    //checks if there are no legal moves left
    private boolean noLegalMoves(int colour) {
        int tokenswithcolour = 0;
        int cantmove = 0;

        for (Node node : nodes) {
            // if node has token and colour of token is same as designated
            // colour
            if (node.getNumberTokens() == 1 && node.getTokencolour() == colour) {
                tokenswithcolour++;
                // setting up checking every node around chosen if node exists
                // or has token
                boolean checkleft = (node.getLeft() == null) || (node.getLeft().getNumberTokens() == 1);
                boolean checkright = (node.getRight() == null) || (node.getRight().getNumberTokens() == 1);
                boolean checkup = (node.getUp() == null) || (node.getUp().getNumberTokens() == 1);
                boolean checkdown = (node.getDown() == null) || (node.getDown().getNumberTokens() == 1);

                // check left exists or has token
                if (checkleft && checkright && checkup && checkdown) {
                    cantmove++;
                }

            }
        }
        return tokenswithcolour == cantmove;

    }

    //gets the number of tokens of each colour
    private int numbColourToken(int colour) {
        int numberOfColour = 0;

        for (Node node : nodes) {
            if (node.getNumberTokens() == 1 && node.getTokencolour() == colour) {
                numberOfColour++;
            }
        }
        return numberOfColour;
    }

    //checks if a piece can be selected
    private boolean canSelect(int nodeNumber) {
        if (nodes[nodeNumber].getNumberTokens() < 1)
            return false;
        if (turnNumber % 2 == 0 && nodes[nodeNumber].getTokencolour() == colour2Code)
            return false;
        else
            return !(turnNumber % 2 == 1 && nodes[nodeNumber].getTokencolour() == colour1Code);
    }

    //ACTUALLY places selected piece
    private boolean placePiece(int pNum, int NN) {
        int colour = (pNum == 0) ? colour1Code : colour2Code;
        if (players[pNum].removeToken() && nodes[NN].getNumberTokens() == 0) {
            nodes[NN].addToken(colour);
            board.placeToken(nodes[NN]);
            playerViews[pNum].removeToken();
        } else {
            players[pNum].addToken();
            if (nodes[NN].getNumberTokens() > 0) {
                JOptionPane.showMessageDialog(null, "Piece is already placed at selected node", "Six Men's Morris",
                        JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        return true;
    }

    //removes a selected piece
    private void removePiece(int NN) { // get rid of pNum later

        if (nodes[NN].getNumberTokens() != 0) {
            int millColour = nodes[previousNode].getTokencolour();
            int chosenColour = nodes[NN].getTokencolour();

            //System.out.println("Colour: " + (millColour != chosencolour) + " !inMill:" + (!nodes[NN].inMill()) + " "
              //      + "onlyMills:" + onlyMills(chosencolour) + " number:" + (numbColourToken(chosencolour) <= 3));

            if (millColour != chosenColour
                    && ((!nodes[NN].inMill() || onlyMills(chosenColour)) || numbColourToken(chosenColour) <= 3)) {

                board.removeToken(nodes[NN].getRelX(), nodes[NN].getRelY());
                nodes[NN].removeTopToken();

                if (chosenColour == colour1Code && redTokens != 0) { // if chosencolour red
                    redTokens--;
                } else if (chosenColour == colour2Code && redTokens != 0) {// if chosencolour blue
                    blueTokens--;
                }

                if (numbColourToken(colour1Code) == 2 && redTokens != 0) {
                    JOptionPane.showMessageDialog(null, "Blue Wins", "Six Men's Morris",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (numbColourToken(colour2Code) == 2 && blueTokens != 0) {
                    JOptionPane.showMessageDialog(null, "Red Wins", "Six Men's Morris",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                if (blueTokens == 0 || redTokens == 0) {
                    gameState = GameStates.START;
                } else {
                    gameState = GameStates.MOVE;
                }
                updateTurn();
            } else {
                JOptionPane.showMessageDialog(null, "Need to Choose other player Piece to remove", "Six Men's Morris",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Remove opponent piece", "Six Men's Morris",
                    JOptionPane.INFORMATION_MESSAGE);

        }

    }

    //finds node based on coordinates and the hitbox of the nodes
    private int findNodeNumber(int x, int y) { // finds the node number where u have selected
        int nodeNumber = -1;
        double relX = (x - (board.getWidth() / 2.0)) / board.getDimension();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].inRange(relX, ((double) y / board.getDimension())))
                nodeNumber = i;
        }
        return nodeNumber;
    }

    //saves info on the game is it can be loaded
    private void saveGame(String filename) {
        Stack <Token> tokens;
        try {
            PrintWriter fout = new PrintWriter(new File(filename + ".sav"));
            fout.println(turnNumber % 2);
            fout.println(aiTurnNumber);
            fout.println(gameState.toString());
            fout.println(previousNode);
            fout.println(players[0].getNumTokens());
            fout.println(players[1].getNumTokens());
            fout.println(blueTokens);
            fout.println(redTokens);
            for (Node node : nodes) {
                tokens = new Stack<>();
                while (node.getNumberTokens() > 0) {
                    fout.println(node.getNodeNumber() + "," + node.getTokencolour());
                    tokens.push(node.removeTopToken());
                }while(!tokens.empty()){
                    node.addToken(tokens.pop());
                }
            }
            fout.close();
        } catch (IOException e) {
            System.out.println("Error writing to save file!");
        }
    }

    //loads old game info to continue game
    private void loadGame (String filename, boolean restore){
        saveGame("backup");
        String [] line;
        int tempInt;
        String result="";
        String oldGameState;
        try{
            Scanner fin=new Scanner(new File(filename+".sav"));
            turnNumber=Integer.parseInt(fin.nextLine());
            aiTurnNumber=Integer.parseInt(fin.nextLine());
            oldGameState=fin.nextLine();
            switch (oldGameState){
                case "NONE": gameState=GameStates.NONE;
                    break;
                case "START": gameState=GameStates.START;
                    break;
                case "MOVE": gameState=GameStates.MOVE;
                    break;
                case "Place": gameState=GameStates.PLACE;
                    break;
                case "MILLMADE": gameState=GameStates.MILLMADE;
                    break;
                default: System.out.print("ERROR");
            }
            previousNode=Integer.parseInt(fin.nextLine());
            for (int i = 0; i<2; i++) {
                tempInt = Integer.parseInt(fin.nextLine());
                players[i].setNumTokens(tempInt);
                playerViews[i].setNumTokens(tempInt);
            }
            blueTokens=Integer.parseInt(fin.nextLine());
            redTokens=Integer.parseInt(fin.nextLine());
            for (Node node : nodes) {
                node.removeAllTokens();
            }
            board.reset();
            while (fin.hasNextLine()){
                line = fin.nextLine().split(",");
                tempInt=Integer.parseInt(line[0]);
                nodes[tempInt].addToken(Integer.parseInt(line[1]));
                board.placeToken(nodes[tempInt]);
            }
            turnNumber--;
            updateTurn();
            fin.close();
            board.repaint();
            board.updateUI();
            board.revalidate();
            playerViews[0].repaint();
            playerViews[0].revalidate();
            playerViews[1].repaint();
            playerViews[1].revalidate();
            revalidate();
            result="good";
        } catch (FileNotFoundException e) {
            result = "no file";
            if (redTokens==0&&blueTokens==0)gameState=GameStates.START;
            header.add(turnIndicator, BorderLayout.NORTH);
            header.revalidate();
            revalidate();
            turnNumber--;
            updateTurn();
        } catch (NumberFormatException e){
            e.printStackTrace();
            result="NaN error";
        } catch (NoSuchElementException e){
            e.printStackTrace();
            result="state error";
        } catch (NullPointerException e) {
            e.printStackTrace();
            result = "node error";
        }catch (Exception e){
            e.printStackTrace();
            result="Unknown Exception";
        }finally {
            if (result.equals("good")){
                JOptionPane.showMessageDialog(null, "Game Loaded!", "Six Men's Morris",JOptionPane.INFORMATION_MESSAGE);
            }else{
                if (result.equals("no file")){
                    JOptionPane.showMessageDialog(null, "Save File is Empty!", "Six Men's Morris",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null, result, "Load Game Error", JOptionPane.ERROR_MESSAGE);
                }
                if (restore) {
                    loadGame("backup", true);
                    JOptionPane.showMessageDialog(null, result, "Could not restore game state.  Please restart the" +
                            " application.", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    //checks if an opponent only has mills
    private boolean onlyMills(int colour) {
        int numMills = 0;
        int[] locations = {1, 4, 6, 9, 11, 14};
        for (int i : locations) {
            if (nodes[i].getTokencolour() == colour && nodes[i].inMill()) {
                numMills++;
            }
        }
        return (numMills == 2);
    }

    //computer opponent
    private void AI (){
        int nodeOfInterest; //node ai is selecting
        if (gameState.equals(GameStates.START)){
            do {
                nodeOfInterest=rng.nextInt(nodes.length);
            }while (nodes[nodeOfInterest].getNumberTokens()!=0); //randomly selects nodes to try to place a new piece on
            placeNewPiece(nodeOfInterest);
        }else if (gameState.equals(GameStates.MOVE)){
            //checks if game is over by default
            if (noLegalMoves(colour1Code)) {
                JOptionPane.showMessageDialog(null, "Blue Wins by default", "Six Men's Morris",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            if (noLegalMoves(colour2Code)) {
                JOptionPane.showMessageDialog(null, "Red Wins by default", "Six Men's Morris",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            do {
                selectedNumber=rng.nextInt(nodes.length);
            }while (nodes[selectedNumber].isSurrounded() || !canSelect(selectedNumber)); //randomly selects a node that has a token that can be moved
            Integer [] possible = nodes[selectedNumber].getConnectedNodeNumbers(); //list of possible positions to move the toekn to
            do {
                nodeOfInterest=rng.nextInt(possible.length);
            }while (nodes[possible[nodeOfInterest]].getNumberTokens()>0); //randomly selects one of the nodes it can move the piece to
            moveToken (possible[nodeOfInterest]);
        }else if (gameState.equals(GameStates.MILLMADE)){
            int colourCheck= (aiTurnNumber % 2 == 0) ? colour1Code : colour2Code;
            do{
                nodeOfInterest=rng.nextInt(nodes.length);
            }while (nodes[nodeOfInterest].getNumberTokens()<1 ||        //selects an opponents piece to remove
                    colourCheck==nodes[nodeOfInterest].getTokencolour() ||
                    (nodes[nodeOfInterest].inMill()&&!onlyMills(nodes[nodeOfInterest].getTokencolour())));
            removePiece(nodeOfInterest);
        }
    }

    public static void main(String[] args) {
        // sets the GUI look based on OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to apply OS theme to GUI.  Switching to default Java.");
        }
        JFrame gui = new GUI();
        Dimension defaultSize = new Dimension(800, 700);
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
        boolean badKey = false;
        switch (Contra) {
            case 0:
            case 1:
                if (keyCode == KeyEvent.VK_UP) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 2:
            case 3:
                if (keyCode == KeyEvent.VK_DOWN) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 4:
            case 6:
                if (keyCode == KeyEvent.VK_LEFT) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 5:
            case 7:
                if (keyCode == KeyEvent.VK_RIGHT) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 8:
                if (keyCode == KeyEvent.VK_B) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 9:
                if (keyCode == KeyEvent.VK_A) {
                    Contra++;
                } else {
                    badKey = true;
                }
                break;
            case 10:
                if (keyCode == KeyEvent.VK_ENTER) {
                    changeBoard();
                    Contra = 0;
                } else {
                    badKey = true;
                }
                break;
        }
        if (badKey) {
            badKey = false;
            Contra = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void changeBoard() {
        try {
            File folder = new File("." + File.separator + "boards");
            File[] listOfFiles = folder.listFiles();
            Random rng = new Random();
            int image = rng.nextInt(listOfFiles.length);
            board.changeBoardImage(listOfFiles[image]);
        } catch (Exception ignored) {
        }
    }
}
