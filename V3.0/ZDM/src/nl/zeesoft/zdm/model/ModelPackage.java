package nl.zeesoft.zdm.model;

/**
 * Represents a model package.
 */
public final class ModelPackage extends ModelNamedObject {
	protected ModelPackage(String name) {
		super(name);
	}
	
	@Override
	public String getFullName() {
		return getName();
	}
}
