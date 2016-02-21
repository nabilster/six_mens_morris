import java.util.ArrayList;

public class Node {
    private ArrayList<Token> tokens=new ArrayList<>();
    private final double relX;
    private final double relY;
    private int counter;
    //private Integer[] colourToken= new Integer[16];
    
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
        
        counter++;
    }

    public Token getTopToken(){
        return tokens.get(tokens.size()-1);
    }
    
    public int getNumberTokens(){
        return counter;
    }
    
    public boolean inRange (double x, double y){
        if (x<1) return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
        return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
    }
    
}
