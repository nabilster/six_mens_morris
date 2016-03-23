import java.util.Stack;

public class Node {
    private Stack<Token> tokens = new Stack<>();
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
        if (tokens.empty())return null;
        return tokens.get(tokens.size() - 1);
    }

    public int getNumberTokens() { // get number of tokens
        return tokens.size();
    }

    public int getTokencolour() {      // get the color of the top token
        if (tokens.empty()) return -1;
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

    public Node getLeft (){   // get left node
        return connected[0];
    }

    public Node getRight (){   // get right node
        return connected[1];
    }

    public Node getUp (){ //get node up from main node
        return connected[2];
    }

    public Node getDown (){ // get node down from main node
        return connected[3];
    }

    public boolean inMill () {
        Node node = this;
        while (node.getLeft()!= null){
            node=node.getLeft();
        }
        if (horizontalCount(node)==3) return true;
        node=this;
        while (node.getUp()!=null){
            node=node.getUp();
        }
        return (verticalCount(node)==3);
    }

    private int horizontalCount(Node node){
        if (node.getRight()==null) return (node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0;
        else return ((node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0)+horizontalCount(node.getRight());
    }

    private int verticalCount(Node node){
        if (node.getDown()==null)return (node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0;
        else return ((node.getTokencolour()==this.getTokencolour())?node.getNumberTokens():0)+verticalCount(node.getDown());
    }

    public Token removeTopToken(){  // remove top token
        return tokens.pop();
    }
  
	

    public void moveToken (Node to){  // move token from one node to another
        to.addToken(getTopToken());
        removeTopToken();
    }

    public void removeAllTokens (){
        tokens.clear();
    }

}