package nl.zeesoft.zdbd.gui;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.ProgressListener;
import nl.zeesoft.zdk.thread.RunCode;

public class ProgressHandler implements ProgressListener {
	private Lock			lock	= new Lock();
	private JLabel			label	= new JLabel();
	private JProgressBar	bar		= new JProgressBar(0,100);
	private int				todo	= 0;
	private int				done	= 0;

	private CodeRunner		cleanUp = null;
	private int				counter	= 0;

	public ProgressHandler() {
		cleanUp = new CodeRunner(getCleanUpRunCode());
		cleanUp.setSleepMs(1000);
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public JProgressBar getBar() {
		return bar;
	}
	
	@Override
	public void initialized(int todo) {
		lock.lock(this);
		this.todo = todo;
		this.done = 0;
		lock.unlock(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				bar.setValue(0);
			}
        });
	}

	@Override
	public void progressed(int steps) {
		lock.lock(this);
		done += steps;
		float perc = ((float)done / (float)todo);
		lock.unlock(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				bar.setValue((int)(perc * 100));
			}
        });
	}

	public void startChain(CodeRunnerChain chain) {
		final ProgressListener listener = this;
		SwingWorker<String, Object> sw = new SwingWorker<String, Object>() {
			@Override
			public String doInBackground() {
				cleanUp.stop();
				chain.addProgressListener(listener);
				chain.add(getStartCleanUpRunCode());
				chain.start();
				return "";
	       }
		};
		sw.execute();
	}

	protected RunCode getStartCleanUpRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				lock.lock(this);
				counter	= 0;
				lock.unlock(this);
				cleanUp.start();
				return true;
			}
		};
	}
	
	protected RunCode getCleanUpRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				boolean done = false;
				lock.lock(this);
				counter++;
				if (counter>1) {
					counter = 0;
					done = true;
				}
				lock.unlock(this);
				if (done) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							label.setText("");
							bar.setValue(0);
						}
	                });
				}
				return done;
			}
		};
	}
}
