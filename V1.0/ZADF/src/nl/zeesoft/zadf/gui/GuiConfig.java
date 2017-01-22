package nl.zeesoft.zadf.gui;

import nl.zeesoft.zadf.controller.ZADFEventHandler;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;

public final class GuiConfig {
	public final static	String			PROPERTY_MAXIMIZE_ON_START			= "maximizeOnStart";
	public final static	String			PROPERTY_LOAD_GUI_MODEL_ON_START	= "loadGuiModelOnStart";

	public final static	String			LABEL_MAXIMIZE_ON_START				= "Maximize on start";
	public final static	String			LABEL_LOAD_GUI_MODEL_ON_START		= "Load GUI model on start";

	public final static	String			HELP_MAXIMIZE_ON_START				= "Maximize the main window when the GUI starts";
	public final static	String			HELP_LOAD_GUI_MODEL_ON_START		= "Load the GUI model containing the menu selections and fetch/filter settings when time the GUI starts";

	private static GuiConfig			config								= null;
	
	private String						factoryClassName 					= ZADFFactory.class.getName();
	private String						eventHandlerClassName 				= ZADFEventHandler.class.getName();
	private String						iconDir 							= "icons/";
	private String						reportDir 							= "";
	private boolean						maximizeOnStart						= true;
	private boolean						loadGuiModelOnStart					= true;
	private	double						mainFrameResizeWeight				= 0.5D;
	
	private ZADFFactory					factory								= null;
	private ZADFEventHandler			eventHandler						= null;
	
	private GuiConfig() {
		// Singleton
	}

	public static GuiConfig getInstance() {
		if (config==null) {
			config = new GuiConfig();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public ZADFFactory getGuiFactory() {
		if (factory==null) {
			factory = (ZADFFactory) Generic.instanceForName(factoryClassName);
		}
		return factory;
	}

	public ZADFEventHandler getGuiEventHandler() {
		if (eventHandler==null) {
			eventHandler = (ZADFEventHandler) Generic.instanceForName(eventHandlerClassName);
		}
		return eventHandler;
	}

	public void serialize() {
		XMLFile f = toXml();
		f.writeFile(DbConfig.getInstance().getFullDataDir() + "GuiConfig.xml", f.toStringReadFormat());
		f.cleanUp();
	}
	
	public void unserialize() {
		XMLFile f = new XMLFile();
		f.parseFile(DbConfig.getInstance().getFullDataDir() + "GuiConfig.xml");
		fromXml(f);
		f.cleanUp();
	}

	private XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("config",null,null));
		new XMLElem("factoryClassName",new StringBuffer(factoryClassName),f.getRootElement());
		new XMLElem("controllerEventHandlerClassName",new StringBuffer(eventHandlerClassName),f.getRootElement());
		new XMLElem("iconDir",new StringBuffer(iconDir),f.getRootElement());
		new XMLElem("reportDir",new StringBuffer(reportDir),f.getRootElement());
		StringBuffer sb = new StringBuffer();
		sb.append(maximizeOnStart);
		new XMLElem(PROPERTY_MAXIMIZE_ON_START,sb,f.getRootElement());
		sb = new StringBuffer();
		sb.append(loadGuiModelOnStart);
		new XMLElem(PROPERTY_LOAD_GUI_MODEL_ON_START,sb,f.getRootElement());
		sb = new StringBuffer();
		sb.append(mainFrameResizeWeight);
		new XMLElem("mainFrameResizeWeight",sb,f.getRootElement());
		return f;
	}

	private void fromXml(XMLFile f) {
		if ((f.getRootElement()!=null) && (f.getRootElement().getName().equals("config"))) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals("factoryClassName")) {
					factoryClassName = v.getValue().toString();
				}
				if (v.getName().equals("controllerEventHandlerClassName")) {
					eventHandlerClassName = v.getValue().toString();
				}
				if (v.getName().equals("iconDir")) {
					setIconDir(v.getValue().toString());
				}
				if (v.getName().equals("reportDir")) {
					setReportDir(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_MAXIMIZE_ON_START)) {
					maximizeOnStart = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_LOAD_GUI_MODEL_ON_START)) {
					loadGuiModelOnStart = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("mainFrameResizeWeight")) {
					setMainFrameResizeWeight(Double.parseDouble(v.getValue().toString()));
				}
			}
		}
	}
	
	public String getFullIconDir() {
		return Generic.dirName(DbConfig.getInstance().getInstallDir() + iconDir);
	}
	
	/**
	 * @param factoryClassName the factoryClassName to set
	 */
	public void setFactoryClassName(String factoryClassName) {
		this.factoryClassName = factoryClassName;
	}

	/**
	 * @param controllerEventHandlerClassName the controllerEventHandlerClassName to set
	 */
	public void setControllerEventHandlerClassName(String controllerEventHandlerClassName) {
		this.eventHandlerClassName = controllerEventHandlerClassName;
	}

	/**
	 * @return the iconDir
	 */
	public String getIconDir() {
		return iconDir;
	}

	/**
	 * @param iconDir the iconDir to set
	 */
	public void setIconDir(String iconDir) {
		this.iconDir = Generic.dirName(iconDir);
	}


	/**
	 * @return the reportDir
	 */
	public String getReportDir() {
		return reportDir;
	}

	/**
	 * @param reportDir the reportDir to set
	 */
	public void setReportDir(String reportDir) {
		this.reportDir = Generic.dirName(reportDir);
	}

	/**
	 * @return the maximizeOnStart
	 */
	public boolean isMaximizeOnStart() {
		return maximizeOnStart;
	}

	/**
	 * @param maximizeOnStart the maximizeOnStart to set
	 */
	public void setMaximizeOnStart(boolean maximizeOnStart) {
		this.maximizeOnStart = maximizeOnStart;
	}
	
	/**
	 * @return the loadGuiModelOnStart
	 */
	public boolean isLoadGuiModelOnStart() {
		return loadGuiModelOnStart;
	}

	/**
	 * @param loadGuiModelOnStart the loadGuiModelOnStart to set
	 */
	public void setLoadGuiModelOnStart(boolean loadGuiModelOnStart) {
		this.loadGuiModelOnStart = loadGuiModelOnStart;
	}

	/**
	 * @return the mainFrameResizeWeight
	 */
	public double getMainFrameResizeWeight() {
		return mainFrameResizeWeight;
	}

	/**
	 * @param mainFrameResizeWeight the mainFrameResizeWeight to set
	 */
	public void setMainFrameResizeWeight(double mainFrameResizeWeight) {
		if (mainFrameResizeWeight>0.9) {
			mainFrameResizeWeight = 0.9;
		} else if (mainFrameResizeWeight<0.1) {
			mainFrameResizeWeight = 0.1;
		}
		this.mainFrameResizeWeight = mainFrameResizeWeight;
	}
}
