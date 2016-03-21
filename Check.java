package six_mens_morris_step2;

import javax.swing.JOptionPane;

public class Check {

	public Check() {
	}
	
	protected static void tokenoverflowcheck(Node [] nodes,int colour,BoardView board){
		for (int i=0; i<nodes.length;i++){
			if(nodes[i].getNumberTokens()>1){
				nodes[i].addToken(colour); // adds purple color token if more than one to indicate location
				board.placeToken(nodes[i]);
			}
		}
	}
	protected static boolean checkMill(Node [] nodes){

		if (nodes[0].getNumberTokens() ==1 && nodes[1].getNumberTokens() ==1 &&nodes[2].getNumberTokens() ==1){
			if ((nodes[0].getTokencolour()== 255 && nodes[1].getTokencolour()== 255 && nodes[2].getTokencolour()==255)||(nodes[0].getTokencolour()== 16711680 && nodes[1].getTokencolour()== 16711680 && nodes[2].getTokencolour()==16711680)){
				return true;
			}
		}
		if (nodes[0].getNumberTokens() ==1 && nodes[6].getNumberTokens() ==1 &&nodes[13].getNumberTokens() ==1){
			if ((nodes[0].getTokencolour()== 255 && nodes[6].getTokencolour()== 255 && nodes[13].getTokencolour()==255)||(nodes[0].getTokencolour()== 16711680 && nodes[6].getTokencolour()== 16711680 && nodes[13].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[2].getNumberTokens() ==1 && nodes[9].getNumberTokens() ==1 &&nodes[15].getNumberTokens() ==1){
			if ((nodes[2].getTokencolour()== 255 && nodes[9].getTokencolour()== 255 && nodes[15].getTokencolour()==255)||(nodes[2].getTokencolour()== 16711680 && nodes[9].getTokencolour()== 16711680 && nodes[15].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[13].getNumberTokens() ==1 && nodes[14].getNumberTokens() ==1 &&nodes[15].getNumberTokens() ==1){
			if ((nodes[13].getTokencolour()== 255 && nodes[14].getTokencolour()== 255 && nodes[15].getTokencolour()==255)||(nodes[13].getTokencolour()== 16711680 && nodes[14].getTokencolour()== 16711680 && nodes[15].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[3].getNumberTokens() ==1 && nodes[7].getNumberTokens() ==1 &&nodes[10].getNumberTokens() ==1){
			if ((nodes[3].getTokencolour()== 255 && nodes[7].getTokencolour()== 255 && nodes[10].getTokencolour()==255)||(nodes[3].getTokencolour()== 16711680 && nodes[7].getTokencolour()== 16711680 && nodes[10].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[3].getNumberTokens() ==1 && nodes[4].getNumberTokens() ==1 &&nodes[5].getNumberTokens() ==1){
			if ((nodes[3].getTokencolour()== 255 && nodes[4].getTokencolour()== 255 && nodes[5].getTokencolour()==255)||(nodes[3].getTokencolour()== 16711680 && nodes[4].getTokencolour()== 16711680 && nodes[5].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[10].getNumberTokens() ==1 && nodes[11].getNumberTokens() ==1 &&nodes[12].getNumberTokens() ==1){
			if ((nodes[10].getTokencolour()== 255 && nodes[11].getTokencolour()== 255 && nodes[12].getTokencolour()==255)||(nodes[10].getTokencolour()== 16711680 && nodes[11].getTokencolour()== 16711680 && nodes[12].getTokencolour()==16711680)){
				return true;
			}	
		}
		if (nodes[5].getNumberTokens() ==1 && nodes[8].getNumberTokens() ==1 &&nodes[12].getNumberTokens() ==1){
			if ((nodes[5].getTokencolour()== 255 && nodes[8].getTokencolour()== 255 && nodes[12].getTokencolour()==255)||(nodes[5].getTokencolour()== 16711680 && nodes[8].getTokencolour()== 16711680 && nodes[12].getTokencolour()==16711680)){
				return true;
			}	
		}
		return false;
	}

	protected static void checkScreen(String gamestate,Node [] nodes,Player [] players){

		for (int i=0; i<nodes.length;i++){
			if (nodes[i].getNumberTokens() >1){

				JOptionPane.showMessageDialog(null, "Pieces Stacked (Highlighted in purple): Choose another spot  ", "Fail", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

		if (gamestate== "New"){
			JOptionPane.showMessageDialog(null, "Continue placing pieces until board is full", "Successful", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int player1=Math.abs(players[0].getNumTokens()-6);
		int player2=Math.abs(players[1].getNumTokens()-6);

		// if either player has token one token on board, or both have 1 successful
		if ((player1==1 && player2==0) || (player2==1 && player1==0)||(player1==1 && player2==1)){
			JOptionPane.showMessageDialog(null, "No Errors :)", "Successful1", JOptionPane.INFORMATION_MESSAGE);

			return;
		}
		// if one player has 3 on board and other has 1, legal only when mill
		if ((player1==3 && player2==1) || (player2==3 && player1==1)){

			if (checkMill(nodes)==true){
				JOptionPane.showMessageDialog(null, "No Errors :)", "Successful2", JOptionPane.INFORMATION_MESSAGE);
			}

			JOptionPane.showMessageDialog(null, "Not Legal","Failure", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		if ((player1==3 && player2==2) || (player2==3 && player1==2)){

			if (checkMill(nodes)==true){
				JOptionPane.showMessageDialog(null, "No Errors :)", "Successful3", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			else{
				JOptionPane.showMessageDialog(null,"Not Enough Pieces on Board: Need to add more pieces to play","Failure1", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

		//if any player has less than two while other can have any amount, Fail
		if (player1<2 || player2<2){
			JOptionPane.showMessageDialog(null, "Not Enough Pieces on Board: Need to add more pieces to play", "Fail", JOptionPane.INFORMATION_MESSAGE);

			return;
		}
		// if both players have two error to add more pieces, Fail
		if (player1==2 && player2==2){
			JOptionPane.showMessageDialog(null, "Not Enough Pieces on Board: Need to add more pieces to play", "Fail", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		else{
			JOptionPane.showMessageDialog(null, "No Errors :)", "Successful4", JOptionPane.INFORMATION_MESSAGE);

		}

		return;

	}
}


