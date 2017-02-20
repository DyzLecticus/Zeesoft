package nl.zeesoft.zdm.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdm.model.transformations.TransformationNamedObject;
import nl.zeesoft.zdm.model.transformations.TransformationNamedPackageClassObject;
import nl.zeesoft.zdm.model.transformations.TransformationNamedPackageObject;
import nl.zeesoft.zdm.model.transformations.TransformationObject;
import nl.zeesoft.zdm.model.transformations.impl.AddClass;
import nl.zeesoft.zdm.model.transformations.impl.AddPackage;
import nl.zeesoft.zdm.model.transformations.impl.AddProperty;
import nl.zeesoft.zdm.model.transformations.impl.ConvertModel;
import nl.zeesoft.zdm.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdm.model.transformations.impl.RemoveClass;
import nl.zeesoft.zdm.model.transformations.impl.RemovePackage;
import nl.zeesoft.zdm.model.transformations.impl.RemovePackageAll;
import nl.zeesoft.zdm.model.transformations.impl.RemoveProperty;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersion;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersionCurrent;
import nl.zeesoft.zdm.model.transformations.impl.SetClassAbstract;
import nl.zeesoft.zdm.model.transformations.impl.SetClassExtendsClass;
import nl.zeesoft.zdm.model.transformations.impl.SetClassName;
import nl.zeesoft.zdm.model.transformations.impl.SetPackageName;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyList;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyName;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyType;

/**
 * A Model uses a ModelStructure to contain package, class and property definitions. 
 * In order to bring a Model to a certain state, transformations can be applied to it.
 * Model versions are tracked and can be created and restored using specific transformations.
 */
public class Model {
	private boolean				appendLog			= true;
	private List<ModelVersion>	versions			= new ArrayList<ModelVersion>();
	private ModelStructure		structure			= new ModelStructure();

	public Model() {
		
	}
	
	public Model(ModelStructure structure) {
		initialize(structure,null);
	}
	
	public Model(ModelStructure structure,List<ModelVersion> versions) {
		initialize(structure,versions);
	}

	public void initialize(ModelStructure structure) {
		initialize(structure,null);
	}
	
	/**
	 * Initializes the model with the specified package structure and optional version history.
	 * 
	 * Creates a new version before adding the packages if none is provided.
	 * 
	 * @param structure The package structure
	 * @param versions The optional versions
	 */
	public void initialize(ModelStructure structure,List<ModelVersion> versions) {
		this.structure = structure;
		this.versions.clear();
		if (versions!=null) {
			this.versions = versions;
		} else {
			ModelVersion version = getCurrentVersion();
			if (appendLog) {
				List<ModelPackage> packages = structure.getPackages(); 
				if (packages.size()==0) {
					version.addLogLine("Initialized model");
				} else if (packages.size()==1) {
					version.addLogLine("Initialized model (package: " + packages.get(0).getName() + ")");
				} else {
					StringBuilder pkgs = new StringBuilder();
					for (ModelPackage pack: packages) {
						if (pkgs.length()>0) {
							pkgs.append(", ");
						}
						pkgs.append(pack.getName());
					}
					version.addLogLine("Initialized model (packages: " + pkgs + ")");
				}
			}
		}
	}
	
	/**
	 * Applies a transformation to the model.
	 * 
	 * @param transformation The transformation to apply
	 * @return The error message if applicable
	 */
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
				for (ModelPackage pack: structure.getPackages()) {
					structure.removePackage(pack.getName());
				}
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
	 * Applies a list of transformations to the model.
	 * 
	 * @param transformations The transformations to apply
	 * @return The error message if applicable
	 */
	protected String applyTransformations(List<TransformationObject> transformations) {
		String error = "";
		for (TransformationObject trans: transformations) {
			error = applyTransformation(trans);
			if (error.length()>0) {
				break;
			}
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
		for (ModelPackage pack: structure.getPackages()) {
			list.add(new AddPackage(pack.getName()));
			for (ModelClass cls: structure.getClasses(pack.getName())) {
				AddClass addClass = new AddClass(pack.getName(),cls.getName());
				if (cls.isAbstr()) {
					addClass.setAbstract("true");
				}
				list.add(addClass);
				for (ModelProperty prop: structure.getProperties(cls)) {
					AddProperty addProperty = new AddProperty(cls.getPackageName(),cls.getName(),prop.getName(),prop.getType());
					if (prop.isList()) {
						addProperty.setList("true");
					}
					list.add(addProperty);
				}
			}
		}
		for (ModelPackage pack: structure.getPackages()) {
			for (ModelClass cls: structure.getClasses(pack.getName())) {
				if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
					SetClassExtendsClass setExtends = new SetClassExtendsClass(); 
					setExtends.setPackageName(cls.getPackageName());
					setExtends.setName(cls.getName());
					setExtends.setExtendsPackageName(cls.getExtendsPackageName());
					setExtends.setExtendsClassName(cls.getExtendsClassName());
					list.add(setExtends);
				}
			}
		}
	}

	/**
	 * Adds transformations that can convert the current model to an object model to a specified list.
	 * 
	 * @param list The list to add the transformations to
	 */
	public void addConvertModelTransformationsToList(List<TransformationObject> list) {
		for (ModelPackage pack: structure.getPackages()) {
			list.add(new AddPackage(pack.getName()));
			for (ModelClass cls: structure.getClasses(pack.getName())) {
				if (!cls.isAbstr()) {
					list.add(new AddClass(pack.getName(),cls.getName()));
					for (ModelProperty prop: structure.getPropertiesExtended(cls)) {
						list.add(new AddProperty(pack.getName(),cls.getName(),prop.getName(),prop.getType(),prop.isList()));
					}
				}
			}
		}
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
	public List<ModelVersion> getVersions() {
		return new ArrayList<ModelVersion>(versions);
	}

	
	/**
	 * Returns the model structure.
	 * 
	 * @return The model structure
	 */
	public ModelStructure getStructure() {
		return structure;
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
		if (structure.getPackages().size()==0) {
			error = "No packages to convert";
		}
		if (error.length()==0 && idPropertyName.length()>0) {
			for (ModelClass cls: structure.getClasses()) {
				if (!cls.isAbstr() && structure.getProperty(cls,idPropertyName,true)!=null) {
					error = "Class " + cls.getFullName() + " already contains a property named " + idPropertyName;
					break;
				}
			}
		}
		if (error.length()==0) {
			List<TransformationObject> list = new ArrayList<TransformationObject>();
			addConvertModelTransformationsToList(list);
			if (idPropertyName!=null && idPropertyName.length()>0) {
				for (ModelClass cls: structure.getClasses()) {
					if (!cls.isAbstr()) {
						list.add(new AddProperty(cls.getPackageName(),cls.getName(),idPropertyName,idPropertyType));
					}
				}
			}
			if (getCurrentVersion().getTransformations().size()>1) {
				applyTransformation(new IncrementVersion());
			}
			applyTransformation(new RemovePackageAll());
			error = applyTransformations(list);
		}
		return error;
	}
	
	private String applyNamedTransformation(TransformationNamedObject transformation) {
		String error = "";
		String name = transformation.getName();
		if (transformation instanceof AddPackage) {
			ModelPackage pack = structure.getPackage(name);
			if (pack!=null) {
				error = "Package " + name + " already exists";
			} else {
				pack = structure.addPackage(name);
			}
		} else if (transformation instanceof SetPackageName) {
			ModelPackage pack = structure.getPackage(name);
			String newName = ((SetPackageName) transformation).getNewName();
			ModelPackage newPack = structure.getPackage(newName);
			if (pack==null) {
				error = "Package " + name + " does not exist";
			} else if (newPack!=null) {
				error = "Package " + newPack.getName() + " already exists";
			} else {
				structure.renamePackage(name, newName);
			}
		} else if (transformation instanceof RemovePackage) {
			ModelPackage pack = structure.getPackage(name);
			if (pack==null) {
				error = "Package " + name + " does not exist";
			} else {
				error = checkRemovePackage(pack);
			}
			if (error.length()==0) {
				structure.removePackage(name);
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
		ModelPackage pack = structure.getPackage(packageName);
		if (pack!=null) {
			if (transformation instanceof AddClass) {
				AddClass trans = (AddClass) transformation;
				ModelClass cls = structure.getClass(packageName,name);
				ModelClass extCls = null;
				if (cls!=null) {
					error = "Class " + packageName + "." + name + " already exists";
				}
				if (error.length()==0 && trans.getExtendsClassName().length()>0) {
					ModelPackage extendsPackage = structure.getPackage(trans.getExtendsPackageName());
					if (extendsPackage==null) {
						error = "Extend class package " + trans.getExtendsPackageName() + " does not exist";
					} else {
						extCls = structure.getClass(trans.getExtendsPackageName(),trans.getExtendsClassName());
						if (extCls==null) {
							error = "Extend class " + trans.getExtendsPackageName() + "." + trans.getExtendsClassName() + " does not exist";
						}
					}
				}
				if (error.length()==0 && extCls!=null && (extCls==cls || structure.getSuperClasses(extCls).contains(cls))) {
					error = "Classes may not indirectly extend themselves";
				}
				if (error.length()==0) {
					cls = structure.addClass(packageName,name);
					if (trans.getAbstract().length()>0) {
						cls.setAbstr(Boolean.parseBoolean(trans.getAbstract()));
					}
					if (extCls!=null) {
						cls.setExtendsPackageName(extCls.getPackageName());
						cls.setExtendsClassName(extCls.getName());
					}
				}
			} else if (transformation instanceof SetClassName) {
				ModelClass cls = structure.getClass(packageName, name);
				String newName = ((SetClassName) transformation).getNewName();
				ModelClass newCls = structure.getClass(packageName,newName);
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else if (newCls!=null) {
					error = "Class " + packageName + "." + newCls.getName() + " already exists";
				} else {
					String type = cls.getFullName();
					structure.renameClass(packageName, name, newName);
					updatePropertyType(type,cls.getFullName());
				}
			} else if (transformation instanceof SetClassExtendsClass) {
				SetClassExtendsClass trans = (SetClassExtendsClass) transformation;
				ModelClass cls = structure.getClass(packageName, name);
				ModelClass extCls = null;
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				}
				if (error.length()==0 && trans.getExtendsClassName().length()>0) {
					ModelPackage extendsPackage = structure.getPackage(trans.getExtendsPackageName());
					if (extendsPackage==null) {
						error = "Extend class package " + trans.getExtendsPackageName() + " does not exist";
					} else {
						extCls = structure.getClass(trans.getExtendsPackageName(),trans.getExtendsClassName());
						if (extCls==null) {
							error = "Extend class " + trans.getExtendsPackageName() + "." + trans.getExtendsClassName() + " does not exist";
						}
					}
				}
				if (error.length()==0 && extCls!=null && (extCls==cls || structure.getSuperClasses(cls).contains(cls))) {
					error = "Classes may not extend themselves";
				}
				if (error.length()==0 && extCls!=null) {
					for (ModelProperty prop: structure.getProperties(cls)) {
						ModelProperty eProp = structure.getProperty(extCls,prop.getName(),true);
						if (eProp!=null && !eProp.getType().equals(prop.getType())) {
							error = "Extended class already defines a property named " + prop.getName() + " with a different type";
							break;
						}
					}
				}
				if (error.length()==0 && extCls!=null) {
					for (ModelProperty prop: structure.getPropertiesExtended(extCls)) {
						error = checkSubclassContainsProperty(cls,prop.getName());
						if (error.length()>0) {
							break;
						}
					}
				}
				if (error.length()==0) {
					cls.setExtendsPackageName(extCls.getPackageName());
					cls.setExtendsClassName(extCls.getName());
				}
			} else if (transformation instanceof SetClassAbstract) {
				SetClassAbstract trans = (SetClassAbstract) transformation;
				ModelClass cls = structure.getClass(packageName, name);
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else {
					cls.setAbstr(Boolean.parseBoolean(trans.getAbstract()));
				}
			} else if (transformation instanceof RemoveClass) {
				ModelClass cls = structure.getClass(packageName, name);
				if (cls==null) {
					error = "Class " + packageName + "." + name + " does not exist";
				} else {
					error = checkRemoveClass(cls);
				}
				if (error.length()==0) {
					structure.removeClass(packageName, name);
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
		ModelPackage pack = structure.getPackage(packageName);
		ModelClass cls = null;
		if (pack==null) {
			error = "Package " + packageName + " does not exist";
		}
		if (error.length()==0) {
			cls = structure.getClass(packageName,className);
			if (cls==null) {
				error = "Class " + packageName + "." + className + " does not exist";
			}
		}
		if (error.length()==0) {
			if (transformation instanceof AddProperty) {
				AddProperty trans = (AddProperty) transformation;
				ModelProperty prop = structure.getProperty(cls,name,true);
				if (prop!=null) {
					if (structure.getProperties(cls).contains(prop)) {
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
					prop = structure.addProperty(cls,name);
					if (trans.getType().length()>0) {
						prop.setType(trans.getType());
					}
					if (trans.getList().length()>0) {
						prop.setList(Boolean.parseBoolean(trans.getList()));
					}
				}
			} else if (transformation instanceof SetPropertyName) {
				ModelProperty prop = structure.getProperty(cls,name,true);
				String newName = ((SetPropertyName) transformation).getNewName();
				ModelProperty newProp = structure.getProperty(cls,newName,true);
				if (prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				} else if (newProp!=null) {
					if (!structure.getProperties(cls).contains(newProp) && !newProp.getType().equals(prop.getType())) {
						error = "Override of " + newProp.getFullName() + " must equal type " + newProp.getFullName();
					} else {
						error = "Property " + cls.getFullName() + ":" + name + " already exists";
					}
				}
				if (error.length()==0) {
					error = checkSubclassContainsProperty(cls,newName);
				}
				if (error.length()==0) {
					structure.renameProperty(cls,name,newName);
					setSubclassPropertyName(cls,name,prop.getName());
				}
			} else if (transformation instanceof SetPropertyType) {
				ModelProperty prop = structure.getProperty(cls,name,true);
				if (prop!=null) {
					if (!structure.getProperties(cls).contains(prop)) {
						error = "Class " + cls.getFullName() + " does not define a property named " + name;
					} else if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
						ModelClass extCls = structure.getClass(cls.getExtendsPackageName(),cls.getExtendsClassName());
						if (structure.getProperty(extCls,name,true)!=null) {
							error = "Property " + prop.getFullName() + " overrides a superclass property";
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
				ModelProperty prop = structure.getProperty(cls,name,true);
				if (prop!=null) {
					if (!structure.getProperties(cls).contains(prop)) {
						error = "Class " + cls.getFullName() + " does not define a property named " + name;
					} else if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
						ModelClass extCls = structure.getClass(cls.getExtendsPackageName(),cls.getExtendsClassName());
						if (structure.getProperty(extCls,name,true)!=null) {
							error = "Property " + prop.getFullName() + " overrides a superclass property";
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
				ModelProperty prop = structure.getProperty(cls,name,true);
				if (prop!=null && !structure.getProperties(cls).contains(prop)) {
					error = "Class " + cls.getFullName() + " does not define a property named " + name;
				}
				if (error.length()==0 && prop==null) {
					error = "Property " + cls.getFullName() + ":" + name + " does not exist";
				}
				if (error.length()==0) {
					structure.removeProperty(cls, name);
				}
			}
		}
		return error;
	}
	
	private String checkRemovePackage(ModelPackage pack) {
		String error = "";
		for (ModelClass cls: structure.getClasses()) {
			if (cls.getPackageName().equals(pack.getName())) {
				if (error.length()==0) {
					for (ModelProperty prop: structure.getProperties(cls.getFullName())) {
						if (!prop.getPackageName().equals(pack.getName())) {
							error = "Unable to remove package due to outside property type dependency; " + prop.getFullName();
							break;
						}
					}
				}
			} else {
				if (cls.getExtendsPackageName().equals(pack.getName())) {
					error = "Unable to remove package due to outside subclass dependency; " + cls.getFullName();
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
		List<ModelClass> subClss = structure.getSubClasses(cls,1);
		if (subClss.size()>0) {
			error = "Unable to remove class due to subclass dependency; " + subClss.get(0).getFullName();
		}
		if (error.length()==0) {
			for (ModelProperty prop: structure.getProperties(cls.getFullName())) {
				error = "Unable to remove class due to property type dependency; " + prop.getFullName();
				break;
			}
		}
		return error;
	}

	private String checkSubclassContainsProperty(ModelClass cls,String propertyName) {
		String error = "";
		List<ModelClass> subClss = structure.getSubClasses(cls,0);
		for (ModelClass subCls: subClss) {
			for (ModelProperty prop: structure.getProperties(subCls)) {
				if (prop.getName().equals(propertyName)) {
					error = "Subclass " + cls.getFullName() + " already defines a property named " + propertyName;
				}
			}
		}
		return error;
	}

	private List<ModelProperty> getSubclassProperties(ModelClass cls,String name) {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		for (ModelClass subCls: structure.getSubClasses(cls,0)) {
			for (ModelProperty prop: structure.getProperties(subCls)) {
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
	
	private void updatePropertyType(String type,String newType) {
		for (ModelProperty prop: structure.getProperties(type)) {
			prop.setType(newType);
		}
	}
}
