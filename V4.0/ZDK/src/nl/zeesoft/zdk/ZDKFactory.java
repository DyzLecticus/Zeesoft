package nl.zeesoft.zdk;

import java.awt.Color;

import nl.zeesoft.zdk.image.ImageIcon;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Factory for ZDK application singletons.
 * Helper for dynamic class instantiation.
 */
public class ZDKFactory {
	private Messenger	msgr	= null;
	private WorkerUnion	union	= null;

	private ImageIcon	icon32	= null;
	private ImageIcon	icon48	= null;
	private ImageIcon	icon64	= null;

	public Messenger getMessenger() {
		if (msgr==null) {
			msgr = new Messenger(getWorkerUnion(null));
		}
		return msgr;
	}
	
	public Messenger getMessenger(WorkerUnion union) {
		if (msgr==null) {
			msgr = new Messenger(union);
		}
		return msgr;
	}
	
	public WorkerUnion getWorkerUnion() {
		if (union==null) {
			union = new WorkerUnion(getMessenger(null));
		}
		return union;
	}

	public WorkerUnion getWorkerUnion(Messenger msgr) {
		if (union==null) {
			union = new WorkerUnion(msgr);
		}
		return union;
	}
	
	public ImageIcon getZeesoftIcon32() {
		if (icon32==null) {
			icon32 = new ImageIcon("z",32,Color.WHITE);
		}
		return icon32;
	}

	public ImageIcon getZeesoftIcon48() {
		if (icon48==null) {
			icon48 = new ImageIcon("z",48,Color.WHITE);
		}
		return icon48;
	}

	public ImageIcon getZeesoftIcon64() {
		if (icon64==null) {
			icon64 = new ImageIcon("z",64,Color.WHITE);
		}
		return icon64;
	}

	public static boolean hasClassForName(String className) {
		boolean r = true;
		try {
			Class<?> cls = Class.forName(className);
			r = cls!=null;
		} catch (ClassNotFoundException e) {
			r = false;
		}
		return r;
	}
	
	public static Class<?> getClassForName(String className) {
		Class<?> r = null;
		if (hasClassForName(className)) {
			try {
				r = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static boolean canInstantiateClass(String className) {
		boolean r = true;
		Class<?> cls = getClassForName(className);
		if (cls==null) {
			r = false;
		} else {
			try {
				Object o = cls.newInstance();
				r = o!=null;
			} catch (InstantiationException e) {
				r = false;
			} catch (IllegalAccessException e) {
				r = false;
			}
		}
		return r;
	}
	
	public static Object getNewClassInstanceForName(String className) {
		Object r = null;
		if (canInstantiateClass(className)) {
			Class<?> cls = getClassForName(className);
			try {
				r = cls.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
}
