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
	private WorkerUnion					union		= null;
	private List<InitializeClass>		classes		= new ArrayList<InitializeClass>();
	private List<InitializeWorker>		workers		= new ArrayList<InitializeWorker>();
	private List<InitializerListener>	listeners	= new ArrayList<InitializerListener>();
	
	private int							initialized	= 0;

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
	 * Starts the initialization of classes.
	 */
	public void start() {
		for (InitializeWorker worker: workers) {
			worker.start();
		}
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
	
	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		if (initialized==workers.size()) {
			r = true;
		}
		unlockMe(this);
		return r;
	}
	
	protected boolean initializedClass(String name) {
		boolean done = false;
		InitializeClass cls = getClassByName(name);
		lockMe(this);
		initialized++;
		if (initialized==workers.size()) {
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
				obj = (Initializable) clas.newInstance();
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
			cls.fileName = fileName;
			classes.add(cls);
			InitializeWorker worker = new InitializeWorker(getMessenger(),union,this,cls);
			workers.add(worker);
		}
	}
}
