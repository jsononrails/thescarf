package thescarf.examples;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import thescarf.framework.*;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.utility.PolygonWrapper;
import thescarf.prototype.*;
import thescarf.framework.math.*;

public class FlyingShipExample extends GameBase {

	private PrototypeShip ship;
	private PolygonWrapper wrapper;
	private ArrayList<PrototypeBullet>bullets;
	
	public FlyingShipExample() {
		
		appBorderScale = 0.9f;
		appWidth = 1280;
		appHeight = 1024;
		appMaintainRatio = true;
		appSleep = 1L;
		appTitle = "Flying Ship Example";
		
	}
	
	@Override
	protected void initialize() {
		
		super.initialize();
		
		bullets = new ArrayList<PrototypeBullet>();
		
		wrapper = new PolygonWrapper(appWorldWidth, appWorldHeight);
		
		ship = new PrototypeShip(wrapper);
		
	}
	
	@Override
	protected void processInput(float delta) {
		
		super.processInput(delta);
		
		if(keyboard.keyDown(KeyEvent.VK_LEFT))
		{
			
			ship.rotateLeft(delta);
			
		}
		
		if(keyboard.keyDown(KeyEvent.VK_RIGHT))
		{
			
			ship.rotateRight(delta);
			
		}
		
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE))
		{
			
			bullets.add(ship.launchBullet());
			
		}
		
		ship.setThrusting(keyboard.keyDown(KeyEvent.VK_UP));
	}
	
	@Override
	protected void updateObjects(float delta) {
		
		super.updateObjects(delta);
		
		ship.update(delta);
		
		ArrayList<PrototypeBullet> copy = new ArrayList<PrototypeBullet>(bullets);
		
		for(PrototypeBullet bullet : copy) {
			
			bullet.update(delta);
			
			if(wrapper.hasLeftWorld(bullet.getPosition())) {
				
				bullets.remove(bullet);
				
			}
			
		}
	}
	
	@Override
	protected void render(Graphics g) {
		
		super.render(g);
		
		g.setColor(Color.GREEN);
		g.drawString("Rotate: Left/Right Arrow", 20, 35);
		g.drawString("Thrust: Up Arrow", 20, 50);
		g.drawString("Fire: Space Bar", 20, 65);
		
		Matrix3x3f view = getViewportTransform();
		
		// draw if only in world view
		if(!wrapper.hasLeftWorld(ship.getPosition())) {
			
			ship.draw((Graphics2D)g, view);
			
		}
		
		for(PrototypeBullet b : bullets) {
			
			b.draw((Graphics2D)g, view);
					
		}
		
	}
	
	public static void main(String[] args) {
			
		launchApp(new FlyingShipExample());
			
	}
}
