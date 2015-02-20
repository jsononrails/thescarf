package thescarf.prototype;

import java.util.Random;

import thescarf.prototype.PrototypeAsteroid.SizeEnum;
import thescarf.framework.math.Matrix3x3f;
import thescarf.framework.math.Vector2f;
import thescarf.framework.utility.PolygonWrapper;

public class PrototypeAsteroidFactory {

	private static final Vector2f[][] LARGE = {
		
		{// Large 0
			new Vector2f(-0.67155427f, 0.37679273f),
		    new Vector2f(-0.17888564f, 0.5019557f),
		    new Vector2f(0.083088994f, 0.014341593f),
		    new Vector2f(-0.49951124f, -0.035202026f),
		    new Vector2f(-0.38220918f, 0.14732724f)
		},
		{// Large 1
			new Vector2f(0.3294233f, 0.43676662f),
		     new Vector2f(-0.143695f, 0.009126484f),
		     new Vector2f(0.3294233f, -0.23859191f),
		     new Vector2f(0.47018576f, 0.12646675f),
		     new Vector2f(0.17693055f, 0.1734029f),
		     new Vector2f(0.3528837f, 0.2594524f),
		     new Vector2f(0.51319647f, 0.20990872f),
		     new Vector2f(0.4780059f, 0.4393742f),
		     new Vector2f(0.35092866f, 0.37679273f)
		},
		{// Large 2
			new Vector2f(0.33919847f, 0.1942634f),
		     new Vector2f(-0.28054738f, 0.001303792f),
		     new Vector2f(-0.34115344f, -0.25945234f),
		     new Vector2f(0.18475068f, -0.56714463f),
		     new Vector2f(0.20625615f, -0.48631024f),
		     new Vector2f(0.4486804f, -0.5462842f),
		     new Vector2f(0.52297163f, -0.3585397f),
		     new Vector2f(0.4134897f, -0.16036499f),
		     new Vector2f(0.26686215f, -0.10039115f),
		     new Vector2f(0.3958944f, -0.009126425f),
		     new Vector2f(0.43304002f, 0.07431555f),
		     new Vector2f(0.38220918f, 0.11864406f),
		},
	};
	
	private static final Vector2f[][] MEDIUM = {
		
		{// Medium 0
			new Vector2f(0.25904202f, 0.61408085f),
		     new Vector2f(-0.11827958f, 0.4680574f),
		     new Vector2f(-0.12805474f, 0.13950455f),
		     new Vector2f(0.2629521f, 0.21251631f),
		     new Vector2f(0.35483873f, 0.07953066f),
		     new Vector2f(0.35679376f, 0.32464147f),
		     new Vector2f(0.39784944f, 0.5306389f)
		},
		{// Medium 1
			new Vector2f(-0.6989247f, 0.12385923f),
		     new Vector2f(-0.5581623f, -0.30899608f),
		     new Vector2f(-0.48973608f, -0.23337674f),
		     new Vector2f(-0.46627563f, -0.029986978f),
		     new Vector2f(-0.43890518f, 0.06388527f),
		     new Vector2f(-0.51124144f, 0.1734029f),
		     new Vector2f(-0.6735093f, 0.1864407f),
		     new Vector2f(-0.72238517f, 0.20208609f),
		     new Vector2f(-0.7986315f, 0.058670163f)
		},
		{// Medium 2
			new Vector2f(0.11241448f, -0.0925684f),
		     new Vector2f(-0.18475074f, -0.17079532f),
		     new Vector2f(-0.20821112f, 0.04302478f),
		     new Vector2f(-0.32160312f, 0.15254241f),
		     new Vector2f(-0.07331377f, 0.30899608f),
		     new Vector2f(0.043988228f, 0.32203388f)
		},
	};
	
	private static final Vector2f[][] SMALL = {
		
		{// Small 0
			new Vector2f(-0.2961877f, 0.494133f),
		     new Vector2f(-0.086999f, 0.48370272f),
		     new Vector2f(-0.116324544f, 0.7131682f),
		     new Vector2f(-0.32160312f, 0.6949153f),
		     new Vector2f(-0.3470186f, 0.61408085f),
		     new Vector2f(-0.32746822f, 0.53846157f),
		     new Vector2f(-0.28836757f, 0.5462842f),
		     new Vector2f(-0.21994138f, 0.5202086f),
		},
		{// Small 1
			new Vector2f(-0.21798635f, 0.33246416f),
		     new Vector2f(-0.006842613f, 0.33767927f),
		     new Vector2f(-0.07331377f, 0.57757497f),
		     new Vector2f(-0.16324538f, 0.4732725f),
		     new Vector2f(-0.14760506f, 0.4393742f),
		     new Vector2f(-0.11241448f, 0.39504564f)
		},
		{// Small 2
			new Vector2f(-0.2961877f, 0.41590613f),
		     new Vector2f(-0.2512219f, 0.4993481f),
		     new Vector2f(-0.19843596f, 0.41329855f),
		     new Vector2f(-0.2336266f, 0.35593224f),
		     new Vector2f(-0.3079179f, 0.33767927f),
		     new Vector2f(-0.32942325f, 0.41851372f),
		     new Vector2f(-0.3020528f, 0.38461542f),
		     new Vector2f(-0.25317693f, 0.452412f)
		},
	};
	
	private PolygonWrapper wrapper;
	private Random rand;
	
	public PrototypeAsteroidFactory(PolygonWrapper wrapper) {
		
		this.wrapper = wrapper;
		this.rand = new Random();
		
	}
	
	public PrototypeAsteroid createAsteroid(Vector2f position, SizeEnum size) {
		
		PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
		
		asteroid.setPosition(position);
		
		switch(size) 
		{
		case Large:
			asteroid.setPolygon(getRandomAsteroid(LARGE));
			break;
		case Medium:
			asteroid.setPolygon(getRandomAsteroid(MEDIUM));
			break;
		case Small:
			asteroid.setPolygon(getRandomAsteroid(SMALL));
			break;
		default:
			asteroid.setPolygon(getRandomAsteroid(SMALL));
			break;
		
		}
		
		asteroid.setSize(size);
		
		return asteroid;
	}
	
	private Vector2f[] getRandomAsteroid(Vector2f[][] asteroids) {
		
		return mirror(asteroids[rand.nextInt(asteroids.length)]);
		
	}
	
	private Vector2f[] mirror(Vector2f[] polygon) {
		
		Vector2f[] mirror = new Vector2f[polygon.length];
		
		float x = rand.nextBoolean() ? 1.0f : -1.0f;
		float y = rand.nextBoolean() ? 1.0f : -1.0f;
		
		Matrix3x3f mat = Matrix3x3f.scale(x, y);
		
		for(int i = 0; i < polygon.length; ++i) {
			
			mirror[i] = mat.multiply(polygon[i]);
		}
		
		return mirror;
	}
	
}
