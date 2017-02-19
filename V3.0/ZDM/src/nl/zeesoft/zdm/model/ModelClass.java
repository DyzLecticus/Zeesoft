package nl.zeesoft.zdm.model;

/**
 * Represents a model package class.
 */
public final class ModelClass extends ModelNamedObject {
	private String				packageName			= "";
	private String				extendsPackageName	= "";
	private String				extendsClassName	= "";
	private boolean				abstr				= false;
	
	protected ModelClass(String packageName,String name) {
		super(name);
		this.packageName = packageName;
	}

	@Override
	public String getFullName() {
		return packageName + "." + getName();
	}

	public boolean isAbstr() {
		return abstr;
	}

	public void setAbstr(boolean abstr) {
		this.abstr = abstr;
	}

	public String getPackageName() {
		return packageName;
	}

	protected void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getExtendsPackageName() {
		return extendsPackageName;
	}

	public void setExtendsPackageName(String extendsPackageName) {
		this.extendsPackageName = extendsPackageName;
	}

	public String getExtendsClassName() {
		return extendsClassName;
	}

	public void setExtendsClassName(String extendsClassName) {
		this.extendsClassName = extendsClassName;
	}
}
