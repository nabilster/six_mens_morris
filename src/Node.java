import java.util.ArrayList;

/**
 * Created by mooyl on 2016-02-20.
 */
public class Node {
    private ArrayList<Token> tokens=new ArrayList<>();
    private final double relX;
    private final double relY;

    public Node (double relX, double relY){
        this.relX=relX;
        this.relY=relY;
    }

    public double getRelX(){
        return relX;
    }

    public double getRelY (){
        return relY;
    }

    public void addToken(int colour){
        this.tokens.add(new Token(colour));
    }

    public Token getTopToken(){
        return tokens.get(tokens.size()-1);
    }

    public boolean inRange (double x, double y){
        return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
    }

}
