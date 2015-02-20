package thescarf.framework;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import javax.swing.*;

import thescarf.framework.utility.FrameRate;
import thescarf.framework.utility.GraphicsUtils;

public class GameFramework extends JFrame implements Runnable {

	private BufferStrategy bs;
	private volatile boolean running;
	private Thread GameThread;
	
	protected int vx;
	protected int vy;
	protected int vw;
	protected int vh;
	
	protected FrameRate frameRate;
	
}
