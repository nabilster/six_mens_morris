import java.util.ArrayList;

/**
 * Represents the state of a player, what colour they are, how many tokens they have (model for player)
 *
 * @version 1.0
 */
public class Player {
    private int numTokens; //number of tokens
    private int colour; //colour

    /**
     * Set's the players colour and how many tokens they start with
     *
     * @param colour    player's designated colour, stored as the hexadecimal colour code converted to base 10
     * @param numTokens number of tokens they start with
     */
    public Player(int colour, int numTokens){
        this.numTokens=numTokens;
        this.colour=colour;
    }

    /**
     * increases the number of tokens a player has by 1
     */
    public void addToken (){ //add a token
        numTokens++;
    }

    /**
     * Attempts to take a token away from the player (presumably to place it on the board)
     *
     * @return whether the attempt to remove a piece was successful or not (less than zero)
     */
    public boolean removeToken (){ //remove a token (also returns if it was able to remove a token)
        if (numTokens>0){
            numTokens--;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Getter method for colour
     *
     * @return player's colour represented as the hexadecimal colour code converted to base 10
     */
    public int getColour(){ //get player's colour
        return colour;
    }

    /**
     * getter for numTokens
     *
     * @return the number of tokens a player has remaining
     */
    public int getNumTokens(){ //get number of tokens player has
        return numTokens;
    }

}
