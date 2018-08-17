package nl.zeesoft.zsd.initialize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * An Initializer can be used to initialize a set of objects using a separate thread for each object.
 */
public class Initializer extends Locker {
	private WorkerUnion					union			= null;
	private List<InitializeClass>		classes			= new ArrayList<InitializeClass>();
	private List<InitializerWorker>		workers			= new ArrayList<InitializerWorker>();
	private List<InitializerListener>	listeners		= new ArrayList<InitializerListener>();

	private boolean						initializing	= false;
	private int							initialized		= -1;

	public Initializer() {
		super(null);
	}

	public Initializer(Messenger msgr,WorkerUnion uni) {
		super(msgr);
		union = uni;
	}

	/**
	 * Adds a listener to the initializer.
	 * 
	 * @param l The initializer listener
	 */
	public void addListener(InitializerListener l) {
		listeners.add(l);
	}

	/**
	 * Adds a class to the initializer.
	 * 
	 * @param cls The class to add
	 */
	public void addClass(InitializeClass cls) {
		if (cls.obj!=null) {
			classes.add(cls);
			InitializerWorker worker = new InitializerWorker(getMessenger(),union,this,cls);
			workers.add(worker);
		}
	}

	/**
	 * Adds a class to the initializer.
	 * 
	 * @param uniqueName The unique name of the class
	 * @param obj The object to initialize
	 * @param fileName The optional name of the file to use for initialization
	 */
	public void addClass(String uniqueName,Initializable obj,String fileName) {
		addClass(uniqueName,obj.getClass().getName(),fileName,obj);
	}


	/**
	 * Adds a class to the initializer.
	 * 
	 * @param uniqueName The unique name of the class
	 * @param className The full java class name
	 * @param fileName The optional name of the file to use for initialization
	 */
	public void addClass(String uniqueName,String className,String fileName) {
		addClass(uniqueName,className,fileName,null);
	}

	/**
	 * Removes a class from the initializer.
	 * 
	 * @param uniqueName The unique name of the class
	 */
	public void removeClass(String uniqueName) {
		int i = 0;
		for (InitializeClass cls: getClasses()) {
			if (cls.name.equals(uniqueName)) {
				classes.remove(cls);
				workers.remove(i);
			} else {
				i++;
			}
		}
	}

	/**
	 * Starts the initialization of classes.
	 * 
	 * @return True if the initialization has started
	 */
	public boolean start() {
		return startIfNotWorking();
	}
		
	/**
	 * Returns a specific class using the unique name.
	 * 
	 * @param name The name of the class
	 * @return The class or null
	 */
	public InitializeClass getClassByName(String name) {
		InitializeClass r = null;
		for (InitializeClass cls: classes) {
			if (cls.name.equals(name)) {
				r = cls;
				break;
			}
		}
		return r;
	}
	
	/**
	 * Returns true if the initialization is done.
	 * 
	 * @return True if the initialization is done
	 */
	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		if (!isWorkingNoLock() && initialized==workers.size()) {
			r = true;
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Returns a copy of the list of classes.
	 * 
	 * @return A copy of the list of classes
	 */
	public List<InitializeClass> getClasses() {
		return new ArrayList<InitializeClass>(classes);
	}

	public WorkerUnion getUnion() {
		return union;
	}
	
	protected boolean initializedClass(String name) {
		boolean done = false;
		InitializeClass cls = getClassByName(name);
		lockMe(this);
		initialized++;
		if (initialized==workers.size()) {
			initializing = false;;
			done = true;
		}
		unlockMe(this);
		for (InitializerListener l: listeners) {
			l.initializedClass(cls,done);
		}
		return done;
	}

	private void addClass(String uniqueName,String className,String fileName, Initializable obj) {
		if (obj==null) {
			try {
				Class<?> clas = Class.forName(className);
				Object o = (Initializable) clas.newInstance();
				if (o instanceof Initializable) {
					obj = (Initializable) o;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (obj!=null) {
			InitializeClass cls = new InitializeClass();
			cls.name = uniqueName;
			cls.className = className;
			cls.obj = obj;
			if (fileName.length()>0) {
				cls.fileNames.add(fileName);
			}
			addClass(cls);
		}
	}
	
	private boolean startIfNotWorking() {
		boolean r = false;
		lockMe(this);
		if (!initializing && !isWorkingNoLock()) {
			initializing = true;
			initialized = 0;
			for (InitializerWorker worker: workers) {
				worker.start();
			}
			r = initializing;
		}
		unlockMe(this);
		return r;
	}
	
	private boolean isWorkingNoLock() {
		boolean r = false;
		for (InitializerWorker worker: workers) {
			if (worker.isWorking()) {
				r = true;
				break;
			}
		}
		return r;
	}
}
