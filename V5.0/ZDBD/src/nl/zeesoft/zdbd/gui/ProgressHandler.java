package nl.zeesoft.zdbd.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import nl.zeesoft.zdbd.Event;
import nl.zeesoft.zdbd.EventListener;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.ProgressListener;
import nl.zeesoft.zdk.thread.RunCode;

public class ProgressHandler implements EventListener, ProgressListener, ActionListener {
	private Lock			lock	= new Lock();
	private MainWindow		window	= null;
	
	private JLabel			label	= new JLabel();
	private JProgressBar	bar		= new JProgressBar(0,100);
	
	private String			text	= "";
	private int				todo	= 0;
	private int				done	= 0;

	private Timer			timer	= null;
	
	public ProgressHandler(MainWindow window) {
		this.window = window;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public JProgressBar getBar() {
		return bar;
	}

	@Override
	public void handleEvent(Event event) {
		lock.lock(this);
		this.text = event.name;
		refresh(done,todo,text);
		lock.unlock(this);
	}
	
	@Override
	public void initialized(int todo) {
		lock.lock(this);
		this.todo = todo;
		this.done = 0;
		refresh(done,todo,text);
		lock.unlock(this);
	}

	@Override
	public void progressed(int steps) {
		lock.lock(this);
		done += steps;
		refresh(done,todo,text);
		lock.unlock(this);
	}

	public void startChain(CodeRunnerChain chain) {
		lock.lock(this);
		if (timer!=null) {
			timer.stop();
			timer = null;
		}
		lock.unlock(this);
		final ProgressHandler self = this;
		chain.add(new RunCode() {
			@Override
			protected boolean run() {
				window.refresh();
				lock.lock(this);
				timer = new Timer(1000,self);
				timer.start();
				lock.unlock(this);
				return true;
			}
		});
		chain.addProgressListener(this);
		chain.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==timer) {
			lock.lock(this);
			text = "";
			done = 0;
			timer.stop();
			timer = null;
			refresh(0,1,"");
			lock.unlock(this);
		}
	}
	
	protected void refresh(int done, int todo, String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				label.setText(text);
				float perc = ((float)done / (float)todo);
				bar.setValue((int)(perc * 100));
			}
		});
	}
}
