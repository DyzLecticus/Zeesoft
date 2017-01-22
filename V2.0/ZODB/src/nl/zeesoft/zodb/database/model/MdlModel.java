package nl.zeesoft.zodb.database.model;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

/**
 * This is the ZODB default model.
 * To customize, extend this class and refer to it in the database configuration.
 */
public class MdlModel {
	public static final String			PACKAGE_CLASS_NAME 					= "Package";
	public static final String			CLASS_CLASS_NAME 					= "Class";
	public static final String			PROPERTY_CLASS_NAME 				= "Property";
	public static final String			STRING_CLASS_NAME 					= "String";
	public static final String			NUMBER_CLASS_NAME 					= "Number";
	public static final String			LINK_CLASS_NAME 					= "Link";
	public static final String			UNIQUE_CONSTRAINT_CLASS_NAME		= "UniqueConstraint";
	
	public static final String			PACKAGE_CLASS_FULL_NAME 			= MdlModel.class.getPackage().getName() + "." + PACKAGE_CLASS_NAME;
	public static final String			CLASS_CLASS_FULL_NAME 				= MdlModel.class.getPackage().getName() + "." + CLASS_CLASS_NAME;
	public static final String			PROPERTY_CLASS_FULL_NAME 			= MdlModel.class.getPackage().getName() + "." + PROPERTY_CLASS_NAME;
	public static final String			STRING_CLASS_FULL_NAME 				= MdlModel.class.getPackage().getName() + "." + STRING_CLASS_NAME;
	public static final String			NUMBER_CLASS_FULL_NAME 				= MdlModel.class.getPackage().getName() + "." + NUMBER_CLASS_NAME;
	public static final String			LINK_CLASS_FULL_NAME 				= MdlModel.class.getPackage().getName() + "." + LINK_CLASS_NAME;
	public static final String			UNIQUE_CONSTRAINT_CLASS_FULL_NAME	= MdlModel.class.getPackage().getName() + "." + UNIQUE_CONSTRAINT_CLASS_NAME;
	
	private List<MdlPackage> 			packages 							= new ArrayList<MdlPackage>();
	private List<MdlUniqueConstraint> 	uniqueConstraints 					= new ArrayList<MdlUniqueConstraint>();

	/**
	 * Model contents defined within this method are ensured to always exist.
	 * This way, applications that extend the ZODB can depend on certain structures existing like the ZODB does.
	 */
	public void initializeCustomizations() {
		
	}
	
	public final void initialize() {
		cleanUp();
		
		MdlPackage pkg = getNewPackage();
		pkg.setName(MdlModel.class.getPackage().getName());

		// Package
		MdlClass packageCls = pkg.getNewClass(); 
		packageCls.setName(PACKAGE_CLASS_NAME);
		
		MdlString str = packageCls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		
		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(packageCls.getFullName());
		uc.getProperties().add(str.getName());

		// Class
		MdlClass classCls = pkg.getNewClass();
		classCls.setName(CLASS_CLASS_NAME);

		MdlLink lnk = classCls.getNewLink();
		lnk.setName("package");
		lnk.setClassTo(packageCls.getFullName());

		str = classCls.getNewString();
		str.setName("name");
		str.setMaxLength(24);

		uc = getNewUniqueConstraint();
		uc.getClasses().add(classCls.getFullName());
		uc.getProperties().add(lnk.getName());
		uc.getProperties().add(str.getName());

		str = classCls.getNewString();
		str.setName("abstract");
		str.setMaxLength(5);

		lnk = classCls.getNewLink();
		lnk.setName("extendsClasses");
		lnk.setClassTo(classCls.getFullName());
		lnk.setMaxSize(3);

		// Property
		MdlClass propCls = pkg.getNewClass();
		propCls.setName(PROPERTY_CLASS_NAME);
		propCls.setAbstr(true);

		lnk = propCls.getNewLink();
		lnk.setName("class");
		lnk.setClassTo(classCls.getFullName());
		MdlLink classLink = lnk; 

		str = propCls.getNewString();
		str.setName("name");
		str.setMaxLength(24);
		MdlString nameString = str; 

		str = propCls.getNewString();
		str.setName("index");
		str.setMaxLength(5);

		// Strings
		MdlClass stringCls = pkg.getNewClass();
		stringCls.setName(STRING_CLASS_NAME);
		stringCls.getExtendsClasses().add(propCls.getFullName());

		MdlNumber nmbr = stringCls.getNewNumber();
		nmbr.setName("maxLength");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("9999999"));

		str = stringCls.getNewString();
		str.setName("encode");
		str.setMaxLength(5);

		// Numbers
		MdlClass numberCls = pkg.getNewClass();
		numberCls.setName(NUMBER_CLASS_NAME);
		numberCls.getExtendsClasses().add(propCls.getFullName());

		nmbr = numberCls.getNewNumber();
		nmbr.setName("minValue");

		nmbr = numberCls.getNewNumber();
		nmbr.setName("maxValue");

		// Links
		MdlClass linkCls = pkg.getNewClass();
		linkCls.setName(LINK_CLASS_NAME);
		linkCls.getExtendsClasses().add(propCls.getFullName());
		
		lnk = linkCls.getNewLink();
		lnk.setName("classTo");
		lnk.setClassTo(classCls.getFullName());
		
		nmbr = linkCls.getNewNumber();
		nmbr.setName("maxSize");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("999999"));

		uc = getNewUniqueConstraint();
		uc.getClasses().add(stringCls.getFullName());
		uc.getClasses().add(numberCls.getFullName());
		uc.getClasses().add(linkCls.getFullName());
		uc.getProperties().add(classLink.getName());
		uc.getProperties().add(nameString.getName());
		
		// Unique constraints
		MdlClass ucCls = pkg.getNewClass();
		ucCls.setName(UNIQUE_CONSTRAINT_CLASS_NAME);

		str = ucCls.getNewString();
		str.setName("caseSensitive");
		str.setMaxLength(5);
		
		lnk = ucCls.getNewLink();
		lnk.setName("classes");
		lnk.setClassTo(classCls.getFullName());
		lnk.setMaxSize(4);

		str = ucCls.getNewString();
		str.setName("properties");
		str.setMaxLength(100);

		uc = getNewUniqueConstraint();
		uc.getClasses().add(ucCls.getFullName());
		uc.getProperties().add(lnk.getName());
		uc.getProperties().add(str.getName());
		
		initializeCustomizations();
	}

	public final List<String> getFullNames() {
		List<String> names = new ArrayList<String>();
		for (MdlPackage pkg: getPackages()) {
			names.add(pkg.getName());
			for (MdlClass cls: pkg.getClasses()) {
				names.add(cls.getFullName());
				for (MdlProperty prop: cls.getProperties()) {
					names.add(prop.getFullName());
				}
			}
		}
		for (MdlUniqueConstraint uc: uniqueConstraints) {
			names.add(uc.getFullName());
		}
		return names;
	}
	
	public final List<String> getIndexAndClassDirs() {
		List<String> dirs = new ArrayList<String>();
		for (MdlClass cls: getClasses()) {
			if (cls.isAbstr()) {
				dirs.add(cls.getClassDirName());
				dirs.add(cls.getIndexDirName());
				for (MdlProperty prop: cls.getProperties()) {
					if (prop.isIndex()) {
						dirs.add(prop.getIndexDirName());
					}
				}
			}
		}
		for (MdlUniqueConstraint uc: uniqueConstraints) {
			dirs.add(uc.getIndexDirName());
		}
		return dirs;
	}

	public final XMLFile toXML() {
		return toXML(false,false);
	}
	
	public final XMLFile toXML(boolean extended,boolean real) {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("model",null,null));
		XMLElem pkgsElem = new XMLElem("packages",null,file.getRootElement());
		for (MdlPackage pkg: packages) {
			XMLElem pkgElem = new XMLElem("package",null,pkgsElem);
			for (XMLElem elem: pkg.toXML(extended,real).getRootElement().getChildren()) {
				elem.setParent(pkgElem);
			}
		}
		if (uniqueConstraints.size()>0) {
			XMLElem ucsElem = new XMLElem("uniqueConstraints",null,file.getRootElement());
			for (MdlUniqueConstraint uc: uniqueConstraints) {
				XMLElem ucElem = new XMLElem("uniqueConstraint",null,ucsElem);
				for (XMLElem elem: uc.toXML().getRootElement().getChildren()) {
					elem.setParent(ucElem);
				}
			}
		}
		return file;
	}
	
	public final void fromXML(XMLFile file) {
		initialize();
		for (XMLElem cElem: file.getRootElement().getChildren()) {
			if (cElem.getName().equals("packages")) {
				for (XMLElem pkgElem: cElem.getChildren()) {
					MdlPackage pkg = new MdlPackage(this);
					pkg.fromXML(pkgElem);
					if (pkg.getName().length()>0) {
						MdlPackage mergePackage = getPackageByName(pkg.getName());
						if (mergePackage==null) {
							packages.add(pkg);
						} else {
							for (MdlClass cls: pkg.getClasses()) {
								MdlClass mergeCls = null;
								for (MdlClass testCls: mergePackage.getClasses()) {
									if (testCls.getName().equals(cls.getName())) {
										mergeCls = testCls;
										break;
									}
								}
								if (mergeCls==null) {
									mergeCls = mergePackage.getNewClass();
									mergeCls.setName(cls.getName());
									mergeCls.setAbstr(cls.isAbstr());
									for (String name: cls.getExtendsClasses()) {
										mergeCls.getExtendsClasses().add(name);
									}
								}
								for (MdlProperty prop: cls.getProperties()) {
									MdlProperty mergeProp = null;
									for (MdlProperty testProp: mergeCls.getProperties()) {
										if (testProp.getName().equals(prop.getName())) {
											mergeProp = testProp;
											break;
										}
									}
									if (mergeProp==null) {
										if (prop instanceof MdlString) {
											mergeProp = mergeCls.getNewString();
											((MdlString) mergeProp).setMaxLength(((MdlString) prop).getMaxLength());
											((MdlString) mergeProp).setEncode(((MdlString) prop).isEncode());
										} else if (prop instanceof MdlNumber) {
											mergeProp = mergeCls.getNewNumber();
											((MdlNumber) mergeProp).setMinValue(((MdlNumber) prop).getMinValue());
											((MdlNumber) mergeProp).setMaxValue(((MdlNumber) prop).getMaxValue());
										} else if (prop instanceof MdlLink) {
											mergeProp = mergeCls.getNewLink();
											((MdlLink) mergeProp).setClassTo(((MdlLink) prop).getClassTo());
											((MdlLink) mergeProp).setMaxSize(((MdlLink) prop).getMaxSize());
										}
										mergeProp.setName(prop.getName());
										mergeProp.setIndex(prop.isIndex());
									}
								}
							}
						}
					}
				}
			}
			if (cElem.getName().equals("uniqueConstraints")) {
				for (XMLElem pkgElem: cElem.getChildren()) {
					MdlUniqueConstraint uc = new MdlUniqueConstraint(this);
					uc.fromXML(pkgElem);
					if (uc.getClasses().size()>0 && uc.getProperties().size()>0 && getUniqueConstraintByFullName(uc.getFullName())==null) {
						uniqueConstraints.add(uc);
					}
				}
			}
		}
		initializeExtensionsAndIndexProperties();
		check();
	}

	public final void initializeExtensionsAndIndexProperties() {
		for (MdlClass cls: getClasses()) {
			cls.getPropertiesExtended();
			cls.getChildLinks();
			cls.getUniqueConstraintList();
		}
		for (MdlUniqueConstraint uc: getUniqueConstraints()) {
			uc.getClassesList();
			uc.getPropertiesListForClass(uc.getClasses().get(0));
		}
	}
	
	public final boolean check() {
		return getErrorText().length()>0;
	}

	public final StringBuilder getErrorHTML() {
		return getErrorsAsString(true);
	}
	
	public final StringBuilder getErrorText() {
		return getErrorsAsString(false);
	}

	public final void serialize(String fileName) {
		XMLFile f = toXML();
		f.writeFile(fileName, f.toStringReadFormat());
		f.cleanUp();
	}
	
	public final void unserialize(String fileName) {
		XMLFile f = new XMLFile();
		f.parseFile(fileName);
		fromXML(f);
		f.cleanUp();
	}

	public final static String getFullFileName() {
		return DbConfig.getInstance().getDataDir() + "DbModel.xml";
	}

	public final MdlPackage getPackageByName(String name) {
		MdlPackage r = null;
		for (MdlPackage pkg: packages) {
			if (pkg.getName().equals(name)) {
				r = pkg;
				break;
			}
		}
		return r;
	}

	public final List<MdlClass> getClasses() {
		List<MdlClass> r = new ArrayList<MdlClass>();
		for (MdlPackage pkg: packages) {
			for (MdlClass cls: pkg.getClasses()) {
				r.add(cls);
			}
		}
		return r;
	}

	public final List<String> getClassFullNameList() {
		List<String> r = new ArrayList<String>();
		for (MdlClass cls:getClasses()) {
			r.add(cls.getFullName());
		}
		return r;
	}

	public final MdlClass getClassByFullName(String fullName) {
		MdlClass r = null;
		if (fullName!=null && fullName.length()>0) {
			for (MdlPackage pkg: packages) {
				for (MdlClass cls: pkg.getClasses()) {
					if (cls.getFullName().equals(fullName)) {
						r = cls;
						break;
					}
				}
				if (r!=null) {
					break;
				}
			}
		}
		return r;
	}

	public final MdlUniqueConstraint getUniqueConstraintByFullName(String fullName) {
		MdlUniqueConstraint r = null;
		if (fullName!=null && fullName.length()>0) {
			for (MdlUniqueConstraint uc: uniqueConstraints) {
				if (uc.getFullName().equals(fullName)) {
					r = uc;
					break;
				}
			}
		}
		return r;
	}

	public final void cleanUp() {
		for (MdlUniqueConstraint uc: uniqueConstraints) {
			uc.cleanUp();
		}
		uniqueConstraints.clear();
		for (MdlPackage pkg: packages) {
			pkg.cleanUp();
		}
		packages.clear();
	}

	public final List<MdlPackage> getPackages() {
		return new ArrayList<MdlPackage>(packages);
	}
	
	public final MdlPackage getNewPackage() {
		MdlPackage pkg = new MdlPackage(this);
		packages.add(pkg);
		return pkg;
	}

	public final void removePackage(MdlPackage pkg) {
		if (packages.remove(pkg)) {
			pkg.cleanUp();
		}
	}

	public final List<MdlUniqueConstraint> getUniqueConstraints() {
		return new ArrayList<MdlUniqueConstraint>(uniqueConstraints);
	}
	
	public final MdlUniqueConstraint getNewUniqueConstraint() {
		MdlUniqueConstraint uc = new MdlUniqueConstraint(this);
		uniqueConstraints.add(uc);
		return uc;
	}

	public final void removeUniqueConstraint(MdlUniqueConstraint uc) {
		if (uniqueConstraints.remove(uc)) {
			uc.cleanUp();
		}
	}
	
	public final boolean createDirectories(Object source) {
		boolean error = false;
		for (MdlClass cls: getClasses()) {
			if (!cls.isAbstr()) {
				error = MdlModel.createDataAndIndexDirectoriesForClass(cls,source);
				if (error) {
					break;
				}
			}
		}
		if (!error) {
			for (MdlUniqueConstraint uc: getUniqueConstraints()) {
				error = MdlModel.createIndexDirectoryForUniqueConstraint(uc,source);
				if (error) {
					break;
				}
			}
		}
		return !error;
	}
	
	public final static boolean createDataAndIndexDirectoriesForClass(MdlClass cls, Object source) {
		boolean error = false;
		File classDir = new File(cls.getClassDirName());
		if (!classDir.exists()) {
			classDir.mkdir();
		}
		if (!classDir.exists()) {
			Messenger.getInstance().error(source, "Unable to create class data directory: " + classDir.getAbsolutePath());
			error = true;
		}
		if (!error) {
			File indexDir = new File(cls.getIndexDirName());
			if (!indexDir.exists()) {
				indexDir.mkdir();
			}
			if (!indexDir.exists()) {
				Messenger.getInstance().error(source, "Unable to create class index directory: " + indexDir.getAbsolutePath());
				error = true;
			}
		}
		if (!error) {
			for (MdlProperty prop: cls.getPropertiesExtended()) {
				if (prop.isIndex()) {
					File propertyDir = new File(prop.getIndexDirName());
					if (!propertyDir.exists()) {
						propertyDir.mkdir();
					}
					if (!propertyDir.exists()) {
						Messenger.getInstance().error(source, "Unable to create property index directory: " + propertyDir.getAbsolutePath());
						error = true;
						break;
					}
				}
			}
		}
		return error;
	}
	
	public final static boolean createIndexDirectoryForUniqueConstraint(MdlUniqueConstraint uc, Object source) {
		boolean error = false;
		File indexDir = new File(uc.getIndexDirName());
		if (!indexDir.exists()) {
			indexDir.mkdir();
		}
		if (!indexDir.exists()) {
			Messenger.getInstance().error(source, "Unable to create unique index directory: " + indexDir.getAbsolutePath());
			error = true;
		}
		return error;
	}

	private final StringBuilder getErrorsAsString(boolean formatHTML) {
		String boldStart = "";
		String boldStop = "";
		String lineFeed = "\n";
		if (formatHTML) {
			boldStart = "<b>";
			boldStop = "</b>";
			lineFeed = "<br/>";
		}
		StringBuilder txt = new StringBuilder();
		for (MdlClass cls: getClasses()) {
			for (String fullName: cls.getExtendsClasses()) {
				if (fullName.equals(cls.getFullName())) {
					appendErrorLine(txt,"Classes may not extend themselves: " + boldStart + cls.getFullName() + boldStop + ", extends class: " + boldStart + fullName + boldStop,boldStart,boldStop,lineFeed);
				} else if (getClassByFullName(fullName)==null) {
					appendErrorLine(txt,"Extended class not found: " + boldStart + cls.getFullName() + boldStop + ", extends class: " + boldStart + fullName + boldStop,boldStart,boldStop,lineFeed);
				}
			}
			for (MdlProperty prop: cls.getProperties()) {
				if (prop instanceof MdlNumber) {
					MdlNumber num = (MdlNumber) prop;
					if (num.getMinValue().compareTo(num.getMaxValue())>=0) {
						appendErrorLine(txt,"Number minimum value is greater or equal to the maximum value: " + boldStart + num.getFullName() + boldStop + ", minimum value: " + boldStart + num.getMinValue() + boldStop + " >= maximum value: " + boldStart + num.getMaxValue() + boldStop,boldStart,boldStop,lineFeed);
					}
				} else if (prop instanceof MdlLink) {
					MdlLink lnk = (MdlLink) prop;
					MdlClass clsTo = getClassByFullName(lnk.getClassTo());
					if (clsTo==null) {
						appendErrorLine(txt,"Link classTo not found: " + boldStart + lnk.getFullName() + boldStop + ", classTo: " + boldStart + lnk.getClassTo() + boldStop,boldStart,boldStop,lineFeed);
					} else if (clsTo.isAbstr()) {
						appendErrorLine(txt,"Link classTo is abstract: " + boldStart + lnk.getFullName() + boldStop + ", classTo: " + boldStart + lnk.getClassTo() + boldStop,boldStart,boldStop,lineFeed);
					}
				}
			}
		}
		for (MdlUniqueConstraint uc: getUniqueConstraints()) {
			if (uc.getClasses().size()!=uc.getClassesList().size()) {
				for (String className: uc.getClasses()) {
					if (getClassByFullName(className)==null) {
						appendErrorLine(txt,"Unique constraint class not found: " + boldStart + className + boldStop,boldStart,boldStop,lineFeed);
					}
				}
			} else {
				for (MdlClass cls: uc.getClassesList()) {
					if (cls.isAbstr()) {
						appendErrorLine(txt,"Unique constraint class is abstract: " + boldStart + cls.getFullName() + boldStop,boldStart,boldStop,lineFeed);
					} else if (uc.getProperties().size()!=uc.getPropertiesListForClass(cls.getFullName()).size()) {
						for (String propertyName: uc.getProperties()) {
							if (cls.getPropertyByName(propertyName)==null) {
								appendErrorLine(txt,"Unique constraint class property not found: " + cls.getFullName() + ":" + propertyName,boldStart,boldStop,lineFeed);
							} else if (propertyName.equals(MdlProperty.ID)) {
								appendErrorLine(txt,"Unique constraint class property not allowed: " + cls.getFullName() + ":" + propertyName,boldStart,boldStop,lineFeed);
							}
						}
					}
				}
			}
		}		
		return txt;
	}
	
	private final void appendErrorLine(StringBuilder txt, String line,String boldStart, String boldStop,String lineFeed) {
		Messenger.getInstance().debug(this,line);
		txt.append(boldStart);
		txt.append("ERROR:");
		txt.append(boldStop);
		txt.append(" ");
		txt.append(line);
		txt.append(lineFeed);
	}	
}
