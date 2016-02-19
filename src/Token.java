
//this class is used to represent a token (piece)  Doesn't seem too useful atm, so we may just scrap it and move
    //anything we need from it to the node class
//TODO Impliment node
public class Token {
    //private BoardNode node;
    int colour;

    public Token (int colour/*, BoardNode node*/){
        this.colour=colour;
        //this.node=node;
    }

    /*
    public Token(int colour){
        this.colour=colour;
    }
    */

    //TODO change coordinates to node's
    public int getX(){return 173;}
    public int getY(){return 46;}
    public int getColour(){return colour;}
}
