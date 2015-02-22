package thescarf.examples;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import thescarf.framework.math.*;
import thescarf.framework.*;
import thescarf.framework.input.KeyboardInput;
import thescarf.framework.input.RelativeMouseInput;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.FrameRate;
import thescarf.framework.input.*;

public class ScreenMappingExample extends JFrame implements Runnable {

	private Canvas canvas;
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private Vector2f[] triangle;
	private Vector2f[] triangleWorld;
	
	private Vector2f[] rectangle;
	private Vector2f[] rectangleWorld;
	
	private float worldHeight;
	private float worldWidth;
	
	public ScreenMappingExample() {
		
	}
	
	protected void createAndShowGUI() {
		
		canvas = new Canvas();
		canvas.setSize(640, 640);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		
		getContentPane().add(canvas);
		
		getContentPane().addComponentListener(new ComponentAdapter() {
			
			public void componentResized(ComponentEvent e) {
				
				onComponentResized(e);
				
			}
			
		});
		
		setTitle("Screen Mapping Example");
		setIgnoreRepaint(true);
		pack();
		
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		
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
	
	protected void onComponentResized(ComponentEvent e) {
		
		Dimension size = getContentPane().getSize();
		
		int vw = size.width * 3 / 4;
		int vh = size.height * 3 / 4;
		int vx = (size.width - vw) / 2;
		int vy = (size.height - vh) / 2;
		
		int newW = vw;
		
		int newH = (int)(vw * worldHeight / worldWidth);
		
		if(newH > vh) {
			
			newW = (int)(vh * worldWidth / worldHeight);
			newH = vh;
			
		}
		
		// center
		vx += (vw - newW) / 2;
		vy += (vh - newH) / 2;
		
		canvas.setLocation(vx, vy);
		canvas.setSize(newW, newH);
		
		
	}
	
	public void run() {
		
		running = true;
		
		initialize();
		
		long curTime = System.nanoTime();
		long lastTime = curTime;
		
		double nsPerFrame;
		
		while(running) {
			
			curTime = System.nanoTime();
			nsPerFrame = curTime - lastTime;
			
			gameLoop(nsPerFrame / 1.0E9);
			lastTime = curTime;
			
		}
	}
	
	private void initialize() {
		
		frameRate = new FrameRate();
		frameRate.initialize();
		
		triangle = new Vector2f[] {
			new Vector2f(0.0f, 0.05f),
			new Vector2f(-0.5f, -0.5f),
			new Vector2f(0.5f, -0.5f)
		};
		
		triangleWorld = new Vector2f[triangle.length];
		
		rectangle = new Vector2f[] {
				
				new Vector2f(-1.0f, 1.0f),
				new Vector2f(1.0f, 1.0f),
				new Vector2f(1.0f, -1.0f),
				new Vector2f(-1.0f, -1.0f)
				
		};
		
		rectangleWorld = new Vector2f[rectangle.length];
		
		worldWidth = 16.0f;
		worldHeight = 9.0f;
		
	}
	
	private void gameLoop(double delta) {
		
		processInput(delta);
		updateObjects(delta);
		renderFrame();
		sleep(10L);
		
	}
	
	private void renderFrame() {
		
		do {
			
			do {
			
				Graphics g = null;
			
				try{
				
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				
				} 
				finally {
				
					if(g!=null) {
					
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
	
	private void processInput(double delta) {
		
		keyboard.poll();
		mouse.poll();
		
	}
	
	private void updateObjects(double delta) {
		
	}
	
	private void render(Graphics g) {
		
		g.setColor(Color.BLACK);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 20);
		
		float sx = (canvas.getWidth() -1) / worldWidth;
		float sy = (canvas.getHeight() - 1) / worldHeight;
		
		float tx = (canvas.getWidth() - 1) / 2.0f;
		float ty = (canvas.getHeight() - 1) / 2.0f;
		
		Matrix3x3f viewport = Matrix3x3f.identity();
		viewport = viewport.multiply(Matrix3x3f.scale(sx, -sy));
		viewport = viewport.multiply(Matrix3x3f.translate(tx, ty));
		
		for(int i = 0; i < triangle.length; ++i) {
			
			triangleWorld[i] = viewport.multiply(triangle[i]);
			
		}
		
		drawPolygon(g, triangleWorld);
		
		for(int i = 0; i < rectangle.length; ++i) {
			
			rectangleWorld[i] = viewport.multiply(rectangle[i]);
			
		}
		
		drawPolygon(g, rectangleWorld);
		
	}
	
	private void drawPolygon(Graphics g, Vector2f[] polygon) {
		
		Vector2f P;
		Vector2f S = polygon[polygon.length-1];
		
		for(int i = 0; i < polygon.length; ++i) {
			
			P = polygon[i];
			g.drawLine((int)S.x, (int)S.y, (int)P.x, (int)P.y);
			S = P;
		}
	}
	
	protected void onWindowClosing() {
		
		try {
			
			running = false;
			gameThread.join();
			
		} catch(InterruptedException e) {
			
			e.printStackTrace();
			
		}
		
		System.exit(0);
	}
	
	public static void main(String[] args) {
		
		final ScreenMappingExample app = new ScreenMappingExample();
		
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
