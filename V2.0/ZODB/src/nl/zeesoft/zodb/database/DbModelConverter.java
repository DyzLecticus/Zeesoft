package nl.zeesoft.zodb.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.index.IdxClass;
import nl.zeesoft.zodb.database.index.IdxLink;
import nl.zeesoft.zodb.database.index.IdxNumber;
import nl.zeesoft.zodb.database.index.IdxObject;
import nl.zeesoft.zodb.database.index.IdxString;
import nl.zeesoft.zodb.database.index.IdxUniqueConstraint;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public final class DbModelConverter extends Locker {
	// Used when loading differences
	private boolean 							loadDifferencesError		= false;

	// Used when updating database
	private boolean 							updateDbModelPreRemoveDone	= false;
	private boolean 							updateDbModelRemoveDone		= false;
	private boolean 							updateDbModelAddDone		= false;
	private boolean 							updateDbModelUpdateDone		= false;
	
	// Used when updating model
	private List<IdxObject>						removeIndexes				= new ArrayList<IdxObject>();
	private List<IdxObject>						addIndexes					= new ArrayList<IdxObject>();
	
	// Used when loading differences
	private boolean 							loadDifferencesDone 		= false;

	private SortedMap<Long,String> 				packageIdNameMap 			= new TreeMap<Long,String>();
	private SortedMap<Long,String> 				classIdFullNameMap 			= new TreeMap<Long,String>();
	private SortedMap<String,Long> 				classFullNameIdMap 			= new TreeMap<String,Long>();

	private SortedMap<Long,MdlPackage> 			packageIdMdlPackageMap	 	= new TreeMap<Long,MdlPackage>();
	private SortedMap<Long,MdlClass> 			classIdMdlClassMap 			= new TreeMap<Long,MdlClass>();
	private SortedMap<Long,MdlString> 			stringIdMdlStringMap 		= new TreeMap<Long,MdlString>();
	private SortedMap<Long,MdlNumber> 			numberIdMdlNumberMap 		= new TreeMap<Long,MdlNumber>();
	private SortedMap<Long,MdlLink> 			linkIdMdlLinkMap 			= new TreeMap<Long,MdlLink>();
	private SortedMap<Long,MdlUniqueConstraint> ucIdMdlUcMap 				= new TreeMap<Long,MdlUniqueConstraint>();
	
	private SortedMap<Long,DbDataObject>		packageIdObjectMap 		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 		classIdObjectMap 		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 		stringIdObjectMap 		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 		numberIdObjectMap 		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 		linkIdObjectMap 		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 		ucIdObjectMap 			= new TreeMap<Long,DbDataObject>();

	private List<MdlPackage> 					addMdlPackageList		= new ArrayList<MdlPackage>();
	private List<MdlClass> 						addMdlClassList			= new ArrayList<MdlClass>();
	private List<MdlString> 					addMdlStringList		= new ArrayList<MdlString>();
	private List<MdlNumber> 					addMdlNumberList		= new ArrayList<MdlNumber>();
	private List<MdlLink> 						addMdlLinkList			= new ArrayList<MdlLink>();
	private List<MdlUniqueConstraint> 			addMdlUcList			= new ArrayList<MdlUniqueConstraint>();
	
	private List<Long> 							removePackageIdList		= new ArrayList<Long>();
	private List<Long> 							removeClassIdList		= new ArrayList<Long>();
	private List<Long> 							removeStringIdList		= new ArrayList<Long>();
	private List<Long> 							removeNumberIdList		= new ArrayList<Long>();
	private List<Long> 							removeLinkIdList		= new ArrayList<Long>();
	private List<Long> 							removeUcIdList			= new ArrayList<Long>();
	
	private StringBuilder						updateLog				= new StringBuilder();
	private boolean								revert 					= false;
	
	protected boolean updateDbModel(MdlModel useModel) {
		Messenger.getInstance().debug(this,"Updating database model ...");
		boolean error = false;
		if (useModel==null) {
			useModel = DbConfig.getInstance().getModel();
		}
		final MdlModel model = useModel;
		error = !this.loadDifferences(useModel,true);
	
		if (!error) {
			// Update stuff before removing
			final List<ReqUpdate> updateList = new ArrayList<ReqUpdate>();
			EvtEventSubscriber updateSubscriber = new EvtEventSubscriber() {
				private int updateListIndex = 0;
				@Override
				public void handleEvent(EvtEvent e) {
					if (e.getValue()==updateList.get(updateListIndex)) {
						updateListIndex++;
						if (updateListIndex < (updateList.size() - 1)) {
							DbRequestQueue.getInstance().addRequest(updateList.get(updateListIndex),this);
						} else {
							lockMe(this);
							updateDbModelPreRemoveDone = true;
							unlockMe(this);
						}
					}
				}
			};
			for (long id: linkIdObjectMap.keySet()) {
				if (removeLinkIdList.contains(id)) {
					List<Long> objClassTo = linkIdObjectMap.get(id).getLinkValue("classTo");
					if (objClassTo!=null) {
						List<Long> mdlClassTo = new ArrayList<Long>();
						mdlClassTo.add(classFullNameIdMap.get(linkIdMdlLinkMap.get(id).getClassTo()));
						if (!Generic.idListEquals(mdlClassTo,objClassTo)) {
							ReqUpdate reqUpd = new ReqUpdate(MdlModel.LINK_CLASS_FULL_NAME,id);
							reqUpd.getUpdateObject().setLinkValue("classTo",objClassTo);
							reqUpd.addSubscriber(updateSubscriber);
							updateList.add(reqUpd);
						}
					}
				}
			}
			for (long id: classIdObjectMap.keySet()) {
				List<Long> objExtendsClasses = classIdObjectMap.get(id).getLinkValue("extendsClasses");
				if (objExtendsClasses!=null && objExtendsClasses.size()>0) {
					boolean update = false;
					for (long rId: removeClassIdList) {
						if (objExtendsClasses.remove(rId)) {
							update = true;
						}
					}
					if (update) {
						ReqUpdate reqUpd = new ReqUpdate(MdlModel.CLASS_CLASS_FULL_NAME,id);
						reqUpd.getUpdateObject().setLinkValue("extendsClasses",objExtendsClasses);
						reqUpd.addSubscriber(updateSubscriber);
						updateList.add(reqUpd);
					}
				}
			}
			if (updateList.size()>0) {
				lockMe(this);
				updateDbModelPreRemoveDone = false;
				unlockMe(this);
				DbRequestQueue.getInstance().addRequest(updateList.get(0),this);
				while (!isUpdateDbModelPreRemoveDone()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						Messenger.getInstance().error(this,"Updating database model was interrupted");
						error = true;
					}
				}
			}
		}
		
		if (!error) {
			// Remove stuff
			final List<ReqRemove> removeList = new ArrayList<ReqRemove>();
			EvtEventSubscriber removeSubscriber = new EvtEventSubscriber() {
				private int removeListIndex = 0;
				@Override
				public void handleEvent(EvtEvent e) {
					if (e.getValue()==removeList.get(removeListIndex)) {
						removeListIndex++;
						if (removeListIndex < (removeList.size() - 1)) {
							DbRequestQueue.getInstance().addRequest(removeList.get(removeListIndex),this);
						} else {
							lockMe(this);
							updateDbModelRemoveDone = true;
							unlockMe(this);
						}
					}
				}
			};
			for (long id: removeStringIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.STRING_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			for (long id: removeNumberIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.NUMBER_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			for (long id: removeLinkIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.NUMBER_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			for (long id: removeUcIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			for (long id: removeClassIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.CLASS_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			for (long id: removePackageIdList) {
				ReqRemove reqRem = new ReqRemove(MdlModel.PACKAGE_CLASS_FULL_NAME,id);
				reqRem.addSubscriber(removeSubscriber);
				removeList.add(reqRem);
			}
			if (removeList.size()>0) {
				lockMe(this);
				updateDbModelRemoveDone = false;
				unlockMe(this);
				DbRequestQueue.getInstance().addRequest(removeList.get(0),this);
				while (!isUpdateDbModelRemoveDone()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						Messenger.getInstance().error(this,"Updating database model was interrupted");
						error = true;
					}
				}
			}
		}

		if (!error) {
			// Add stuff
			final ReqAdd reqAddPackages = new ReqAdd(MdlModel.PACKAGE_CLASS_FULL_NAME);
			for (MdlPackage pkg: addMdlPackageList) {
				DbDataObject pkgObj = new DbDataObject();
				pkgObj.setPropertyValue("name",new StringBuilder(pkg.getName()));
				reqAddPackages.getObjects().add(new ReqDataObject(pkgObj));
			}
			reqAddPackages.addSubscriber(new EvtEventSubscriber() {
				private ReqGet 					reqGetPackages 		= new ReqGet(MdlModel.PACKAGE_CLASS_FULL_NAME);
				private ReqAdd 					reqAddClasses 		= new ReqAdd(MdlModel.CLASS_CLASS_FULL_NAME);
				private ReqGet 					reqGetClasses 		= new ReqGet(MdlModel.CLASS_CLASS_FULL_NAME);
				private ReqAdd 					reqAddStrings 		= new ReqAdd(MdlModel.STRING_CLASS_FULL_NAME);
				private ReqAdd 					reqAddNumbers 		= new ReqAdd(MdlModel.NUMBER_CLASS_FULL_NAME);
				private ReqAdd					reqAddLinks 		= new ReqAdd(MdlModel.LINK_CLASS_FULL_NAME);
				private ReqAdd					reqAddConstraints	= new ReqAdd(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME);
				
				private SortedMap<Long,String> 	packageIdNameMap 	= new TreeMap<Long,String>();

				@Override
				public void handleEvent(EvtEvent e) {
					if (e.getValue()==reqAddPackages) {
						reqGetPackages.getProperties().add(ReqGet.ALL_PROPERTIES);
						reqGetPackages.addSubscriber(this);
						DbRequestQueue.getInstance().addRequest(reqGetPackages,this);
					}
					if (e.getValue()==reqGetPackages) {
						for (ReqDataObject obj: reqGetPackages.getObjects()) {
							String packageName = obj.getDataObject().getPropertyValue("name").toString(); 
							MdlPackage pkg = model.getPackageByName(packageName);
							packageIdNameMap.put(obj.getDataObject().getId(),packageName);
							for (MdlClass testCls: pkg.getClasses()) {
								for (MdlClass cls: addMdlClassList) {
									if (testCls==cls) {
										DbDataObject clsObj = new DbDataObject();
										clsObj.setLinkValue("package",obj.getDataObject().getId());
										clsObj.setPropertyValue("name",new StringBuilder(cls.getName()));
										clsObj.setPropertyValue("abstract",new StringBuilder("" + cls.isAbstr()));
										reqAddClasses.getObjects().add(new ReqDataObject(clsObj));
										break;
									}
								}
							}
						}
						reqAddClasses.addSubscriber(this);
						DbRequestQueue.getInstance().addRequest(reqAddClasses,this);
					}
					if (e.getValue()==reqAddClasses) {
						reqGetClasses.getProperties().add(ReqGet.ALL_PROPERTIES);
						reqGetClasses.addSubscriber(this);
						DbRequestQueue.getInstance().addRequest(reqGetClasses,this);
					}
					if (e.getValue()==reqGetClasses) {
						reqAddStrings.addSubscriber(this);
						reqAddNumbers.addSubscriber(this);
						reqAddLinks.addSubscriber(this);

						for (ReqDataObject obj: reqGetClasses.getObjects()) {
							String className = obj.getDataObject().getPropertyValue("name").toString();
							long packageId = obj.getDataObject().getLinkValue("package").get(0);
							String packageName = packageIdNameMap.get(packageId);
							classFullNameIdMap.put(packageName + "." + className,obj.getDataObject().getId());
						}
						
						for (ReqDataObject obj: reqGetClasses.getObjects()) {
							String className = obj.getDataObject().getPropertyValue("name").toString();
							long packageId = obj.getDataObject().getLinkValue("package").get(0);
							String packageName = packageIdNameMap.get(packageId);
							MdlClass cls = model.getClassByFullName(packageName + "." + className);
							if (cls!=null) {
								classIdMdlClassMap.put(obj.getDataObject().getId(),cls);
								classIdObjectMap.put(obj.getDataObject().getId(),obj.getDataObject());
								List<Long> classVal = new ArrayList<Long>();
								classVal.add(obj.getDataObject().getId());
								for (MdlProperty testProp: cls.getProperties()) {
									for (MdlString str: addMdlStringList) {
										if (str == testProp) {
											DbDataObject strObj = new DbDataObject();
											strObj.setLinkValue("class",classVal);
											strObj.setPropertyValue("name",new StringBuilder(str.getName()));
											strObj.setPropertyValue("index",new StringBuilder("" + str.isIndex()));
											strObj.setPropertyValue("maxLength",new StringBuilder("" + str.getMaxLength()));
											strObj.setPropertyValue("encode",new StringBuilder("" + str.isEncode()));
											reqAddStrings.getObjects().add(new ReqDataObject(strObj));
											break;
										}
									}
									for (MdlNumber num: addMdlNumberList) {
										if (num == testProp) {
											DbDataObject numObj = new DbDataObject();
											numObj.setLinkValue("class",classVal);
											numObj.setPropertyValue("name",new StringBuilder(num.getName()));
											numObj.setPropertyValue("index",new StringBuilder("" + num.isIndex()));
											numObj.setPropertyValue("minValue",new StringBuilder("" + num.getMinValue()));
											numObj.setPropertyValue("maxValue",new StringBuilder("" + num.getMaxValue()));
											reqAddNumbers.getObjects().add(new ReqDataObject(numObj));
											break;
										}
									}
									for (MdlLink lnk: addMdlLinkList) {
										if (lnk == testProp) {
											DbDataObject lnkObj = new DbDataObject();
											lnkObj.setLinkValue("class",classVal);
											lnkObj.setPropertyValue("name",new StringBuilder(lnk.getName()));
											lnkObj.setPropertyValue("index",new StringBuilder("true"));
											lnkObj.setPropertyValue("maxSize",new StringBuilder("" + lnk.getMaxSize()));
											List<Long> classToVal = new ArrayList<Long>();
											classToVal.add(classFullNameIdMap.get(lnk.getClassTo()));
											lnkObj.setLinkValue("classTo",classToVal);
											reqAddLinks.getObjects().add(new ReqDataObject(lnkObj));
											break;
										}
									}
								}
							}
						}
						
						DbRequestQueue.getInstance().addRequest(reqAddStrings,this);
					}
					if (e.getValue()==reqAddStrings) {
						DbRequestQueue.getInstance().addRequest(reqAddNumbers,this);
					}
					if (e.getValue()==reqAddNumbers) {
						DbRequestQueue.getInstance().addRequest(reqAddLinks,this);
					}
					if (e.getValue()==reqAddLinks) {
						reqAddConstraints.addSubscriber(this);
						for (MdlUniqueConstraint uc: addMdlUcList) {
							List<Long> classesVal = new ArrayList<Long>();
							StringBuilder propertiesVal = new StringBuilder();
							for (String className: uc.getClasses()) {
								long classId = classFullNameIdMap.get(className); 
								classesVal.add(classId);
							}
							for (String propertyName: uc.getProperties()) {
								if (propertiesVal.length()>0) {
									propertiesVal.append(",");
								}
								propertiesVal.append(propertyName);
							}
							DbDataObject ucObj = new DbDataObject();
							ucObj.setPropertyValue("caseSensitive",new StringBuilder("" + uc.isCaseSensitive()));
							ucObj.setLinkValue("classes",classesVal);
							ucObj.setPropertyValue("properties",propertiesVal);
							reqAddConstraints.getObjects().add(new ReqDataObject(ucObj));
						}
						DbRequestQueue.getInstance().addRequest(reqAddConstraints,this);
					} 
					if (e.getValue()==reqAddConstraints) {
						lockMe(this);
						updateDbModelAddDone = true;
						unlockMe(this);
					}
				}
			});
			lockMe(this);
			updateDbModelAddDone = false;
			unlockMe(this);
			DbRequestQueue.getInstance().addRequest(reqAddPackages,this);
			while (!isUpdateDbModelAddDone()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					Messenger.getInstance().error(this,"Updating database model was interrupted");
					error = true;
				}
			}
		}

		if (!error) {
			// Update stuff
			final List<ReqUpdate> updateList = new ArrayList<ReqUpdate>();
			EvtEventSubscriber updateSubscriber = new EvtEventSubscriber() {
				private int updateListIndex = 0;
				@Override
				public void handleEvent(EvtEvent e) {
					if (e.getValue()==updateList.get(updateListIndex)) {
						updateListIndex++;
						if (updateListIndex < (updateList.size() - 1)) {
							DbRequestQueue.getInstance().addRequest(updateList.get(updateListIndex),this);
						} else {
							lockMe(this);
							updateDbModelUpdateDone = true;
							unlockMe(this);
						}
					}
				}
			};
			for (long id: classIdMdlClassMap.keySet()) {
				MdlClass cls = classIdMdlClassMap.get(id);
				DbDataObject clsObj = classIdObjectMap.get(id);
				StringBuilder mdlAbstr = new StringBuilder("" + cls.isAbstr());
				StringBuilder objAbstr = clsObj.getPropertyValue("abstract");
				List<Long> mdlExtendsClasses = new ArrayList<Long>();
				for (String className: cls.getExtendsClasses()) {
					mdlExtendsClasses.add(classFullNameIdMap.get(className));
				}
				List<Long> objExtendsClasses = clsObj.getLinkValue("extendsClasses");
				if (objExtendsClasses==null) {
					objExtendsClasses = new ArrayList<Long>();
				}
				if (!Generic.stringBuilderEquals(mdlAbstr,objAbstr) ||
					!Generic.idListEquals(mdlExtendsClasses,objExtendsClasses)
					) {
					clsObj.setPropertyValue("abstract",mdlAbstr);
					clsObj.setLinkValue("extendsClasses",mdlExtendsClasses);
					ReqUpdate reqUpd = new ReqUpdate(MdlModel.CLASS_CLASS_FULL_NAME,id);
					reqUpd.getObjects().add(new ReqDataObject(clsObj));
					reqUpd.addSubscriber(updateSubscriber);
					updateList.add(reqUpd);
				}
			}
			for (long id: stringIdMdlStringMap.keySet()) {
				MdlString str = stringIdMdlStringMap.get(id);
				DbDataObject strObj = stringIdObjectMap.get(id);
				StringBuilder mdlIndex = new StringBuilder("" + str.isIndex());
				StringBuilder objIndex = strObj.getPropertyValue("index");
				StringBuilder mdlMaxLength = new StringBuilder("" + str.getMaxLength());
				StringBuilder objMaxLength = strObj.getPropertyValue("maxLength");
				StringBuilder mdlEncode = new StringBuilder("" + str.isEncode());
				StringBuilder objEncode = strObj.getPropertyValue("encode");
				if (!Generic.stringBuilderEquals(mdlIndex,objIndex) ||
					!Generic.stringBuilderEquals(mdlMaxLength,objMaxLength) ||
					!Generic.stringBuilderEquals(mdlEncode,objEncode)
					) {
					strObj.setPropertyValue("index",mdlIndex);
					strObj.setPropertyValue("maxLength",mdlMaxLength);
					strObj.setPropertyValue("encode",mdlEncode);
					ReqUpdate reqUpd = new ReqUpdate(MdlModel.STRING_CLASS_FULL_NAME,id);
					reqUpd.getObjects().add(new ReqDataObject(strObj));
					reqUpd.addSubscriber(updateSubscriber);
					updateList.add(reqUpd);
				}
			}
			for (long id: numberIdMdlNumberMap.keySet()) {
				MdlNumber num = numberIdMdlNumberMap.get(id);
				DbDataObject numObj = numberIdObjectMap.get(id);
				StringBuilder mdlIndex = new StringBuilder("" + num.isIndex());
				StringBuilder objIndex = numObj.getPropertyValue("index");
				StringBuilder mdlMinLength = new StringBuilder("" + num.getMinValue());
				StringBuilder objMinLength = numObj.getPropertyValue("minValue");
				StringBuilder mdlMaxLength = new StringBuilder("" + num.getMaxValue());
				StringBuilder objMaxLength = numObj.getPropertyValue("maxValue");
				if (!Generic.stringBuilderEquals(mdlIndex,objIndex) ||
					!Generic.stringBuilderEquals(mdlMinLength,objMinLength) ||
					!Generic.stringBuilderEquals(mdlMaxLength,objMaxLength)
					) {
					numObj.setPropertyValue("index",mdlIndex);
					numObj.setPropertyValue("minValue",mdlMinLength);
					numObj.setPropertyValue("maxValue",mdlMaxLength);
					ReqUpdate reqUpd = new ReqUpdate(MdlModel.NUMBER_CLASS_FULL_NAME,id);
					reqUpd.getObjects().add(new ReqDataObject(numObj));
					reqUpd.addSubscriber(updateSubscriber);
					updateList.add(reqUpd);
				}
			}
			for (long id: linkIdMdlLinkMap.keySet()) {
				MdlLink lnk = linkIdMdlLinkMap.get(id);
				DbDataObject lnkObj = linkIdObjectMap.get(id);
				StringBuilder mdlIndex = new StringBuilder("true");
				StringBuilder objIndex = lnkObj.getPropertyValue("index");
				StringBuilder mdlMaxSize = new StringBuilder("" + lnk.getMaxSize());
				StringBuilder objMaxSize = lnkObj.getPropertyValue("maxSize");
				List<Long> mdlClassTo = new ArrayList<Long>();
				mdlClassTo.add(classFullNameIdMap.get(lnk.getClassTo()));
				List<Long> objClassTo = lnkObj.getLinkValue("classTo");
				if (!Generic.stringBuilderEquals(mdlIndex,objIndex) ||
					!Generic.idListEquals(mdlClassTo,objClassTo) ||
					!Generic.stringBuilderEquals(mdlMaxSize,objMaxSize)
					) {
					lnkObj.setPropertyValue("index",mdlIndex);
					lnkObj.setPropertyValue("maxSize",mdlMaxSize);
					lnkObj.setLinkValue("classTo",mdlClassTo);
					ReqUpdate reqUpd = new ReqUpdate(MdlModel.LINK_CLASS_FULL_NAME,id);
					reqUpd.getObjects().add(new ReqDataObject(lnkObj));
					reqUpd.addSubscriber(updateSubscriber);
					updateList.add(reqUpd);
				}
			}
			if (updateList.size()>0) {
				lockMe(this);
				updateDbModelUpdateDone = false;
				unlockMe(this);
				DbRequestQueue.getInstance().addRequest(updateList.get(0),this);
				while (!isUpdateDbModelUpdateDone()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						Messenger.getInstance().error(this,"Updating database model was interrupted");
						error = true;
					}
				}
			}
		}
		
		if (!error) {
			Messenger.getInstance().debug(this,"Updated database model");
		}
		return !error;
	}
	
	public boolean updateMdlModel(MdlModel useModel, List<String> originalFullNames, boolean logAsHtml) {
		Messenger.getInstance().debug(this,"Updating model ...");
		boolean error = false;
		if (useModel==null) {
			useModel = DbConfig.getInstance().getModel();
			originalFullNames = useModel.getFullNames();
		}
		if (originalFullNames == null) {
			originalFullNames = new ArrayList<String>();
		}
		
		error = !this.loadDifferences(useModel,false);

		removeIndexes.clear();
		addIndexes.clear();

		updateLog = new StringBuilder();
		String lineEnd = "<br/>";
		String boldStart = "<b>";
		String boldEnd = "</b>";
		if (!logAsHtml) {
			lineEnd = "\n";
			boldStart = "";
			boldEnd = "";
		}
		
		revert = error;

		if (!error) {
			// Add stuff
			for (Long id: removePackageIdList) {
				StringBuilder name = null;
				DbDataObject pkgObj = packageIdObjectMap.get(id);
				if (pkgObj!=null) {
					name = pkgObj.getPropertyValue("name");
				}
				if (name!=null) {
					MdlPackage pkg = useModel.getNewPackage();
					pkg.setName(name.toString());
					addUpdateLogLine("Add package: " + boldStart + pkg.getName() + boldEnd,lineEnd);
				}
			}
			for (Long id: removeClassIdList) {
				StringBuilder name = null;
				MdlPackage pkg = null;
				StringBuilder abstr = null;
				List<Long> extendsClasses = null;
				DbDataObject clsObj = classIdObjectMap.get(id);
				if (clsObj!=null && clsObj.hasPropertyValue("package") && clsObj.getLinkValue("package").size()>0) {
					name = clsObj.getPropertyValue("name");
					abstr = clsObj.getPropertyValue("abstract");
					extendsClasses = clsObj.getLinkValue("extendsClasses");
					List<Long> packageVal = clsObj.getLinkValue("package");
					String packageName = packageIdNameMap.get(packageVal.get(0));
					if (packageName!=null) {
						pkg = useModel.getPackageByName(packageName);
					}
				}
				// Create model class
				if (pkg!=null && name!=null) {
					MdlClass cls = pkg.getNewClass();
					cls.setName(name.toString());
					if (abstr!=null && abstr.length()>0) {
						cls.setAbstr(Boolean.parseBoolean(abstr.toString()));
					}
					StringBuilder extCls = new StringBuilder();
					if (extendsClasses!=null && extendsClasses.size()>0) {
						for (long clsId: extendsClasses) {
							if (extCls.length()>0) {
								extCls.append(", ");
							}
							extCls.append(classIdFullNameMap.get(clsId));
							cls.getExtendsClasses().add(classIdFullNameMap.get(clsId));
						}
					}
					addUpdateLogLine("Add class: " + boldStart + cls.getFullName() + boldEnd + ", abstract: " + boldStart + cls.isAbstr() + boldEnd + ", extendsClasses: " + boldStart + extCls + boldEnd,lineEnd);
				}
			}
			for (Long id: removeStringIdList) {
				StringBuilder name = null;
				MdlClass cls = null;
				StringBuilder index = null;
				StringBuilder maxLength = null;
				StringBuilder encode = null;
				DbDataObject strObj = stringIdObjectMap.get(id);
				if (strObj!=null) {
					name = strObj.getPropertyValue("name");
					index = strObj.getPropertyValue("index");
					maxLength = strObj.getPropertyValue("maxLength");
					encode = strObj.getPropertyValue("encode");
					List<Long> classVal = strObj.getLinkValue("class");
					String className = classIdFullNameMap.get(classVal.get(0));
					if (className!=null) {
						cls = useModel.getClassByFullName(className);
					}
				}
				// Create model string
				if (cls!=null && name!=null) {
					MdlString str = cls.getNewString();
					str.setName(name.toString());
					if (index!=null && index.length()>0) {
						str.setIndex(Boolean.parseBoolean(index.toString()));
					}
					if (maxLength!=null && maxLength.length()>0) {
						str.setMaxLength(Integer.parseInt(maxLength.toString()));
					}
					if (encode!=null && encode.length()>0) {
						str.setEncode(Boolean.parseBoolean(encode.toString()));
					}
					addUpdateLogLine("Add string: " + boldStart + str.getFullName() + boldEnd + ", index: " + boldStart + str.isIndex() + boldEnd + ", maxLength: " + boldStart + str.getMaxLength() + boldEnd + ", encode: " + boldStart + str.isEncode() + boldEnd,lineEnd);
				}
			}
			for (Long id: removeNumberIdList) {
				StringBuilder name = null;
				MdlClass cls = null;
				StringBuilder index = null;
				StringBuilder minValue = null;
				StringBuilder maxValue = null;
				DbDataObject numObj = numberIdObjectMap.get(id);
				if (numObj!=null) {
					name = numObj.getPropertyValue("name");
					index = numObj.getPropertyValue("index");
					minValue = numObj.getPropertyValue("minValue");
					maxValue = numObj.getPropertyValue("maxValue");
					List<Long> classVal = numObj.getLinkValue("class");
					String className = classIdFullNameMap.get(classVal.get(0));
					if (className!=null) {
						cls = useModel.getClassByFullName(className);
					}
				}
				// Create model number
				if (cls!=null && name!=null && !name.toString().equals(MdlProperty.ID)) {
					MdlNumber num = cls.getNewNumber();
					num.setName(name.toString());
					if (index!=null && index.length()>0) {
						num.setIndex(Boolean.parseBoolean(index.toString()));
					}
					if (minValue!=null && minValue.length()>0) {
						num.setMinValue(new BigDecimal(minValue.toString()));
					}
					if (maxValue!=null && maxValue.length()>0) {
						num.setMaxValue(new BigDecimal(maxValue.toString()));
					}
					addUpdateLogLine("Add number: " + boldStart + num.getFullName() + boldEnd + ", index: " + boldStart + num.isIndex() + boldEnd + ", minValue: " + boldStart + num.getMinValue() + boldEnd + ", maxValue: " + boldStart + num.getMaxValue() + boldEnd,lineEnd);
				}
			}
			for (Long id: removeLinkIdList) {
				StringBuilder name = null;
				MdlClass cls = null;
				StringBuilder maxSize = null;
				String classTo = null;
				DbDataObject lnkObj = numberIdObjectMap.get(id);
				if (lnkObj!=null) {
					name = lnkObj.getPropertyValue("name");
					maxSize = lnkObj.getPropertyValue("maxSize");
					List<Long> classToVal = lnkObj.getLinkValue("classTo");
					classTo = classIdFullNameMap.get(classToVal.get(0));
					List<Long> classVal = lnkObj.getLinkValue("class");
					String className = classIdFullNameMap.get(classVal.get(0));
					if (className!=null) {
						cls = useModel.getClassByFullName(className);
					}
				}
				// Create model link
				if (cls!=null && name!=null && classTo!=null && classTo.length()>0) {
					MdlLink lnk = cls.getNewLink();
					lnk.setName(name.toString());
					lnk.setClassTo(classTo);
					if (maxSize!=null && maxSize.length()>0) {
						lnk.setMaxSize(Integer.parseInt(maxSize.toString()));
					}
					addUpdateLogLine("Add link: " + boldStart + lnk.getFullName() + boldEnd + ", index: " + boldStart + lnk.isIndex() + boldEnd + ", maxSize: " + boldStart + lnk.getMaxSize() + boldEnd,lineEnd);
				}
			}
			for (Long id: removeUcIdList) {
				List<String> classes = null;
				List<String> properties = null;
				DbDataObject ucObj = ucIdObjectMap.get(id);
				if (ucObj!=null && 
					ucObj.hasPropertyValue("classes") && ucObj.getLinkValue("classes").size()>0  && 
					ucObj.hasPropertyValue("properties") && ucObj.getPropertyValue("properties").length()>0
					) {
					classes = new ArrayList<String>();
					properties = new ArrayList<String>();
					for (long classId: ucObj.getLinkValue("classes")) {
						String className = classIdFullNameMap.get(classId);
						classes.add(className);
					}
					for (String propertyName: ucObj.getPropertyValue("properties").toString().split(",")) {
						properties.add(propertyName);
					}
				}
				// Create model unique constraint
				if (properties!=null && classes!=null && properties.size()>0 && classes.size()>0) {
					MdlUniqueConstraint uc = useModel.getNewUniqueConstraint();
					for (String property: properties) {
						uc.getProperties().add(property);
					}
					for (String className: classes) {
						uc.getClasses().add(className);
					}
					addUpdateLogLine("Add unique constraint: " + boldStart + uc.getFullName() + boldEnd,lineEnd);
					addIndexes.add(new IdxUniqueConstraint(uc));
				}
			}
			
			// Remove stuff
			for (MdlPackage pkg: addMdlPackageList) {
				if (!originalFullNames.contains(pkg.getName())) {
					addUpdateLogLine("Remove package: " + boldStart + pkg.getName() + boldEnd,lineEnd);
					for (MdlClass cls: pkg.getClasses()) {
						removeIndexes.add(new IdxClass(cls));
					}
					useModel.removePackage(pkg);
				} else {
					addUpdateLogLine("Revert remove package: " + boldStart + pkg.getName() + boldEnd,lineEnd);
					revert = true;
				}
			}
			if (addMdlClassList.size()>0) {
				for (MdlPackage pkg: useModel.getPackages()) {
					for (MdlClass cls: pkg.getClasses()) {
						if (!originalFullNames.contains(cls.getFullName())) {
							if (addMdlClassList.contains(cls)) {
								addUpdateLogLine("Remove class: " + boldStart + cls.getFullName() + boldEnd,lineEnd);
								removeIndexes.add(new IdxClass(cls));
								pkg.removeClass(cls);
							}
						} else {
							addUpdateLogLine("Revert remove class: " + boldStart + cls.getFullName() + boldEnd,lineEnd);
							revert = true;
						}
					}
				}
			}
			if (addMdlStringList.size()>0 || addMdlNumberList.size()>0 || addMdlLinkList.size()>0) {
				for (MdlPackage pkg: useModel.getPackages()) {
					for (MdlClass cls: pkg.getClasses()) {
						for (MdlProperty prop: cls.getProperties()) {
							if (addMdlStringList.contains(prop) || addMdlNumberList.contains(prop) || addMdlLinkList.contains(prop)) {
								if (!originalFullNames.contains(prop.getFullName())) {
									if (prop instanceof MdlString) {
										addUpdateLogLine("Remove string: " + boldStart + prop.getFullName() + boldEnd,lineEnd);
									} else if (prop instanceof MdlNumber) {
										addUpdateLogLine("Remove number: " + boldStart + prop.getFullName() + boldEnd,lineEnd);
									} else if (prop instanceof MdlLink) {
										addUpdateLogLine("Remove link: " + boldStart + prop.getFullName() + boldEnd,lineEnd);
									}
									if (prop.isIndex()) {
										if (prop instanceof MdlString) {
											removeIndexes.add(new IdxString((MdlString)prop));
										} else if (prop instanceof MdlNumber) {
											removeIndexes.add(new IdxNumber((MdlNumber)prop));
										} else if (prop instanceof MdlLink) {
											removeIndexes.add(new IdxLink((MdlLink)prop));
										}
									}
									cls.removeProperty(prop);
								} else {
									addUpdateLogLine("Revert remove property: " + boldStart + prop.getFullName() + boldEnd,lineEnd);
									revert = true;
								}
							}
						}
					}
				}
			}
			for (MdlUniqueConstraint uc: addMdlUcList) {
				if (!originalFullNames.contains(uc.getFullName())) {
					addUpdateLogLine("Remove unique constraint: " + boldStart + uc.getFullName() + boldEnd,lineEnd);
					removeIndexes.add(new IdxUniqueConstraint(uc));
					useModel.removeUniqueConstraint(uc);
				} else {
					addUpdateLogLine("Revert remove unique constraint: " + boldStart + uc.getFullName() + boldEnd,lineEnd);
					revert = true;
				}
			}
			
			// Update stuff (only classes and properties need to be updated)
			for (Entry<Long,MdlClass> entry: classIdMdlClassMap.entrySet()) {
				DbDataObject clsObj = classIdObjectMap.get(entry.getKey());
				boolean abstr = false;
				if (clsObj!=null && clsObj.hasPropertyValue("abstract") && clsObj.getPropertyValue("abstract").length()>0) {
					abstr = Boolean.parseBoolean(clsObj.getPropertyValue("abstract").toString());
				}
				if (entry.getValue().isAbstr()!=abstr) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update class: " + boldStart + entry.getValue().getFullName() + boldEnd + ", abstract: " + boldStart + abstr + boldEnd,lineEnd);
						if (!abstr) {
							removeIndexes.add(new IdxClass(entry.getValue()));
						}
						entry.getValue().setAbstr(abstr);
					} else {
						addUpdateLogLine("Revert update class: " + boldStart + entry.getValue().getFullName() + boldEnd + ", abstract: " + boldStart + abstr + boldEnd,lineEnd);
						revert = true;
					}
				}
			}
			for (Entry<Long,MdlString> entry: stringIdMdlStringMap.entrySet()) {
				DbDataObject strObj = stringIdObjectMap.get(entry.getKey());
				boolean index = false;
				StringBuilder maxLength = null;
				if (strObj!=null) {
					if (strObj.hasPropertyValue("index") && strObj.getPropertyValue("index").length()>0) {
						index = Boolean.parseBoolean(strObj.getPropertyValue("index").toString());
					}
					if (strObj.hasPropertyValue("maxLength") && strObj.getPropertyValue("maxLength").length()>0) {
						maxLength = strObj.getPropertyValue("maxLength");
					}
				}
				if (entry.getValue().isIndex()!=index) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update string: " + boldStart + entry.getValue().getFullName() + boldEnd + ", index: " + boldStart + index + boldEnd,lineEnd);
						if (index) {
							addIndexes.add(new IdxString((MdlString)entry.getValue()));
						} else {
							removeIndexes.add(new IdxString((MdlString)entry.getValue()));
						}
						entry.getValue().setIndex(index);
					} else {
						addUpdateLogLine("Revert update string: " + boldStart + entry.getValue().getFullName() + boldEnd + ", index: " + boldStart + index + boldEnd,lineEnd);
						revert = true;
					}
				}
				if (maxLength!=null && maxLength.length()>0 && entry.getValue().getMaxLength()!=Integer.parseInt(maxLength.toString())) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update string: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxLength: " + boldStart + maxLength + boldEnd,lineEnd);
						entry.getValue().setMaxLength(Integer.parseInt(maxLength.toString()));
					} else {
						addUpdateLogLine("Revert update string: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxLength: " + boldStart + maxLength + boldEnd,lineEnd);
						revert = true;
					}
				}
			}
			for (Entry<Long,MdlNumber> entry: numberIdMdlNumberMap.entrySet()) {
				DbDataObject numObj = numberIdObjectMap.get(entry.getKey());
				boolean index = false;
				StringBuilder minValue = null;
				StringBuilder maxValue = null;
				if (numObj!=null) {
					if (numObj.hasPropertyValue("index") && numObj.getPropertyValue("index").length()>0) {
						index = Boolean.parseBoolean(numObj.getPropertyValue("index").toString());
					}
					if (numObj.hasPropertyValue("minValue") && numObj.getPropertyValue("minValue").length()>0) {
						minValue = numObj.getPropertyValue("minValue");
					}
					if (numObj.hasPropertyValue("maxValue") && numObj.getPropertyValue("maxValue").length()>0) {
						maxValue = numObj.getPropertyValue("maxValue");
					}
				}
				if (entry.getValue().isIndex()!=index) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", index: " + boldStart + index + boldEnd,lineEnd);
						if (index) {
							addIndexes.add(new IdxNumber((MdlNumber)entry.getValue()));
						} else {
							removeIndexes.add(new IdxNumber((MdlNumber)entry.getValue()));
						}
						entry.getValue().setIndex(index);
					} else {
						addUpdateLogLine("Revert update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", index: " + boldStart + index + boldEnd,lineEnd);
						revert = true;
					}
				}
				if (minValue!=null && minValue.length()>0 && entry.getValue().getMinValue().compareTo(new BigDecimal(minValue.toString()))!=0) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", minValue: " + boldStart + minValue + boldEnd,lineEnd);
						entry.getValue().setMinValue(new BigDecimal(minValue.toString()));
					} else {
						addUpdateLogLine("Revert update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", minValue: " + boldStart + minValue + boldEnd,lineEnd);
						revert = true;
					}
				}
				if (maxValue!=null && maxValue.length()>0 && entry.getValue().getMaxValue().compareTo(new BigDecimal(maxValue.toString()))!=0) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxValue: " + boldStart + maxValue + boldEnd,lineEnd);
						entry.getValue().setMaxValue(new BigDecimal(maxValue.toString()));
					} else {
						addUpdateLogLine("Revert update number: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxValue: " + boldStart + maxValue + boldEnd,lineEnd);
						revert = true;
					}
				}
			}
			for (Entry<Long,MdlLink> entry: linkIdMdlLinkMap.entrySet()) {
				DbDataObject lnkObj = linkIdObjectMap.get(entry.getKey());
				StringBuilder maxSize = null;
				String classTo = null;
				if (lnkObj!=null) {
					if (lnkObj.hasPropertyValue("maxSize") && lnkObj.getPropertyValue("maxSize").length()>0) {
						maxSize = lnkObj.getPropertyValue("maxSize");
					}
					if (lnkObj.hasPropertyValue("classTo") && lnkObj.getLinkValue("classTo").size()>0) {
						classTo = classIdFullNameMap.get(lnkObj.getLinkValue("classTo").get(0));
					}
				}
				if (maxSize!=null && maxSize.length()>0 && entry.getValue().getMaxSize()!=Integer.parseInt(maxSize.toString())) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update link: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxSize: " + boldStart + maxSize + boldEnd,lineEnd);
						entry.getValue().setMaxSize(Integer.parseInt(maxSize.toString()));
					} else {
						addUpdateLogLine("Revert update link: " + boldStart + entry.getValue().getFullName() + boldEnd + ", maxSize: " + boldStart + maxSize + boldEnd,lineEnd);
						revert = true;
					}
				}
				if (classTo!=null && classTo.length()>0 && !entry.getValue().getClassTo().equals(classTo)) {
					if (!originalFullNames.contains(entry.getValue().getFullName())) {
						addUpdateLogLine("Update link: " + boldStart + entry.getValue().getFullName() + boldEnd + ", classTo: " + boldStart + classTo + boldEnd,lineEnd);
						removeIndexes.add(new IdxLink((MdlLink)entry.getValue()));
						addIndexes.add(new IdxLink((MdlLink)entry.getValue()));
						entry.getValue().setClassTo(classTo);
					} else {
						addUpdateLogLine("Revert update link: " + boldStart + entry.getValue().getFullName() + boldEnd + ", classTo: " + boldStart + classTo + boldEnd,lineEnd);
						revert = true;
					}
				}
			}
			
			if (revert) {
				addUpdateLogLine("Revert mandatory database model changes",lineEnd);
			}
			
			for (IdxObject idx: addIndexes) {
				if (idx instanceof IdxUniqueConstraint) {
					addUpdateLogLine("<b>WARNING:</b> one or more unique indexes will be created (If one of the classes the index references contains two objects that violate the unique constraint the entire update will fail)",lineEnd);
				}
			}

			StringBuilder errHTML = useModel.getErrorHTML();
			if (errHTML.length()>0) {
				updateLog = errHTML;
				error = true;
			}
		}
		
		if (!error) {
			Messenger.getInstance().debug(this,"Updated model");
		}
		
		return !error;
	}

	/**
	 * @return the removeIndexes
	 */
	public List<IdxObject> getRemoveIndexes() {
		return removeIndexes;
	}

	/**
	 * @return the addIndexes
	 */
	public List<IdxObject> getAddIndexes() {
		return addIndexes;
	}

	/**
	 * @return the updateLog
	 */
	public StringBuilder getUpdateLog() {
		return updateLog;
	}

	/**
	 * @return the revert
	 */
	public boolean isRevert() {
		return revert;
	}
	
	/**************************** PRIVATE METHODS **************************/
	
	/**
	 * Compares the current model to the database.
	 * 
	 * Builds lists to indicate what needs to be done to make the database match the model.
	 * The reverse can be used to indicate what needs to be done make the model match the database.
	 *  
	 * @param useModel The model to compare to the database
	 * @param forRevert Used to clarify debug messages with intent
	 * @return true if no error occurred
	 */
	private boolean loadDifferences(MdlModel useModel,boolean forRevert) {
		Messenger.getInstance().debug(this,"Loading differences ...");
		loadDifferencesError = false;
		if (useModel==null) {
			useModel = DbConfig.getInstance().getModel();
		}

		packageIdNameMap.clear();
		classIdFullNameMap.clear();
		
		packageIdMdlPackageMap.clear();
		classIdMdlClassMap.clear();
		stringIdMdlStringMap.clear();
		numberIdMdlNumberMap.clear();
		linkIdMdlLinkMap.clear();
		ucIdMdlUcMap.clear();
		
		packageIdObjectMap.clear();
		classIdObjectMap.clear();
		stringIdObjectMap.clear();
		numberIdObjectMap.clear();
		linkIdObjectMap.clear();
		ucIdObjectMap.clear();

		addMdlPackageList.clear();
		addMdlClassList.clear();
		addMdlStringList.clear();
		addMdlNumberList.clear();
		addMdlLinkList.clear();
		addMdlUcList.clear();
		
		removePackageIdList.clear();
		removeClassIdList.clear();
		removeStringIdList.clear();
		removeNumberIdList.clear();
		removeLinkIdList.clear();
		removeUcIdList.clear();
		
		final MdlModel model = useModel;
		final ReqGet reqGetPackages = new ReqGet(MdlModel.PACKAGE_CLASS_FULL_NAME);
		reqGetPackages.getProperties().add(ReqGet.ALL_PROPERTIES);
		reqGetPackages.addSubscriber(new EvtEventSubscriber() {
			private ReqGet 							reqGetClasses 		= new ReqGet(MdlModel.CLASS_CLASS_FULL_NAME);
			private ReqGet 							reqGetStrings 		= new ReqGet(MdlModel.STRING_CLASS_FULL_NAME);
			private ReqGet 							reqGetNumbers 		= new ReqGet(MdlModel.NUMBER_CLASS_FULL_NAME);
			private ReqGet 							reqGetLinks 		= new ReqGet(MdlModel.LINK_CLASS_FULL_NAME);
			private ReqGet 							reqGetConstraints 	= new ReqGet(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME);
			
			@Override
			public void handleEvent(EvtEvent e) {
				if (e.getValue()==reqGetPackages) {
					for (ReqDataObject reqObj: reqGetPackages.getObjects()) {
						long packageId = reqObj.getDataObject().getId();
						String packageName = reqObj.getDataObject().getPropertyValue("name").toString();
						MdlPackage pkg = model.getPackageByName(packageName);
						if (pkg!=null) {
							packageIdMdlPackageMap.put(packageId,pkg);
						} else {
							removePackageIdList.add(packageId);
						}
						packageIdObjectMap.put(packageId,reqObj.getDataObject());
						packageIdNameMap.put(packageId,packageName);
					}
					for (MdlPackage pkg: model.getPackages()) {
						if (!packageIdMdlPackageMap.containsValue(pkg)) {
							addMdlPackageList.add(pkg);
						}
					}
					
					reqGetClasses.getProperties().add(ReqGet.ALL_PROPERTIES);
					reqGetClasses.addSubscriber(this);
					DbRequestQueue.getInstance().addRequest(reqGetClasses,this);
				}
				if (e.getValue()==reqGetClasses) {
					for (ReqDataObject reqObj: reqGetClasses.getObjects()) {
						long classId = reqObj.getDataObject().getId();
						String className = reqObj.getDataObject().getPropertyValue("name").toString();
						List<Long> packageVal = reqObj.getDataObject().getLinkValue("package");
						if (packageVal!=null && packageVal.size()>0) {
							MdlPackage pkg = packageIdMdlPackageMap.get(packageVal.get(0));
							MdlClass cls = null;
							if (pkg!=null) {
								cls = pkg.getClassByName(className);
							}
							if (cls!=null) {
								classIdMdlClassMap.put(classId,cls);
							} else {
								removeClassIdList.add(classId);
							}
							classIdObjectMap.put(classId,reqObj.getDataObject());
							classIdFullNameMap.put(classId,packageIdNameMap.get(packageVal.get(0)) + "." + className);
							classFullNameIdMap.put(packageIdNameMap.get(packageVal.get(0)) + "." + className,classId);
						} else {
							Messenger.getInstance().error(this, "Class package not set for class id: " + classId);
							loadDifferencesError = true;
						}
					}
					for (MdlClass cls: model.getClasses()) {
						if (!classIdMdlClassMap.containsValue(cls)) {
							addMdlClassList.add(cls);
						}
					}
					
					reqGetStrings.getProperties().add(ReqGet.ALL_PROPERTIES);
					reqGetNumbers.getProperties().add(ReqGet.ALL_PROPERTIES);
					reqGetLinks.getProperties().add(ReqGet.ALL_PROPERTIES);

					reqGetStrings.addSubscriber(this);
					reqGetNumbers.addSubscriber(this);
					reqGetLinks.addSubscriber(this);

					DbRequestQueue.getInstance().addRequest(reqGetStrings,this);
				}
				if (e.getValue()==reqGetStrings) {
					for (ReqDataObject reqObj: reqGetStrings.getObjects()) {
						long propertyId = reqObj.getDataObject().getId();
						String propertyName = reqObj.getDataObject().getPropertyValue("name").toString();
						List<Long> classVal = reqObj.getDataObject().getLinkValue("class");
						MdlClass cls = classIdMdlClassMap.get(classVal.get(0));
						MdlProperty prop = null;
						if (cls!=null) {
							prop = cls.getPropertyByName(propertyName);
							if (!(prop instanceof MdlString)) {
								prop = null;
							}
						}
						if (prop!=null) {
							stringIdMdlStringMap.put(propertyId,(MdlString) prop);
						} else {
							removeStringIdList.add(propertyId);
						}
						stringIdObjectMap.put(propertyId,reqObj.getDataObject());
					}
					for (MdlClass cls: model.getClasses()) {
						for (MdlProperty prop: cls.getProperties()) {
							if (prop instanceof MdlString && !stringIdMdlStringMap.containsValue(prop)) {
								addMdlStringList.add((MdlString) prop);
							}
						}
					}
					
					DbRequestQueue.getInstance().addRequest(reqGetNumbers,this);
				}
				if (e.getValue()==reqGetNumbers) {
					for (ReqDataObject reqObj: reqGetNumbers.getObjects()) {
						long propertyId = reqObj.getDataObject().getId();
						String propertyName = reqObj.getDataObject().getPropertyValue("name").toString();
						List<Long> classVal = reqObj.getDataObject().getLinkValue("class");
						MdlClass cls = classIdMdlClassMap.get(classVal.get(0));
						MdlProperty prop = null;
						if (cls!=null && !propertyName.equals(MdlProperty.ID)) {
							prop = cls.getPropertyByName(propertyName);
							if (!(prop instanceof MdlNumber)) {
								prop = null;
							}
						}
						if (prop!=null) {
							numberIdMdlNumberMap.put(propertyId,(MdlNumber) prop);
						} else {
							removeNumberIdList.add(propertyId);
						}
						numberIdObjectMap.put(propertyId,reqObj.getDataObject());
					}
					for (MdlClass cls: model.getClasses()) {
						for (MdlProperty prop: cls.getProperties()) {
							if (prop instanceof MdlNumber && !numberIdMdlNumberMap.containsValue(prop)) {
								addMdlNumberList.add((MdlNumber) prop);
							}
						}
					}

					DbRequestQueue.getInstance().addRequest(reqGetLinks,this);
				} 
				if (e.getValue()==reqGetLinks) {
					for (ReqDataObject reqObj: reqGetLinks.getObjects()) {
						long propertyId = reqObj.getDataObject().getId();
						String propertyName = reqObj.getDataObject().getPropertyValue("name").toString();
						List<Long> classVal = reqObj.getDataObject().getLinkValue("class");
						MdlClass cls = classIdMdlClassMap.get(classVal.get(0));
						MdlProperty prop = null;
						if (cls!=null) {
							prop = cls.getPropertyByName(propertyName);
							if (!(prop instanceof MdlLink)) {
								prop = null;
							}
						}
						if (prop!=null) {
							linkIdMdlLinkMap.put(propertyId,(MdlLink) prop);
						} else {
							removeLinkIdList.add(propertyId);
						}
						linkIdObjectMap.put(propertyId,reqObj.getDataObject());
					}
					for (MdlClass cls: model.getClasses()) {
						for (MdlProperty prop: cls.getProperties()) {
							if (prop instanceof MdlLink && !linkIdMdlLinkMap.containsValue(prop)) {
								addMdlLinkList.add((MdlLink) prop);
							}
						}
					}
					
					reqGetConstraints.getProperties().add(ReqGet.ALL_PROPERTIES);
					reqGetConstraints.addSubscriber(this);
					DbRequestQueue.getInstance().addRequest(reqGetConstraints,this);
				}
				if (e.getValue()==reqGetConstraints) {
					for (ReqDataObject reqObj: reqGetConstraints.getObjects()) {
						long ucId = reqObj.getDataObject().getId();
						boolean caseSensitive = Boolean.parseBoolean(reqObj.getDataObject().getPropertyValue("caseSensitive").toString());
						String properties[] = reqObj.getDataObject().getPropertyValue("properties").toString().split(",");
						List<Long> classesVal = reqObj.getDataObject().getLinkValue("classes");
						MdlUniqueConstraint uc = null;
						for (MdlUniqueConstraint tuc: model.getUniqueConstraints()) {
							if (tuc.isCaseSensitive()==caseSensitive && tuc.getClasses().size()==classesVal.size() && tuc.getProperties().size()==properties.length) {
								boolean classesMatch = true;
								int index = 0;
								for (Long classId: classesVal) {
									MdlClass cls = classIdMdlClassMap.get(classId);
									if (cls==null || !cls.getFullName().equals(tuc.getClasses().get(index))) {
										classesMatch = false;
										break;
									}
									index++;
								}
								boolean propertiesMatch = true;
								for (index = 0; index < properties.length; index++) {
									if (!properties[index].equals(tuc.getProperties().get(index))) {
										propertiesMatch = false;
										break;
									}
								}
								if (classesMatch && propertiesMatch) {
									uc = tuc;
									break;
								}
							}
						}
						if (uc!=null) {
							ucIdMdlUcMap.put(ucId,(MdlUniqueConstraint) uc);
						} else {
							removeUcIdList.add(ucId);
						}
						ucIdObjectMap.put(ucId,reqObj.getDataObject());
					}
					for (MdlUniqueConstraint uc: model.getUniqueConstraints()) {
						if (!ucIdMdlUcMap.containsValue(uc)) {
							addMdlUcList.add(uc);
						}
					}
					lockMe(this);
					loadDifferencesDone = true;
					unlockMe(this);
				}
			}
		});
		lockMe(this);
		loadDifferencesDone = false;
		unlockMe(this);
		DbRequestQueue.getInstance().addRequest(reqGetPackages,this);
	
		while (!isLoadDifferencesDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				Messenger.getInstance().error(this,"Loading differences was interrupted");
				loadDifferencesError = true;
			}
		}
		
		if (!loadDifferencesError) {
			Messenger.getInstance().debug(this,"Loaded differences:");
			if (forRevert) {
				Messenger.getInstance().debug(this,"- Packages found: " + packageIdMdlPackageMap.size() + ", add: " + addMdlPackageList.size() + ", remove: " + removePackageIdList.size());
				Messenger.getInstance().debug(this,"- Classes found: " + classIdMdlClassMap.size() + ", add: " + addMdlClassList.size() + ", remove: " + removeClassIdList.size());
				Messenger.getInstance().debug(this,"- Strings found: " + stringIdMdlStringMap.size() + ", add: " + addMdlStringList.size() + ", remove: " + removeStringIdList.size());
				Messenger.getInstance().debug(this,"- Numbers found: " + numberIdMdlNumberMap.size() + ", add: " + addMdlNumberList.size() + ", remove: " + removeNumberIdList.size());
				Messenger.getInstance().debug(this,"- Links found: " + linkIdMdlLinkMap.size() + ", add: " + addMdlLinkList.size() + ", remove: " + removeLinkIdList.size());
				Messenger.getInstance().debug(this,"- Constraints found: " + ucIdMdlUcMap.size() + ", add: " + addMdlUcList.size() + ", remove: " + removeUcIdList.size());
			} else {
				Messenger.getInstance().debug(this,"- Packages found: " + packageIdMdlPackageMap.size() + ", remove: " + addMdlPackageList.size() + ", add: " + removePackageIdList.size());
				Messenger.getInstance().debug(this,"- Classes found: " + classIdMdlClassMap.size() + ", remove: " + addMdlClassList.size() + ", add: " + removeClassIdList.size());
				Messenger.getInstance().debug(this,"- Strings found: " + stringIdMdlStringMap.size() + ", remove: " + addMdlStringList.size() + ", add: " + removeStringIdList.size());
				Messenger.getInstance().debug(this,"- Numbers found: " + numberIdMdlNumberMap.size() + ", remove: " + addMdlNumberList.size() + ", add: " + removeNumberIdList.size());
				Messenger.getInstance().debug(this,"- Links found: " + linkIdMdlLinkMap.size() + ", remove: " + addMdlLinkList.size() + ", add: " + removeLinkIdList.size());
				Messenger.getInstance().debug(this,"- Constraints found: " + ucIdMdlUcMap.size() + ", remove: " + addMdlUcList.size() + ", add: " + removeUcIdList.size());
			}
		}
		return !loadDifferencesError;
	}

	/**
	 * @return the updateDbModelPreRemoveDone
	 */
	private boolean isUpdateDbModelPreRemoveDone() {
		lockMe(this);
		boolean r = updateDbModelPreRemoveDone;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the updateDbModelRemoveDone
	 */
	private boolean isUpdateDbModelRemoveDone() {
		lockMe(this);
		boolean r = updateDbModelRemoveDone;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the updateDbModelAddDone
	 */
	private boolean isUpdateDbModelAddDone() {
		lockMe(this);
		boolean r = updateDbModelAddDone;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the updateDbModelUpdateDone
	 */
	private boolean isUpdateDbModelUpdateDone() {
		lockMe(this);
		boolean r = updateDbModelUpdateDone;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the loadDifferencesDone
	 */
	private boolean isLoadDifferencesDone() {
		lockMe(this);
		boolean r = loadDifferencesDone;
		unlockMe(this);
		return r;
	}
	
	private void addUpdateLogLine(String line, String lineEnd) {
		Messenger.getInstance().debug(this,line);
		updateLog.append(line);
		updateLog.append(lineEnd);
	}
}
