package nl.zeesoft.zadf.controller.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.controller.GuiGridController;
import nl.zeesoft.zadf.controller.GuiTreeController;
import nl.zeesoft.zadf.gui.panel.PnlGrid;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

public class MainFrameGridController extends GuiGridController {
	private DbCollectionProperty 	filterProperty 		= null;
	private MdlDataObject			parentObject		= null;
	private boolean					child				= false;
	private GuiModelContextFilter 	filterValue			= null;
	
	public MainFrameGridController(PnlGrid gridPanel,boolean child) {
		super(gridPanel);
		this.child = child;
		setControlController(new GenericGridControlController(gridPanel.getControlPanel(),true,true));
		setGrid(gridPanel.getGrid());
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(GuiGridController.GRID_SELECTION_CHANGED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			GuiGridController gc = (GuiGridController) e.getSource();
			MdlDataObject parentObject = null;
			if (gc.getSelectedIdList().size()>0) {
				long id = gc.getSelectedIdList().get(0);
				parentObject = gc.getResults().getMdlObjectRefById(id).getDataObject();
			}
			setParentObject(parentObject);
		} else if (e.getType().equals(GuiTreeController.TREE_VALUE_CHANGED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			GuiTreeController tc = (GuiTreeController) e.getSource();
			boolean refresh = (!child) || (parentObject!=null);
			setFilterProperty(tc.getSelectedProperty(),refresh);
			if (getFilterProperty()==null) {
				if (tc.getSelectedCollection()!=null) {
					setContext(tc.getSelectedCollection().getName().getValue());
				}
				setCollection(tc.getSelectedCollection());
				refresh();
			}
		} else {
			super.handleEvent(e);
		}
	}

	@Override
	public void delayedRefreshData() {
		super.delayedRefreshData();
	}

	@Override
	protected void addDefaultFilters(DbCollection collection) {
		removeFilterValue();
		addFilterValue();
	}

	private void removeFilterValue() {
		if ((filterProperty!=null) && (filterValue!=null)) {
			List<GuiModelContextFilter> filters = new ArrayList<GuiModelContextFilter>(getFilters());
			for (GuiModelContextFilter filter: filters) {
				if (filter == filterValue) {
					removeFilter(filter);
				}
			}
		}
	}

	private void addFilterValue() {
		if (filterProperty!=null) {
			DtIdRef value = new DtIdRef();
			String stringValue = "";
			if (parentObject!=null) {
				value.setValue(parentObject);
				stringValue = parentObject.getName().getValue();
			} else {
				value.setValue(0);
			}
			filterValue = new GuiModelContextFilter(true,filterProperty.getName().getValue(),false,QryFetchCondition.OPERATOR_CONTAINS,value,stringValue);
			addFilter(filterValue);
		}
	}

	/**
	 * @return the filterProperty
	 */
	public DbCollectionProperty getFilterProperty() {
		return filterProperty;
	}

	/**
	 * @param filterProperty the filterProperty to set
	 */
	public void setFilterProperty(DbCollectionProperty filterProperty,boolean refreshData) {
		removeFilterValue();
		this.filterProperty = filterProperty;
		if (filterProperty!=null) {
			setContext(filterProperty.getColl().getName().getValue() + Generic.SEP_STR + filterProperty.getName().getValue());
			setCollection(filterProperty.getColl());
			if (refreshData) {
				refresh();
			} else {
				refreshProperties();
			}
		}
	}

	/**
	 * @return the parentObject
	 */
	public MdlDataObject getParentObject() {
		return parentObject;
	}

	/**
	 * @param parentObject the parentObject to set
	 */
	public void setParentObject(MdlDataObject parentObject) {
		this.parentObject = parentObject;
		removeFilterValue();
		addFilterValue();
		if ((getCollection()!=null) && (parentObject!=null)) {
			delayedRefreshData();
		} else {
			getControlController().setResults("","");
			getGrid().clearData();
		}
	}
	
}
