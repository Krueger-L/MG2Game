import java.awt.Point;

import javax.swing.JFrame;


public class GameWindow extends JFrame{

	GamePanel panel;
	Thread panelThread;
	Level level;
	Point pointOfView = new Point(0,0);
	
	public GameWindow(){
		super("");
		setVisible(true);
		setSize(800,600);
		this.setIgnoreRepaint(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		level = new Level();
		
		panel = new GamePanel(this);
		
		add(panel);
		
		panelThread = new Thread(panel);
		
		panelThread.run();
		
		pack();
		
		
	}
	
	public static void main(String[] args){
		
		new GameWindow();
		
	}
}
