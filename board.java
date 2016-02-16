package nine_mens_morris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class board extends JFrame {

	JButton b1;
	JLabel l1;
	
		public board()
		{
		setTitle("Six Men's Morris");
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		setSize(750,750);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	
		setLayout(new BorderLayout());
		setContentPane(new JLabel(new ImageIcon("board2.png")));
		setLayout(new FlowLayout());
		l1=new JLabel("Start:");
		b1=new JButton("New Game");
		add(l1);
		add(b1);
		
		//JFrame frame = new JFrame("Some name goes here");
		//Container contentPane = frame.getContentPane();
		
		//setLayout(null);
		JButton b2= new JButton("me");
		//pnlButton.setLayout(null);
		b2.setBounds(67, 81, 10, 10);
		setVisible(true);
		add(b2);
//		createbutton(350,82);
//		createbutton(638,82);
//		createbutton(203,221);
//		createbutton(350,225);
//		createbutton(491,223);
//		createbutton(66,361);
//		createbutton(208,369);
////		createbutton(67,81);
//		createbutton(67,81);
//		createbutton(67,81);
//		createbutton(67,81);
//		createbutton(67,81);
		
		
		// Just for refresh :) Not optional!
		setSize(700,700);
		setSize(700,700);
		}
		
		public void createbutton(int x, int y){
			JFrame frame = new JFrame("Some name goes here");
			Container contentPane = frame.getContentPane();
			
			JButton b1= new JButton("me");
			//pnlButton.setLayout(null);
			b1.setBounds(x, y, 10, 10);
			setVisible(true);
			contentPane.add(b1);
			
					
		}
}

