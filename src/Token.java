
//this class is used to represent a token (piece)  Doesn't seem too useful atm, so we may just scrap it and move
    //anything we need from it to the node class
//TODO Impliment node
public class Token {
    int colour;

    public Token (int colour/*, BoardNode node*/){
        this.colour=colour;
    }

    public int getColour(){
        return colour;
    }
}
