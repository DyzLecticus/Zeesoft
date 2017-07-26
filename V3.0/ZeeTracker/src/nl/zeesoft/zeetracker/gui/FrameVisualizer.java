package nl.zeesoft.zeetracker.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.jme3.system.JmeCanvasContext;

import nl.zeesoft.ztv.Visualizer;

public class FrameVisualizer extends FrameObject implements ActionListener {
	private static final String	TITLE				= "ZeeTracker Visualizer";

	private Visualizer			visualizer			= null;
	
	public FrameVisualizer(Controller controller,Visualizer vis) {
		super(controller);
		visualizer = vis;
	}

	@Override
	public void initialize() {
		getFrame().setTitle(TITLE);

		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(getController().getAdapter());
		getFrame().addWindowFocusListener(getController().getAdapter());
		getFrame().addKeyListener(getController().getPlayerKeyListener());
		
	    JmeCanvasContext context = (JmeCanvasContext) visualizer.getContext();
	    Canvas canvas = context.getCanvas();
	    getFrame().add(canvas);

	    canvas.addKeyListener(getController().getPlayerKeyListener());

	    getFrame().pack();
		
		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		getFrame().setSize(width,height);
		getFrame().setLocation(50,50);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO: Implement
	}
}
