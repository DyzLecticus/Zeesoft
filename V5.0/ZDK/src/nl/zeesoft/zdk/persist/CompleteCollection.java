package nl.zeesoft.zdk.persist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Reflector;
import nl.zeesoft.zdk.Str;

public class CompleteCollection extends QueryableCollection {
	@Override
	protected Str putNoLock(Str id, Object object) {
		Str r = id;
		if (isSupportedObject(object.getClass())) {
			if (id==null) {
				id = getObjectIdForObjectNoLock(object);
				r = id;
			}
			boolean add = false;
			if (r==null) {
				add = true;
			}
			r = super.putNoLock(id, object);
			if (add) {
				addObjectChildrenNoLock(object);
				addObjectReferencesNoLock(object);
			}
		}
		return r;
	}
	
	protected Str putNoLock(Object object) {
		return putNoLock(null, object);
	}
	
	protected boolean isSupportedObject(Class<?> cls) {
		return isSupportedObject(cls.getName());
	}
	
	protected boolean isSupportedObject(String className) {
		boolean r = true;
		className = Reflector.getClassName(className);
		if ((className.startsWith("[") && !className.equals("[L")) ||
			className.startsWith("java.lang.") ||
			className.startsWith("java.util.") ||
			className.equals(Str.class.getName())
			) {
			r = false;
		}
		return r;
	}
	
	protected boolean isSupportedField(Field field) {
		boolean r = true;
		String className = Reflector.getClassName(field.getType().toString());
		if ((className.startsWith("[") && !className.equals("[L")) ||
			className.startsWith("java.lang.")
			) {
			r = false;
		}
		return r;
	}
	
	protected List<Field> getSupportedFields(Object object) {
		List<Field> r = new ArrayList<Field>();
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (isSupportedField(field)) {
					field.setAccessible(true);
					r.add(field);
				}
			}
			cls = cls.getSuperclass();
		}
		return r;
	}

	protected Str getObjectIdForObjectNoLock(Object object) {
		Str r = null;
		for (Entry<Str,Object> entry: objects.entrySet()) {
			if (entry.getValue()==object) {
				r = entry.getKey();
				break;
			}
		}
		return r;
	}

	protected void addObjectChildrenNoLock(Object object) {
		List<Field> fields = getSupportedFields(object);
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(List.class)) {
				List<?> objects = (List<?>) Reflector.getFieldValue(object, field);
				if (objects!=null) {
					for (Object child: objects) {
						putNoLock(child);
					}
				}
			}
		}
	}
	
	protected void addObjectReferencesNoLock(Object object) {
		List<Field> fields = getSupportedFields(object);
		for (Field field : fields) {
			if (isSupportedObject(field.getType().toString())) {
				if (isArrayType(field.getType().toString())) {
					Object[] arrayValue = (Object[]) Reflector.getFieldValue(object, field);
					if (arrayValue!=null) {
						for (int i = 0; i < arrayValue.length; i++) {
							if (arrayValue[i]!=null) {
								putNoLock(arrayValue[i]);
							}
						}
					}
				} else {
					Object reference = Reflector.getFieldValue(object, field);
					if (reference!=null) {
						putNoLock(reference);
					}
				}
			}
		}
	}

	protected static boolean isArrayType(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		return className.startsWith("[");
	}
}
