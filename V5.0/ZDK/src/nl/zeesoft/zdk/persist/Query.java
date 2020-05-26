package nl.zeesoft.zdk.persist;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

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
		Query r = new Query();
		for (QueryFilter filter: filters) {
			r.filters.add(filter.copy());
		}
		return r;
	}
	
	public Query addClass(Class<?> cls) {
		return addClass(cls.getName());
	}
	
	public Query addClass(String className) {
		if (className.length()>0) {
			filter(QueryFilter.CLASS_NAME,className);
		}
		return this;
	}
	
	public Query filter(String propertyName, Object value) {
		return filter(propertyName,false,value);
	}
	
	public Query filter(String propertyName, boolean invert, Object value) {
		return filter(propertyName,invert,QueryFilter.EQUALS,value);
	}
	
	public Query filter(String propertyName, String operator, Object value) {
		return filter(propertyName,false,operator,value);
	}
	
	public Query filter(String propertyName, boolean invert, String operator, Object value) {
		if (propertyName!=null && propertyName.length()>0 &&
			QueryFilter.isValidOperator(operator) &&
			(value!=null || !operator.equals(QueryFilter.EQUALS))
			) {
			QueryFilter filter = new QueryFilter();
			filter.propertyName = propertyName;
			filter.invert = invert;
			filter.operator = operator;
			filter.value = value;
			filters.add(filter);
		}
		return this;
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
	
	protected Query applyAllFilters() {
		for (QueryFilter filter: filters) {
			if (!filter.propertyName.equals(QueryFilter.CLASS_NAME)) {
				applyFilter(filter);
			}
		}
		return this;
	}
	
	protected Query applyFilter(QueryFilter filter) {
		SortedMap<Str,Object> objects = new TreeMap<Str,Object>(results);
		for (Entry<Str,Object> entry: objects.entrySet()) {
			Object object = entry.getValue();
			Str id = entry.getKey();
			Field field = QueryableCollection.getFieldByName(object, filter.propertyName);
			if (field==null) {
				Str error = new Str();
				error.sb().append("Property not found: ");
				error.sb().append(object.getClass().getName());
				error.sb().append(".");
				error.sb().append(filter.propertyName);
				if (!errors.contains(error)) {
					errors.add(error);
				}
			} else {
				Object value = QueryableCollection.getFieldValue(object, field);
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
			filter.value!=null && value!=null && filter.value.equals(value)
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
			value instanceof boolean[] && filter.value instanceof Boolean
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
		} else if (value instanceof BigDecimal && filter.value instanceof BigDecimal) {
			if ((!filter.invert && !(((BigDecimal)value).compareTo((BigDecimal)filter.value) > 0)) ||
				(filter.invert && (((BigDecimal)value).compareTo((BigDecimal)filter.value) > 0))
				) {
				results.remove(id);
			}
		}
	}
}
