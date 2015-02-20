package thescarf.examples;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import thescarf.math.*;
import thescarf.framework.*;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.PolygonWrapper;
import thescarf.framework.utility.Utility;

public class ScreenWrapExample extends GameBase {

	private Vector2f pos;
	private Vector2f[] poly;
	private ArrayList<Vector2f[]> renderList;
	private PolygonWrapper wrapper;
	
	public ScreenWrapExample() {
		
		appBorderScale = 0.9f;
		appWidth = 1280;
		appHeight = 1024;
		appMaintainRatio = true;
		appTitle = "Screen Wrap Example";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
		
	}
	
	@Override
	protected void initialize() {
		
		super.initialize();
		
		mouse.setRelative(true);
		renderList = new ArrayList<Vector2f[]>();
		
		wrapper = new PolygonWrapper(appWorldWidth, appWorldHeight);
		
		poly = new Vector2f[] {
				
				new Vector2f(-0.125f, 0.125f),
				new Vector2f(0.125f, 0.125f),
				new Vector2f(0.125f, -0.125f),
				new Vector2f(-0.125f, -0.125f),
		};
		
		pos = new Vector2f();
		
	}
	
	@Override
	protected void processInput(float delta) {
		
		super.processInput(delta);
		
		if(mouse.isRelative()) {
			
			Vector2f v = getRelativeWorldMousePosition();
			
			pos = pos.add(v);
			
		} else {
			
			pos = getWorldMousePosition();
			
		}
		
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			
			mouse.setRelative(!mouse.isRelative());
			
			if(mouse.isRelative()) {
				
				pos = new Vector2f();
				
			}
			
		}
	}
	
	@Override
	protected void updateObjects(float delta) {
		
		super.updateObjects(delta);
		
		renderList.clear();
		
		pos = wrapper.wrapPosition(pos);
		
		Vector2f[] world = transform(poly, Matrix3x3f.translate(pos));
		
		renderList.add(world);
		wrapper.wrapPolygon(world, renderList);
		
	}
	
	
	private Vector2f[] transform(Vector2f[] poly, Matrix3x3f mat) {
		
		Vector2f[] copy = new Vector2f[poly.length];
		
		for(int i = 0; i < poly.length; ++i) {
			
			copy[i] = mat.multiply(poly[i]);
			
		}
		
		return copy;
		
	}
	
	@Override
	protected void render(Graphics g) {
		
		super.render(g);
		g.drawString("Press Space to Toggle mouse", 20, 35);
		
		Matrix3x3f view = getViewportTransform();
		
		for(Vector2f[] toRender : renderList) {
			
			for(int i = 0; i < toRender.length; ++i) {
				
				toRender[i] = view.multiply(toRender[i]);
				
			}
			
			Utility.drawPolygon(g, toRender);
			
		}
		
	}
	
	public static void main(String[] args) {
		
		launchApp(new ScreenWrapExample());
		
	}
}
