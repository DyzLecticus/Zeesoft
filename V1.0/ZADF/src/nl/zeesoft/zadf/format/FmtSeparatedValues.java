package nl.zeesoft.zadf.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public abstract class FmtSeparatedValues extends FmtObject {
	private boolean 						header 			= true;
	private String 							separator 		= ";";
	private boolean							stringDateTime	= true;
	
	private SortedMap<String,BigDecimal> 	totals			= new TreeMap<String,BigDecimal>();
	private SortedMap<String,Integer> 		counts 			= new TreeMap<String,Integer>();
	
	@Override
	public StringBuffer format() {
		StringBuffer result = new StringBuffer();

		List<String> addedProps = new ArrayList<String>();
		List<DbCollectionProperty> addedDbProps = new ArrayList<DbCollectionProperty>();

		if (header) {
			for (DbFetch dbFetch: getFetches()) {
				DbCollection col = getCollectionById(dbFetch.getCollection().getValue());
				for (DbCollectionProperty dbProp: getCollectionProperties().get(col.getName().getValue())) {
					if (!addedProps.contains(dbProp.getName().getValue())) {
						if (addedProps.size()>0) {
							result.append(separator);
						}
						result.append(dbProp.getLabel().getValue());
						addedProps.add(dbProp.getName().getValue());
						addedDbProps.add(dbProp);
					}
				}
			}
			result.append("\n");
		}
		
		MdlObjectRefList colResults = getResults().getReferenceListForCollection(QryFetch.MAIN_RESULTS);

		for (MdlObjectRef ref: colResults.getReferences()) {
			DbCollection dbCol = this.getCollectionByName(ref.getClassName().getValue());
			int propNum = 0;
			for (String propName: addedProps) {
				if (propNum>0) {
					result.append(separator);
				}
				DbCollectionProperty dbProp = null;
				for (DbCollectionProperty tstDbProp: getCollectionProperties().get(dbCol.getName().getValue())) {
					if (tstDbProp.getName().getValue().equals(propName)) {
						dbProp = tstDbProp;
						break;
					}
				}
				DtObject valObj = null;
				if (dbProp!=null) {
					valObj = ref.getDataObject().getPropertyValue(dbProp.getName().getValue());
					if (
						(valObj.getValue()!=null)
						&&
						(
							(valObj instanceof DtDecimal) ||
							(valObj instanceof DtFloat) || 
							(valObj instanceof DtInteger) ||
							(valObj instanceof DtLong)
						)
						) {
						if (getTotalizeProperty(dbProp)) {
							BigDecimal value = new BigDecimal(valObj.toString());
							this.addValueToTotal(dbProp.getName().getValue(),value);
						}
					}
				}
				if (valObj!=null) {
					if (dbProp.getReferenceCollection().getValue()>0)  {
						DbCollection refCol = getCollectionById(dbProp.getReferenceCollection().getValue());
						if (refCol!=null) {
							MdlObjectRefList refColResults = null;
							String refColNm = ""; 
							for (DbFetch ftc: getFetches()) {
								if (refCol.getId().getValue().equals(ftc.getCollection().getValue())) {
									refColNm = QryFetch.MAIN_RESULTS;
								}
							}
							if (refColNm.length()==0) {
								refColNm = refCol.getName().getValue();
							}
							refColResults = getResults().getReferenceListForCollection(refColNm);
							if (valObj instanceof DtIdRef) {
								DtIdRef idRef = (DtIdRef) valObj;
								long id = idRef.getValue();
								MdlObjectRef refRef = refColResults.getMdlObjectRefById(id);
								if (refRef!=null) {
									valObj = refRef.getName();
								}
							} else if (valObj instanceof DtIdRefList) {
								DtIdRefList idRefList = (DtIdRefList) valObj;
								DtStringBuffer newValObj = new DtStringBuffer();
								for (long id: idRefList.getValue()) {
									MdlObjectRef refRef = refColResults.getMdlObjectRefById(id);
									if (refRef!=null) {
										if (newValObj.getValue().length()>0) {
											newValObj.getValue().append(", ");
										}
										newValObj.getValue().append(refRef.getName().getValue());
									}
								}
								valObj = newValObj;
							}
						}
					}
					
					valObj = getConvertedValue(valObj);
					StringBuffer value = getSafeValue(valObj.toStringBuffer());
					result.append(value);
				} else {
					result.append("");
				}
				propNum++;
			}
			result.append("\n");
			formatted();
		}
		
		if (header) {
			for (DbCollectionProperty dbProp: addedDbProps) {
				BigDecimal total = getTotal(dbProp.getName().getValue());
				if (total!=null) {
					BigDecimal average = getAverage(dbProp.getName().getValue());
					result.append("Total ");
					result.append(dbProp.getLabel().getValue().toLowerCase());
					result.append(": ");
					result.append(total);
					result.append(" (average: ");
					result.append(average);
					result.append(")\n");
				}
			}
		}
		
		return result;
	}

	protected DtObject getConvertedValue(DtObject valObj) {
		if ((valObj instanceof DtDateTime) && (stringDateTime))   { 
			if (valObj.getValue()!=null) {
				DtStringBuffer newValObj = new DtStringBuffer();
				newValObj.getValue().append(Generic.getDateTimeString(((DtDateTime) valObj).getValue()));
				valObj = newValObj;
			} else {
				valObj = new DtStringBuffer();
			}
		} else if ((valObj instanceof DtIdRef) && (valObj.getValue()!=null))   {
			if (valObj.getValue().equals(0L)) {
				valObj = new DtStringBuffer();
			}
		} else if ((valObj instanceof DtIdRefList) && (valObj.getValue()!=null))   { 
			if (((DtIdRefList) valObj).getValue().size()==0) {
				valObj = new DtStringBuffer();
			}
		}
		return valObj;
	}

	protected StringBuffer getSafeValue(StringBuffer value) {
		value = Generic.stringBufferReplace(value,"\n"," ");
		value = Generic.stringBufferReplace(value,separator," ");
		return value;
	}
	
	protected boolean getTotalizeProperty(DbCollectionProperty dbProp) {
		return false;
	}

	protected void addValueToTotal(String propertyName, BigDecimal value) {
		BigDecimal total = totals.get(propertyName);
		if (total==null) {
			totals.put(propertyName,value);
			counts.put(propertyName, 1);
		} else {
			int count = counts.get(propertyName);
			total = total.add(value);
			count++;
			totals.put(propertyName, total);
			counts.put(propertyName, count);
		}
	}

	protected BigDecimal getTotal(String propertyName) {
		return totals.get(propertyName);
	}

	protected BigDecimal getAverage(String propertyName) {
		BigDecimal avg = new BigDecimal("0.00");
		BigDecimal total = totals.get(propertyName);
		if (total!=null) {
			int count = counts.get(propertyName);
			BigDecimal divisor = new BigDecimal(count);
			divisor = divisor.setScale(total.scale());
			avg = total.divide(divisor,BigDecimal.ROUND_HALF_UP);
		}
		return avg;
	}

	/**
	 * @param header the header to set
	 */
	protected void setHeader(boolean header) {
		this.header = header;
	}

	/**
	 * @param separator the separator to set
	 */
	protected void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @param stringDateTime the stringDateTime to set
	 */
	protected void setStringDateTime(boolean stringDateTime) {
		this.stringDateTime = stringDateTime;
	}
}
