/**
 * Used as a model for a token (piece).  Would really work better as a something akin a struct/record in node, but that
 * isn't available in Java, so for modularity sake, it's its own model
 *
 * @version 1.0
 */

public class Token {
	  private int colour;

    /**
     * Sets a token's colour
     *
     * @param colour colour of token
     */
	  public Token (int colour){
	      this.colour=colour;
	  }

    /**
     * getter for colour
     *
     * @return the colour of a token as a hexadecimal colour code converted to base 10
     */
	  public int getColour(){
	      return colour;
	  }
}
