package nl.zeesoft.zadf.model.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Module",nameMulti="Modules")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_OFF,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Modules provide the GUI with module labels and descriptions.\n" +
	"They are also used to group collections together.\n" +
	"")
public class DbModule extends MdlDataObject {
	private DtString 					label						= new DtString();
	private DtStringBuffer				description					= new DtStringBuffer();
	
	public String getDescriptionLabel() {
		return
			"<b>" + getLabel().getValue() + "</b>\n" +
			 getDescription().getValue().toString();				
	}

	public String getToolTipText() {
		return "<html>" + getDescription().getValue().toString().replaceAll("\n","<br/>") + "<html>";
	}

	/**
	 * @return the label
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property = "label",label="Label")
	public DtString getLabel() {
		return label;
	}

	/**
	 * @return the description
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=2048)
	@ConstrainPropertyMandatory
	@PersistProperty(property = "description",label="Description")
	public DtStringBuffer getDescription() {
		return description;
	}

	/**
	 * @return the collections
	 */
	public List<DbCollection> getCollections() {
		List<DbCollection> list = new ArrayList<DbCollection>();
		String childCollectionAndPropertyName = DbCollection.class.getName() + Generic.SEP_STR + "module";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbCollection) obj);
		}
		return list;
	}

}
