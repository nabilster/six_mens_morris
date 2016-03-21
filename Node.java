package six_mens_morris_step2;
import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    private ArrayList<Token> tokens = new ArrayList<>();
    private final double relX;
    private final double relY;
    private final int nodeNumber;
    private Node [] connected;


    public Node(double relX, double relY, int nodeNumber) {
        this.relX = relX;
        this.relY = relY;
        this.nodeNumber = nodeNumber;
    }

    public void setConnectedNodes (Node [] connectedNodes){
        connected=connectedNodes;

    }

    public double getRelX() {
        return relX;
    }

    public double getRelY() {	
        return relY;
    }
    
    public void addToken(int colour) {  // add token based on color
        this.tokens.add(new Token(colour));
    }

    public void addToken(Token token){ // add token 
        tokens.add(token);
    }
    
    public Token getTopToken() {  // get top token on node
        return tokens.get(tokens.size() - 1);
    }

    public int getNumberTokens() { // get number of tokens
        return tokens.size();
    }

    public int getTokencolour() {      // get the color of the top token
        return getTopToken().getColour();
    }

    public boolean inRange(double x, double y) {    
        if (x < 1) return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
        return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
    }

    public int getNodeNumber (){    // get the node number
        return nodeNumber;
    }

    public boolean isConnectedTo (int checkNodeNumber){ // check if one node is connected to another
        for (Node node:connected){
            if (node!=null) {
                if (node.getNodeNumber() == checkNodeNumber) return true;
            }
        }
        return false;
    }

    //SHOULD WORK.  IF NOT, SHOULEN'T BE HARD TO FIX
    public int getLeft (){   // get left node
        return nodeExists(0);
    }

    public int getRight (){   // get right node
        return nodeExists(1);
    }

    public int getUp (){ //get node up from main node
        return nodeExists(2);
    }

    public int getDown (){ // get node down from main node
        return nodeExists(3);
    }

	private int nodeExists (int index){ // check if the node exists
		if (connected[index]==null){
			return -1;
		}else {
			return connected[index].getNodeNumber();
		}
	}
	
    public void removeTopToken(){  // remove top token
        tokens.remove(tokens.size()-1);
    }
  
	

    public void moveToken (Node to){  // move token from one node to another
        to.addToken(getTopToken());
        removeTopToken();
    }

}