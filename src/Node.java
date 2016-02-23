import java.util.ArrayList;
import java.util.Stack;

/**
 * Model for a node (place on the board that a token can be placed).  Contains a node's position on the board, any
 * connections to other nodes (TODO)
 *  as well as any tokens on top of the node
 *
 * @version 1.0
 */
public class Node {
    private Stack<Token> tokens=new Stack<>();
    private final double relX;
    private final double relY;

    /**
     * Initialises the nodes with the specified coordinates. coordinate system uses the relative distance from the
     * origin, which is the top-center of the board, and a scale where 1 unit along the x-axis is the entire width of
     * the board, and 1 unit along the y-axis is the entire height of the board
     *
     * @param relX x coordinate of the node
     * @param relY y coordinate of the node
     */
    public Node (double relX, double relY){
        this.relX=relX;
        this.relY=relY;
    }

    /**
     * the coordinate of the node along the x axis
     *
     * @return the x coordinate relative to the center of be board
     */
    public double getRelX(){
        return relX;
    }

    /**
     * the coordinate of the node along the y axis
     *
     * @return the y coordinate relative to the top of the board
     */
    public double getRelY (){
        return relY;
    }

    /**
     * Places a token on this node of the specified colour
     *
     * @param colour the colour of the token, given as a hexadecimal colour code converted to base 10
     */
    public void addToken(int colour){
        this.tokens.add(new Token(colour));
    }

    /**
     * returns the colour of the top most token of any stack of tokens
     *
     * @return token on the top of the stack (if there is one)
     */
    public Token getTopToken(){
        if (tokens.size()==0) return null;
        return tokens.get(tokens.size()-1);
    }

    /**
     * Gets the number of tokens in the stack
     *
     * @return the number of tokens in the stack of tokens
     */
    public int getNumberTokens(){
        return tokens.size();
    }

    /**
     * checks if a given coordinate is within the hitbox of the node
     *
     * @param x x coordinate to check
     * @param y y coordinate to check
     * @return whether the point specified by the x and y coordinates falls inside the hitbox of the node
     */
    public boolean inRange (double x, double y){
        return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
    }
    
}


