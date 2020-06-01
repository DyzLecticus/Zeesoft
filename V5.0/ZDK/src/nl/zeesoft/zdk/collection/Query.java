package nl.zeesoft.zdk.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Reflector;
import nl.zeesoft.zdk.Str;

@PersistableObject
public class Query {
	@PersistableProperty
	protected List<QueryFilter>		filters		= new ArrayList<QueryFilter>();
	
	public List<Str>				errors		= new ArrayList<Str>();
	public SortedMap<Str,Object>	results		= new TreeMap<Str,Object>();
	
	public Query() {
		
	}
	
	public Query(Class<?> cls) {
		if (cls!=null) {
			addClass(cls.getName());
		}
	}
	
	public Query(String className) {
		if (className!=null && className.length()>0) {
			addClass(className);
		}
	}
	
	public Query copy() {
		Query r = (Query) Instantiator.getNewClassInstanceForName(this.getClass().getName());
		for (QueryFilter filter: filters) {
			r.filters.add(filter.copy());
			copyTo(r);
		}
		return r;
	}
	
	public Query addClass(Class<?> cls) {
		return addClass(cls.getName());
	}
	
	public Query addClass(String className) {
		if (className.length()>0) {
			equals(QueryFilter.CLASS_NAME,className);
		}
		return this;
	}
	
	public Query equals(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,false,QueryFilter.EQUALS,value);
	}
	
	public Query notEquals(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,true,QueryFilter.EQUALS,value);
	}
	
	public Query contains(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,false,QueryFilter.CONTAINS,value);
	}
	
	public Query notContains(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,true,QueryFilter.CONTAINS,value);
	}
	
	public Query lessThan(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,false,QueryFilter.LESS,value);
	}
	
	public Query lessOrEquals(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,true,QueryFilter.GREATER,value);
	}
	
	public Query greaterThan(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,false,QueryFilter.GREATER,value);
	}
	
	public Query greaterOrEquals(String methodOrPropertyName, Object value) {
		return filter(methodOrPropertyName,true,QueryFilter.LESS,value);
	}
	
	public static Query create() {
		return new Query();
	}
	
	public static Query create(Class<?> cls) {
		return new Query(cls);
	}
	
	public Query create(String className) {
		return new Query(className);
	}
	
	public List<String> getClassNames() {
		List<String> r = new ArrayList<String>();
		for (QueryFilter filter: filters) {
			if (filter.methodOrPropertyName.equals(QueryFilter.CLASS_NAME) &&
				filter.value!=null &&
				filter.value instanceof String &&
				((String)filter.value).length()>0
				) {
				r.add((String)filter.value);
			}
		}
		return r;
	}
	
	public List<QueryFilter> getFilters() {
		List<QueryFilter> r = new ArrayList<QueryFilter>();
		for (QueryFilter filter: filters) {
			if (!filter.methodOrPropertyName.equals(QueryFilter.CLASS_NAME)) {
				r.add(filter.copy());
			}
		}
		return r;
	}
	
	protected void copyTo(Query query) {
		for (QueryFilter filter: filters) {
			query.filters.add(filter.copy());
		}
	}
	
	protected Query filter(String methodOrPropertyName, boolean invert, String operator, Object value) {
		if (methodOrPropertyName!=null && methodOrPropertyName.length()>0 &&
			QueryFilter.isValidOperator(operator) &&
			(value!=null || !operator.equals(QueryFilter.EQUALS))
			) {
			QueryFilter filter = new QueryFilter();
			filter.methodOrPropertyName = methodOrPropertyName;
			filter.invert = invert;
			filter.operator = operator;
			filter.value = value;
			filters.add(filter);
		}
		return this;
	}
	
	protected Query applyAllFilters() {
		for (QueryFilter filter: getFilters()) {
			applyFilter(filter);
		}
		return this;
	}
	
	protected Query applyFilter(QueryFilter filter) {
		SortedMap<Str,Object> objects = new TreeMap<Str,Object>(results);
		for (Entry<Str,Object> entry: objects.entrySet()) {
			Object object = entry.getValue();
			boolean apply = true;
			Object value = null;
			if (Reflector.hasMethod(object, filter.methodOrPropertyName)) {
				value = Reflector.invokeMethod(object, filter.methodOrPropertyName);
			} else if (Reflector.hasField(object, filter.methodOrPropertyName)) {
				value = Reflector.getFieldValue(object, filter.methodOrPropertyName);
			} else {
				Str error = new Str();
				error.sb().append("Method or property not found: ");
				error.sb().append(object.getClass().getName());
				error.sb().append(".");
				error.sb().append(filter.methodOrPropertyName);
				logError(error);
				apply = false;
			}
			if (apply) {
				Str id = entry.getKey();
				if (filter.operator.equals(QueryFilter.EQUALS)) {
					applyEqualsFilter(filter,value,id);
				} else if (filter.operator.equals(QueryFilter.CONTAINS)) {
					applyContainsFilter(filter,value,id);
				} else if (filter.operator.equals(QueryFilter.LESS)) {
					applyLessFilter(filter,value,id);
				} else if (filter.operator.equals(QueryFilter.GREATER)) {
					applyGreaterFilter(filter,value,id);
				}
			}
		}
		return this;
	}
	
	protected void applyEqualsFilter(QueryFilter filter, Object value, Str id) {
		boolean equals = (
			filter.value==null && value==null ||
			filter.value!=null && value!=null && (filter==value || filter.value.equals(value))
		);
		if ((!filter.invert && !equals) || (filter.invert && equals)) {
			results.remove(id);
		}
	}
	
	protected void applyContainsFilter(QueryFilter filter, Object value, Str id) {
		if (value==null) {
			if (!filter.invert) {
				results.remove(id);
			}
		} else if (value instanceof StringBuilder && filter.value instanceof StringBuilder) {
			Str vStr = new Str((StringBuilder) value);
			if ((!filter.invert && !vStr.contains(filter.value.toString())) ||
				(filter.invert && vStr.contains(filter.value.toString()))
				) {
				results.remove(id);
			}
		} else if (value instanceof String && filter.value instanceof String) {
			if ((!filter.invert && !((String)value).contains(filter.value.toString())) ||
				(filter.invert && ((String)value).contains(filter.value.toString()))
				) {
				results.remove(id);
			}
		} else if (value instanceof Str && filter.value instanceof Str) {
			if ((!filter.invert && !((Str)value).contains(filter.value.toString())) ||
				(filter.invert && ((Str)value).contains(filter.value.toString()))
				) {
				results.remove(id);
			}
		} else if (value instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> values = (List<Object>) value;
			if ((!filter.invert && !values.contains(filter.value)) ||
				(filter.invert && values.contains(filter.value))
				) {
				results.remove(id);
			}
		} else if (value instanceof Object[]) {
			Object[] values = (Object[]) value;
			boolean contains = false;
			for (int i = 0; i < values.length; i++) {
				if (values[i]!=null && values[i].equals(filter.value)) {
					contains = true;
					break;
				}
			}
			if ((!filter.invert && !contains) ||
				(filter.invert && contains)
				) {
				results.remove(id);
			}
		} else if (
			value instanceof int[] && filter.value instanceof Integer ||
			value instanceof long[] && filter.value instanceof Long ||
			value instanceof float[] && filter.value instanceof Float ||
			value instanceof double[] && filter.value instanceof Double ||
			value instanceof boolean[] && filter.value instanceof Boolean ||
			value instanceof byte[] && filter.value instanceof Byte ||
			value instanceof short[] && filter.value instanceof Short
			) {
			boolean contains = false;
			if (value instanceof int[]) {
				int[] values = (int[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Integer)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof long[]) {
				long[] values = (long[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Long)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof float[]) {
				float[] values = (float[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Float)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof double[]) {
				double[] values = (double[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Double)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof boolean[]) {
				boolean[] values = (boolean[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Boolean)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof byte[]) {
				byte[] values = (byte[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Byte)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			} else if (value instanceof short[]) {
				short[] values = (short[]) value;
				for (int i = 0; i < values.length; i++) {
					if ((Short)filter.value == values[i]) {
						contains = true;
						break;
					}
				}
			}
			if ((!filter.invert && !contains) ||
				(filter.invert && contains)
				) {
				results.remove(id);
			}
		}
	}
	
	protected void applyLessFilter(QueryFilter filter, Object value, Str id) {
		if (value==null) {
			if (!filter.invert) {
				results.remove(id);
			}
		} else if (value instanceof Integer && filter.value instanceof Integer) {
			if ((!filter.invert && !(((Integer)value) < ((Integer)filter.value))) ||
				(filter.invert && (((Integer)value) < ((Integer)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Long && filter.value instanceof Long) {
			if ((!filter.invert && !(((Long)value) < ((Long)filter.value))) ||
				(filter.invert && (((Long)value) < ((Long)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Float && filter.value instanceof Float) {
			if ((!filter.invert && !(((Float)value) < ((Float)filter.value))) ||
				(filter.invert && (((Float)value) < ((Float)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Double && filter.value instanceof Double) {
			if ((!filter.invert && !(((Double)value) < ((Double)filter.value))) ||
				(filter.invert && (((Double)value) < ((Double)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Byte && filter.value instanceof Byte) {
			if ((!filter.invert && !(((Byte)value) < ((Byte)filter.value))) ||
				(filter.invert && (((Byte)value) < ((Byte)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Short && filter.value instanceof Short) {
			if ((!filter.invert && !(((Short)value) < ((Short)filter.value))) ||
				(filter.invert && (((Short)value) < ((Short)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof BigDecimal && filter.value instanceof BigDecimal) {
			if ((!filter.invert && !(((BigDecimal)value).compareTo((BigDecimal)filter.value) < 0)) ||
				(filter.invert && (((BigDecimal)value).compareTo((BigDecimal)filter.value) < 0))
				) {
				results.remove(id);
			}
		}
	}
	
	protected void applyGreaterFilter(QueryFilter filter, Object value, Str id) {
		if (value==null) {
			if (!filter.invert) {
				results.remove(id);
			}
		} else if (value instanceof Integer && filter.value instanceof Integer) {
			if ((!filter.invert && !(((Integer)value) > ((Integer)filter.value))) ||
				(filter.invert && (((Integer)value) > ((Integer)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Long && filter.value instanceof Long) {
			if ((!filter.invert && !(((Long)value) > ((Long)filter.value))) ||
				(filter.invert && (((Long)value) > ((Long)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Float && filter.value instanceof Float) {
			if ((!filter.invert && !(((Float)value) > ((Float)filter.value))) ||
				(filter.invert && (((Float)value) > ((Float)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Double && filter.value instanceof Double) {
			if ((!filter.invert && !(((Double)value) > ((Double)filter.value))) ||
				(filter.invert && (((Double)value) > ((Double)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Byte && filter.value instanceof Byte) {
			if ((!filter.invert && !(((Byte)value) > ((Byte)filter.value))) ||
				(filter.invert && (((Byte)value) > ((Byte)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof Short && filter.value instanceof Short) {
			if ((!filter.invert && !(((Short)value) > ((Short)filter.value))) ||
				(filter.invert && (((Short)value) > ((Short)filter.value)))
				) {
				results.remove(id);
			}
		} else if (value instanceof BigDecimal && filter.value instanceof BigDecimal) {
			if ((!filter.invert && !(((BigDecimal)value).compareTo((BigDecimal)filter.value) > 0)) ||
				(filter.invert && (((BigDecimal)value).compareTo((BigDecimal)filter.value) > 0))
				) {
				results.remove(id);
			}
		}
	}
	
	private void logError(Str error) {
		if (!errors.contains(error)) {
			errors.add(error);
		}
	}
}
