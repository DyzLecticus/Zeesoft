package nl.zeesoft.zadf.controller.impl;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiLabel;
import nl.zeesoft.zadf.gui.GuiPanel;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.GuiSplitPanel;
import nl.zeesoft.zadf.gui.GuiTree;
import nl.zeesoft.zadf.gui.GuiWindow;
import nl.zeesoft.zadf.gui.panel.PnlBusy;
import nl.zeesoft.zadf.gui.panel.PnlDetail;
import nl.zeesoft.zadf.gui.panel.PnlDetailGrid;
import nl.zeesoft.zadf.gui.panel.PnlGrid;
import nl.zeesoft.zadf.gui.panel.PnlReportFilters;
import nl.zeesoft.zadf.gui.panel.PnlZoomTree;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpComboBox;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpInteger;
import nl.zeesoft.zadf.gui.property.PrpPassword;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zadf.gui.property.PrpTextAreaString;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

/**
 * This class is designed to be extended for specific client implementations
 */
public class ZADFFactory {
	private final static String		APPLICATION_NAME					= "ZADF";
	private final static String		APPLICATION_LABEL					= "Zeesoft Application Development Framework";
	private final static String		APPLICATION_VERSION					= "1.1";
	private final static String		APPLICATION_DESCRIPTION				= 
		"The Zeesoft Application Development Framework provides fast, secure and reliable application development and management.\n" +
		"";

	public static final String[]	MONTHS								= {"January","February","March","April","May","June","July","August","September","October","November","December"};
	public static final String[]	DAYS_SHORT							= {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

	public static final String 		FRAME_MAIN 							= "FRAME_MAIN";
	public static final String 		PANEL_MAIN_SPLIT					= "PANEL_MAIN_SPLIT";
	public static final String 		PANEL_MAIN_SPLIT_TOP				= "PANEL_MAIN_SPLIT_TOP";
	public static final String 		PANEL_MAIN_SPLIT_TOP_RIGHT			= "PANEL_MAIN_SPLIT_TOP_RIGHT";
	public static final String 		PANEL_MAIN_SPLIT_TOP_RIGHT_TOP		= "PANEL_MAIN_SPLIT_TOP_RIGHT_TOP";
	public static final String 		PANEL_MAIN_SPLIT_BOTTOM				= "PANEL_MAIN_SPLIT_BOTTOM";
	public static final String 		TREE_MAIN_PARENT					= "TREE_MAIN_PARENT";
	public static final String 		TREE_MAIN_CHILD						= "TREE_MAIN_CHILD";
	public static final String		PANEL_MAIN_PARENT_DETAIL_GRID		= "PANEL_MAIN_PARENT_DETAIL_GRID";
	public static final String		PANEL_MAIN_PARENT_DETAIL			= "PANEL_MAIN_PARENT_DETAIL";
	public static final String		GRID_MAIN_PARENT					= "GRID_MAIN_PARENT";
	public static final String		PANEL_MAIN_CHILD_DETAIL_GRID		= "PANEL_MAIN_CHILD_DETAIL_GRID";
	public static final String		PANEL_MAIN_CHILD_DETAIL				= "PANEL_MAIN_CHILD_DETAIL";
	public static final String		GRID_MAIN_CHILD						= "GRID_MAIN_CHILD";
	public static final String		PANEL_MAIN_ZOOM_TREE				= "PANEL_MAIN_ZOOM_GRID";

	public static final String		MENU_MAIN_SHOW_PREFIX				= "MENU_MAIN_SHOW_";
	public static final String		MENU_MAIN_SHOW_CONFIGURATION		= MENU_MAIN_SHOW_PREFIX + "CONFIGURATION";
	public static final String		MENU_MAIN_SHOW_MODEL				= MENU_MAIN_SHOW_PREFIX + "MODEL";
	public static final String		MENU_MAIN_SHOW_REPORTER				= MENU_MAIN_SHOW_PREFIX + "REPORTER";
	public static final String		MENU_MAIN_SHOW_DEBUGGER				= MENU_MAIN_SHOW_PREFIX + "DEBUGGER";
	
	public static final String 		DIALOG_LOGIN 						= "DIALOG_LOGIN";
	public static final String 		PANEL_LOGIN 						= "PANEL_LOGIN";
	public static final String 		LABEL_LOGIN_NAME 					= "LABEL_LOGIN_NAME";
	public static final String 		LABEL_LOGIN_PASSWORD				= "LABEL_LOGIN_PASSWORD";
	public static final String 		PROPERTY_LOGIN_NAME 				= "PROPERTY_LOGIN_NAME";
	public static final String 		PROPERTY_LOGIN_PASSWORD				= "PROPERTY_LOGIN_PASSWORD";
	public static final String 		BUTTON_LOGIN						= "BUTTON_LOGIN";

	public static final String	 	FRAME_CONTROL 						= "FRAME_CONTROL";
	public static final String 		PANEL_CONTROL 						= "PANEL_CONTROL";
	public static final String 		LABEL_CONTROL_SERVER				= "LABEL_CONTROL_SERVER";
	public static final String 		LABEL_CONTROL_BATCH					= "LABEL_CONTROL_BATCH";
	public static final String	 	LABEL_CONTROL_DEBUG					= "LABEL_CONTROL_DEBUG";
	public static final String	 	LABEL_CONTROL_ENCRYPT				= "LABEL_CONTROL_ENCRYPT";
	public static final String 		LABEL_CONTROL_SERVER_IS_WORKING		= "LABEL_CONTROL_SERVER_IS_WORKING";
	public static final String 		LABEL_CONTROL_BATCH_IS_WORKING		= "LABEL_CONTROL_BATCH_IS_WORKING";
	public static final String 		LABEL_CONTROL_SERVER_CACHE			= "LABEL_CONTROL_SERVER_CACHE";
	public static final String 		BUTTON_CONTROL_SERVER_IS_WORKING	= "BUTTON_CONTROL_SERVER_IS_WORKING";
	public static final String 		BUTTON_CONTROL_STOP_SERVER			= "BUTTON_CONTROL_STOP_SERVER";
	public static final String 		BUTTON_CONTROL_START_SERVER			= "BUTTON_CONTROL_START_SERVER";
	public static final String 		BUTTON_CONTROL_BATCH_IS_WORKING		= "BUTTON_CONTROL_BATCH_IS_WORKING";
	public static final String 		BUTTON_CONTROL_START_BATCH			= "BUTTON_CONTROL_START_BATCH";
	public static final String 		BUTTON_CONTROL_STOP_BATCH			= "BUTTON_CONTROL_STOP_BATCH";
	public static final String 		BUTTON_CONTROL_SERVER_CACHE			= "BUTTON_CONTROL_SERVER_CACHE";
	public static final String 		BUTTON_CONTROL_CACHE_CLEAR			= "BUTTON_CONTROL_CACHE_CLEAR";
	public static final String		MENU_CONTROL_SHOW_CONFIGURATION		= "MENU_CONTROL_SHOW_CONFIGURATION";

	public static final String		DIALOG_CONTROL_CONFIGURATION		= "DIALOG_CONTROL_CONFIGURATION";
	public static final String		PANEL_CONTROL_CONFIGURATION			= "PANEL_CONTROL_CONFIGURATION";
	public static final String		BUTTON_CONTROL_CONFIGURATION_SET	= "BUTTON_CONTROL_CONFIGURATION_SET";

	public static final String		DIALOG_CLIENT_CONFIGURATION			= "DIALOG_CLIENT_CONFIGURATION";
	public static final String		PANEL_CLIENT_CONFIGURATION			= "PANEL_CLIENT_CONFIGURATION";
	public static final String		BUTTON_CLIENT_CONFIGURATION_SET		= "BUTTON_CLIENT_CONFIGURATION_SET";
	
	public static final String 		WINDOW_BUSY 						= "WINDOW_BUSY";
	public static final String 		PANEL_BUSY 							= "PANEL_BUSY";

	public static final String		DIALOG_ID_REF						= "DIALOG_ID_REF";
	public static final String		PANEL_ID_REF						= "PANEL_ID_REF";
	public static final String		PANEL_ID_REF_SELECT					= "PANEL_ID_REF_SELECT";
	public static final String		PANEL_ID_REF_BUTTONS				= "PANEL_ID_REF_BUTTONS";
	public static final String		BUTTON_ID_REF_ADD					= "BUTTON_ID_REF_ADD";
	public static final String		BUTTON_ID_REF_REMOVE				= "BUTTON_ID_REF_REMOVE";
	public static final String		BUTTON_ID_REF_SET					= "BUTTON_ID_REF_SET";
	public static final String		GRID_ID_REF_VALUE					= "GRID_ID_REF_VALUE";
	public static final String		GRID_ID_REF_COLLECTION				= "GRID_ID_REF_COLLECTION";

	public static final String		DIALOG_DATETIME						= "DIALOG_DATETIME";
	public static final String		PANEL_DATETIME						= "PANEL_DATETIME";
	public static final String		BUTTON_DATETIME_NEXT_YEAR			= "BUTTON_DATETIME_NEXT_YEAR";
	public static final String		PROPERTY_DATETIME_YEAR				= "PROPERTY_DATETIME_YEAR";
	public static final String		BUTTON_DATETIME_PREV_YEAR			= "BUTTON_DATETIME_PREV_YEAR";
	public static final String		BUTTON_DATETIME_NEXT_MONTH			= "BUTTON_DATETIME_NEXT_MONTH";
	public static final String		PROPERTY_DATETIME_MONTH				= "PROPERTY_DATETIME_MONTH";
	public static final String		BUTTON_DATETIME_PREV_MONTH			= "BUTTON_DATETIME_PREV_MONTH";
	public static final String		BUTTON_DATETIME_DATE_PREFIX			= "BUTTON_DATETIME_DATE_PREFIX";
	public static final String		PROPERTY_DATETIME_HOUR				= "PROPERTY_DATETIME_HOUR";
	public static final String		PROPERTY_DATETIME_MINUTE			= "PROPERTY_DATETIME_MINUTE";
	public static final String		PROPERTY_DATETIME_SECOND			= "PROPERTY_DATETIME_SECOND";
	public static final String		BUTTON_DATETIME_SET					= "BUTTON_DATETIME_SET";
	public static final String		BUTTON_DATETIME_CLEAR				= "BUTTON_DATETIME_CLEAR";
	public static final String		BUTTON_DATETIME_TODAY				= "BUTTON_DATETIME_TODAY";

	public static final String		DIALOG_REPORT						= "DIALOG_REPORT";
	public static final String		PANEL_REPORT						= "PANEL_REPORT";
	public static final String		LABEL_REPORT						= "LABEL_REPORT";
	public static final String		PANEL_REPORT_FILTERS				= "PANEL_REPORT_FILTERS";
	public static final String		BUTTON_REPORT_FETCH					= "BUTTON_REPORT_FETCH";
	public static final String		LABEL_FETCH_STATUS					= "LABEL_FETCH_STATUS";
	
	public static final String 		FRAME_DEBUG 						= "FRAME_DEBUG";
	public static final String 		PANEL_DEBUG_SPLIT					= "PANEL_DEBUG_SPLIT";
	public static final String 		PANEL_DEBUG_DETAIL					= "PANEL_DEBUG_DETAIL";
	public static final String 		LABEL_DEBUG_DESC					= "LABEL_DEBUG_DESC";
	public static final String 		LABEL_DEBUG_ERROR					= "LABEL_DEBUG_ERROR";
	public static final String 		LABEL_DEBUG_OBJ						= "LABEL_DEBUG_OBJ";
	public static final String 		PROPERTY_DEBUG_DESC					= "PROPERTY_DEBUG_DESC";
	public static final String 		PROPERTY_DEBUG_ERROR				= "PROPERTY_DEBUG_ERROR";
	public static final String 		PROPERTY_DEBUG_OBJ					= "PROPERTY_DEBUG_OBJ";
	public static final String		GRID_DEBUG							= "GRID_DEBUG";
	public static final String		MENU_DEBUG_ALWAYS_ON_TOP			= "MENU_DEBUG_ALWAYS_ON_TOP";
	public static final String		MENU_DEBUG_AUTO_SELECT				= "MENU_DEBUG_AUTO_SELECT";
	public static final String		MENU_DEBUG_SHOW_ON_ERROR			= "MENU_DEBUG_SHOW_ON_ERROR";

	public static final String 		FRAME_MODEL 						= "FRAME_MODEL";
	public static final String 		TREE_MODEL 							= "TREE_MODEL";

	public static final String 		DIALOG_DETAIL 						= "DIALOG_DETAIL";
	public static final String 		PANEL_DETAIL 						= "PANEL_DETAIL";
	public static final String 		PANEL_DETAIL_PROPERTIES				= "PANEL_DETAIL_PROPERTIES";
	public static final String 		BUTTON_DETAIL_SAVE 					= "BUTTON_DETAIL_SAVE";
	public static final String		MENU_MODEL_SHOW_ENTITY_MODEL		= "MENU_MODEL_SHOW_ENTITY_MODEL";

	public static final String		DIALOG_INFO							= "DIALOG_INFO";
	public static final String		PANEL_INFO							= "PANEL_INFO";
	public static final String		PROPERTY_INFO_TEXT					= "PROPERTY_INFO_TEXT";
	public static final String		BUTTON_INFO_OK						= "BUTTON_INFO_OK";

	public static final String		DIALOG_ERROR						= "DIALOG_ERROR";
	
	/**
	 * Override to implement
	 * 
	 * @return The abbreviated name of the application
	 */
	public String getApplicationName() {
		return APPLICATION_NAME;
	}

	/**
	 * Override to implement
	 * 
	 * @return The label of the application
	 */
	public String getApplicationLabel() {
		return APPLICATION_LABEL;
	}

	/**
	 * Override to implement
	 * 
	 * @return The version of the application
	 */
	public String getApplicationVersion() {
		return APPLICATION_VERSION;
	}

	/**
	 * Override to implement
	 * 
	 * @return The description of the application
	 */
	public String getApplicationDescription() {
		return APPLICATION_DESCRIPTION;
	}

	public String getApplicationDescriptionLabel() {
		return 
			"<b>" + getApplicationLabel() + "</b>\n" +
			"Version: " + getApplicationVersion() + "\n" +
			"Model CRC: " + DbConfig.getInstance().getModel().getCrc() + "\n" +
			"\n" +
			getApplicationDescription() +
			"";
	}

	public String getToolTipText() {
		return "<html>" + getApplicationDescriptionLabel().replaceAll("\n","<br/>") + "<html>";
	}
	
	public MainFrameController getNewMainFrameController() {
		MainFrameController c = new MainFrameController(getNewMainFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public LoginDialogController getNewLoginDialogController() {
		LoginDialogController c = new LoginDialogController(getNewLoginDialog());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public ControlFrameController getNewControlFrameController() {
		ControlFrameController c = new ControlFrameController(getNewControlFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public ControlConfigurationDialogController getNewControlConfigurationDialogController() {
		ControlConfigurationDialogController c = new ControlConfigurationDialogController(getNewControlConfigurationFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public ClientConfigurationDialogController getNewClientConfigurationDialogController() {
		ClientConfigurationDialogController c = new ClientConfigurationDialogController(getNewClientConfigurationFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public BusyWindowController getNewBusyWindowController() {
		BusyWindowController c = new BusyWindowController(getNewBusyWindow());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public PrpIdRefDialogController getNewPrpIdRefDialogController(String unique) {
		PrpIdRefDialogController c = new PrpIdRefDialogController(getNewPrpIdRefDialog(unique),unique);
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public PrpDateTimeDialogController getNewPrpDateTimeDialogController() {
		PrpDateTimeDialogController c = new PrpDateTimeDialogController(getNewPrpDateTimeDialog());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public ReportDialogController getNewReportDialogController() {
		ReportDialogController c = new ReportDialogController(getNewReportDialog());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public DebugFrameController getNewDebugFrameController() {
		DebugFrameController c = new DebugFrameController(getNewDebugFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public ModelFrameController getNewModelFrameController() {
		ModelFrameController c = new ModelFrameController(getNewModelFrame());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public DetailDialogController getNewDetailDialogController() {
		DetailDialogController c = new DetailDialogController(getNewDetailDialog());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	public InfoDialogController getNewInfoDialogController() {
		InfoDialogController c = new InfoDialogController(getNewInfoDialog());
		GuiController.getInstance().addGuiObjectController(c);
		return c;
	}

	protected GuiFrame getNewMainFrame() {
		GuiFrame frame = new GuiFrame(FRAME_MAIN);
		
		GuiSplitPanel mainSplitPanel = new GuiSplitPanel(PANEL_MAIN_SPLIT, 0, 0, JSplitPane.VERTICAL_SPLIT);
		GuiSplitPanel topSplitPanel = new GuiSplitPanel(PANEL_MAIN_SPLIT_TOP, 0, 0, JSplitPane.HORIZONTAL_SPLIT);
		GuiSplitPanel bottomSplitPanel = new GuiSplitPanel(PANEL_MAIN_SPLIT_BOTTOM, 1, 0, JSplitPane.HORIZONTAL_SPLIT);
		
		mainSplitPanel.setFill(GridBagConstraints.BOTH);
		mainSplitPanel.getPanel().setResizeWeight(GuiConfig.getInstance().getMainFrameResizeWeight());
		
		topSplitPanel.setFill(GridBagConstraints.BOTH);
		bottomSplitPanel.setFill(GridBagConstraints.BOTH);
		
		frame.getPanelObjects().add(mainSplitPanel);
		mainSplitPanel.getPanelObjects().add(topSplitPanel);
		mainSplitPanel.getPanelObjects().add(bottomSplitPanel);

		final JSplitPane main = mainSplitPanel.getPanel();
		PropertyChangeListener mainChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent changeEvent) {
				JSplitPane sp = (JSplitPane) changeEvent.getSource();
		    	String propertyName = changeEvent.getPropertyName();
		    	if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
		    		if (sp==main) {
		    			Double resizeWeight = (sp.getDividerLocation() * 1.0D) / (main.getHeight() * 1.0D); 
		    			GuiConfig.getInstance().setMainFrameResizeWeight(resizeWeight);
		    		}
		    	}
			}
		};
		main.addPropertyChangeListener(mainChangeListener);
		
		final JSplitPane top = topSplitPanel.getPanel();
		final JSplitPane bottom = bottomSplitPanel.getPanel();
		PropertyChangeListener topBottomChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent changeEvent) {
				JSplitPane sp = (JSplitPane) changeEvent.getSource();
		    	String propertyName = changeEvent.getPropertyName();
		    	if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
		    		int pos = sp.getDividerLocation();
		    		if (sp==top) {
		    			bottom.setDividerLocation(pos);
		    		} else {
		    			top.setDividerLocation(pos);
		    		}
		    	}
			}
		};
		top.addPropertyChangeListener(topBottomChangeListener);
		bottom.addPropertyChangeListener(topBottomChangeListener);
		
		// TOP LEFT
		GuiTree tree = getNewMainFrameParentTree();
		GenericTreeController tcp = new GenericTreeController(tree,false);
		GuiController.getInstance().addGuiObjectController(tcp);
		topSplitPanel.getPanelObjects().add(tree);
		
		// TOP RIGHT
		PnlDetail parentDetailPanel = getNewMainFrameParentDetailPanel();
		PnlGrid parentGridPanel = getNewMainFrameParentGridPanel();
		
		PnlDetailGrid parentDetailGridPanel = getNewMainFrameParentDetailGridPanel(parentDetailPanel,parentGridPanel);
		topSplitPanel.getPanelObjects().add(parentDetailGridPanel);
		GenericDetailGridController parentDetailGridController = new GenericDetailGridController(parentDetailGridPanel);
		GuiController.getInstance().addGuiObjectController(parentDetailGridController);
		
		GenericDetailController parentDetailController = new GenericDetailController(parentDetailPanel);
		parentDetailController.setEnabled(false);
		GuiController.getInstance().addGuiObjectController(parentDetailController);
		parentDetailGridController.setDetailController(parentDetailController);
		
		MainFrameGridController parentGridController = new MainFrameGridController(parentGridPanel,false);
		GuiController.getInstance().addGuiObjectController(parentGridController);
		//parentGridController.addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
		parentDetailGridController.setGridController(parentGridController);
		
		tcp.addSubscriber(parentDetailGridController);
				
		// BOTTOM LEFT
		GuiTree treeChild = getNewMainFrameChildTree();
		GenericTreeController tcc = new GenericTreeController(treeChild,true);
		GuiController.getInstance().addGuiObjectController(tcc);
		
		//bottomSplitPanel.getPanelObjects().add(treeChild);
		PnlZoomTree zoomTreePanel = getNewMainFrameZoomTree(treeChild);
		bottomSplitPanel.getPanelObjects().add(zoomTreePanel);
		
		tcp.addSubscriber(tcc);
		
		// BOTTOM RIGHT
		PnlDetail childDetailPanel = getNewMainFrameChildDetailPanel();
		PnlGrid childGridPanel = getNewMainFrameChildGridPanel();

		PnlDetailGrid childDetailGridPanel = getNewMainFrameChildDetailGridPanel(childDetailPanel,childGridPanel);
		bottomSplitPanel.getPanelObjects().add(childDetailGridPanel);
		GenericDetailGridController childDetailGridController = new GenericDetailGridController(childDetailGridPanel);
		GuiController.getInstance().addGuiObjectController(childDetailGridController);
		
		GenericDetailController childDetailController = new GenericDetailController(childDetailPanel);
		childDetailController.setEnabled(false);
		GuiController.getInstance().addGuiObjectController(childDetailController);
		childDetailGridController.setDetailController(childDetailController);

		MainFrameGridController childGridController = new MainFrameGridController(childGridPanel,true);
		GuiController.getInstance().addGuiObjectController(childGridController);
		//childGridController.addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
		childDetailGridController.setGridController(childGridController);
		
		parentGridController.addSubscriber(childDetailGridController);
		tcc.addSubscriber(childDetailGridController);
	
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
	
		JMenuItem exit = new JMenuItem("Exit");
		frame.addMenuItem(exit);
		exit.setActionCommand(GuiWindowController.ACTION_CLOSE_FRAME);
		exit.setIcon(getIcon("MenuExit"));
		file.add(exit);

		JMenu show = new JMenu("Show");
		menuBar.add(show);

		JMenuItem config = new JMenuItem("Configuration");
		frame.addMenuItem(config);
		config.setActionCommand(MENU_MAIN_SHOW_CONFIGURATION);
		config.setIcon(getIcon("DialogConfiguration"));
		show.add(config);
		
		JMenuItem model = new JMenuItem("Model");
		frame.addMenuItem(model);
		model.setActionCommand(MENU_MAIN_SHOW_MODEL);
		model.setIcon(getIcon("FrameModel"));
		show.add(model);

		JMenuItem reporter = new JMenuItem("Reporter");
		frame.addMenuItem(reporter);
		reporter.setActionCommand(MENU_MAIN_SHOW_REPORTER);
		reporter.setIcon(getIcon("DialogReport"));
		show.add(reporter);
		
		if (DbConfig.getInstance().isDebug()) {
			JMenuItem debugger = new JMenuItem("Debugger");
			frame.addMenuItem(debugger);
			debugger.setActionCommand(MENU_MAIN_SHOW_DEBUGGER);
			debugger.setIcon(getIcon("FrameDebug"));
			show.add(debugger);
		}
		
		frame.getFrame().setJMenuBar(menuBar);
		frame.getFrame().setTitle(getApplicationLabel() + " " + getApplicationVersion());
		frame.getFrame().setIconImage(getIconImage("FrameMain"));
		frame.maximizeFrameSize();
		frame.getFrame().setMinimumSize(new Dimension(800,500));
		frame.getPanelObjects().calculateWeights();
		frame.renderComponent();
		frame.centreFrameLocation();
		
		return frame;
	}
	
	protected GuiDialog getNewLoginDialog() {
		GuiDialog dialog = new GuiDialog(DIALOG_LOGIN);
		//GuiPanel panel = dialog.getNewPanel(PANEL_LOGIN,0,0);

		GuiPanel panel = new GuiPanel(PANEL_LOGIN,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(0.001D);
				getPanelObjects().getColumnWeights().add(0.999D);
				super.renderComponent();
			}
		};
		
		String toolTip = "";
		
		toolTip = "Your user name";
		GuiLabel ln = new GuiLabel(LABEL_LOGIN_NAME,0,0,"Name*");
		ln.setAnchor(GridBagConstraints.FIRST_LINE_START);
		ln.setFill(GridBagConstraints.HORIZONTAL);
		ln.getLabel().setToolTipText(toolTip);
		PrpString prpN = new PrpString(PROPERTY_LOGIN_NAME,0,1,new DtString(ClConfig.getInstance().getUserName()));
		prpN.setToolTipText(toolTip);
		panel.getPanelObjects().add(ln);
		panel.getPanelObjects().add(prpN);

		toolTip = "Your user password";
		GuiLabel lp = new GuiLabel(LABEL_LOGIN_PASSWORD,1,0,"Password*");
		lp.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lp.setFill(GridBagConstraints.HORIZONTAL);
		lp.getLabel().setToolTipText(toolTip);
		PrpPassword prpP = new PrpPassword(PROPERTY_LOGIN_PASSWORD,1,1,new DtStringBuffer(ClConfig.getInstance().getUserPassword()));
		prpP.setToolTipText(toolTip);
		panel.getPanelObjects().add(lp);
		panel.getPanelObjects().add(prpP);

		GuiButton loginButton = new GuiButton(BUTTON_LOGIN,2,0,"Login");
		loginButton.setGridWidth(2);
		panel.getPanelObjects().add(loginButton);

		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 2;
		panel.getGridBagConstraints().ipady = 2;
		panel.getGridBagConstraints().insets = new Insets(2,2,2,2);

		dialog.getPanelObjects().add(panel);

		panel.getPanelObjects().calculateWeights();
		panel.getPanelObjects().getColumnWeights().clear();
		panel.getPanelObjects().getColumnWeights().add(0.001D);
		panel.getPanelObjects().getColumnWeights().add(0.999D);

		dialog.getPanelObjects().calculateWeights();
		
		dialog.getDialog().setTitle(getApplicationName() +" Login");
		dialog.getDialog().setResizable(false);
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setIconImage(getIconImage("DialogLogin"));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	protected GuiFrame getNewControlFrame() {
		GuiFrame frame = new GuiFrame(FRAME_CONTROL);
		GuiPanel panel = frame.getNewPanel(PANEL_CONTROL,0,0);

		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		
		Dimension preferred = new Dimension(60,10);
		
		GuiLabel ls = new GuiLabel(LABEL_CONTROL_SERVER,0,0,"Server:");
		ls.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(ls);
		GuiLabel lsw = new GuiLabel(LABEL_CONTROL_SERVER_IS_WORKING,0,1,"?");
		lsw.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(lsw);
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_SERVER_IS_WORKING,0,2,"Refresh"));
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_STOP_SERVER,0,3,"Stop"));
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_START_SERVER,0,4,"Start"));

		GuiLabel lb = new GuiLabel(LABEL_CONTROL_BATCH,1,0,"Batch:");
		lb.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(lb);
		GuiLabel lbw = new GuiLabel(LABEL_CONTROL_BATCH_IS_WORKING,1,1,"?");
		lbw.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(lbw);
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_BATCH_IS_WORKING,1,2,"Refresh"));
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_STOP_BATCH,1,3,"Stop"));
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_START_BATCH,1,4,"Start"));

		GuiLabel ld = new GuiLabel(LABEL_CONTROL_DEBUG,2,0,"Cache:");
		ld.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(ld);
		GuiLabel ldw = new GuiLabel(LABEL_CONTROL_SERVER_CACHE,2,1,"?");
		ldw.getLabel().setPreferredSize(preferred);
		panel.getPanelObjects().add(ldw);
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_SERVER_CACHE,2,2,"Refresh"));
		panel.getPanelObjects().add(new GuiButton(BUTTON_CONTROL_CACHE_CLEAR,2,3,"Clear"));
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
	
		JMenuItem exit = new JMenuItem("Exit");
		frame.addMenuItem(exit);
		exit.setActionCommand(GuiWindowController.ACTION_CLOSE_FRAME);
		exit.setIcon(getIcon("MenuExit"));
		file.add(exit);

		JMenu show = new JMenu("Show");
		menuBar.add(show);

		JMenuItem model = new JMenuItem("Configuration");
		frame.addMenuItem(model);
		model.setActionCommand(MENU_CONTROL_SHOW_CONFIGURATION);
		model.setIcon(getIcon("DialogConfiguration"));
		show.add(model);

		if (DbConfig.getInstance().isDebug()) {
			JMenuItem debugger = new JMenuItem("Debugger");
			frame.addMenuItem(debugger);
			debugger.setActionCommand(MENU_MAIN_SHOW_DEBUGGER);
			debugger.setIcon(getIcon("FrameDebug"));
			show.add(debugger);
		}
		
		frame.getFrame().setJMenuBar(menuBar);
		
		frame.getPanelObjects().calculateWeights();
		
		frame.getFrame().setTitle(getApplicationName() + " Control");
		frame.getFrame().setResizable(false);
		frame.getFrame().setIconImage(getIconImage("FrameControl"));
		
		frame.renderComponent();

		frame.centreFrameLocation();
		
		return frame;
	}

	protected GuiDialog getNewControlConfigurationFrame() {
		GuiDialog dialog = new GuiDialog(DIALOG_CONTROL_CONFIGURATION);
		
		GuiPanel panel = new GuiPanel(PANEL_CONTROL_CONFIGURATION,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(0.001D);
				getPanelObjects().getColumnWeights().add(0.999D);
				super.renderComponent();
			}
		};
		int row = 0;
		int pNum = 0;
		for (String prop: DbConfig.PROPERTIES) {
			GuiLabel lbl = new GuiLabel(prop + "Label",row,0,DbConfig.PROPERTY_LABELS[pNum]);
			GuiProperty prp = null;
			if (prop.equals(DbConfig.PROPERTY_PORT)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_MAX_SESSIONS)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_DEBUG)) {
				prp = new PrpCheckBox(prop,row,1,new DtBoolean());
			} else if (prop.equals(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX)) {
				prp = new PrpString(prop,row,1,new DtString());
			} else if (prop.equals(DbConfig.PROPERTY_START_BATCH)) {
				prp = new PrpCheckBox(prop,row,1,new DtBoolean());
			} else if (prop.equals(DbConfig.PROPERTY_REMOTE_BACKUP_DIR)) {
				prp = new PrpString(prop,row,1,new DtString());
			} else if (prop.equals(DbConfig.PROPERTY_ENCRYPT)) {
				prp = new PrpCheckBox(prop,row,1,new DtBoolean());
			} else if (prop.equals(DbConfig.PROPERTY_XML_COMPRESSION)) {
				String[] options = {DbConfig.XML_COMPRESSION_FULL,DbConfig.XML_COMPRESSION_TAGS,DbConfig.XML_COMPRESSION_NONE};
				prp = new PrpComboBox(prop,row,1,new DtString(),options);
			} else if (prop.equals(DbConfig.PROPERTY_CACHE)) {
				prp = new PrpCheckBox(prop,row,1,new DtBoolean());
			} else if (prop.equals(DbConfig.PROPERTY_CACHE_SIZE)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_CACHE_PERSIST_SIZE)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_MAX_FETCH_LOAD)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_MAX_FETCH_RESULTS)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			} else if (prop.equals(DbConfig.PROPERTY_PRELOAD_SPEED)) {
				prp = new PrpInteger(prop,row,1,new DtInteger());
			}
			if (prp!=null) {
				lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
				lbl.setFill(GridBagConstraints.NONE);
				lbl.getLabel().setToolTipText(DbConfig.PROPERTY_HELP[pNum]);
				prp.setAnchor(GridBagConstraints.FIRST_LINE_START);
				prp.setFill(GridBagConstraints.BOTH);
				prp.setToolTipText(DbConfig.PROPERTY_HELP[pNum]);
				panel.getPanelObjects().add(lbl);
				panel.getPanelObjects().add(prp);
				row++;
			}
			pNum++;
		}
		GuiButton setButton = new GuiButton(BUTTON_CONTROL_CONFIGURATION_SET,row,0,"Set");
		setButton.setGridWidth(2);
		panel.getPanelObjects().add(setButton);

		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 2;
		panel.getGridBagConstraints().ipady = 2;
		panel.getGridBagConstraints().insets = new Insets(2,2,2,2);

		dialog.getPanelObjects().add(panel);
		
		dialog.getPanelObjects().calculateWeights();
		
		dialog.getDialog().setTitle(getApplicationName() + " Configuration");
		dialog.getDialog().setResizable(false);
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setIconImage(getIconImage("DialogConfiguration"));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();
		
		return dialog;
	}

	protected GuiDialog getNewClientConfigurationFrame() {
		GuiDialog dialog = new GuiDialog(DIALOG_CLIENT_CONFIGURATION);
		
		GuiPanel panel = new GuiPanel(PANEL_CLIENT_CONFIGURATION,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(0.001D);
				getPanelObjects().getColumnWeights().add(0.999D);
				super.renderComponent();
			}
		};
		
		
		int row = 0;
		GuiLabel lblH = new GuiLabel(ClConfig.PROPERTY_HOST_NAME_OR_IP + "Label",row,0,ClConfig.LABEL_HOST_NAME_OR_IP + "*");
		PrpString prpH = new PrpString(ClConfig.PROPERTY_HOST_NAME_OR_IP ,row,1,new DtString());
		row++;

		lblH.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblH.setFill(GridBagConstraints.NONE);
		lblH.getLabel().setToolTipText(ClConfig.HELP_HOST_NAME_OR_IP);
		prpH.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpH.setFill(GridBagConstraints.BOTH);
		prpH.setToolTipText(ClConfig.HELP_HOST_NAME_OR_IP);
		panel.getPanelObjects().add(lblH);
		panel.getPanelObjects().add(prpH);
		
		int pNum = 0;
		for (String prop: DbConfig.PROPERTIES) {
			if (
				(prop.equals(DbConfig.PROPERTY_PORT)) ||
				(prop.equals(DbConfig.PROPERTY_DEBUG)) ||
				(prop.equals(DbConfig.PROPERTY_DEBUG)) ||
				(prop.equals(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX)) ||
				(prop.equals(DbConfig.PROPERTY_XML_COMPRESSION))
				) {
				GuiLabel lbl = new GuiLabel(prop + "Label",row,0,DbConfig.PROPERTY_LABELS[pNum]);
				GuiProperty prp = null;
				if (prop.equals(DbConfig.PROPERTY_PORT)) {
					prp = new PrpInteger(prop,row,1,new DtInteger());
				} else if (prop.equals(DbConfig.PROPERTY_DEBUG)) {
					prp = new PrpCheckBox(prop,row,1,new DtBoolean());
				} else if (prop.equals(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX)) {
					prp = new PrpString(prop,row,1,new DtString());
				} else if (prop.equals(DbConfig.PROPERTY_XML_COMPRESSION)) {
					String[] options = {DbConfig.XML_COMPRESSION_FULL,DbConfig.XML_COMPRESSION_TAGS,DbConfig.XML_COMPRESSION_NONE};
					prp = new PrpComboBox(prop,row,1,new DtString(),options);
				}
				if (prp!=null) {
					lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
					lbl.setFill(GridBagConstraints.NONE);
					lbl.getLabel().setToolTipText(DbConfig.PROPERTY_HELP[pNum]);
					prp.setAnchor(GridBagConstraints.FIRST_LINE_START);
					prp.setFill(GridBagConstraints.BOTH);
					prp.setToolTipText(DbConfig.PROPERTY_HELP[pNum]);
					panel.getPanelObjects().add(lbl);
					panel.getPanelObjects().add(prp);
					row++;
				}
			}
			pNum++;
		}

		GuiLabel lblM = new GuiLabel(GuiConfig.PROPERTY_MAXIMIZE_ON_START + "Label",row,0,GuiConfig.LABEL_MAXIMIZE_ON_START);
		PrpCheckBox prpM = new PrpCheckBox(GuiConfig.PROPERTY_MAXIMIZE_ON_START ,row,1,new DtBoolean());
		row++;

		lblM.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblM.setFill(GridBagConstraints.NONE);
		lblM.getLabel().setToolTipText(GuiConfig.HELP_MAXIMIZE_ON_START);
		prpM.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpM.setFill(GridBagConstraints.BOTH);
		prpM.setToolTipText(GuiConfig.HELP_MAXIMIZE_ON_START);
		panel.getPanelObjects().add(lblM);
		panel.getPanelObjects().add(prpM);

		GuiLabel lblC = new GuiLabel(GuiConfig.PROPERTY_LOAD_GUI_MODEL_ON_START + "Label",row,0,GuiConfig.LABEL_LOAD_GUI_MODEL_ON_START);
		PrpCheckBox prpC = new PrpCheckBox(GuiConfig.PROPERTY_LOAD_GUI_MODEL_ON_START ,row,1,new DtBoolean());
		row++;

		lblC.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblC.setFill(GridBagConstraints.NONE);
		lblC.getLabel().setToolTipText(GuiConfig.HELP_LOAD_GUI_MODEL_ON_START);
		prpC.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpC.setFill(GridBagConstraints.BOTH);
		prpC.setToolTipText(GuiConfig.HELP_LOAD_GUI_MODEL_ON_START);
		panel.getPanelObjects().add(lblC);
		panel.getPanelObjects().add(prpC);
		
		GuiButton setButton = new GuiButton(BUTTON_CLIENT_CONFIGURATION_SET,row,0,"Set");
		setButton.setGridWidth(2);
		panel.getPanelObjects().add(setButton);

		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 2;
		panel.getGridBagConstraints().ipady = 2;
		panel.getGridBagConstraints().insets = new Insets(2,2,2,2);

		dialog.getPanelObjects().add(panel);
		
		dialog.getPanelObjects().calculateWeights();
		
		dialog.getDialog().setTitle(getApplicationName() + " Configuration");
		dialog.getDialog().setResizable(false);
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setIconImage(getIconImage("DialogConfiguration"));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();
		
		return dialog;
	}
	
	protected GuiWindow getNewBusyWindow() {
		GuiWindow window = new GuiWindow(WINDOW_BUSY);
		PnlBusy busyPanel = new PnlBusy(PANEL_BUSY,0,0);
		window.getPanelObjects().add(busyPanel);
		window.getPanelObjects().calculateWeights();
		
		window.getWindow().setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		window.getWindow().setOpacity(0.5F);
		window.renderComponent();

		window.centreFrameLocation();
		
		return window;
	}
	
	protected GuiTree getNewMainFrameParentTree() {
		GuiTree tree = new GuiTree(TREE_MAIN_PARENT, 0, 0, getApplicationName());
		tree.setAnchor(GridBagConstraints.FIRST_LINE_START);
		tree.setFill(GridBagConstraints.BOTH);
		tree.getScrollPanel().setMinimumSize(new Dimension(160,100));
		tree.getScrollPanel().setPreferredSize(new Dimension(180,100));
		return tree;
	}

	protected GuiTree getNewMainFrameChildTree() {
		GuiTree tree = new GuiTree(TREE_MAIN_CHILD, 0, 0, "");
		tree.setAnchor(GridBagConstraints.FIRST_LINE_START);
		tree.setFill(GridBagConstraints.BOTH);
		tree.getScrollPanel().setMinimumSize(new Dimension(160,100));
		tree.getScrollPanel().setPreferredSize(new Dimension(180,100));
		return tree;
	}

	protected PnlZoomTree getNewMainFrameZoomTree(GuiTree tree) {
		PnlZoomTree zoomTree = new PnlZoomTree(PANEL_MAIN_ZOOM_TREE, 0, 0, tree);
		zoomTree.setAnchor(GridBagConstraints.FIRST_LINE_START);
		zoomTree.setFill(GridBagConstraints.BOTH);
		zoomTree.getScrollPanel().setMinimumSize(new Dimension(160,100));
		zoomTree.getScrollPanel().setPreferredSize(new Dimension(180,100));
		return zoomTree;
	}
	
	protected PnlGrid getNewMainFrameParentGridPanel() {
		PnlGrid gridPanel = new PnlGrid(GRID_MAIN_PARENT, 0, 1, true);
		return gridPanel;
	}

	protected PnlDetail getNewMainFrameParentDetailPanel() {
		PnlDetail detailPanel = new PnlDetail(PANEL_MAIN_PARENT_DETAIL, 0, 0);
		return detailPanel;
	}

	protected PnlDetailGrid getNewMainFrameParentDetailGridPanel(PnlDetail detailPanel, PnlGrid gridPanel) {
		PnlDetailGrid detailGridPanel = new PnlDetailGrid(PANEL_MAIN_PARENT_DETAIL_GRID, 0, 1, detailPanel, gridPanel);
		detailGridPanel.getPanel().setMinimumSize(new Dimension(700,150));
		return detailGridPanel;
	}

	protected PnlGrid getNewMainFrameChildGridPanel() {
		PnlGrid gridPanel = new PnlGrid(GRID_MAIN_CHILD, 0, 1, true);
		return gridPanel;
	}

	protected PnlDetail getNewMainFrameChildDetailPanel() {
		PnlDetail detailPanel = new PnlDetail(PANEL_MAIN_CHILD_DETAIL, 0, 0);
		return detailPanel;
	}

	protected PnlDetailGrid getNewMainFrameChildDetailGridPanel(PnlDetail detailPanel, PnlGrid gridPanel) {
		PnlDetailGrid detailGridPanel = new PnlDetailGrid(PANEL_MAIN_CHILD_DETAIL_GRID, 0, 1, detailPanel, gridPanel);
		detailGridPanel.getPanel().setMinimumSize(new Dimension(700,150));
		return detailGridPanel;
	}
	
	protected GuiDialog getNewPrpIdRefDialog(String unique) {
		GuiDialog dialog = new GuiDialog(DIALOG_ID_REF + unique);

		GuiPanel mainPanel = new GuiPanel(PANEL_ID_REF + unique,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(0.999D);
				getPanelObjects().getRowWeights().add(0.001D);
				super.renderComponent();
			}
		};
		mainPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		mainPanel.setFill(GridBagConstraints.BOTH);
		mainPanel.getPanel().setLayout(new GridBagLayout());
		mainPanel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		mainPanel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		mainPanel.getGridBagConstraints().weightx = 1;
		mainPanel.getGridBagConstraints().weighty = 1;
		mainPanel.getGridBagConstraints().gridheight = 1;
		mainPanel.getGridBagConstraints().gridwidth = 1;
		mainPanel.getGridBagConstraints().ipadx = 0;
		mainPanel.getGridBagConstraints().ipady = 0;
		
		dialog.getPanelObjects().add(mainPanel);

		GuiSplitPanel splitPanel = new GuiSplitPanel(PANEL_ID_REF_SELECT + unique,0,0,JSplitPane.HORIZONTAL_SPLIT);
		splitPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		splitPanel.setFill(GridBagConstraints.BOTH);
		splitPanel.getPanel().setResizeWeight(0.4);

		mainPanel.getPanelObjects().add(splitPanel);
		
		GuiGrid gridValue = new GuiGrid(GRID_ID_REF_VALUE + unique,0,0);
		gridValue.getScrollPanel().setMinimumSize(new Dimension(160,160));
		gridValue.getScrollPanel().setPreferredSize(new Dimension(160,160));
		gridValue.setFill(GridBagConstraints.BOTH);
		gridValue.setAnchor(GridBagConstraints.FIRST_LINE_START);
		gridValue.addProperty(MdlObject.PROPERTY_ID, "ID", 0L);
		gridValue.addProperty(MdlObject.PROPERTY_NAME, "Name", "");
		gridValue.fireStructureChanged();
		
		PnlGrid gridCollection = new PnlGrid(GRID_ID_REF_COLLECTION + unique, 0, 1, false);
		gridCollection.getPanel().setMinimumSize(new Dimension(500,150));
		gridCollection.setFill(GridBagConstraints.BOTH);
		gridCollection.setAnchor(GridBagConstraints.FIRST_LINE_START);
		PrpIdRefDialogGridController collectionController = new PrpIdRefDialogGridController(gridCollection);
		GuiController.getInstance().addGuiObjectController(collectionController);
		
		splitPanel.getPanelObjects().add(gridValue);
		splitPanel.getPanelObjects().add(gridCollection);
		
		GuiPanel buttonPanel = new GuiPanel(PANEL_ID_REF_BUTTONS + unique,1,0);
		buttonPanel.setAnchor(GridBagConstraints.LAST_LINE_START);
		buttonPanel.setFill(GridBagConstraints.HORIZONTAL);
		mainPanel.getPanelObjects().add(buttonPanel);
		
		GuiButton buttonRemove = new GuiButton(BUTTON_ID_REF_REMOVE + unique,0,0,">");
		GuiButton buttonSet = new GuiButton(BUTTON_ID_REF_SET + unique,0,1,"Set");
		GuiButton buttonAdd = new GuiButton(BUTTON_ID_REF_ADD + unique,0,2,"<");
		
		buttonPanel.getPanelObjects().add(buttonRemove);
		buttonPanel.getPanelObjects().add(buttonSet);
		buttonPanel.getPanelObjects().add(buttonAdd);
		
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setMinimumSize(new Dimension(800,400));
		dialog.getDialog().setPreferredSize(new Dimension(800,400));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	protected GuiDialog getNewPrpDateTimeDialog() {
		GuiDialog dialog = new GuiDialog(DIALOG_DATETIME);

		GuiPanel mainPanel = new GuiPanel(PANEL_DATETIME,0,0);
		mainPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		mainPanel.setFill(GridBagConstraints.BOTH);

		dialog.getPanelObjects().add(mainPanel);

		GuiButton btnPrevYear = new GuiButton(BUTTON_DATETIME_PREV_YEAR,0,1,"<");
		btnPrevYear.setAnchor(GridBagConstraints.LINE_END);
		PrpInteger propYear = new PrpInteger(PROPERTY_DATETIME_YEAR,0,2,new DtInteger());
		propYear.setGridWidth(3);
		propYear.setFill(GridBagConstraints.HORIZONTAL);
		GuiButton btnNextYear = new GuiButton(BUTTON_DATETIME_NEXT_YEAR,0,5,">");
		btnNextYear.setAnchor(GridBagConstraints.LINE_START);
		
		mainPanel.getPanelObjects().add(btnPrevYear);
		mainPanel.getPanelObjects().add(propYear);
		mainPanel.getPanelObjects().add(btnNextYear);

		GuiButton btnPrevMonth = new GuiButton(BUTTON_DATETIME_PREV_MONTH,1,1,"<");
		btnPrevMonth.setAnchor(GridBagConstraints.LINE_END);
		PrpComboBox propMonth = new PrpComboBox(PROPERTY_DATETIME_MONTH,1,2,new DtString(),MONTHS);
		propMonth.setGridWidth(3);
		propMonth.setFill(GridBagConstraints.HORIZONTAL);
		GuiButton btnNextMonth = new GuiButton(BUTTON_DATETIME_NEXT_MONTH,1,5,">");
		btnNextMonth.setAnchor(GridBagConstraints.LINE_START);

		mainPanel.getPanelObjects().add(btnPrevMonth);
		mainPanel.getPanelObjects().add(propMonth);
		mainPanel.getPanelObjects().add(btnNextMonth);
		
		for (int weekday = 0; weekday < 7; weekday++) {
			String lblName = "day" + Generic.SEP_STR + weekday;
			GuiLabel lblDay = new GuiLabel(lblName,2,weekday,DAYS_SHORT[weekday]);
			mainPanel.getPanelObjects().add(lblDay);
		}
		for (int week = 0; week < 6; week++) {
			for (int weekday = 0; weekday < 7; weekday++) {
				String btnName = BUTTON_DATETIME_DATE_PREFIX + Generic.SEP_STR + week + Generic.SEP_STR + weekday;
				GuiButton btnDay = new GuiButton(btnName,3 + week,weekday," ");
				btnDay.setFill(GridBagConstraints.HORIZONTAL);
				btnDay.getButton().setMinimumSize(new Dimension(60,20));
				btnDay.getButton().setPreferredSize(new Dimension(60,20));
				mainPanel.getPanelObjects().add(btnDay);
			}
		}
		
		PrpString propHour = new PrpString(PROPERTY_DATETIME_HOUR,9,2,new DtString());
		propHour.setFill(GridBagConstraints.HORIZONTAL);
		propHour.setColumns(2);
		
		PrpString propMinute = new PrpString(PROPERTY_DATETIME_MINUTE,9,3,new DtString());
		propMinute.setFill(GridBagConstraints.HORIZONTAL);
		propMinute.setColumns(2);
		
		PrpString propSecond = new PrpString(PROPERTY_DATETIME_SECOND,9,4,new DtString());
		propSecond.setFill(GridBagConstraints.HORIZONTAL);
		propSecond.setColumns(2);

		mainPanel.getPanelObjects().add(propHour);
		mainPanel.getPanelObjects().add(propMinute);
		mainPanel.getPanelObjects().add(propSecond);

		GuiButton buttonSet = new GuiButton(BUTTON_DATETIME_SET,10,3,"Set");
		mainPanel.getPanelObjects().add(buttonSet);

		GuiButton buttonToday = new GuiButton(BUTTON_DATETIME_TODAY,11,0,"Today");
		mainPanel.getPanelObjects().add(buttonToday);

		GuiButton buttonClear = new GuiButton(BUTTON_DATETIME_CLEAR,11,6,"Clear");
		mainPanel.getPanelObjects().add(buttonClear);
		
		dialog.getDialog().setTitle("Calendar");
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setResizable(false);
		dialog.getDialog().setIconImage(getIconImage("DialogCalendar"));
		dialog.getDialog().setPreferredSize(new Dimension(450,320));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	protected GuiDialog getNewReportDialog() {
		GuiDialog dialog = new GuiDialog(DIALOG_REPORT);
		
		GuiPanel panel = new GuiPanel(PANEL_REPORT,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(0.001D);
				getPanelObjects().getRowWeights().add(0.998D);
				getPanelObjects().getRowWeights().add(0.001D);
				getPanelObjects().getRowWeights().add(0.001D);
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(0.1D);
				getPanelObjects().getColumnWeights().add(0.9D);
				super.renderComponent();
			}
		};
		panel.getPanel().setLayout(new GridBagLayout());
		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 0;
		panel.getGridBagConstraints().ipady = 0;
		panel.getGridBagConstraints().insets = new Insets(2,2,2,2);
		dialog.getPanelObjects().add(panel);
		
		GuiLabel lblReport = new GuiLabel(LABEL_REPORT,0,0,"Report");
		lblReport.setFill(GridBagConstraints.BOTH);
		lblReport.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.getPanelObjects().add(lblReport);

		PrpIdRef prpReport = new PrpIdRef(DbReport.class.getName(),0,1,new DtIdRef());
		prpReport.setFill(GridBagConstraints.HORIZONTAL);
		prpReport.setAnchor(GridBagConstraints.PAGE_START);
		panel.getPanelObjects().add(prpReport);
		
		PnlReportFilters filterPanel = new PnlReportFilters(PANEL_REPORT_FILTERS,1,0);
		filterPanel.setFill(GridBagConstraints.BOTH);
		filterPanel.setAnchor(GridBagConstraints.LINE_START);
		filterPanel.setGridWidth(2);
		panel.getPanelObjects().add(filterPanel);
		
		GuiButton btnFetch = new GuiButton(BUTTON_REPORT_FETCH,2,0,"Fetch");
		btnFetch.setAnchor(GridBagConstraints.PAGE_END);
		btnFetch.setGridWidth(2);
		panel.getPanelObjects().add(btnFetch);

		GuiLabel lblFetch = new GuiLabel(LABEL_FETCH_STATUS,3,0," ");
		lblFetch.setFill(GridBagConstraints.BOTH);
		lblFetch.setAnchor(GridBagConstraints.PAGE_END);
		lblFetch.setGridWidth(2);
		panel.getPanelObjects().add(lblFetch);
		
		dialog.getDialog().setTitle(getApplicationName() + " Reporter");
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setResizable(true);
		dialog.getDialog().setIconImage(getIconImage("DialogReport"));
		dialog.getDialog().setMinimumSize(new Dimension(400,160));
		dialog.getDialog().setPreferredSize(new Dimension(500,200));

		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}
	
	protected GuiFrame getNewDebugFrame() {
		GuiFrame frame = new GuiFrame(FRAME_DEBUG);

		GuiSplitPanel splitPanel = new GuiSplitPanel(PANEL_DEBUG_SPLIT,0,0,JSplitPane.HORIZONTAL_SPLIT);
		splitPanel.setFill(GridBagConstraints.BOTH);
		splitPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		splitPanel.getPanel().setResizeWeight(0.3D);
		
		frame.getPanelObjects().add(splitPanel);
		
		GuiGrid gridValue = new GuiGrid(GRID_DEBUG,0,0);
		gridValue.getScrollPanel().setMinimumSize(new Dimension(160,160));
		gridValue.getScrollPanel().setPreferredSize(new Dimension(160,160));
		gridValue.setFill(GridBagConstraints.BOTH);
		gridValue.setAnchor(GridBagConstraints.FIRST_LINE_START);
		gridValue.addProperty("desc", "Description", "");
		gridValue.fireStructureChanged();
		
		splitPanel.getPanelObjects().add(gridValue);
		
		GuiPanel detailPanel = new GuiPanel(PANEL_DEBUG_DETAIL,0,1) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(0.001D);
				getPanelObjects().getColumnWeights().add(0.999D);
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(0.001D);
				getPanelObjects().getRowWeights().add(0.001D);
				getPanelObjects().getRowWeights().add(0.998D);
				super.renderComponent();
			}
		};
		detailPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		detailPanel.setFill(GridBagConstraints.BOTH);
		detailPanel.getPanel().setLayout(new GridBagLayout());
		detailPanel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		detailPanel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		detailPanel.getGridBagConstraints().weightx = 1;
		detailPanel.getGridBagConstraints().weighty = 1;
		detailPanel.getGridBagConstraints().gridheight = 1;
		detailPanel.getGridBagConstraints().gridwidth = 1;
		detailPanel.getGridBagConstraints().ipadx = 2;
		detailPanel.getGridBagConstraints().ipady = 2;
		detailPanel.getGridBagConstraints().insets = new Insets(2,2,2,2);
		
		splitPanel.getPanelObjects().add(detailPanel);
		
		GuiLabel lblDesc = new GuiLabel(LABEL_DEBUG_DESC,0,0,"Description");
		lblDesc.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblDesc.setFill(GridBagConstraints.HORIZONTAL);

		GuiLabel lblErr = new GuiLabel(LABEL_DEBUG_ERROR,1,0,"Error");
		lblErr.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblErr.setFill(GridBagConstraints.HORIZONTAL);

		GuiLabel lblObj = new GuiLabel(LABEL_DEBUG_OBJ,2,0,"Details");
		lblObj.setAnchor(GridBagConstraints.FIRST_LINE_START);
		lblObj.setFill(GridBagConstraints.HORIZONTAL);
		
		detailPanel.getPanelObjects().add(lblDesc);
		detailPanel.getPanelObjects().add(lblErr);
		detailPanel.getPanelObjects().add(lblObj);

		PrpString prpDesc = new PrpString(PROPERTY_DEBUG_DESC,0,1,new DtString());
		prpDesc.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpDesc.setFill(GridBagConstraints.HORIZONTAL);

		PrpCheckBox prpErr = new PrpCheckBox(PROPERTY_DEBUG_ERROR,1,1,new DtBoolean());
		prpErr.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpErr.setFill(GridBagConstraints.HORIZONTAL);

		PrpTextAreaString prpObj = new PrpTextAreaString(PROPERTY_DEBUG_OBJ,2,1,new DtString());
		prpObj.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpObj.setFill(GridBagConstraints.BOTH);
		
		detailPanel.getPanelObjects().add(prpDesc);
		detailPanel.getPanelObjects().add(prpErr);
		detailPanel.getPanelObjects().add(prpObj);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu settings = new JMenu("Settings");
		menuBar.add(settings);
	
		JCheckBoxMenuItem alwaysOnTop = new JCheckBoxMenuItem("Always on top");
		frame.addMenuItem(alwaysOnTop);
		alwaysOnTop.setSelected(true);
		alwaysOnTop.setIcon(getIcon("MenuAlwaysOnTop"));
		alwaysOnTop.setActionCommand(MENU_DEBUG_ALWAYS_ON_TOP);
		settings.add(alwaysOnTop);

		JCheckBoxMenuItem autoSelect = new JCheckBoxMenuItem("Auto select");
		frame.addMenuItem(autoSelect);
		autoSelect.setSelected(true);
		autoSelect.setIcon(getIcon("MenuAutoSelect"));
		autoSelect.setActionCommand(MENU_DEBUG_AUTO_SELECT);
		settings.add(autoSelect);

		JCheckBoxMenuItem showOnError = new JCheckBoxMenuItem("Show on error");
		frame.addMenuItem(showOnError);
		showOnError.setSelected(true);
		showOnError.setIcon(getIcon("MenuShowOnError"));
		showOnError.setActionCommand(MENU_DEBUG_SHOW_ON_ERROR);
		settings.add(showOnError);
		
		frame.getFrame().setJMenuBar(menuBar);
		frame.getFrame().setTitle(getApplicationName() + " Debugger");
		frame.getFrame().setAlwaysOnTop(true);
		frame.getFrame().setIconImage(getIconImage("FrameDebug"));
		frame.getFrame().setMinimumSize(new Dimension(800,400));
		frame.getFrame().setPreferredSize(new Dimension(800,400));
		
		frame.renderComponent();

		frame.cornerFrameLocation(false,false);

		prpDesc.setEnabled(false);
		prpErr.setEnabled(false);
		prpObj.setEnabled(false);

		return frame;
	}
	
	protected GuiFrame getNewModelFrame() {
		GuiFrame frame = new GuiFrame(FRAME_MODEL);

		GuiTree tree = new GuiTree(TREE_MODEL,0,0,getApplicationName());
		tree.setAnchor(GridBagConstraints.FIRST_LINE_START);
		tree.setFill(GridBagConstraints.BOTH);
		tree.getScrollPanel().setMinimumSize(new Dimension(200,160));
		tree.getScrollPanel().setPreferredSize(new Dimension(200,160));
		GenericTreeController tcm = new GenericTreeController(tree,false);
		
		GuiController.getInstance().addGuiObjectController(tcm);

		frame.getPanelObjects().add(tree);

		JMenuBar menuBar = new JMenuBar();
		
		JMenu settings = new JMenu("Settings");
		menuBar.add(settings);
	
		JCheckBoxMenuItem asEntityModel = new JCheckBoxMenuItem("Show as entity model");
		frame.addMenuItem(asEntityModel);
		asEntityModel.setSelected(true);
		asEntityModel.setIcon(getIcon("MenuEntityModel"));
		asEntityModel.setActionCommand(MENU_MODEL_SHOW_ENTITY_MODEL);
		settings.add(asEntityModel);
		
		frame.getFrame().setJMenuBar(menuBar);
		frame.getFrame().setTitle(getApplicationName() + " Model");
		frame.getFrame().setIconImage(getIconImage("FrameModel"));
		frame.getFrame().setMinimumSize(new Dimension(300,300));
		frame.getFrame().setPreferredSize(new Dimension(400,600));
		
		frame.renderComponent();

		frame.centreFrameLocation();
		
		return frame;
	}

	protected GuiDialog getNewDetailDialog() {
		GuiDialog dialog = new GuiDialog(DIALOG_DETAIL);

		GuiPanel panel = new GuiPanel(PANEL_DETAIL,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(0.999D);
				getPanelObjects().getRowWeights().add(0.001D);
				super.renderComponent();
			}
		};
		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 2;
		panel.getGridBagConstraints().ipady = 2;
		
		PnlDetail detailPanel = new PnlDetail(PANEL_DETAIL_PROPERTIES,0,0);

		detailPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		detailPanel.setFill(GridBagConstraints.BOTH);
		detailPanel.getPanel().setLayout(new GridBagLayout());
		detailPanel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		detailPanel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		detailPanel.getGridBagConstraints().weightx = 1;
		detailPanel.getGridBagConstraints().weighty = 1;
		detailPanel.getGridBagConstraints().gridheight = 1;
		detailPanel.getGridBagConstraints().gridwidth = 1;
		detailPanel.getGridBagConstraints().ipadx = 2;
		detailPanel.getGridBagConstraints().ipady = 2;
		detailPanel.getGridBagConstraints().insets = new Insets(2,2,2,2);
		
		panel.getPanelObjects().add(detailPanel);
		panel.getPanelObjects().add(new GuiButton(BUTTON_DETAIL_SAVE,1,0,"Save"));
		
		dialog.getPanelObjects().add(panel);

		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setMinimumSize(new Dimension(300,300));
		dialog.getDialog().setPreferredSize(new Dimension(700,500));
		dialog.getDialog().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	protected GuiDialog getNewInfoDialog() {
		GuiDialog dialog = new GuiDialog(DIALOG_INFO);

		GuiPanel panel = new GuiPanel(PANEL_INFO,0,0) {
			@Override
			public void renderComponent() {
				getPanelObjects().calculateWeights();
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(0.999D);
				getPanelObjects().getRowWeights().add(0.001D);
				super.renderComponent();
			}
		};
		panel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		panel.setFill(GridBagConstraints.BOTH);
		panel.getPanel().setLayout(new GridBagLayout());
		panel.getGridBagConstraints().fill = GridBagConstraints.BOTH;
		panel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
		panel.getGridBagConstraints().weightx = 1;
		panel.getGridBagConstraints().weighty = 1;
		panel.getGridBagConstraints().gridheight = 1;
		panel.getGridBagConstraints().gridwidth = 1;
		panel.getGridBagConstraints().ipadx = 2;
		panel.getGridBagConstraints().ipady = 2;
		
		PrpTextAreaString prpErr = new PrpTextAreaString(PROPERTY_INFO_TEXT,0,0,new DtString());
		prpErr.setAnchor(GridBagConstraints.FIRST_LINE_START);
		prpErr.setFill(GridBagConstraints.BOTH);
		GuiButton btnOk = new GuiButton(BUTTON_INFO_OK,1,0,"OK");
		
		panel.getPanelObjects().add(prpErr);
		panel.getPanelObjects().add(btnOk);
				
		dialog.getPanelObjects().add(panel);

		dialog.getDialog().setTitle("Information");
		dialog.getDialog().setAlwaysOnTop(true);
		dialog.getDialog().setMinimumSize(new Dimension(300,240));
		dialog.getDialog().setPreferredSize(new Dimension(600,240));
		dialog.getDialog().setIconImage(getIconImage("DialogInfo"));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	protected GuiDialog getNewErrorDialog() {
		GuiDialog dialog = getNewInfoDialog();
		dialog.setName(DIALOG_ERROR);

		dialog.getDialog().setTitle("Errors");
		dialog.getDialog().setIconImage(getIconImage("DialogError"));
		
		dialog.renderComponent();

		dialog.centreFrameLocation();

		return dialog;
	}

	public static Image getIconImage(String name) {
		Image r = null;
		String fileName = GuiConfig.getInstance().getFullIconDir() + name + ".png";
		File iconFile = new File(fileName);
		if (iconFile.exists()) {
			r = Toolkit.getDefaultToolkit().getImage(iconFile.getAbsolutePath());
		} else {
			Messenger.getInstance().warn(GuiConfig.getInstance().getGuiFactory(), "Icon image not found: " + fileName);
		}
		return r;
	}

	public static Icon getIcon(String name) {
		Icon ico = null;
		Image img = getIconImage(name);
		if (img!=null) {
	    	img = img.getScaledInstance(16, 16, 0);
        	ico = new ImageIcon(img);
        }
		return ico;
	}
}
