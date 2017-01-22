package nl.zeesoft.zadf.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PnlBusyJPanel extends JPanel {
	public static final long serialVersionUID = 0;
	
	private static int 	SQUARES 	= 20;
	private static int 	STEPS 		= 24;
	
	private int 		step 		= 0;

	public PnlBusyJPanel() {
		setPreferredSize(new Dimension(68,38));
	}

	public void step() {
		step++;
		if (step>=STEPS) {
			step = 0;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		int drawStep = step;
		int red = 0;
		int green = 0;
		int blue = 0;
		int sq = 0;
		int x = 0;
		int y = 0;
		int size = 10;
		
		boolean[] done = new boolean[SQUARES];
		for (int d = 0; d < SQUARES; d++) {
			done[d] = false;
		}
		
		for (int s = 0; s < STEPS; s++) {
			sq = getSquareForStep(drawStep);
			x = getPosXForStep(drawStep);
			y = getPosYForStep(drawStep);
			if (!done[sq]) {
				g.setColor(new Color(red,green,blue));
				g.fillRect(x, y, size, size);
				done[sq] = true;
			}
			red = red + 10;
			green = green + 10;
			blue = blue + 10;
			
			if (drawStep==0) {
				drawStep = (STEPS - 1);
			} else {
				drawStep--;
			}
		}
    }
	
	private int getSquareForStep(int step) {
		int sq = 0;
		if ((step>=15) && (step<=18)) {
			sq = (step - 12);
		} else if (step>18) {
			sq = step - 4;
		} else {
			sq = step;
		}
		return sq;
	}

	private int getPosXForStep(int step) {
		int x = 0;
		int sq = getSquareForStep(step);
		if ((sq==0) || (sq==17) || (sq==18) || (sq==17)) {
			x = 0;
		} else if ((sq==1) || (sq==16)) {
			x = 10;
		} else if ((sq==2) || (sq==15)) {
			x = 20;
		} else if ((sq==3) || (sq==4) || (sq==5) || (sq==6)) {
			x = 30;
		} else if ((sq==14) || (sq==7)) {
			x = 40;
		} else if ((sq==13) || (sq==8)) {
			x = 50;
		} else if ((sq==12) || (sq==11) || (sq==10) || (sq==9)) {
			x = 60;
		}
		return x;
	}

	private int getPosYForStep(int step) {
		int y = 0;
		int sq = getSquareForStep(step);
		if ((sq==0) || (sq==1) || (sq==2) || (sq==3) || (sq==14) || (sq==13) || (sq==12)) {
			y = 0;
		} else if ((sq==19) || (sq==4) || (sq==11)) {
			y = 10;
		} else if ((sq==18) || (sq==5) || (sq==10)) {
			y = 20;
		} else if ((sq==17) || (sq==16) || (sq==15) || (sq==6) || (sq==7) || (sq==8) || (sq==9)) {
			y = 30;
		}
		return y;
	}

}
