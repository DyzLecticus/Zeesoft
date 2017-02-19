package nl.zeesoft.zdm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

/**
 * A Model structure contains package, class and property definitions. 
 */
public class ModelStructure extends Locker {
	private List<ModelPackage>						packages			= new ArrayList<ModelPackage>();
	private SortedMap<String,List<ModelClass>>		packageClasses		= new TreeMap<String,List<ModelClass>>();
	private SortedMap<String,List<ModelProperty>>	classProperties		= new TreeMap<String,List<ModelProperty>>();
	
	public ModelStructure() {
		super(null);
	}

	public ModelStructure(Messenger msgr) {
		super(msgr);
	}

	/*========================== Package methods ===========================*/

	public List<ModelPackage> getPackages() {
		List<ModelPackage> r = null;
		lockMe(this);
		r = new ArrayList<ModelPackage>(packages);
		unlockMe(this);
		return r;
	}

	public ModelPackage addPackage(String name) {
		ModelPackage pack = null;
		lockMe(this);
		if (getPackageNoLock(name)==null) {
			pack = new ModelPackage(name);
			packages.add(pack);
		}
		unlockMe(this);
		return pack;
	}

	public ModelPackage getPackage(String name) {
		ModelPackage r = null;
		lockMe(this);
		r = getPackageNoLock(name);
		unlockMe(this);
		return r;
	}

	public void removePackage(String name) {
		ModelPackage r = null;
		lockMe(this);
		r = getPackageNoLock(name);
		if (r!=null) {
			packages.remove(r);
			List<ModelClass> pkgClss = packageClasses.remove(name);
			if (pkgClss!=null && pkgClss.size()>0) {
				for (ModelClass cls: pkgClss) {
					classProperties.remove(name + "." + cls.getName());
				}
			}
		}
		unlockMe(this);
	}

	public void renamePackage(String name,String newName) {
		lockMe(this);
		ModelPackage r = getPackageNoLock(name);
		ModelPackage n = getPackageNoLock(newName);
		if (r!=null && n==null) {
			r.setName(newName);
			List<ModelClass> pkgClss = packageClasses.remove(name);
			if (pkgClss!=null && pkgClss.size()>0) {
				for (ModelClass cls: pkgClss) {
					cls.setPackageName(newName);
					String type = name + "." + cls.getName();
					List<ModelProperty> clsProps = classProperties.remove(type);
					if (clsProps!=null && clsProps.size()>0) {
						for (ModelProperty prop: clsProps) {
							prop.setPackageName(newName);
						}
						classProperties.put(newName + "." + cls.getName(),clsProps);
					}
					// Update property types
					List<ModelProperty> typeProps = getPropertiesNoLock(type);
					for (ModelProperty prop: typeProps) {
						prop.setType(newName + "." + cls.getName());
					}
				}
				packageClasses.put(newName,pkgClss);
			}
			// Update sub classes
			List<ModelClass> classes = getClassesNoLock();
			for (ModelClass cls: classes) {
				if (cls.getExtendsPackageName().equals(name)) {
					cls.setExtendsPackageName(newName);
				}
			}
		}
		unlockMe(this);
	}

	/*========================== Class methods ===========================*/

	public List<ModelClass> getClasses() {
		List<ModelClass> r = new ArrayList<ModelClass>();
		lockMe(this);
		r = getClassesNoLock();
		unlockMe(this);
		return r;
	}

	public List<ModelClass> getClasses(String packageName) {
		List<ModelClass> r = null;
		lockMe(this);
		List<ModelClass> pkgClss = packageClasses.get(packageName);
		if (pkgClss!=null) {
			r = new ArrayList<ModelClass>(pkgClss);
		} else {
			r = new ArrayList<ModelClass>();
		}
		unlockMe(this);
		return r;
	}

	public List<ModelClass> getSubClasses(String packageName,String className,int maxDepth) {
		List<ModelClass> r = null;
		lockMe(this);
		r = getSubClassesNoLock(packageName, className, maxDepth);
		unlockMe(this);
		return r;
	}

	public List<ModelClass> getSuperClasses(String packageName,String className) {
		List<ModelClass> r = null;
		lockMe(this);
		r = getSuperClassesNoLock(packageName, className);
		unlockMe(this);
		return r;
	}

	public ModelClass addClass(String packageName,String name) {
		ModelClass cls = null;
		lockMe(this);
		ModelPackage pkg = getPackageNoLock(packageName);
		if (pkg!=null && getClassNoLock(packageName,name)==null) {
			cls = new ModelClass(packageName,name);
			List<ModelClass> pkgClss = packageClasses.get(packageName);
			if (pkgClss==null) {
				pkgClss = new ArrayList<ModelClass>();
				packageClasses.put(packageName,pkgClss);
			}
			pkgClss.add(cls);
		}
		unlockMe(this);
		return cls;
	}

	public ModelClass getClass(String packageName,String name) {
		ModelClass r = null;
		lockMe(this);
		r = getClassNoLock(packageName,name);
		unlockMe(this);
		return r;
	}

	public void removeClass(String packageName,String name) {
		ModelClass r = null;
		lockMe(this);
		r = getClassNoLock(packageName,name);
		if (r!=null) {
			packageClasses.get(packageName).remove(r);
			classProperties.remove(packageName + "." + name);
		}
		unlockMe(this);
	}

	public void renameClass(String packageName, String name,String newName) {
		lockMe(this);
		ModelClass r = getClassNoLock(packageName,name);
		ModelClass n = getClassNoLock(packageName,newName);
		if (r!=null && n==null) {
			r.setName(newName);
			String type = packageName + "." + name;
			List<ModelProperty> clsProps = classProperties.remove(type);
			if (clsProps!=null) {
				if (clsProps!=null && clsProps.size()>0) {
					for (ModelProperty prop: clsProps) {
						prop.setPackageName(newName);
					}
				}
				classProperties.put(packageName + "." + newName,clsProps);
			}
			// Update property types
			List<ModelProperty> typeProps = getPropertiesNoLock(type);
			for (ModelProperty prop: typeProps) {
				prop.setType(packageName + "." + newName);
			}
			// Update sub classes
			List<ModelClass> classes = getClassesNoLock();
			for (ModelClass cls: classes) {
				if (cls.getExtendsPackageName().equals(packageName) && cls.getExtendsClassName().equals(name)) {
					cls.setExtendsClassName(newName);
				}
			}
		}
		unlockMe(this);
	}

	/*========================== Property methods ===========================*/

	public List<ModelProperty> getProperties() {
		List<ModelProperty> r = null;
		lockMe(this);
		r = getPropertiesNoLock();
		unlockMe(this);
		return r;
	}

	public List<ModelProperty> getProperties(String type) {
		List<ModelProperty> r = null;
		lockMe(this);
		r = getPropertiesNoLock(type);
		unlockMe(this);
		return r;
	}
	
	public List<ModelProperty> getProperties(String packageName,String className) {
		List<ModelProperty> r = null;
		lockMe(this);
		r = getPropertiesNoLock(packageName,className);
		unlockMe(this);
		return r;
	}

	public List<ModelProperty> getPropertiesExtended(String packageName,String className) {
		List<ModelProperty> r = null;
		lockMe(this);
		r = getPropertiesExtendedNoLock(packageName,className);
		unlockMe(this);
		return r;
	}
	
	public ModelProperty addProperty(String packageName,String className,String name) {
		ModelProperty prop = null;
		lockMe(this);
		ModelClass cls = getClassNoLock(packageName,className);
		if (cls!=null && getPropertyNoLock(packageName,className,name)==null) {
			prop = new ModelProperty(packageName,className,name);
			List<ModelProperty> clsProps = classProperties.get(packageName + "." + className);
			if (clsProps==null) {
				clsProps = new ArrayList<ModelProperty>();
				classProperties.put(packageName + "." + className,clsProps);
			}
			clsProps.add(prop);
		}
		unlockMe(this);
		return prop;
	}

	public ModelProperty getProperty(String packageName,String className,String name,boolean includeExtendedProperties) {
		ModelProperty r = null;
		lockMe(this);
		r = getPropertyNoLock(packageName,className,name);
		if (r==null && includeExtendedProperties) {
			for (ModelProperty eProp: getPropertiesExtendedNoLock(packageName, className)) {
				if (eProp.getName().equals(name)) {
					r = eProp;
					break;
				}
			}
		}
		unlockMe(this);
		return r;
	}

	public void removeProperty(String packageName,String className,String name) {
		ModelProperty r = null;
		lockMe(this);
		r = getPropertyNoLock(packageName,className,name);
		if (r!=null) {
			classProperties.get(packageName + "." + name).remove(r);
		}
		unlockMe(this);
	}

	public void renameProperty(String packageName,String className,String name,String newName) {
		lockMe(this);
		ModelProperty r = getPropertyNoLock(packageName,className,name);
		ModelProperty n = getPropertyNoLock(packageName,className,newName);
		if (r!=null && n==null) {
			r.setName(newName);
		}
		unlockMe(this);
	}

	/*========================== Private methods ===========================*/
	
	private ModelPackage getPackageNoLock(String name) {
		ModelPackage r = null;
		for (ModelPackage pack: packages) {
			if (pack.getName().equals(name)) {
				r = pack;
				break;
			}
		}
		return r;
	}

	private ModelClass getClassNoLock(String packageName,String name) {
		ModelClass r = null;
		if (packageClasses.containsKey(packageName)) {
			for (ModelClass cls: packageClasses.get(packageName)) {
				if (cls.getName().equals(name)) {
					r = cls;
					break;
				}
			}
		}
		return r;
	}

	private ModelProperty getPropertyNoLock(String packageName,String className,String name) {
		ModelProperty r = null;
		if (classProperties.containsKey(packageName + "." + className)) {
			for (ModelProperty prop: classProperties.get(packageName + "." + className)) {
				if (prop.getName().equals(name)) {
					r = prop;
					break;
				}
			}
		}
		return r;
	}

	private List<ModelClass> getClassesNoLock() {
		List<ModelClass> r = new ArrayList<ModelClass>();
		for (Entry<String,List<ModelClass>> entry: packageClasses.entrySet()) {
			r.addAll(entry.getValue());
		}
		return r;
	}

	private List<ModelProperty> getPropertiesNoLock() {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		for (Entry<String,List<ModelProperty>> entry: classProperties.entrySet()) {
			r.addAll(entry.getValue());
		}
		return r;
	}

	private List<ModelProperty> getPropertiesNoLock(String type) {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		List<ModelProperty> properties = getPropertiesNoLock();
		for (ModelProperty prop: properties) {
			if (prop.getType().equals(type)) {
				r.add(prop);
			}
		}
		return r;
	}

	public List<ModelProperty> getPropertiesNoLock(String packageName,String className) {
		List<ModelProperty> r = null;
		List<ModelProperty> clsProps = classProperties.get(packageName + "." + className);
		if (clsProps!=null) {
			r = new ArrayList<ModelProperty>(clsProps);
		} else {
			r = new ArrayList<ModelProperty>();
		}
		return r;
	}
	
	public List<ModelProperty> getPropertiesExtendedNoLock(String packageName,String className) {
		List<ModelProperty> r = getPropertiesNoLock(packageName,className);
		for (ModelClass superCls: getSuperClassesNoLock(packageName,className)) {
			for (ModelProperty prop: getPropertiesNoLock(superCls.getPackageName(),superCls.getName())) {
				boolean found = false;
				for (ModelProperty tstProp: r) {
					if (tstProp.getName().equals(prop.getName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					r.add(prop);
				}
			}
		}
		return r;
	}
	
	private List<ModelClass> getSubClassesNoLock(String packageName,String className,int maxDepth) {
		List<ModelClass> r = new ArrayList<ModelClass>();
		List<ModelClass> all = getClassesNoLock();
		addSubClassesToList(r,all,packageName,className,0,maxDepth);
		return r;
	}

	private List<ModelClass> getSuperClassesNoLock(String packageName,String className) {
		List<ModelClass> r = new ArrayList<ModelClass>();
		ModelClass cls = getClassNoLock(packageName,className);
		if (cls!=null && cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
			addSuperClassesToList(r,cls.getExtendsPackageName(),cls.getExtendsClassName());
		}
		return r;
	}
	
	private void addSubClassesToList(List<ModelClass> classes,List<ModelClass> allClasses, String packageName,String className,int depth,int maxDepth) {
		depth++;
		if (maxDepth>0 && depth>maxDepth) {
			return;
		}
		for (ModelClass cls: allClasses) {
			if (!classes.contains(cls) && cls.getExtendsPackageName().equals(packageName) && cls.getExtendsClassName().equals(className)) {
				classes.add(cls);
				addSubClassesToList(classes,allClasses,cls.getPackageName(),cls.getName(),depth,maxDepth);
			}
		}
	}

	private void addSuperClassesToList(List<ModelClass> classes, String extendsPackageName,String extendsClassName) {
		ModelClass cls = getClassNoLock(extendsPackageName, extendsClassName);
		if (cls!=null) {
			classes.add(cls);
			if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
				addSuperClassesToList(classes,cls.getExtendsPackageName(),cls.getExtendsClassName());
			}
		}
	}
}
