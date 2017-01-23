package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedObject;
import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;
import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageObject;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.AddClass;
import nl.zeesoft.zdmk.model.transformations.impl.AddPackage;
import nl.zeesoft.zdmk.model.transformations.impl.AddProperty;
import nl.zeesoft.zdmk.model.transformations.impl.ConvertModel;
import nl.zeesoft.zdmk.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdmk.model.transformations.impl.RemoveClass;
import nl.zeesoft.zdmk.model.transformations.impl.RemovePackage;
import nl.zeesoft.zdmk.model.transformations.impl.RemovePackageAll;
import nl.zeesoft.zdmk.model.transformations.impl.RemoveProperty;
import nl.zeesoft.zdmk.model.transformations.impl.RevertVersion;
import nl.zeesoft.zdmk.model.transformations.impl.RevertVersionCurrent;
import nl.zeesoft.zdmk.model.transformations.impl.SetClassAbstract;
import nl.zeesoft.zdmk.model.transformations.impl.SetClassExtendsClass;
import nl.zeesoft.zdmk.model.transformations.impl.SetClassName;
import nl.zeesoft.zdmk.model.transformations.impl.SetPackageName;
import nl.zeesoft.zdmk.model.transformations.impl.SetPropertyList;
import nl.zeesoft.zdmk.model.transformations.impl.SetPropertyName;
import nl.zeesoft.zdmk.model.transformations.impl.SetPropertyType;

/**
 * A Model can contain package, class and property definitions. 
 * In order to bring a model to a certain state, transformations can be applied to it.
 * Models versions are tracked and can be created and restored using specific transformations.
 */
public class Model extends ModelObject {
	private boolean				appendLog			= true;
	private List<ModelVersion>	versions			= new ArrayList<ModelVersion>();
	private List<ModelPackage>	packages			= new ArrayList<ModelPackage>();

	public Model() {
		
	}
	
	public Model(List<ModelPackage> packages,List<ModelVersion> versions) {
		initialze(packages,versions);
	}
	
	/**
	 * Initializes the model with the specified versions and package structure.
	 * 
	 * Creates a new version if none is provided.
	 * 
	 * @param packages The packages
	 * @param versions The optional versions
	 */
	public void initialze(List<ModelPackage> packages,List<ModelVersion> versions) {
		getPackages().clear();
		getVersions().clear();
		for (ModelPackage pack: packages) {
			pack.setModel(this);
			getPackages().add(pack);
		}
		if (versions.size()==0) {
			for (ModelVersion version: versions) {			
				getVersions().add(version);
			}
		} else {
			getCurrentVersion();
		}
	}
	
	@Override
	public String applyTransformation(TransformationObject transformation) {
		String error = transformation.checkParameters();
		if (error.length()==0) {
			if (transformation instanceof IncrementVersion) {
				incrementVersion();
			} if (transformation instanceof RevertVersion) {
				error = revertVersion(Integer.parseInt(((RevertVersion) transformation).getNumber()));
			} else if (transformation instanceof RevertVersionCurrent) {
				error = revertVersionCurrent();
			} else if (transformation instanceof RemovePackageAll) {
				cleanUp();
			} else if (transformation instanceof ConvertModel) {
				ConvertModel conv = (ConvertModel) transformation;
				error = convertModel(conv.getIdPropertyName(),conv.getIdPropertyType());
			} else if (transformation instanceof TransformationNamedObject) {
				error = applyNamedTransformation((TransformationNamedObject) transformation);
			}
		}

		if (error.length()==0) {
			getCurrentVersion().appliedTransformation(transformation, appendLog);
		} else {
			getCurrentVersion().failedToApplyTransformation(transformation, error, appendLog);
		}
		
		return error;
	}

	/**
	 * Adds initial model state transformations to a list.
	 * 
	 * These transformations can be used to reconstruct the current model state.
	 * 
	 * @param list The list to add the transformations to
	 */
	public void addInitialTransformationsToList(List<TransformationObject> list) {
		for (ModelPackage pack: packages) {
			list.add(new AddPackage(pack.getName()));
			for (ModelClass cls: pack.getClasses()) {
				AddClass addClass = new AddClass(pack.getName(),cls.getName());
				if (cls.isAbstr()) {
					addClass.setAbstract("true");
				}
				list.add(addClass);
				for (ModelProperty prop: cls.getProperties()) {
					AddProperty addProperty = new AddProperty(cls.getPack().getName(),cls.getName(),prop.getName(),prop.getType());
					if (prop.isList()) {
						addProperty.setList("true");
					}
					list.add(addProperty);
				}
			}
		}
		List<ModelClass> added = new ArrayList<ModelClass>();
		for (ModelPackage pack: packages) {
			for (ModelClass cls: pack.getClasses()) {
				if (cls.getExtendsClass()==null) {
					for (ModelClass subCls: getSubClassesForClass(cls,0)) {
						if (!added.contains(subCls)) {
							added.add(subCls);
							SetClassExtendsClass setExtends = new SetClassExtendsClass(); 
							setExtends.setPackageName(subCls.getPack().getName());
							setExtends.setName(subCls.getName());
							setExtends.setExtendsPackageName(subCls.getExtendsClass().getPack().getName());
							setExtends.setExtendsClassName(subCls.getExtendsClass().getName());
							list.add(setExtends);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a copy of the module
	 * 
	 * @return A copy of the module
	 */
	@Override
	public Model getCopy() {
		return new Model(getPackagesCopy(),getVersionsCopy());
	}

	/**
	 * Adds transformations that can convert the current model to an object model to a specified list.
	 * 
	 * @param list The list to add the transformations to
	 */
	public void addConvertModelTransformationsToList(List<TransformationObject> list) {
		for (ModelPackage pack: packages) {
			list.add(new AddPackage(pack.getName()));
			for (ModelClass cls: pack.getClasses()) {
				if (!cls.isAbstr()) {
					list.add(new AddClass(pack.getName(),cls.getName()));
					for (ModelProperty prop: cls.getExtendedProperties()) {
						list.add(new AddProperty(pack.getName(),cls.getName(),prop.getName(),prop.getType(),prop.isList()));
					}
				}
			}
		}
	}
	
	/**
	 * Call this method when the model is no longer needed to ensure resources can be garbage collected.
	 */
	@Override
	public final void cleanUp() {
		for (ModelPackage pack: packages) {
			pack.cleanUp();
		}
		packages.clear();
	}

	/**
	 * Creates a new version.
	 * Applies the transformations.
	 * Reverts the created version.
	 * Re-applies the transformations if no error has occurred.
	 * 
	 * @param transformations The transformations to apply
	 * @return String The error message if applicable
	 */
	public String applyTransformationList(List<TransformationObject> transformations) {
		String error = "";
		applyTransformation(new IncrementVersion());
		ModelVersion test = getCurrentVersion(); 
		error = applyTransformations(transformations);
		revertVersion(test);
		if (error.length()==0) {
			applyTransformations(transformations);
		}
		return error;
	}
	
	/**
	 * Creates a new version
	 */
	public void incrementVersion() {
		ModelVersion v = new ModelVersion();
		if (versions.size()>0) {
			v.setNumber((getCurrentVersion().getNumber() + 1));
		}
		addInitialTransformationsToList(v.getInitialTransformations());
		versions.add(v);
	}

	/**
	 * Reverts the model state back to the initial state of a certain version.
	 * 
	 * Will remove the specified version and all higher versions.
	 * 
	 * @param number The version number of the version to revert back to
	 * @return The error message if applicable
	 */
	public String revertVersion(int number) {
		String error = "";
		ModelVersion v = getVersion(number);
		if (v==null) {
			error = "Version " + number + " does not exist";
		} else {
			revertVersion(v);
		}
		return error;
	}

	/**
	 * Reverts the model state back to the initial state of a certain version.
	 * 
	 * Will remove the specified version and all higher versions.
	 * 
	 * @param version The version to revert backward to
	 */
	protected void revertVersion(ModelVersion version) {
		if (version!=null && versions.contains(version)) {
			applyTransformation(new RemovePackageAll());
			applyTransformations(version.getInitialTransformations());
			List<ModelVersion> testVersions = new ArrayList<ModelVersion>(versions);
			boolean remove = false;
			for (ModelVersion vers: testVersions) {
				if (vers==version) {
					remove = true;
				}
				if (remove) {
					versions.remove(vers);
				}
			}
		}
		getCurrentVersion();
	}

	/**
	 * Reverts all changes in the current version.
	 * 
	 * @return The error message if applicable
	 */
	public String revertVersionCurrent() {
		String error = "";
		if (getCurrentVersion().getTransformations().size()<=1) {
			error = "The current model version does not contain any changes";
		} else {
			applyTransformation(new RemovePackageAll());
			error = applyTransformations(getCurrentVersion().getInitialTransformations());
			getCurrentVersion().getTransformations().clear();
		}
		return error;
	}
	
	/**
	 * Returns the current model version.
	 * 
	 * Creates a version if the model has none.
	 * 
	 * @return The current model version
	 */
	protected ModelVersion getCurrentVersion() {
		if (versions.size()==0) {
			applyTransformation(new IncrementVersion());
		}
		return versions.get(versions.size() - 1);
	}
	
	/**
	 * Returns all model versions.
	 * 
	 * @return All model versions
	 */
	protected List<ModelVersion> getVersions() {
		return versions;
	}

	
	/**
	 * Returns a copy of all model versions.
	 * 
	 * @return A copy of all model versions
	 */
	public List<ModelVersion> getVersionsCopy() {
		List<ModelVersion> r = new ArrayList<ModelVersion>();
		for (ModelVersion v: versions) {
			r.add(v.getCopy());
		}
		return r;
	}

	/**
	 * Returns a specific version number.
	 * 
	 * @param number The version number of the version 
	 * @return The version or null if it does not exist
	 */
	protected ModelVersion getVersion(int number) {
		ModelVersion r = null;
		for (ModelVersion version: versions) {
			if (version.getNumber()==number) {
				r = version;
				break;
			}
		}
		return r;
	}
	
	/**
	 * Converts the model to an object model.
	 * 
	 * If idPropertyName is specified, a property of with type set to idPropertyType is added to all classes.
	 *  
	 * @param idPropertyName The optional ID property name
	 * @param idPropertyType The optional ID property type (defaults to 'java.lang.String' if omitted)
	 * @return The error message if applicable
	 */
	public String convertModel(String idPropertyName,String idPropertyType) {
		if (idPropertyType==null || idPropertyType.length()==0) {
			idPropertyType = String.class.getName();
		}
		String error = "";
		if (packages.size()==0) {
			error = "No packages to convert";
		}
		if (error.length()==0 && idPropertyName.length()>0) {
			for (ModelPackage pack: packages) {
				for (ModelClass cls: pack.getClasses()) {
					if (!cls.isAbstr() && cls.getProperty(idPropertyName)!=null) {
						error = "Class " + cls.getFullName() + " already contains a property named " + idPropertyName;
						break;
					}
				}
			}
		}
		if (error.length()==0) {
			List<TransformationObject> list = new ArrayList<TransformationObject>();
			addConvertModelTransformationsToList(list);
			if (idPropertyName!=null && idPropertyName.length()>0) {
				for (ModelPackage pack: packages) {
					for (ModelClass cls: pack.getClasses()) {
						if (!cls.isAbstr()) {
							list.add(new AddProperty(pack.getName(),cls.getName(),idPropertyName,idPropertyType));
						}
					}
				}
			}
			if (getCurrentVersion().getTransformations().size()>1) {
				applyTransformation(new IncrementVersion());
			}
			if (packages.size()>0) {
				applyTransformation(new RemovePackageAll());
			}
			error = applyTransformations(list);
		}
		return error;
	}

	/**
	 * Returns all model packages.
	 * 
	 * @return All model packages
	 */
	protected List<ModelPackage> getPackages() {
		return packages;
	}
	
	/**
	 * Returns a complete copy of all model packages.
	 * 
	 * @return A complete copy of all model packages
	 */
	public List<ModelPackage> getPackagesCopy() {
		List<ModelPackage> r = new ArrayList<ModelPackage>();
		for (ModelPackage pack: packages) {
			r.add((ModelPackage)pack.getCopy());
		}
		for (ModelPackage pack: r) {
			for (ModelClass cls: pack.getClasses()) {
				if (cls.getExtendsClass()!=null) {
					ModelClass clsExt = null;
					for (ModelPackage sPack: r) {
						clsExt = sPack.getClass(cls.getExtendsClass().getName());
						if (clsExt!=null) {
							break;
						}
					}
					cls.setExtendsClass(clsExt);
				}
			}
		}
		return packages;
	}

	/**
	 * Returns a specific package.
	 * 
	 * @param name The name of the package
	 * @return The package or null if it does not exist
	 */
	protected ModelPackage getPackage(String name) {
		ModelPackage r = null;
		for (ModelPackage pack: packages) {
			if (pack.getName().equals(name)) {
				r = pack;
				break;
			}
		}
		return r;
	}
	
	private String applyNamedTransformation(TransformationNamedObject transformation) {
		String error = "";
		String name = transformation.getName();
		if (transformation instanceof AddPackage) {
			ModelPackage pack = getPackage(name);
			if (pack!=null) {
				error = "Package " + name + " already exists";
			} else {
				pack = new ModelPackage();
				pack.setModel(this);
				pack.setName(name);
				packages.add(pack);
			}
		} else if (transformation instanceof SetPackageName) {
			ModelPackage pack = getPackage(name);
			ModelPackage newPack = getPackage(((SetPackageName) transformation).getNewName());
			if (pack==null) {
				error = "Package " + name + " does not exist";
			} else if (newPack!=null) {
				error = "Package " + newPack.getName() + " already exists";
			} else {
				error = pack.applyTransformation(transformation);
			}
		} else if (transformation instanceof RemovePackage) {
			ModelPackage pack = getPackage(name);
			if (pack==null) {
				error = "Package " + name + " does not exist";
			} else {
				error = checkRemovePackage(pack);
			}
			if (error.length()==0) {
				pack.cleanUp();
				packages.remove(pack);
			}
		} else if (transformation instanceof TransformationNamedPackageObject) {
			error = applyNamedPackageTransformation((TransformationNamedPackageObject) transformation);
		}
		return error;
	}

	private String applyNamedPackageTransformation(TransformationNamedPackageObject transformation) {
		String error = "";
		String name = transformation.getName();
		String packageName = transformation.getPackageName();
		ModelPackage pack = getPackage(packageName);
		if (pack!=null) {
			if (transformation instanceof AddClass) {
				AddClass trans = (AddClass) transformation;
				ModelClass cls = pack.getClass(name);
				ModelClass extCls = null;
				if (cls!=null) {
					error = "Class " + packageName + "." + name + " already exists";
				}
				if (error.length()==0 && trans.getExtendsClassName().length()>0) {
					ModelPackage extendsPackage = getPackage(trans.getExtendsPackageName());
					if (extendsPackage==null) {
						error = "Extend class package " + trans.getExtendsPackageName() + " does not exist";
					} else {
						extCls = extendsPackage.getClass(trans.getExtendsClassName());
						if (extCls==null) {
							error = "Extend class " + trans.getExtendsPackageName() + "." + trans.getExtendsClassName() + " does not exist";
						}
					}
				}
				if (error.length()==0 && extCls!=null && (extCls==cls || extCls.getSuperClasses().contains(cls))) {
					error = "Classes may not extend themselves";
				}
				if (error.length()==0) {
					cls = pack.getNewClass();
					cls.setName(name);
					if (trans.getAbstract().length()>0) {
						cls.setAbstr(Boolean.parseBoolean(trans.getAbstract()));
					}
					if (extCls!=null) {
						cls.setExtendsClass(extCls);
					}
				}
			} else if (transformation instanceof SetClassName) {
				ModelClass cls = pack.getClass(name);
				ModelClass newCls = pack.getClass(((SetClassName) transformation).getNewName());
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else if (newCls!=null) {
					error = "Class " + packageName + "." + newCls.getName() + " already exists";
				} else {
					String type = cls.getFullName();
					cls.applyTransformation(transformation);
					updatePropertyType(type,cls.getFullName());
				}
			} else if (transformation instanceof SetClassExtendsClass) {
				SetClassExtendsClass trans = (SetClassExtendsClass) transformation;
				ModelClass cls = pack.getClass(name);
				ModelClass extCls = null;
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				}
				if (error.length()==0 && trans.getExtendsClassName().length()>0) {
					ModelPackage extendsPackage = getPackage(trans.getExtendsPackageName());
					if (extendsPackage==null) {
						error = "Extend class package " + trans.getExtendsPackageName() + " does not exist";
					} else {
						extCls = extendsPackage.getClass(trans.getExtendsClassName());
						if (extCls==null) {
							error = "Extend class " + trans.getExtendsPackageName() + "." + trans.getExtendsClassName() + " does not exist";
						}
					}
				}
				if (error.length()==0 && extCls!=null && (extCls==cls || extCls.getSuperClasses().contains(cls))) {
					error = "Classes may not extend themselves";
				}
				if (error.length()==0 && extCls!=null) {
					for (ModelProperty prop: cls.getProperties()) {
						ModelProperty eProp = extCls.getProperty(prop.getName());
						if (eProp!=null && !eProp.getType().equals(prop.getType())) {
							error = "Extended class already defines a property named " + prop.getName() + " with a different type";
							break;
						}
					}
				}
				if (error.length()==0 && extCls!=null) {
					for (ModelProperty prop: extCls.getExtendedProperties()) {
						error = checkSubclassContainsProperty(cls,prop.getName());
						if (error.length()>0) {
							break;
						}
					}
				}
				if (error.length()==0) {
					cls.setExtendsClass(extCls);
				}
			} else if (transformation instanceof SetClassAbstract) {
				SetClassAbstract trans = (SetClassAbstract) transformation;
				ModelClass cls = pack.getClass(name);
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else {
					cls.setAbstr(Boolean.parseBoolean(trans.getAbstract()));
				}
			} else if (transformation instanceof RemoveClass) {
				ModelClass cls = pack.getClass(name);
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else {
					error = checkRemoveClass(cls);
				}
				if (error.length()==0) {
					cls.cleanUp();
					pack.getClasses().remove(cls);
				}
			} else if (transformation instanceof TransformationNamedPackageClassObject) {
				error = applyNamedPackageClassTransformation((TransformationNamedPackageClassObject) transformation);
			}
		} else {
			error = "Package " + packageName + " does not exist";
		}
		return error;
	}

	private String applyNamedPackageClassTransformation(TransformationNamedPackageClassObject transformation) {
		String error = "";
		String name = transformation.getName();
		String packageName = transformation.getPackageName();
		String className = transformation.getClassName();
		ModelPackage pack = getPackage(packageName);
		ModelClass cls = null;
		if (pack==null) {
			error = "Package " + packageName + " does not exist";
		}
		if (error.length()==0) {
			cls = pack.getClass(className);
			if (cls==null) {
				error = "Class " + packageName + "." + className + " does not exist";
			}
		}
		if (error.length()==0) {
			if (transformation instanceof AddProperty) {
				AddProperty trans = (AddProperty) transformation;
				ModelProperty prop = cls.getProperty(name);
				if (prop!=null) {
					if (cls.getProperties().contains(prop)) {
						error = "Property " + cls.getFullName() + ":" + name + " already exists";
					} else if (!prop.getType().equals(trans.getType()) || prop.isList()!=Boolean.parseBoolean(trans.getList())) {
						if (prop.isList()) {
							error = "Override of " + prop.getFullName() + " must equal type List<" + prop.getType() + ">";
						} else {
							error = "Override of " + prop.getFullName() + " must equal type " + prop.getType();
						}
					}
				}
				if (error.length()==0) {
					error = checkSubclassContainsProperty(cls,name);
				}
				if (error.length()==0) {
					prop = cls.getNewProperty();
					prop.setName(trans.getName());
					if (trans.getType().length()>0) {
						prop.setType(trans.getType());
					}
					if (trans.getList().length()>0) {
						prop.setList(Boolean.parseBoolean(trans.getList()));
					}
				}
			} else if (transformation instanceof SetPropertyName) {
				ModelProperty prop = cls.getProperty(name);
				ModelProperty newProp = cls.getProperty(((SetPropertyName) transformation).getNewName());
				if (prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				} else if (newProp!=null) {
					if (!cls.getProperties().contains(newProp) && !newProp.getType().equals(prop.getType())) {
						error = "Override of " + newProp.getFullName() + " must equal type " + newProp.getFullName();
					} else {
						error = "Property " + cls.getFullName() + ":" + name + " already exists";
					}
				}
				if (error.length()==0) {
					error = checkSubclassContainsProperty(cls,((SetPropertyName) transformation).getNewName());
				}
				if (error.length()==0) {
					prop.applyTransformation(transformation);
					setSubclassPropertyName(cls,name,prop.getName());
				}
			} else if (transformation instanceof SetPropertyType) {
				ModelProperty prop = cls.getProperty(name);
				if (prop!=null) {
					if (!cls.getProperties().contains(prop)) {
						error = "Class " + cls.getFullName() + " does not define a property named " + name;
					} else if (cls.getExtendsClass()!=null) {
						if (cls.getExtendsClass().getProperty(name)!=null) {
							error = "Property " + cls.getFullName() + " overrides a superclass property";
						}
					}
				}
				if (error.length()==0 && prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				}
				if (error.length()==0) {
					prop.setType(((SetPropertyType) transformation).getType());
					setSubclassPropertyType(cls,name,prop.getType());
				}
			} else if (transformation instanceof SetPropertyList) {
				ModelProperty prop = cls.getProperty(name);
				if (prop!=null) {
					if (!cls.getProperties().contains(prop)) {
						error = "Class " + cls.getFullName() + " does not define a property named " + name;
					} else if (cls.getExtendsClass()!=null) {
						if (cls.getExtendsClass().getProperty(name)!=null) {
							error = "Property " + cls.getFullName() + " is overrides a superclass property";
						}
					}
				}
				if (error.length()==0 && prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				}
				if (error.length()==0) {
					prop.setList(Boolean.parseBoolean(((SetPropertyList) transformation).getList()));
					setSubclassPropertyList(cls,name,prop.isList());
				}
			} else if (transformation instanceof RemoveProperty) {
				ModelProperty prop = cls.getProperty(name);
				if (prop!=null && !cls.getProperties().contains(prop)) {
					error = "Class " + cls.getFullName() + " does not define a property named " + name;
				}
				if (error.length()==0 && prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				}
				if (error.length()==0) {
					prop.cleanUp();
					cls.getProperties().remove(prop);
				}
			}
		}
		return error;
	}
	
	private String checkRemovePackage(ModelPackage pack) {
		String error = "";
		for (ModelClass cls: pack.getClasses()) {
			for (ModelClass subCls: getSubClassesForClass(cls,0)) {
				if (subCls.getPack()!=pack) {
					error = "Unable to remove package due to outside subclass dependency; " + subCls.getFullName();
					break;
				}
			}
			if (error.length()==0) {
				for (ModelProperty prop: getProperties(cls.getFullName())) {
					if (prop.getCls().getPack()!=pack) {
						error = "Unable to remove package due to outside property type dependency; " + prop.getFullName();
						break;
					}
				}
			}
			if (error.length()>0) {
				break;
			}
		}
		return error;
	}

	private String checkRemoveClass(ModelClass cls) {
		String error = "";
		List<ModelClass> subClss = getSubClassesForClass(cls,1);
		if (subClss.size()>0) {
			error = "Unable to remove class due to subclass dependency; " + subClss.get(0).getFullName();
		}
		if (error.length()==0) {
			for (ModelProperty prop: getProperties(cls.getFullName())) {
				error = "Unable to remove class due to property type dependency; " + prop.getFullName();
				break;
			}
		}
		return error;
	}

	private String checkSubclassContainsProperty(ModelClass cls,String propertyName) {
		String error = "";
		List<ModelClass> subClss = getSubClassesForClass(cls,0);
		for (ModelClass subCls: subClss) {
			for (ModelProperty prop: subCls.getProperties()) {
				if (prop.getName().equals(propertyName)) {
					error = "Subclass " + cls.getFullName() + " already defines a property named " + propertyName;
				}
			}
		}
		return error;
	}

	private List<ModelProperty> getSubclassProperties(ModelClass cls,String name) {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		for (ModelClass subCls: getSubClassesForClass(cls,0)) {
			for (ModelProperty prop: subCls.getProperties()) {
				if (prop.getName().equals(name)) {
					r.add(prop);
				}
			}
		}
		return r;
	}

	private void setSubclassPropertyName(ModelClass cls,String name,String newName) {
		for (ModelProperty prop: getSubclassProperties(cls,name)) {
			prop.setName(newName);
		}
	}

	private void setSubclassPropertyType(ModelClass cls,String name,String newType) {
		for (ModelProperty prop: getSubclassProperties(cls,name)) {
			prop.setType(newType);
		}
	}

	private void setSubclassPropertyList(ModelClass cls,String name,boolean newList) {
		for (ModelProperty prop: getSubclassProperties(cls,name)) {
			prop.setList(newList);
		}
	}

	private List<ModelProperty> getProperties(String type) {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		for (ModelPackage pack: packages) {
			for (ModelClass cls: pack.getClasses()) {
				for (ModelProperty prop: cls.getProperties()) {
					if (prop.getType().equals(type)) {
						r.add(prop);
					}
				}
			}
		}
		return r;
	}
	
	private void updatePropertyType(String type,String newType) {
		for (ModelProperty prop: getProperties(type)) {
			prop.setType(newType);
		}
	}
	
	private List<ModelClass> getSubClassesForClass(ModelClass cls,int maxDepth) {
		List<ModelClass> r = new ArrayList<ModelClass>();
		addSubClassesToList(cls,0,maxDepth,r);
		return r;
		
	}

	private void addSubClassesToList(ModelClass supCls,int depth,int maxDepth,List<ModelClass> clss) {
		if (depth==maxDepth && maxDepth>0) {
			return;
		}
		depth++;
		for (ModelPackage pack: packages) {
			for (ModelClass subCls: pack.getClasses()) {
				if (subCls.getExtendsClass()==supCls && !clss.contains(subCls)) {
					clss.add(subCls);
					addSubClassesToList(subCls,depth,maxDepth,clss);
				}
			}
		}
	}
}
