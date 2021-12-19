package nl.zeesoft.zdk.im;

import java.util.ArrayList;
import java.util.List;

public class ObjectArrayList {
	public List<ObjectArray>	list	= new ArrayList<ObjectArray>();
	public int					maxSize	= 1000;
	
	public void add(ObjectArray input) {
		list.add(0, input);
		applyMaxSize();
	}
	
	public void applyMaxSize() {
		while(list.size()>maxSize) {
			list.remove((int)(list.size() - 1));
		}
	}
}
