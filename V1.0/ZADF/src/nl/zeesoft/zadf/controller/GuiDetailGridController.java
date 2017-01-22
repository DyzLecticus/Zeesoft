package nl.zeesoft.zadf.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.SortedMap;

import javax.swing.event.ListSelectionEvent;

import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.panel.PnlDetailGrid;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;

public abstract class GuiDetailGridController extends GuiObjectController implements EvtEventSubscriber {

	private GuiDetailController	detailController 	= null;
	private GuiGridController	gridController 		= null;
	
	private boolean 			showingGrid			= false;
	
	public GuiDetailGridController(PnlDetailGrid detailChildPanel) {
		super(detailChildPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			if ((gridController!=null) && (gridController.getGuiObject().getGuiObjectForSourceComponent((Component) e.getSource())!=null)) {
				gridController.actionPerformed(e);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			if ((gridController!=null) && (gridController.getGuiObject().getGuiObjectForSourceComponent((Component) e.getSource())!=null)) {
				gridController.valueChanged(e);
			}
		}
	}
		
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(GuiGridController.GRID_SELECTION_CHANGED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			GuiGridController gc = (GuiGridController) e.getSource();
			MdlDataObject obj = null;
			SortedMap<Long, String> extendedReferences = null;
			if (gc.getSelectedIdList().size()>0) {
				long id = gc.getSelectedIdList().get(0);
				obj = gc.getResults().getMdlObjectRefById(id).getDataObject();
				extendedReferences = gc.getExtendedReferences();
			}
			if (detailController!=null) {
				detailController.setObject(obj,extendedReferences);
			}
			if (gridController!=null) {
				gridController.handleEvent(e);
			}
		} else if (e.getType().equals(GuiTreeController.TREE_VALUE_CHANGED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			PnlDetailGrid panel = (PnlDetailGrid) getGuiObject();
			GuiTreeController tc = (GuiTreeController) e.getSource();
			if (tc.getSelectedCollection()!=null) {
				if (tc.isShowAsSubTree()) {
					if (detailController!=null) {
						if (showingGrid) {
							panel.showDetail();
							showingGrid = false;
						}
						detailController.setText("");
						detailController.setCollection(tc.getSelectedCollection());
					}
					if (gridController!=null) {
						gridController.setCollection(null);
						gridController.refresh();
					}
				} else {
					if (gridController!=null) {
						if (!showingGrid) {
							panel.showGrid();
							showingGrid = true;
						}
						gridController.handleEvent(e);
					}
				}
			} else if (tc.getSelectedProperty()!=null) {
				if (gridController!=null) {
					if (!showingGrid) {
						panel.showGrid();
						showingGrid = true;
					}
					gridController.handleEvent(e);
				}
			} else if (tc.getSelectedModule()!=null) {
				if (detailController!=null) {
					if (showingGrid) {
						panel.showDetail();
						showingGrid = false;
					}
					detailController.setCollection(null);
					detailController.setText(tc.getSelectedModule().getDescriptionLabel());
				}
				if (gridController!=null) {
					gridController.setCollection(null);
				}
			} else {
				if (detailController!=null) {
					if (showingGrid) {
						panel.showDetail();
						showingGrid = false;
					}
					detailController.setCollection(null);
					if (tc.getSelectedString().equals(GuiConfig.getInstance().getGuiFactory().getApplicationName())) {
						detailController.setText(GuiConfig.getInstance().getGuiFactory().getApplicationDescriptionLabel());
					}
				}
				if (gridController!=null) {
					gridController.setCollection(null);
				}
			}
		}
	}
	
	/**
	 * @return the detailController
	 */
	public GuiDetailController getDetailController() {
		return detailController;
	}

	/**
	 * @param detailController the detailController to set
	 */
	public void setDetailController(GuiDetailController detailController) {
		this.detailController = detailController;
	}

	/**
	 * @return the gridController
	 */
	public GuiGridController getGridController() {
		return gridController;
	}

	/**
	 * @param gridController the gridController to set
	 */
	public void setGridController(GuiGridController gridController) {
		this.gridController = gridController;
	}

}
