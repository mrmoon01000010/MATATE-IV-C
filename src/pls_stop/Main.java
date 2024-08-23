package pls_stop;

import javax.swing.JFrame;

public class Main {
	public static void main(String args[]) {
		Control Main_Thread = new Control();
		
		Main_Thread.Window.addKeyListener(Main_Thread.Input);
		Main_Thread.Content.addMouseListener(Main_Thread.Input);
		Main_Thread.Content.addMouseWheelListener(Main_Thread.Input);
		Main_Thread.Window.setContentPane(Main_Thread.Content);
		Main_Thread.Window.setSize(1000, 750);
		Main_Thread.Window.setLocationRelativeTo(null);
		Main_Thread.Window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Main_Thread.Window.setVisible(true);
		
		Main_Thread.run();
	}
}
