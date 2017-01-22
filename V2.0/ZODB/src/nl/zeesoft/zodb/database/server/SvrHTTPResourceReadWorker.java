package nl.zeesoft.zodb.database.server;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.gui.GuiController;


public class SvrHTTPResourceReadWorker extends Worker  {
	private List<String>					resources					= new ArrayList<String>();
	private int								resourceIndex				= 0;
	private SortedMap<String,StringBuilder> cachedHTTPResources			= new TreeMap<String,StringBuilder>();
	private SortedMap<String,byte[]> 		cachedHTTPImageResources	= new TreeMap<String,byte[]>();
	private boolean							done						= false;

	public SvrHTTPResourceReadWorker() {
		setSleep(0);
	}
	
	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<String> resources) {
		this.resources = resources;
	}
	
	@Override
	public void start() {
		if (resources.size()>0) {
			setDone(false);
			resourceIndex = 0;
			cachedHTTPResources.clear();
			cachedHTTPImageResources.clear();
			super.start();
		} else {
			setDone(true);
		}
	}
		
	@Override
	public void whileWorking() {
		if (resourceIndex<resources.size()) {
			String file = resources.get(resourceIndex);
			if (file.endsWith(".html") || 
				file.endsWith(".js") || 
				file.endsWith(".css") || 
				file.endsWith(".xml")
				) {
				cachedHTTPResources.put(file,SvrController.getInstance().readFile(file));
			} else if ( 
				file.endsWith(".ico") || 
				file.endsWith(".png") 
				) {
				cachedHTTPImageResources.put(file,SvrController.getInstance().readImage(file));
			}
			GuiController.getInstance().incrementProgressFrameDone();
			resourceIndex++;
		} else {
			setDone(true);
			stop();
		}
	}
	
	private void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the cachedHTTPResources
	 */
	public SortedMap<String, StringBuilder> getCachedHTTPResources() {
		return cachedHTTPResources;
	}

	/**
	 * @return the cachedHTTPImageResources
	 */
	public SortedMap<String, byte[]> getCachedHTTPImageResources() {
		return cachedHTTPImageResources;
	}
}
