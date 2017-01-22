package nl.zeesoft.zadf.controller.impl;

import java.util.List;

import nl.zeesoft.zadf.controller.GuiGridController;
import nl.zeesoft.zadf.gui.panel.PnlGrid;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.event.EvtEvent;

public class PrpIdRefDialogGridController extends GuiGridController {
	public PrpIdRefDialogGridController(PnlGrid gridPanel) {
		super(gridPanel);
		setControlController(new GenericGridControlController(gridPanel.getControlPanel(),false,false));
		setGrid(gridPanel.getGrid());
	}

	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
	}

	@Override
	protected List<DbCollectionProperty> getGridProperties(DbCollection collection) {
		return collection.getSelectProperties();
	}
	
	@Override
	public void refresh() {
		super.refresh();
	}
}
