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

    public void addToken(int colour) {
        this.tokens.add(new Token(colour));
    }

    public void addToken(Token token){
        tokens.add(token);
    }

    public Token getTopToken() {
        return tokens.get(tokens.size() - 1);
    }

    public int getNumberTokens() {
        return tokens.size();
    }

    public int getTokencolour() {
        return getTopToken().getColour();
    }

    public boolean inRange(double x, double y) {
        if (x < 1) return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
        return ((relX - 0.05) < x && x < (relX + 0.05)) && ((relY - 0.05) < y && y < (relY + 0.05));
    }

    public int getNodeNumber (){
        return nodeNumber;
    }

    public boolean isConnectedTo (int checkNodeNumber){
        for (Node node:connected){
            if (node!=null) {
                if (node.getNodeNumber() == checkNodeNumber) return true;
            }
        }
        return false;
    }

    public int getLeft (){
        return connected[0].getNodeNumber();
    }

    public int getRight (){
        return connected[1].getNodeNumber();
    }

    public int getUp (){
        return connected[2].getNodeNumber();
    }

    public int getDown (){
        return connected[3].getNodeNumber();
    }

    public void removeTopToken(){
        tokens.remove(tokens.size()-1);
    }

    public void moveToken (Node to){
        to.addToken(getTopToken());
        removeTopToken();
    }

}