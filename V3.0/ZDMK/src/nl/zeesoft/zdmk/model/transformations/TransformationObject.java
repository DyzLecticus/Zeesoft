package nl.zeesoft.zdmk.model.transformations;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Generic;

/**
 * Abstract transformation object.
 */
public abstract class TransformationObject {
	private List<TransformationParameter> parameters = new ArrayList<TransformationParameter>();

	public TransformationObject() {
		initializeParameters();
	}

	public abstract String getDescription();

	protected abstract void initializeParameters();

	protected final void addParameter(String name, boolean mandatory, String description) {
		TransformationParameter param = getParameter(name);
		if (param==null) {
			param = new TransformationParameter();
			param.setName(name);
			param.setMandatory(mandatory);
			param.setDescription(description);
			parameters.add(param);
		}
	}

	public final TransformationObject getCopy() {
		TransformationObject r = null;
		try {
			r = (TransformationObject) this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (r!=null) {
			for (TransformationParameter param: getParameters()) {
				r.setParameterValue(param.getName(),param.getValue());
			}
		}
		return r;
	}

	public final TransformationParameter getParameter(String name) {
		TransformationParameter r = null;
		for (TransformationParameter param: parameters) {
			if (param.getName().equals(name)) {
				r = param;
				break;
			}
		}
		return r;
	}

	public final String getParameterValue(String name) {
		String r = "";
		TransformationParameter param = getParameter(name);
		if (param!=null) {
			r = param.getValue();
		}
		return r;
	}

	public final void setParameterValue(String name,String value) {
		TransformationParameter param = getParameter(name);
		if (param!=null) {
			param.setValue(value);
		}
	}

	public final String checkParameters() {
		String error = checkMandatoryParameters();
		if (error.length()==0) {
			for (TransformationParameter param: parameters) {
				error = checkParameter(param);
				if (error.length()>0) {
					break;
				}
			}
		}
		return error;
	}

	public final List<TransformationParameter> getParameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		return toStringBuilder().toString();
	}
	
	public StringBuilder toStringBuilder() {
		StringBuilder sb = new StringBuilder();
		String[] split = this.getClass().getName().split("\\."); 
		sb.append(split[split.length - 1]);
		sb.append("(");
		if (parameters.size()>0) {
			boolean first = true;
			for (TransformationParameter param: parameters) {
				if (param.getValue().length()>0) {
					if (!first) {
						sb.append(",");
					}
					first = false;
					sb.append(param.getName());
					sb.append("=");
					sb.append("\"");
					sb.append(param.getValue());
					sb.append("\"");
				}
			}
		}
		sb.append(")");
		return sb;
	}

	public static final TransformationObject fromString(String s) {
		return fromStringBuilder(new StringBuilder(s));
	}

	public static final TransformationObject fromStringBuilder(StringBuilder sb) {
		TransformationObject r = null;
		sb = Generic.stringBuilderTrim(sb);
		if (Generic.stringBuilderEndsWith(sb,")")) {
			sb.replace(sb.length()-1,sb.length(),"");
		}
		sb = Generic.stringBuilderReplace(sb,")","");
		List<StringBuilder> trans = Generic.stringBuilderSplit(sb,"(");
		if (trans.size()>=1 && trans.size()<=2) {
			String className = Generic.stringBuilderTrim(trans.get(0)).toString();
			try {
				Class<?> clas = Class.forName(TransformationObject.class.getPackage().getName() + ".impl." + className);
				r = (TransformationObject) clas.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (r!=null && trans.size()==2) {
				List<StringBuilder> params = Generic.stringBuilderSplit(trans.get(1),",");
				for (StringBuilder nameValue: params) {
					List<StringBuilder> nv = Generic.stringBuilderSplit(nameValue,"=");
					if (nv.size()==2) {
						String name = Generic.stringBuilderTrim(nv.get(0)).toString();
						String value = Generic.stringBuilderTrim(nv.get(1)).toString();
						if (value.startsWith("\"")) {
							value = value.substring(1,value.length());
						}
						if (value.endsWith("\"")) {
							value = value.substring(0,value.length()-1);
						}
						r.setParameterValue(name,value);
					}
				}
			}
		}
		return r;
	}

	protected String checkParameter(TransformationParameter param) {
		// Override to implement
		return "";
	}

	private String checkMandatoryParameters() {
		String error = "";
		for (TransformationParameter param: parameters) {
			if (param.isMandatory() && (param.getValue()==null || param.getValue().equals(""))) {
				error = "Parameter " + param.getName() + " is mandatory";
				break;
			}
		}
		return error;
	}
}
