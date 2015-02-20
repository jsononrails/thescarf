package thescarf.prototype;

import java.awt.*;
import java.util.*;

import thescarf.framework.math.*;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.PolygonWrapper;
import thescarf.framework.utility.GraphicsUtils;

public class PrototypeAsteroid {
	
	public static enum SizeEnum {
		
		Large,
		Medium,
		Small
		
	}

	private PolygonWrapper wrapper;
	private SizeEnum size;
	
	private float rotation;
	private float rotationDelta;
	
	private Vector2f[] polygon;
	private Vector2f position;
	private Vector2f velocity;
	private ArrayList<Vector2f[]> renderList;
	
	public PrototypeAsteroid(PolygonWrapper wrapper) {
		
		this.wrapper = wrapper;
		renderList = new ArrayList<Vector2f[]>();
		
		velocity = getRandomVelocity();
		rotationDelta = getRandomRotationDelta();
		
	}
	
	private Vector2f getRandomVelocity() {
		
		float angle = getRandomRadians(0, 360);
		float radius = getRandomFloat(0.06f, 0.3f);
		
		return Vector2f.polar(angle, radius);
		
	}
	
	private float getRandomRadians(int minDegree, int maxDegree) {
		
		int rand = new Random().nextInt(maxDegree - minDegree + 1);
		
		return (float)Math.toRadians(rand + minDegree);
		
	}
	
	private float getRandomRotationDelta() {
		
		float radians = getRandomRadians(5, 45);
		
		return new Random().nextBoolean() ? radians: -radians;
		
	}
	
	private float getRandomFloat(float min, float max) {
		
		float rand = new Random().nextFloat();
		
		return rand * (max - min) + min;
		
	}
	
	public void setPolygon(Vector2f[] polygon) {
		
		this.polygon = polygon;
		
	}
	
	public void setPosition(Vector2f position) {
		
		this.position = position;
		
	}
	
	public Vector2f getPosition() {
		
		return position;
		
	}
	
	public void setSize(SizeEnum size) {
		
		this.size = size;
		
	}
	
	public SizeEnum getSize() {
		
		return size;
		
	}
	
	public void update(float time) {
		
		position = position.add(velocity.multiply(time));
		position = wrapper.wrapPosition(position);
		
		rotation += rotationDelta * time;
		
		renderList.clear();
		
		Vector2f[] world = transformPolygon();
		renderList.add(world);
		
		wrapper.wrapPolygon(world, renderList);
		
	}
	
	private Vector2f[] transformPolygon() {
		
		Matrix3x3f mat = Matrix3x3f.rotate(rotation);
		mat = mat.multiply(Matrix3x3f.translate(position));
		
		return transform(polygon, mat);
		
	}
	
	private Vector2f[] transform(Vector2f[] poly, Matrix3x3f mat) {
		
		Vector2f[] copy = new Vector2f[poly.length];
		
		for(int i = 0; i < poly.length; ++i) {
			
			copy[i] = mat.multiply(poly[i]);
			
		}
		
		return copy;
	}
	
	public void draw(Graphics2D g, Matrix3x3f view) {
		
		for(Vector2f[] poly : renderList) {
			
			for(int i = 0; i < poly.length; ++i) {
				
				poly[i] = view.multiply(poly[i]);
				
			}
			
			g.setColor(Color.DARK_GRAY);
			GraphicsUtils.fillPolygon(g, poly);
			g.setColor(Color.BLACK);
			GraphicsUtils.drawPolygon(g, poly);
			
		}
		
	}
	
	public boolean contains(Vector2f point) {
		
		for(Vector2f[] polygon : renderList) {
			
			if(pointInPolygon(point, polygon)) {
				
				return true;
			}
		}
		
		return false;
		
	}
	
	private boolean pointInPolygon(Vector2f point, Vector2f[] polygon) {
		
		boolean inside = false;
		
		Vector2f start = polygon[polygon.length - 1];
		
		boolean startAbove = start.y >= point.y;
		
		for(int i = 0; i < polygon.length; ++i) {
			
			Vector2f end = polygon[i];
			
			boolean endAbove = end.y >= point.y;
			
			if(startAbove != endAbove) {
				
				float m = (end.y - start.y) / (end.x - start.x);
				
				float x = start.x + (point.y - start.y) / m;
				
				if(x >= point.x) {
					
					inside = !inside;
					
				}
				
			}
			
			startAbove = endAbove;
			
			start = end;
			
		}
		
		return inside;
		
	}
	
}
