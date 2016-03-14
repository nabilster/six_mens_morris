public class Player {
    private int numTokens; //number of tokens
    private int colour; //colour

    public Player(int colour, int numTokens){
        this.numTokens=numTokens;
        this.colour=colour;
    }

    public void addToken (){ //add a token
        numTokens++;
    }

    public boolean removeToken (){ //remove a token (also returns if it was able to remove a token)
        if (numTokens>0){
            numTokens--;
            return true;
        }
        else {
            return false;
        }
    }

    public int getColour(){ //get player's colour
        return colour;
    }

    public int getNumTokens(){ //get number of tokens player has
        return numTokens;
    }

}
