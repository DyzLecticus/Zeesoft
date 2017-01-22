package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javax.swing.event.ListSelectionEvent;

import nl.zeesoft.zadf.DebugLog;
import nl.zeesoft.zadf.DebugLogItem;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiWindowObject;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zadf.gui.property.PrpTextAreaString;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public class DebugFrameController extends GuiWindowController implements EvtEventSubscriber {
	private DebugLogItem 	selectedItem 	= null;
	
	private boolean 		autoSelect		= true; 
	private boolean 		showOnError		= true; 
	
	public DebugFrameController(GuiFrame debugFrame) {
		super(debugFrame);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}
		
		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.MENU_DEBUG_ALWAYS_ON_TOP)) {
			GuiWindowObject window = (GuiWindowObject) getGuiObject();
			window.getWindow().setAlwaysOnTop(!window.getWindow().isAlwaysOnTop());
		} else if (action.equals(ZADFFactory.MENU_DEBUG_AUTO_SELECT)) {
			autoSelect = (!autoSelect);
		} else if (action.equals(ZADFFactory.MENU_DEBUG_SHOW_ON_ERROR)) {
			showOnError = (!showOnError);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		GuiGrid grid = getGrid();
		if (grid!=null) {
			List<Long> selectedIdList = grid.getSelectedRowIds();
			if (selectedIdList.size()>0) {
				DebugLogItem item = DebugLog.getInstance().getDebugLogItemMap(this).get(selectedIdList.get(0));
				if (item!=selectedItem) {
					selectedItem = item;
					refreshSelectedItem();
				}
			}
		}
	}
	
	public void refresh() {
		GuiGrid grid = getGrid();
		
		List<Long> idList = DebugLog.getInstance().getDebugLogItemIdList(this);
		SortedMap<Long,DebugLogItem> itemMap = DebugLog.getInstance().getDebugLogItemMap(this);
		List<Object[]> data = new ArrayList<Object[]>();
		List<Long> showIdList = new ArrayList<Long>();
		for (int i = (idList.size() - 1); i >= 0; i--) {
			long id = idList.get(i);
			
			DebugLogItem item = itemMap.get(id);
			if (selectedItem==null) {
				selectedItem = item;
			}
			Object[] datObj = new Object[1];
			datObj[0] = item.getDesc();
			data.add(datObj);
			showIdList.add(id);
		}
		/*
		final GuiGrid withGrid = grid;
		final List<Object[]> showData = data;
		final List<Long> showDataIdList = showIdList;
		final long selectedId = selectedItem.getId();
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		withGrid.clearData();
	    		withGrid.setData(showDataIdList, showData);
	    		if (selectedId>0) {
		    		withGrid.setSelectedRowId(selectedId);
	    		}
	    		refreshSelectedItem();
	    	}
	    });
		*/

		grid.clearData();
		grid.setData(showIdList, data);
		
		if (selectedItem!=null) {
			grid.setSelectedRowId(selectedItem.getId());
		}
		
		refreshSelectedItem();
	}
	
	private void refreshSelectedItem() {
		GuiWindowObject window = (GuiWindowObject) getGuiObject();
		PrpString prpDesc = (PrpString) window.getGuiObjectByName(ZADFFactory.PROPERTY_DEBUG_DESC);
		PrpCheckBox prpErr = (PrpCheckBox) window.getGuiObjectByName(ZADFFactory.PROPERTY_DEBUG_ERROR);
		PrpTextAreaString prpObj = (PrpTextAreaString) window.getGuiObjectByName(ZADFFactory.PROPERTY_DEBUG_OBJ);
		
		if (selectedItem!=null) {
			prpDesc.getValueObject().setValue(selectedItem.getDesc());
			prpErr.getValueObject().setValue(selectedItem.isError());
			String obj = "";
			if (selectedItem.getObj() instanceof EvtEvent) {
				EvtEvent e = (EvtEvent) selectedItem.getObj();
				obj = e.getSource().getClass().getName() + ":\n" + e.getValue().toString();
			} else if (selectedItem.getObj() instanceof ClRequest) {
				obj = ClRequest.toXml((ClRequest) selectedItem.getObj()).toStringReadFormat().toString(); 
			}
			prpObj.getValueObject().setValue(obj); 
		} else {
			prpDesc.getValueObject().setValue("");
			prpErr.getValueObject().setValue(false);
			prpObj.getValueObject().setValue(""); 
		}
		
		prpDesc.updateComponentValue();
		prpErr.updateComponentValue();
		prpObj.updateComponentValue();
		
		prpObj.getTextArea().setCaretPosition(0);
	}

	private GuiGrid getGrid() {
		GuiWindowObject window = (GuiWindowObject) getGuiObject();
		return (GuiGrid) window.getGuiObjectByName(ZADFFactory.GRID_DEBUG);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DebugLog.ADDED_LOG_ITEM)) {
			DebugLogItem item = (DebugLogItem) e.getValue();
			GuiWindowObject window = (GuiWindowObject) getGuiObject();
			boolean refresh = true;
			if (!window.getWindow().isVisible()) {
				refresh = false;
			}
			if (refresh) {
				if (item.getObj() instanceof EvtEvent) {
					EvtEvent evt = (EvtEvent) item.getObj();
					if (evt.getSource()==this) {
						refresh = false;
					}
				}
			}
			if (refresh) {
				refresh();
			}
			if (autoSelect) {
				selectedItem = null;
			}
		}
	}

	/**
	 * @return the showOnError
	 */
	public boolean isShowOnError() {
		return showOnError;
	}
}
