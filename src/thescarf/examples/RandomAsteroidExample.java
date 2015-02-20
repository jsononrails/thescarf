package thescarf.examples;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import thescarf.math.*;
import thescarf.framework.*;
import thescarf.prototype.*;
import thescarf.prototype.PrototypeAsteroid.SizeEnum;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.PolygonWrapper;
import thescarf.framework.utility.Utility;

public class RandomAsteroidExample extends GameBase {

	private PrototypeAsteroidFactory factory;
	private ArrayList<PrototypeAsteroid> asteroids;
	private Random rand;
	
	public RandomAsteroidExample() {
		
		appBorderScale = 0.9f;
		appWidth = 640;
		appHeight = 640;
		
		appMaintainRatio = true;
		appSleep = 1L;
		appTitle = "Random Asteroids";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
		
	}
	
	@Override
	protected void initialize() {
		
		super.initialize();
		
		rand = new Random();
		
		asteroids = new ArrayList<PrototypeAsteroid>();
		
		PolygonWrapper  wrapper = new PolygonWrapper(appWorldWidth, 
				appWorldHeight);
		
		factory = new PrototypeAsteroidFactory(wrapper);
		
		createAsteroids();
		
	}
	
	private void createAsteroids() {
		
		asteroids.clear();
		
		for(int i = 0; i < 42; ++i) {
			
			asteroids.add(getRandomAsteroid());
			
		}
	}
	
	private PrototypeAsteroid getRandomAsteroid() {
		
		float x = rand.nextFloat() * 2.0f - 1.0f;
		float y = rand.nextFloat() * 2.0f - 1.0f;
		
		Vector2f position = new Vector2f(x, y);
		SizeEnum[] sizes = SizeEnum.values();
		
		SizeEnum randomSize = sizes[rand.nextInt(sizes.length)];
		
		return factory.createAsteroid(position, randomSize);
	}
	
	@Override
	protected void processInput(float delta) {
		
		super.processInput(delta);
		
		if(keyboard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
			
			createAsteroids();
			
		}
	}
	
	@Override
	protected void updateObjects(float delta) {
		
		super.updateObjects(delta);
		
		for(PrototypeAsteroid asteroid : asteroids) {
			
			asteroid.update(delta);
			
		}
		
	}
	
	@Override
	protected void render(Graphics g) {
		
		super.render(g);
		
		g.drawString("Press ESC to respawn", 20, 35);
		
		Matrix3x3f view = getViewportTransform();
		
		for(PrototypeAsteroid asteroid : asteroids) {
			
			asteroid.draw((Graphics2D)g, view);
			
		}
		
	}
	
	public static void main(String[] args) {
		
		launchApp(new RandomAsteroidExample());
		
	}
}
