package nl.zeesoft.zdm.model;

/**
 * Represents a model package class property.
 */
public final class ModelProperty extends ModelNamedObject {
	private String			packageName		= "";
	private String			className		= "";
	private String			type			= String.class.getName();
	private boolean			list			= false;
	
	protected ModelProperty(String packageName, String className,String name) {
		super(name);
		this.packageName = packageName;
		this.className = className;
	}

	@Override
	public String getFullName() {
		return packageName + "." + className + ":" + getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public String getPackageName() {
		return packageName;
	}

	protected void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	protected void setClassName(String className) {
		this.className = className;
	}
}
