package thescarf.examples;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;

import javax.swing.*;

import thescarf.framework.input.KeyboardInput;
import thescarf.framework.input.RelativeMouseInput;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.FrameRate;
import thescarf.input.*;
import thescarf.math.*;

public class MatrixMultiplyExample extends JFrame implements Runnable {
	
	private static final int SCREEN_W = 1280;
	private static final int SCREEN_H = 1024;
	
	private FrameRate frameRate;
	
	private BufferStrategy bs;
	
	private volatile boolean running;
	
	private Thread gameThread;
	
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private float earthRot, earthDelta;
	private float moonRot, moonDelta;
	
	private boolean showStars;
	
	private int[] stars;
	
	private Random rand = new Random();
	
	public MatrixMultiplyExample() {
		
	}
	
	protected void createAndShowGUI() {
		
		Canvas canvas = new Canvas();
		
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		
		setTitle("Matrix Multiply Example");
		setIgnoreRepaint(true);
		pack();
		
		// add key listeners
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		
		// add mouse listeners
		// for full screen: mouse = new RelativeMouseInput(this);
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		gameThread = new Thread(this);
		gameThread.start();
		
	}
	
	public void run() {
		
		running = true;
		
		initialize();
		
		while(running) {
			
			gameLoop();
			
		}
		
	}
	
	private void gameLoop() {
		
		processInput();
		
		renderFrame();
		
		sleep(10L);
		
	}
	
	private void renderFrame() {
		
		do {
			
			do {
				
				Graphics g = null;
				
				try {
					
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());
					render(g);
					
				} finally {
					
					if(g != null) {
						
						g.dispose();
						
					}
					
				}
				
			} while(bs.contentsRestored());
			
			bs.show();
			
		} while(bs.contentsLost());
		
	}
	
	private void sleep(long sleep) {
		
		try {
			
			Thread.sleep(sleep);
			
		} catch(InterruptedException ex) {}
	}
	
	private void initialize() {
		
		frameRate = new FrameRate();
		frameRate.initialize();
		
		earthDelta = (float)Math.toRadians(0.5);
		moonDelta = (float)Math.toRadians(2.5);
		
		showStars = true;
		stars = new int [100];
		
		for(int i = 0; i < stars.length -1;  i +=2) {
			
			stars[i] = rand.nextInt(SCREEN_W);
			stars[i+1] = rand.nextInt(SCREEN_H); 
			
		}
	}
	
	private void processInput() {
		
		keyboard.poll();
		mouse.poll();
		
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			
			showStars = !showStars;
		}
		
	}
	
	private void render(Graphics g) {
		
		g.setColor(Color.GREEN);
		frameRate.calculate();
		
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("Press [SPACE] to toggle stars", 20, 35);
		
		if(showStars) {
			
			g.setColor(Color.WHITE);
			
			for(int i=0; i < stars.length - 1; i+=2) {
				
				g.fillRect(stars[i], stars[i+1], 1, 1);
				
			}
		}
		
		// draw the sun...
		Matrix3x3f sunMatrix = Matrix3x3f.identity();
		
		sunMatrix = sunMatrix.multiply(
		
				Matrix3x3f.translate(SCREEN_W / 2, SCREEN_H / 2)
				
		);
		
		Vector2f sun = sunMatrix.multiply(new Vector2f());
		
		g.setColor(Color.YELLOW);
		g.fillOval((int)sun.x - 50, (int)sun.y - 50, 100, 100);
		
		// draw earth's orbit
		g.setColor(Color.WHITE);
		g.drawOval((int)sun.x - SCREEN_W / 4, (int)sun.y - SCREEN_W / 4,
				SCREEN_W / 2, SCREEN_W / 2);
		
		// draw earth
		Matrix3x3f earthMatrix = Matrix3x3f.translate(SCREEN_W / 4, 0);
		earthMatrix = earthMatrix.multiply(Matrix3x3f.rotate(earthRot));
		earthMatrix = earthMatrix.multiply(sunMatrix);
		
		earthRot += earthDelta;
		
		Vector2f earth = earthMatrix.multiply(new Vector2f());
		
		g.setColor(Color.BLUE);
		g.fillOval((int)earth.x - 10, (int)earth.y - 10, 20, 20);
		
		// draw moon
		Matrix3x3f moonMatrix = Matrix3x3f.translate(30, 0);
		moonMatrix = moonMatrix.multiply(Matrix3x3f.rotate(moonRot));
		moonMatrix = moonMatrix.multiply(earthMatrix);
		
		moonRot += moonDelta;
		
		Vector2f moon = moonMatrix.multiply(new Vector2f());
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int)moon.x -5, (int)moon.y -5, 10,10);
	}
	
	protected void onWindowClosing() {
		
		try {
			
			running = false;
			gameThread.join();
			
		} catch(InterruptedException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		
		final MatrixMultiplyExample app = new MatrixMultiplyExample();
		
		app.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				
				app.onWindowClosing();
				
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				app.createAndShowGUI();
				
			}
			
		});
		
	}
}
