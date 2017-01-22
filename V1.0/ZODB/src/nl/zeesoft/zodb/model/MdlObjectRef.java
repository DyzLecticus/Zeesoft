package nl.zeesoft.zodb.model;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;


public class MdlObjectRef extends MdlObject {
	private MdlDataObject 	dataObject	= null;
	private int				fileNum		= -1;

	protected MdlObjectRef() {
		
	}

	public MdlObjectRef(MdlDataObject dataObject) {
		setDataObject(dataObject);			
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

	public StringBuffer toStringBuffer() {
		return toStringBuffer(false);
	}
	
	public StringBuffer toStringBuffer(boolean mini) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClassName().getValue());
		sb.append(Generic.SEP_STR);
		sb.append(getId().getValue());
		sb.append(Generic.SEP_STR);
		sb.append(getName().getValue());
		if (!mini) {
			sb.append(Generic.SEP_STR);
			sb.append(getCreatedOn());
			sb.append(Generic.SEP_STR);
			sb.append(getCreatedBy());
			sb.append(Generic.SEP_STR);
			sb.append(getChangedOn());
			sb.append(Generic.SEP_STR);
			sb.append(getChangedBy());
		}
		return sb;
	}

	public static MdlObjectRef parseMdlObjectRef(String s) {
		MdlObjectRef ref = null;
		if ((s!=null) && (s.contains(Generic.SEP_STR))) {
			ref = new MdlObjectRef();
			String[] val = Generic.getValuesFromString(s);
			if (val.length>=3) {
				ref.getClassName().fromString(new StringBuffer(val[0])); 
				ref.getId().fromString(new StringBuffer(val[1])); 
				ref.getName().fromString(new StringBuffer(val[2]));
			}
			if (val.length>=7) {
				ref.getCreatedOn().fromString(new StringBuffer(val[3]));
				ref.getCreatedBy().fromString(new StringBuffer(val[4]));
				ref.getChangedOn().fromString(new StringBuffer(val[5]));
				ref.getChangedBy().fromString(new StringBuffer(val[6]));
			}
		}
		return ref;
	}
	
	public static MdlObjectRef copy(MdlObjectRef original) {
		MdlObjectRef copy = new MdlObjectRef();
		copy.getClassName().setValue(DtObject.copy(original.getClassName()).getValue());
		copy.getId().setValue(DtObject.copy(original.getId()).getValue());
		copy.getName().setValue(DtObject.copy(original.getName()).getValue());
		copy.getCreatedBy().setValue(DtObject.copy(original.getCreatedBy()).getValue());
		copy.getCreatedOn().setValue(DtObject.copy(original.getCreatedOn()).getValue());
		copy.getChangedBy().setValue(DtObject.copy(original.getChangedBy()).getValue());
		copy.getChangedOn().setValue(DtObject.copy(original.getChangedOn()).getValue());
		copy.setDataObject(original.getDataObject());
		return copy;
	}
	
	private void updateReferenceObject() {
		if (
			(dataObject!=null) &&
			(
				(!dataObject.getClassName().getValue().equals(super.getClassName().getValue())) ||
				(!dataObject.getId().getValue().equals(super.getId().getValue())) ||
				(!dataObject.getName().getValue().equals(super.getName().getValue())) ||
				(!dataObject.getCreatedOn().getValue().equals(super.getCreatedOn().getValue())) ||
				(!dataObject.getCreatedBy().getValue().equals(super.getCreatedBy().getValue())) ||
				(!dataObject.getChangedOn().getValue().equals(super.getChangedOn().getValue())) ||
				(!dataObject.getChangedBy().getValue().equals(super.getChangedBy().getValue()))
			)
			) {
			super.getClassName().setValue(dataObject.getClassName().getValue());
			super.getId().setValue(dataObject.getId().getValue());
			super.getName().setValue(dataObject.getName().getValue());
			super.getCreatedOn().setValue(dataObject.getCreatedOn().getValue());
			super.getCreatedBy().setValue(dataObject.getCreatedBy().getValue());
			super.getChangedOn().setValue(dataObject.getChangedOn().getValue());
			super.getChangedBy().setValue(dataObject.getChangedBy().getValue());
		}
	}
	
	@Override
	public DtString getClassName() {
		updateReferenceObject();
		return super.getClassName();
	}

	@Override
	public DtLong getId() {
		updateReferenceObject();
		return super.getId();
	}

	@Override
	public DtString getName() {
		updateReferenceObject();
		return super.getName();
	}

	@Override
	public DtIdRef getCreatedBy() {
		updateReferenceObject();
		return super.getCreatedBy();
	}

	@Override
	public DtIdRef getChangedBy() {
		updateReferenceObject();
		return super.getChangedBy();
	}

	@Override
	public DtDateTime getCreatedOn() {
		updateReferenceObject();
		return super.getCreatedOn();
	}

	@Override
	public DtDateTime getChangedOn() {
		updateReferenceObject();
		return super.getChangedOn();
	}

	/**
	 * @return the dataObject
	 */
	public MdlDataObject getDataObject() {
		return dataObject;
	}

	/**
	 * @param dataObject the dataObject to set
	 */
	public void setDataObject(MdlDataObject dataObject) {
		this.dataObject = dataObject;
		updateReferenceObject();
	}
	
	/**
	 * @return the fileNum
	 */
	public int getFileNum() {
		return fileNum;
	}

	/**
	 * @param fileNum the fileNum to set
	 */
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

}
