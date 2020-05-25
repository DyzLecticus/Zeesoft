package nl.zeesoft.zdk.persist;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class PersistableCollection {
	private static final String		NULL					= "null";
	
	private static final String		NEXT_OBJECT				= "\n@NO|\n";
	private static final String		PERSISTABLE_OBJECT		= "@PO|";
	private static final String		PERSISTABLE_PROPERTY	= "@PP|";
	private static final String		EQUALS					= "|EQ=";
	private static final String		NEWLINE					= "<NL|";
	
	private static final String		LIST_START				= "@LS|";
	private static final String		LIST_CONCATENATOR		= "|LC|";
	private static final String		LIST_END				= "|LE@";

	private Lock					lock					= new Lock();
	private SortedMap<Str,Object>	objects					= new TreeMap<Str,Object>();
	
	public Str put(Object object) {
		lock.lock(this);
		Str error = putNoLock(object);
		lock.unlock(this);
		return error;
	}
	
	public void remove(PersistableObject object) {
		lock.lock(this);
		Str id = getObjectId(object);
		if (!objects.containsKey(id)) {
			id = getObjectId(object, objects);
		}
		if (objects.containsKey(id)) {
			objects.remove(id);
		}
		lock.unlock(this);
	}
	
	public int size() {
		lock.lock(this);
		int r = objects.size(); 
		lock.unlock(this);
		return r;
	}
	
	public List<Str> getObjectIds() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(objects.keySet()); 
		lock.unlock(this);
		return r;
	}
	
	public List<Object> getObjects() {
		lock.lock(this);
		List<Object> r = new ArrayList<Object>(objects.values()); 
		lock.unlock(this);
		return r;
	}
	
	public List<Object> getObjects(String className) {
		List<Object> r = new ArrayList<Object>(); 
		lock.lock(this);
		for (Entry<Str,Object> entry: objects.entrySet()) {
			if (entry.getKey().startsWith(className)) {
				r.add(entry.getValue());
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public Object get(Str id) {
		lock.lock(this);
		Object r = objects.get(id); 
		lock.unlock(this);
		return r;
	}
	
	public void clear() {
		lock.lock(this);
		objects.clear(); 
		lock.unlock(this);
	}
	
	public Str toStr() {
		Str r = new Str();
		lock.lock(this);
		for (Entry<Str,Object> entry: objects.entrySet()) {
			if (r.length()>0) {
				r.sb().append(NEXT_OBJECT);
			}
			Str objStr = getObjectAsStr(entry.getValue(),objects);
			setObjectIdInStr(entry.getKey(),objStr);
			r.sb().append(objStr);
		}
		lock.unlock(this);
		return r;
	}
	
	public void fromStr(Str str) {
		lock.lock(this);
		objects.clear();
		List<Str> objStrs = str.split(NEXT_OBJECT);
		for (Str objStr: objStrs) {
			Str id = getObjectIdFromStr(objStr);
			if (id!=null) {
				Object object = getObjectFromObjStr(objStr);
				if (object!=null) {
					putNoLock(id, object);
				}
			}
		}
		for (Object object: objects.values()) {
			expandObjectChildrenNoLock(object);
		}
		expandObjectReferencesNoLock(objStrs);
		lock.unlock(this);
	}
	
	public Str toFile(String fileName) {
		return toStr().toFile(fileName);
	}
	
	public Str fromFile(String fileName) {
		Str data = new Str();
		Str r = data.fromFile(fileName);
		if (data.length()>0) {
			fromStr(data);
		}
		return r;
	}
	
	public Str getObjectAsStr(Object object) {
		lock.lock(this);
		Str r = getObjectAsStr(object, objects);
		lock.unlock(this);
		return r;
	}

	public Object getObjectFromStr(Str objStr) {
		lock.lock(this);
		Object r = getObjectFromObjStr(objStr);
		expandObjectChildrenNoLock(r);
		expandObjectReferencesNoLock(r,objStr);
		lock.unlock(this);
		return r;
	}

	private static Str getObjectAsStr(Object object, SortedMap<Str,Object> objects) {
		Str r = new Str();
		r.sb().append(PERSISTABLE_OBJECT);
		r.sb().append(getObjectId(object));
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.isAnnotationPresent(PersistableProperty.class) &&
					isSupportedValueType(field.getType().toString())
					) {
					field.setAccessible(true);
					Object value = null;
					try {
						value = field.get(object);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
					if (value!=null) {
						if (value instanceof StringBuilder) {
							Str str = new Str((StringBuilder)value);
							str.replace("\n", NEWLINE);
							value = str.sb();
						} else if (value instanceof String) {
							String v = ((String) value);
							v = v.replace("\n", NEWLINE);
							value = v;
						} else if (
							!isArrayType(field.getType().toString()) &&
							isPersistableObject(value.getClass())
							) {
							value = getObjectId(value,objects);
						} else if (value instanceof List) {
							@SuppressWarnings("unchecked")
							List<Object> objs = (List<Object>) value;
							Str newValue = new Str();
							for (Object obj: objs) {
								if (obj!=null) {
									if (isPersistableObject(obj.getClass())) {
										if (newValue.length()>0) {
											newValue.sb().append(LIST_CONCATENATOR);
										}
										newValue.sb().append(getObjectId(obj,objects));
									}
								} else {
									newValue.sb().append(NULL);
								}
							}
							newValue.sb().insert(0, LIST_START);
							newValue.sb().append(LIST_END);
							newValue.sb().insert(0, Object.class.getName());
							value = newValue;
						} else if (
							isArrayType(field.getType().toString()) &&
							isSupportedValueType(field.getType().toString())
							) {
							Object[] values = (Object[]) value;
							Str newValue = new Str();
							for (int i = 0; i < values.length; i++) {
								if (newValue.length()>0) {
									newValue.sb().append(LIST_CONCATENATOR);
								}
								if (values[i]!=null) {
									if (isPersistableObject(values[i].getClass())) {
										newValue.sb().append(getObjectId(values[i],objects));
									} else {
										newValue.sb().append(values[i]);
									}
								} else {
									newValue.sb().append(NULL);
								}
							}
							newValue.sb().insert(0, LIST_START);
							newValue.sb().append(LIST_END);
							newValue.sb().insert(0, Object.class.getName());
							value = newValue;
						} else if (value instanceof Str) {
							Str str = new Str((Str)value);
							str.replace("\n", NEWLINE);
							value = str;
						}
					}
					
					r.sb().append("\n");
					r.sb().append(PERSISTABLE_PROPERTY);
					r.sb().append(field.getName());
					r.sb().append(EQUALS);
					r.sb().append(value);
				}
			}
			cls = cls.getSuperclass();
		}
		
		return r;
	}

	private static void setObjectIdInStr(Str id, Str objStr) {
		if (objStr.startsWith(PERSISTABLE_OBJECT)) {
			Str line = new Str(PERSISTABLE_OBJECT);
			line.sb().append(id);
			List<Str> lines = objStr.split("\n");
			lines.remove(0);
			lines.add(0, line);
			objStr.merge(lines,"\n");
		}
	}

	private static Str getObjectIdFromStr(Str objStr) {
		Str r = null;
		if (objStr.startsWith(PERSISTABLE_OBJECT)) {
			List<Str> lines = objStr.split("\n");
			r = lines.get(0);
			r.sb().delete(0, PERSISTABLE_OBJECT.length());
		}
		return r;
	}

	private static Object getObjectFromObjStr(Str objStr) {
		Object r = null;
		if (objStr.startsWith(PERSISTABLE_OBJECT)) {
			List<Str> lines = objStr.split("\n");
			Str id = lines.get(0);
			id.sb().delete(0, PERSISTABLE_OBJECT.length());
			String className = id.split("@").get(0).toString();
			Object object = Instantiator.getNewClassInstanceForName(className);
			if (object!=null) {
				for (int l = 1; l < lines.size(); l++) {
					Str line = lines.get(l);
					line.sb().delete(0, PERSISTABLE_PROPERTY.length());
					List<Str> nameValue = line.split(EQUALS);
					String fieldName = nameValue.get(0).toString();
					
					Field field = null;
					Class<?> cls = object.getClass();
					while(cls!=Object.class) {
						try {
							field = cls.getDeclaredField(fieldName);
							if (field!=null) {
								break;
							}
						} catch (NoSuchFieldException e) {
							// Ignore
						} catch (SecurityException e) {
							// Ignore
						}
						cls = cls.getSuperclass();
					}
					
					if (field!=null) {
						field.setAccessible(true);
						Object currentValue = null;
						try {
							currentValue = field.get(object);
						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						}
						
						Str value = nameValue.get(1);
						value.replace(NEWLINE, "\n");
						
						Object valueObject = value;
						if (value.equals(new Str(NULL))) {
							valueObject = null;
						} else {
							if (field.getType().isAssignableFrom(List.class)) {
								List<Str> typeValues = value.split(LIST_START);
								String type = typeValues.get(0).toString();
								List<Str> vals = parseValuesFromArray(value);
								List<Object> list = new ArrayList<Object>();
								for (Str val: vals) {
									if (val!=null && !val.toString().equals(NULL)) {
										if (type.equals(StringBuilder.class.getName())) {
											list.add(val.sb());
										} else if (type.equals(String.class.getName())) {
											list.add(val.toString());
										} else if (type.equals(Integer.class.getName())) {
											list.add(Integer.parseInt(val.toString()));
										} else if (type.equals(Long.class.getName())) {
											list.add(Long.parseLong(val.toString()));
										} else if (type.equals(Float.class.getName())) {
											list.add(Float.parseFloat(val.toString()));
										} else if (type.equals(Double.class.getName())) {
											list.add(Double.parseDouble(val.toString()));
										} else if (type.equals(Boolean.class.getName())) {
											list.add(Boolean.parseBoolean(val.toString()));
										} else if (type.equals(BigDecimal.class.getName())) {
											list.add(new BigDecimal(val.toString()));
										} else {
											list.add(val);
										}
									} else {
										list.add(null);
									}
								}
								if (currentValue!=null) {
									@SuppressWarnings("unchecked")
									List<Object> currentList = (List<Object>) currentValue;
									currentList.clear();
									if (list.size()>0) {
										for (Object val: list) {
											currentList.add(val);
										}
									}
									list = currentList;
								}
								valueObject = list;
							} else if (field.getType().isAssignableFrom(StringBuilder.class)) {
								valueObject = value.sb();
							} else if (field.getType().isAssignableFrom(String.class)) {
								valueObject = value.toString();
							} else if (
								field.getType().isAssignableFrom(Integer.class) ||
								field.getType() == int.class
								) {
								valueObject = Integer.parseInt(value.toString());
							} else if (
								field.getType().isAssignableFrom(Long.class) ||
								field.getType() == long.class
								) {
								valueObject = Long.parseLong(value.toString());
							} else if (
								field.getType().isAssignableFrom(Float.class) ||
								field.getType() == float.class
								) {
								valueObject = Float.parseFloat(value.toString());
							} else if (
								field.getType().isAssignableFrom(Double.class) ||
								field.getType() == double.class
								) {
								valueObject = Double.parseDouble(value.toString());
							} else if (
								field.getType().isAssignableFrom(Boolean.class) ||
								field.getType() == boolean.class
								) {
								valueObject = Boolean.parseBoolean(value.toString());
							} else if (field.getType().isAssignableFrom(BigDecimal.class)) {
								valueObject = new BigDecimal(value.toCharArray());
							} else if (
								!isArrayType(field.getType().toString()) && 
								isPersistableObjectType(field.getType().toString())
								) {
								valueObject = null;
							} else if (
								isArrayType(field.getType().toString()) && 
								isSupportedValueType(field.getType().toString())
								) {
								className = Instantiator.getClassName(field.getType().toString());
								List<Str> vals = parseValuesFromArray(value);

								Object[] valObj = null;
								if (field.getType().isAssignableFrom(StringBuilder[].class)) {
									valObj = new StringBuilder[vals.size()];
								} else if (field.getType().isAssignableFrom(String[].class)) {
									valObj = new String[vals.size()];
								} else if (field.getType().isAssignableFrom(Integer[].class)) {
									valObj = new Integer[vals.size()];
								} else if (field.getType().isAssignableFrom(Long[].class)) {
									valObj = new Long[vals.size()];
								} else if (field.getType().isAssignableFrom(Float[].class)) {
									valObj = new Float[vals.size()];
								} else if (field.getType().isAssignableFrom(Double[].class)) {
									valObj = new Double[vals.size()];
								} else if (field.getType().isAssignableFrom(Boolean[].class)) {
									valObj = new Boolean[vals.size()];
								} else if (field.getType().isAssignableFrom(BigDecimal[].class)) {
									valObj = new BigDecimal[vals.size()];
								} else if (isPersistableObjectType(className)) {
									valObj = (Object[]) Instantiator.getNewArrayInstanceForName(className, vals.size());
								} else if (field.getType().isAssignableFrom(Str[].class)) {
									valObj = new Str[vals.size()];
								}
								
								int i = 0;
								for (Str val: vals) {
									if (val!=null && !val.toString().equals(NULL)) {
										if (field.getType().isAssignableFrom(StringBuilder[].class)) {
											valObj[i] = val.sb();
										} else if (field.getType().isAssignableFrom(String[].class)) {
											valObj[i] = val.toString();
										} else if (field.getType().isAssignableFrom(Integer[].class)) {
											valObj[i] = Integer.parseInt(val.toString());
										} else if (field.getType().isAssignableFrom(Long[].class)) {
											valObj[i] = Long.parseLong(val.toString());
										} else if (field.getType().isAssignableFrom(Float[].class)) {
											valObj[i] = Float.parseFloat(val.toString());
										} else if (field.getType().isAssignableFrom(Double[].class)) {
											valObj[i] = Double.parseDouble(val.toString());
										} else if (field.getType().isAssignableFrom(Boolean[].class)) {
											valObj[i] = Boolean.parseBoolean(val.toString());
										} else if (field.getType().isAssignableFrom(BigDecimal[].class)) {
											valObj[i] = new BigDecimal(val.toString());
										} else if (isPersistableObjectType(className)) {
											valObj[i] = null;
										} else if (field.getType().isAssignableFrom(Str[].class)) {
											valObj[i] = val;
										}
									}
									i++;
								}
								valueObject = valObj;
							} else if (field.getType().isAssignableFrom(Str.class)) {
								valueObject = value;
							}
						}
						
						try {
							field.set(object, valueObject);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				r = object;
			}
		}
		return r;
	}

	private static boolean isArrayType(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		return className.startsWith("[L");
	}

	private static boolean isSupportedValueType(String className) {
		className = Instantiator.getClassName(className);
		boolean r = false;
		if (
			className.equals(Str.class.getName()) ||
			className.equals(StringBuilder.class.getName()) ||
			className.equals(String.class.getName()) ||
			className.equals(Integer.class.getName()) ||
			className.equals(Long.class.getName()) ||
			className.equals(Float.class.getName()) ||
			className.equals(Double.class.getName()) ||
			className.equals(Boolean.class.getName()) ||
			className.equals(BigDecimal.class.getName()) ||
			className.equals(List.class.getName())
			) {
			r = true;
		}
		if (!r) {
			r = isPersistableObjectType(className);
		}
		return r;
	}

	private static boolean isPersistableObjectType(String className) {
		className = Instantiator.getClassName(className);
		return isPersistableObject(Instantiator.getClassForName(className));
	}

	private static boolean isPersistableObject(Class<?> cls) {
		boolean r = false;
		if (cls!=null) {
			PersistableObject po = cls.getAnnotation(PersistableObject.class);
			if (po!=null) {
				r = true;
			}
		}
		return r;
	}
	
	private static List<Str> parseValuesFromArray(Str arrayStr) {
		List<Str> typeValues = arrayStr.split(LIST_START);
		arrayStr = typeValues.get(1);
		arrayStr.sb().delete(arrayStr.length() - LIST_END.length(), arrayStr.length());
		return arrayStr.split(LIST_CONCATENATOR);
	}
	
	private static Str getObjectId(Object object) {
		Str r = new Str(object.getClass().getName());
		r.sb().append("@");
		r.sb().append(object.hashCode());
		return r;
	}
	
	private static Str getObjectId(Object object, SortedMap<Str,Object> objects) {
		Str r = getObjectId(object);
		if (objects!=null) {
			for (Entry<Str,Object> entry: objects.entrySet()) {
				if (entry.getValue()==object) {
					r = entry.getKey();
					break;
				}
			}
		}
		return r;
	}
	
	private void addObjectChildrenNoLock(Object object) {
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.isAnnotationPresent(PersistableProperty.class) &&
					field.getType().isAssignableFrom(List.class)
					) {
					field.setAccessible(true);
					List<?> objects = new ArrayList<Object>();
					try {
						objects = (List<?>) field.get(object);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					for (Object child: objects) {
						putNoLock(child);
					}
				}
			}
			cls = cls.getSuperclass();
		}
	}
	
	private void addObjectReferencesNoLock(Object object) {
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.isAnnotationPresent(PersistableProperty.class) &&
					isPersistableObjectType(field.getType().toString())
					) {
					field.setAccessible(true);
					if (isArrayType(field.getType().toString())) {
						Object[] arrayValue = null;
						try {
							arrayValue = (Object[]) field.get(object);
							if (arrayValue!=null) {
								for (int i = 0; i < arrayValue.length; i++) {
									if (arrayValue[i]!=null) {
										putNoLock(arrayValue[i]);
									}
								}
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Object reference = field.get(object);
							if (reference!=null) {
								putNoLock(reference);
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			cls = cls.getSuperclass();
		}
	}
	
	private Str putNoLock(Object object) {
		return putNoLock(null, object);
	}
	
	private Str putNoLock(Str id, Object object) {
		Str error = new Str();
		if (id==null) {
			id = getObjectId(object, objects);
		}
		PersistableObject po = object.getClass().getAnnotation(PersistableObject.class);
		if (po!=null) { 
			boolean add = false;
			if (!objects.containsKey(id)) {
				add = true;
			}
			objects.put(id,object);
			if (add) {
				addObjectChildrenNoLock(object);
				addObjectReferencesNoLock(object);
			}
		} else {
			error.sb().append(object.getClass().getName());
			error.sb().append(" is not annotated with @PersistableObject");
		}
		return error;
	}
	
	private void expandObjectChildrenNoLock(Object object) {
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.isAnnotationPresent(PersistableProperty.class) &&
					field.getType().isAssignableFrom(List.class)
					) {
					field.setAccessible(true);
					List<Object> children = new ArrayList<Object>();
					List<Object> idList = null;
					try {
						@SuppressWarnings("unchecked")
						List<Object> list = (List<Object>) field.get(object);
						idList = list;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					if (idList!=null && idList.size()>0) {
						for (Object id: idList) {
							if (id instanceof Str) {
								Object child = objects.get((Str) id);
								if (child!=null) {
									children.add(child);
								}
							}
						}
						try {
							field.set(object, children);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						for (Object child: children) {
							expandObjectChildrenNoLock(child);
						}
					}
				}
			}
			cls = cls.getSuperclass();
		}
	}
	
	private void expandObjectReferencesNoLock(List<Str> objStrs) {
		for (Str objStr: objStrs) {
			Str id = getObjectIdFromStr(objStr);
			Object object = objects.get(id);
			if (object!=null) {
				expandObjectReferencesNoLock(object,objStr);
			}
		}
	}
	
	private void expandObjectReferencesNoLock(Object object, Str objStr) {
		List<Str> lines = objStr.split("\n");
		for (int l = 1; l < lines.size(); l++) {
			Str line = lines.get(l);
			line.sb().delete(0, PERSISTABLE_PROPERTY.length());
			List<Str> nameValue = line.split(EQUALS);
			String fieldName = nameValue.get(0).toString();
			
			Field field = null;
			Class<?> cls = object.getClass();
			while(cls!=Object.class) {
				try {
					field = cls.getDeclaredField(fieldName);
					if (field!=null) {
						break;
					}
				} catch (NoSuchFieldException e) {
					// Ignore
				} catch (SecurityException e) {
					// Ignore
				}
				cls = cls.getSuperclass();
			}
			
			if (field!=null && isPersistableObjectType(field.getType().toString())) {
				field.setAccessible(true);
				Str value = nameValue.get(1);
				if (isArrayType(field.getType().toString())) {
					Object[] arrayValue = null;
					try {
						arrayValue = (Object[]) field.get(object);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					if (arrayValue!=null) {
						List<Str> vals = parseValuesFromArray(value);
						for (int i = 0; i < arrayValue.length; i++) {
							Object reference = objects.get(vals.get(i));
							arrayValue[i] = reference;
						}
					}
				} else {
					Object reference = objects.get(value);
					if (reference!=null) {
						try {
							field.set(object, reference);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
