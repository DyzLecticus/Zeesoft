package nl.zeesoft.zodb.database.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.ZODB;

public class GuiProgressFrame extends GuiFrameObject {
	private int 	width			= 200;
	private int 	height			= 20;

	private int 	todo			= 0;
	private int 	done			= 0;
	private boolean corner			= false;
	
	private JPanel	progressPanel	= null;
	
	public GuiProgressFrame(int width,int height) {
		this.width = width;
		this.height = height;
		setRenderDialog(true);
	}

	@Override
	public void setTitle(String title) {
		lockMe(this);
		super.setTitle(title);
		unlockMe(this);
		setDone(0);
	}

	public void setTodo(int todo) {
		lockMe(this);
		this.todo = todo;
		this.done = 0;
		unlockMe(this);
	}
	
	public void setDone(int done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
		checkDone();
		refreshPanel();
	}

	public void incrementDone() {
		incrementDone(1);
	}

	public void incrementDone(int increment) {
		lockMe(this);
		done = done + increment;
		unlockMe(this);
		checkDone();
		refreshPanel();
	}
	
	@Override
	public void centreFrameLocation() {
		boolean c = false; 
		lockMe(this);
		c = corner;
		unlockMe(this);
		if (c) {
			cornerFrameLocation(false,false);
		} else {
			super.centreFrameLocation();
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	protected void render() {
		JFrame f = new JFrame();
		final Color background = f.getBackground();
		final Font font	= f.getFont();
				
		setIconImage(ZODB.getIconImage().getBufferedImage());
		setResizable(false);
		setAlwaysOnTop(true);
		setLayout(new GridBagLayout());
		
		progressPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
		        return new Dimension(width + 10,height + 10);
		    }
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;

				g2.setColor(background);
				g2.fillRect(0,0,width + 10,height + 10);
				
				lockMe(this);
				int t = todo;
				int d = done;
				boolean c = corner;
				unlockMe(this);

				if (c) {
					g2.setColor(Color.BLACK);
					g2.drawRect(0,0,width + 9,height + 9);
				}

				g2.setColor(Color.WHITE);
				g2.fillRect(5,5,width,height);
				g2.setColor(Color.BLACK);
				g2.drawRect(4,4,width + 1,height + 1);
								
				if (t>0 && d>0) {
					BigDecimal tdo = new BigDecimal("" + t);
					BigDecimal dne = new BigDecimal("" + d);
					tdo = tdo.setScale(2);
					dne = dne.setScale(2);
					int pixels = 0; 

					BigDecimal percentage = dne.divide(tdo,BigDecimal.ROUND_HALF_UP);
					BigDecimal pxls = percentage.multiply(new BigDecimal("" + width));
					pxls = pxls.setScale(0,BigDecimal.ROUND_HALF_UP);
					pixels = pxls.intValue();

					g2.setColor(Color.GREEN);
					g2.fillRect(5,5,pixels,height);
				}

				g2.setColor(Color.BLACK);
				g2.setFont(font);
				g2.drawString(getTitle(),10,((height / 2) + 10));
			}

		};
		setJPanel(progressPanel);
		
		super.render();
	}
	
	protected void setCorner(boolean corner) {
		lockMe(this);
		this.corner = corner;
		unlockMe(this);
		setUndecorated(corner);
		refreshPanel();
	}
	
	private void checkDone() {
		lockMe(this);
		if (done>todo) {
			Messenger.getInstance().debug(this,"Done is greater than todo: " + done + " > " + todo + " (" + getTitle() + ")");
			done = todo;
		}
		unlockMe(this);
	}
}
