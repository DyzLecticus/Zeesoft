package nl.zeesoft.zdk.persist;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Str;

public class PersistableCollectionBase extends CompleteCollection {
	public static final String		NULL					= "null";
	
	public static final String		NEXT_ID					= "@NI|";
	public static final String		START_OBJECTS			= "@SO|\n";
	public static final String		NEXT_OBJECT				= "\n@NO|\n";
	
	public static final String		PERSISTABLE_OBJECT		= "@PO|";
	public static final String		PERSISTABLE_PROPERTY	= "@PP|";
	public static final String		EQUALS					= "|EQ=";
	public static final String		NEWLINE					= "<NL|";
	
	public static final String		LIST_START				= "@LS|";
	public static final String		LIST_CONCATENATOR		= "|LC|";
	public static final String		LIST_END				= "|LE@";
	
	public Str toStr() {
		lock.lock(this);
		Str r = toStrNoLock();
		lock.unlock(this);
		return r;
	}
	
	public void fromStr(Str str) {
		lock.lock(this);
		fromStrNoLock(str);
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
	
	public Str getObjectAsStr(Str id) {
		lock.lock(this);
		Str r = getObjectAsStrNoLock(id);
		lock.unlock(this);
		return r;
	}

	public Object getObjectFromStr(Str objStr) {
		lock.lock(this);
		Object r = getObjectFromObjStr(objStr);
		if (r!=null) {
			expandObjectChildrenNoLock(r);
			expandObjectReferencesNoLock(r,objStr);
		}
		lock.unlock(this);
		return r;
	}
	
	@Override
	protected boolean isSupportedObject(Class<?> cls) {
		return isPersistableObject(cls);
	}
	
	@Override
	protected boolean isSupportedObject(String className) {
		return isPersistableObjectType(className);
	}
	
	@Override
	protected boolean isSupportedField(Field field) {
		return field.isAnnotationPresent(PersistableProperty.class);
	}
	
	protected Str getObjectAsStrNoLock(Str id) {
		Str r = new Str();
		Object object = objects.get(id);
		if (object!=null) {
			r.sb().append(PERSISTABLE_OBJECT);
			r.sb().append(getObjectIdForObjectNoLock(object));
			List<Field> fields = getPersistedFields(object);
			for (Field field : fields) {
				if (isSupportedValueType(field.getType().toString())) {
					Object value = getFieldValue(object, field);
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
							value = getObjectIdForObjectNoLock(value);
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
										newValue.sb().append(getObjectIdForObjectNoLock(obj));
									}
								} else {
									newValue.sb().append(NULL);
								}
							}
							newValue.sb().insert(0, LIST_START);
							newValue.sb().append(LIST_END);
							newValue.sb().insert(0, List.class.getName());
							value = newValue;
						} else if (
							isArrayType(field.getType().toString()) &&
							isSupportedValueType(field.getType().toString())
							) {
							String className = Instantiator.getClassName(field.getType().toString());
							List<Object> values = new ArrayList<Object>();
							if (value instanceof Object[]) {
								Object[] vals = (Object[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
							} else if (value instanceof int[]) {
								int[] vals = (int[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
								className = int.class.getName();
							} else if (value instanceof long[]) {
								long[] vals = (long[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
								className = long.class.getName();
							} else if (value instanceof float[]) {
								float[] vals = (float[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
								className = float.class.getName();
							} else if (value instanceof double[]) {
								double[] vals = (double[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
								className = double.class.getName();
							} else if (value instanceof boolean[]) {
								boolean[] vals = (boolean[]) value;
								for (int i = 0; i < vals.length; i++) {
									values.add(vals[i]);
								}
								className = boolean.class.getName();
							}
							Str newValue = new Str();
							for (Object val: values) {
								if (newValue.length()>0) {
									newValue.sb().append(LIST_CONCATENATOR);
								}
								if (val!=null) {
									if (isPersistableObject(val.getClass())) {
										newValue.sb().append(getObjectIdForObjectNoLock(val));
									} else {
										newValue.sb().append(val);
									}
								} else {
									newValue.sb().append(NULL);
								}
							}
							newValue.sb().insert(0, LIST_START);
							newValue.sb().append(LIST_END);
							newValue.sb().insert(0, className);
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
		}
		return r;
	}
	
	protected void expandObjectChildrenNoLock(Object object) {
		List<Field> fields = getPersistedFields(object);
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(List.class)) {
				List<Object> children = new ArrayList<Object>();
				@SuppressWarnings("unchecked")
				List<Object> idList = (List<Object>) getFieldValue(object, field);
				if (idList!=null && idList.size()>0) {
					for (Object id: idList) {
						if (id instanceof Str) {
							Object child = objects.get((Str) id);
							if (child!=null) {
								children.add(child);
							}
						}
					}
					setFieldValue(object, field, children);
					for (Object child: children) {
						expandObjectChildrenNoLock(child);
					}
				}
			}
		}
	}
	
	protected void expandObjectReferencesNoLock(List<Str> objStrs) {
		for (Str objStr: objStrs) {
			Str id = getObjectIdFromObjStr(objStr);
			Object object = objects.get(id);
			if (object!=null) {
				expandObjectReferencesNoLock(object,objStr);
			}
		}
	}
	
	protected void expandObjectReferencesNoLock(Object object, Str objStr) {
		List<Str> lines = objStr.split("\n");
		for (int l = 1; l < lines.size(); l++) {
			Str line = lines.get(l);
			line.sb().delete(0, PERSISTABLE_PROPERTY.length());
			List<Str> nameValue = line.split(EQUALS);
			String fieldName = nameValue.get(0).toString();
			Field field = getFieldByName(object, fieldName);
			if (field!=null && isPersistableObjectType(field.getType().toString())) {
				field.setAccessible(true);
				Str value = nameValue.get(1);
				if (isArrayType(field.getType().toString())) {
					Object[] arrayValue = (Object[]) getFieldValue(object, field);
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
						setFieldValue(object, field, reference);
					}
				}
			}
		}
	}
	
	protected Str toStrNoLock() {
		Str r = new Str();
		r.sb().append(NEXT_ID);
		r.sb().append(nextId);
		r.sb().append("\n");
		r.sb().append(START_OBJECTS);
		boolean first = true;
		for (Entry<Str,Object> entry: objects.entrySet()) {
			if (!first) {
				r.sb().append(NEXT_OBJECT);
			}
			Str objStr = getObjectAsStrNoLock(entry.getKey());
			r.sb().append(objStr);
			first = false;
		}
		return r;
	}
	
	protected void fromStrNoLock(Str str) {
		clearNoLock();
		List<Str> headerBody = str.split(START_OBJECTS);
		List<Str> headerLines = headerBody.get(0).split("\n");
		for (Str line: headerLines) {
			if (line.startsWith(NEXT_ID)) {
				try {
					nextId = Long.parseLong(line.sb().substring(NEXT_ID.length()).trim());
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		List<Str> objStrs = headerBody.get(1).split(NEXT_OBJECT);
		for (Str objStr: objStrs) {
			Str id = getObjectIdFromObjStr(objStr);
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
	}

	protected static boolean isArrayType(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		return className.startsWith("[");
	}

	protected static Str getObjectIdFromObjStr(Str objStr) {
		Str r = null;
		if (objStr.startsWith(PERSISTABLE_OBJECT)) {
			List<Str> lines = objStr.split("\n");
			r = lines.get(0);
			r.sb().delete(0, PERSISTABLE_OBJECT.length());
		}
		return r;
	}

	protected static Object getObjectFromObjStr(Str objStr) {
		Object r = null;
		if (objStr.startsWith(PERSISTABLE_OBJECT)) {
			List<Str> lines = objStr.split("\n");
			Str id = lines.get(0);
			id.sb().delete(0, PERSISTABLE_OBJECT.length());
			String className = id.split(ID_CONCATENATOR).get(0).toString();
			Object object = Instantiator.getNewClassInstanceForName(className);
			if (object!=null) {
				for (int l = 1; l < lines.size(); l++) {
					Str line = lines.get(l);
					line.sb().delete(0, PERSISTABLE_PROPERTY.length());
					List<Str> nameValue = line.split(EQUALS);
					String fieldName = nameValue.get(0).toString();
					Field field = getFieldByName(object, fieldName);
					if (field!=null) {
						setObjectFieldValue(object, field, nameValue.get(1));
					}
				}
				r = object;
			}
		}
		return r;
	}
	
	protected static void setObjectFieldValue(Object object, Field field, Str value) {
		field.setAccessible(true);
		Object currentValue = getFieldValue(object, field);
		value.replace(NEWLINE, "\n");
		String className = object.getClass().getName();
		try {
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

					if (field.getType().isAssignableFrom(int[].class) ||
						field.getType().isAssignableFrom(long[].class) ||
						field.getType().isAssignableFrom(float[].class) ||
						field.getType().isAssignableFrom(double[].class) ||
						field.getType().isAssignableFrom(boolean[].class)
						) {
						if (field.getType().isAssignableFrom(int[].class)) {
							int[] valObj = new int[vals.size()];
							int i = 0;
							for (Str val: vals) {
								if (val!=null && !val.toString().equals(NULL)) {
									valObj[i] = Integer.parseInt(val.toString());
								}
								i++;
							}
							valueObject = valObj;
						} else if (field.getType().isAssignableFrom(long[].class)) {
							long[] valObj = new long[vals.size()];
							int i = 0;
							for (Str val: vals) {
								if (val!=null && !val.toString().equals(NULL)) {
									valObj[i] = Long.parseLong(val.toString());
								}
								i++;
							}
							valueObject = valObj;
						} else if (field.getType().isAssignableFrom(float[].class)) {
							float[] valObj = new float[vals.size()];
							int i = 0;
							for (Str val: vals) {
								if (val!=null && !val.toString().equals(NULL)) {
									valObj[i] = Float.parseFloat(val.toString());
								}
								i++;
							}
							valueObject = valObj;
						} else if (field.getType().isAssignableFrom(double[].class)) {
							double[] valObj = new double[vals.size()];
							int i = 0;
							for (Str val: vals) {
								if (val!=null && !val.toString().equals(NULL)) {
									valObj[i] = Double.parseDouble(val.toString());
								}
								i++;
							}
							valueObject = valObj;
						} else if (field.getType().isAssignableFrom(boolean[].class)) {
							boolean[] valObj = new boolean[vals.size()];
							int i = 0;
							for (Str val: vals) {
								if (val!=null && !val.toString().equals(NULL)) {
									valObj[i] = Boolean.parseBoolean(val.toString());
								}
								i++;
							}
							valueObject = valObj;
						}
					} else {
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
					}
				} else if (field.getType().isAssignableFrom(Str.class)) {
					valueObject = value;
				}
			}
			setFieldValue(object, field, valueObject);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	protected static boolean isSupportedValueType(String className) {
		className = Instantiator.getClassName(className);
		boolean r = false;
		if (
			className.equals(int.class.getName()) ||
			className.equals(long.class.getName()) ||
			className.equals(float.class.getName()) ||
			className.equals(double.class.getName()) ||
			className.equals(boolean.class.getName()) ||
			className.equals(Str.class.getName()) ||
			className.equals(StringBuilder.class.getName()) ||
			className.equals(String.class.getName()) ||
			className.equals(Integer.class.getName()) ||
			className.equals(Long.class.getName()) ||
			className.equals(Float.class.getName()) ||
			className.equals(Double.class.getName()) ||
			className.equals(Boolean.class.getName()) ||
			className.equals(BigDecimal.class.getName()) ||
			className.equals(List.class.getName()) ||
			(className.startsWith("[") && className.length()==2)
			) {
			r = true;
		}
		if (!r) {
			r = isPersistableObjectType(className);
		}
		return r;
	}

	protected static boolean isPersistableObjectType(String className) {
		className = Instantiator.getClassName(className);
		return isPersistableObject(Instantiator.getClassForName(className));
	}

	protected static boolean isPersistableObject(Class<?> cls) {
		boolean r = false;
		if (cls!=null) {
			PersistableObject po = cls.getAnnotation(PersistableObject.class);
			if (po!=null) {
				r = true;
			}
		}
		return r;
	}
	
	protected static List<Field> getPersistedFields(Object object) {
		List<Field> r = new ArrayList<Field>();
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.isAnnotationPresent(PersistableProperty.class)) {
					field.setAccessible(true);
					r.add(field);
				}
			}
			cls = cls.getSuperclass();
		}
		return r;
	}
	
	protected static List<Str> parseValuesFromArray(Str arrayStr) {
		List<Str> typeValues = arrayStr.split(LIST_START);
		arrayStr = typeValues.get(1);
		arrayStr.sb().delete(arrayStr.length() - LIST_END.length(), arrayStr.length());
		return arrayStr.split(LIST_CONCATENATOR);
	}
}
