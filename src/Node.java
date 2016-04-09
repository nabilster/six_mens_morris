import java.util.ArrayList;
import java.util.Stack;

/**
 * Model for a node (place on the board that a token can be placed).  Contains a node's position on the board, any
 * connections to other nodes, as well as any tokens on top of the node
 *
 * @version 2.0
 */
public class Node {
    private Stack<Token> tokens = new Stack<>(); //tokens
    private final double relX;
    private final double relY;
    private final int nodeNumber; //unique assigned id
    private Node [] connected; //list of connections. [0]=left, [1]=right, [2]=up, [3]=down]

    /**
     * Initialises the nodes with the specified coordinates. coordinate system uses the relative distance from the
     * origin, which is the top-center of the board, and a scale where 1 unit along the x-axis is the entire width of
     * the board, and 1 unit along the y-axis is the entire height of the board
     *
     * @param relX x coordinate of the node
     * @param relY y coordinate of the node
     * @param nodeNumber number assigned to node.
     */
    public Node(double relX, double relY, int nodeNumber) {
        this.relX = relX;
        this.relY = relY;
        this.nodeNumber = nodeNumber;
    }

    /**
     * Sets connections to other nodes
     *
     * @param connectedNodes list of other nodes this one is connected to.  Indices as follows:
     *                       [0]=left, [1]=right, [2]=up, [3]=down.
     */
    public void setConnectedNodes (Node [] connectedNodes){
        connected=connectedNodes;

    }

    /**
     * the coordinate of the node along the x axis
     *
     * @return the x coordinate relative to the center of be board
     */
    public double getRelX() {
        return relX;
    }

    /**
     * the coordinate of the node along the y axis
     *
     * @return the y coordinate relative to the top of the board
     */
    public double getRelY() {	
        return relY;
    }

    /**
     * Places a token on this node of the specified colour
     *
     * @param colour the colour of the token, given as a hexadecimal colour code converted to base 10
     */
    public void addToken(int colour) {  // add token based on color
        this.tokens.add(new Token(colour));
    }

    /**
     * Places a token on this node of the specified colour
     *
     * @param token Takes a token to store as its own
     */
    public void addToken(Token token){ // add token 
        tokens.add(token);
    }

    /**
     * returns the colour of the top most token of any stack of tokens
     *
     * @return token on the top of the stack (if there is one)
     */
    public Token getTopToken() {  // get top token on node
        if (tokens.empty())return null;
        return tokens.get(tokens.size() - 1);
    }

    /**
     * Gets the number of tokens in the stack
     *
     * @return the number of tokens in the stack of tokens
     */
    public int getNumberTokens() { // get number of tokens
        return tokens.size();
    }

    /**
     * returns the colour of the top most token
     * @return the colour of the top token, represented bu its hexadecimal colour code converted to base 10 as an int
     */
    public int getTokencolour() {      // get the color of the top token
        if (tokens.empty()) return -1;
        return getTopToken().getColour();
    }

    /**
     * checks if a given coordinate is within the hitbox of the node
     *
     * @param x x coordinate to check
     * @param y y coordinate to check
     * @return whether the point specified by the x and y coordinates falls inside the hitbox of the node
     */
    public boolean inRange(double x, double y) {    
        if (x < 1) return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
        return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
    }

    /**
     * Returns a node's assigned number
     * @return the unique identifying ID number assiged to this node
     */
    public int getNodeNumber (){    // get the node number
        return nodeNumber;
    }

    /**
     * returns if the queried node is adjacent (directly connected) to this one
     *
     * @param checkNodeNumber the queried node
     * @return true if the nodes are adjacent, false if otherwise
     */
    public boolean isConnectedTo (int checkNodeNumber){ // check if one node is connected to another
        for (Node node:connected){
            if (node!=null) {
                if (node.getNodeNumber() == checkNodeNumber) return true;
            }
        }
        return false;
    }

    /**
     * Returns the node to the left of this one
     * @return the node to the left or null if there is none
     */
    public Node getLeft (){   // get left node
        return connected[0];
    }

    /**
     * Returns the node to the right of this one
     * @return the node to the right or null if there is none
     */
    public Node getRight (){   // get right node
        return connected[1];
    }

    /**
     * Returns the node above this one
     * @return the node above this one or null if there is none
     */
    public Node getUp (){ //get node up from main node
        return connected[2];
    }

    /**
     * Returns the node below this one
     * @return the node below this one or null if there is none
     */
    public Node getDown (){ // get node down from main node
        return connected[3];
    }

    /**
     * checks if a node contains a token that is a part of a mill
     *
     * @return returns true of the node's token is a part of a mill (horizontally or vertically).  Returns false otherwise
     */
    public boolean inMill () {
        Node node = this; //starting node is this one
        while (node.getLeft()!= null){ //make the starting node the left most connection
            node=node.getLeft();
        }
        if (horizontalCount(node)==3) return true; //if its part of a horizontal node, return true
        node=this; //start back at beginning
        while (node.getUp()!=null){ //go to the top most connected node
            node=node.getUp();
        }
        return (verticalCount(node)==3); //returns if there is a vertical mill
    }

    //returns the number of like coloured tokens connected to this one horizontally
    private int horizontalCount(Node node){
        //if we've reached the end of the connections, return the number of tokens if they are the same colour
        if (node.getRight()==null) return (node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0;
        //TODO HERE
        else return ((node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0)+horizontalCount(node.getRight());
    }

    private int verticalCount(Node node){
        if (node.getDown()==null)return (node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0;
        else return ((node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0)+verticalCount(node.getDown());
    }

    /**
     * removes whatever token is on the top of the stack
     *
     * @return the token that was removed from the stack
     */
    public Token removeTopToken(){  // remove top token
        return tokens.pop();
    }


    /**
     * transfers a token from this node to another one
     *
     * @param to destination node
     */
    public void moveToken (Node to){  // move token from one node to another
        to.addToken(getTopToken());
        removeTopToken();
    }

    /**
     * removes any and all tokens on this node
     */
    public void removeAllTokens (){
        tokens.clear();
    }

    /**
     * Gets a list of the connected nodes
     *
     * @return an array of the connected nodes
     */
    public Integer [] getConnectedNodeNumbers(){
        ArrayList<Integer> numbers = new ArrayList<>();
        for (Node aConnected : connected) {
            if (aConnected != null) numbers.add(aConnected.getNodeNumber());
        }
        return numbers.toArray(new Integer[numbers.size()]);
    }

    /**
     * Checks if there are tokens on all the nodes connected to this one
     *
     * @return true if the node is surrounded by tokens, false otherwises
     */
    public boolean isSurrounded (){
        for (Node connection: connected){
            if (connection!=null && connection.getNumberTokens()==0){
                return false;
            }
        }
        return true;
    }

}