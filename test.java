package nine_mens_morris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class test{

	private static int x,y;
	public static board location= new board();
	
	public static void main (String[] args){
		location.addMouseListener(new AL());
		
	}
	
	static class AL extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			x= e.getX();
			y= e.getY();
			System.out.println(x+","+y);
		}
	}
}
