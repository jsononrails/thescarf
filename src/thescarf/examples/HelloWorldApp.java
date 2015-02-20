package thescarf.examples;

import java.awt.*;

import javax.swing.*;

import thescarf.framework.utility.FrameRate;


@SuppressWarnings("serial")
public class HelloWorldApp extends JFrame {

	private FrameRate frameRate;
	
	public HelloWorldApp() {
		
		frameRate = new FrameRate();
		
	}
	
	public void createAndShowGUI() {
		
		GamePanel gamePanel = new GamePanel();
		gamePanel.setBackground(Color.BLACK);
		gamePanel.setPreferredSize(new Dimension(320, 240));
		getContentPane().add(gamePanel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Hello World!");
		pack();
		frameRate.initialize();
		setVisible(true);
		
	}
	
	private class GamePanel extends JPanel {
		
		public void paint(Graphics g) {
			
			super.paint(g);
			onPaint(g);
			
		}		
	}
	
	protected void onPaint(Graphics g) {
		
		frameRate.calculate();
		
		g.setColor(Color.WHITE);
		g.drawString(frameRate.getFrameRate(), 30, 30);
		repaint();
	}
	
	public static void main(String[] args) {
		
		final HelloWorldApp app = new HelloWorldApp();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				app.createAndShowGUI();
			}
			
		});
		
	}
}
