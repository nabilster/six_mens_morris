package six_mens_morris;

import java.util.ArrayList;

public class Node {
	private ArrayList<Token> tokens=new ArrayList<>();
	private final double relX;
	private final double relY;
	private int counter;
	private static Node [] nodes = new Node [16];	
	
	
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
	
	public int getTokencolour(){
		return getTopToken().getColour(); 
	}

	public boolean inRange (double x, double y){
		if (x<1) return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
		return ((relX-0.05)<x&&x<(relX+0.05))&&((relY-0.05)<y&&y<(relY+0.05));
	}
	
	public static Node[] getNodesList(){
		initializeNodes();
		return nodes;
	}
	
	private static void initializeNodes(){
		nodes[0]=new Node(-0.412,0.093);
		nodes[1]=new Node(-0.002,0.093);
		nodes[2]=new Node(0.408,0.093);

		nodes[3]=new Node(-0.207,0.298);
		nodes[4]=new Node(-0.002,0.298);
		nodes[5]=new Node(0.203,0.298);

		nodes[6]=new Node(-0.412,0.503);
		nodes[7]=new Node(-0.207,0.503);
		nodes[8]=new Node(0.203,0.503);
		nodes[9]=new Node(0.408,0.503);

		nodes[10]=new Node(-0.207,0.708);
		nodes[11]=new Node(-0.002,0.708);
		nodes[12]=new Node(0.203,0.708);

		nodes[13]=new Node(-0.412,0.913);
		nodes[14]=new Node(-0.002,0.913);
		nodes[15]=new Node(0.408,0.913);
	}

}


