package nl.zeesoft.zadf.controller;

import java.awt.Component;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiTree;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;

public class GuiTreeController extends GuiObjectController implements EvtEventSubscriber {
	public static final String							TREE_VALUE_CHANGED	= "TREE_VALUE_CHANGED";
	
	private boolean 									showEntityModel 	= true;
	private Object										selectedObject		= null;

	private boolean										showAsSubTree		= false;
	private int 										maximumDepth 		= 0;
	private Object										topObject			= null;
	private	DbCollection								collection			= null;
	
	private SortedMap<String,DefaultMutableTreeNode>	objectNodeMap		= new TreeMap<String,DefaultMutableTreeNode>();
	private MdlDataObject								selectObject		= null;
	
	public GuiTreeController(GuiTree guiTree, boolean asSubTree) {
		super(guiTree);
		showAsSubTree = asSubTree;
		if (showAsSubTree) {
			maximumDepth = 1;
		}
		ToolTipManager.sharedInstance().registerComponent(guiTree.getTree());
		guiTree.getTree().setCellRenderer(new DefaultRenderer());
		guiTree.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		GuiTree tree = (GuiTree) getGuiObject();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getTree().getLastSelectedPathComponent();
		if (node==null) {
			selectedObject = null;
		} else {
			selectedObject = node.getUserObject();
		}
		
		if ((selectedObject!=null) && 
			(selectedObject instanceof TreeObject) && 
			(((TreeObject) selectedObject).getObject() instanceof MdlDataObject)
			) {
			GuiModel.getInstance().setContextObject(getContext(),((MdlDataObject) ((TreeObject) selectedObject).getObject()));
		}
		
		publishEvent(new EvtEvent(TREE_VALUE_CHANGED,this,"Selected tree value changed"));
	}
	
	public DbCollection getSelectedCollection() {
		DbCollection r = null;
		if ((selectedObject!=null) && (selectedObject instanceof TreeObject)) {
			TreeObject tObj = (TreeObject) selectedObject;
			if (tObj.getObject() instanceof DbCollection) {
				r = (DbCollection) tObj.getObject();
			}
		}
		return r;
	}

	public boolean setSelectedCollection(DbCollection c) {
		return setSelectedObject(c);
	}
	
	public DbCollectionProperty getSelectedProperty() {
		DbCollectionProperty r = null;
		if ((selectedObject!=null) && (selectedObject instanceof TreeObject)) {
			TreeObject tObj = (TreeObject) selectedObject;
			if (tObj.getObject() instanceof DbCollectionProperty) {
				r = (DbCollectionProperty) tObj.getObject();
			}
		}
		return r;
	}

	public boolean setSelectedProperty(DbCollectionProperty p) {
		return setSelectedObject(p);
	}

	public DbModule getSelectedModule() {
		DbModule r = null;
		if ((selectedObject!=null) && (selectedObject instanceof TreeObject)) {
			TreeObject tObj = (TreeObject) selectedObject;
			if (tObj.getObject() instanceof DbModule) {
				r = (DbModule) tObj.getObject();
			}
		}
		return r;
	}

	public String getSelectedString() {
		String r = "";
		if ((selectedObject!=null) && (selectedObject instanceof String)) {
			r = (String) selectedObject;
		}
		return r;
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbModel.LOADED_MODEL)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			//buildTreeNodes();
			if (!showAsSubTree) {
			    SwingUtilities.invokeLater(new Runnable() {
			    	public void run() {
			    		buildTreeNodes();
			      	}
			    });
			}
		} else if (e.getType().equals(GuiTreeController.TREE_VALUE_CHANGED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			GuiTreeController tc = (GuiTreeController) e.getSource();
			if (tc.isShowAsSubTree()) {
				if (tc.getSelectedProperty()!=null) {
					setCollection(tc.getSelectedProperty().getColl());
				} else {
					setCollection(null);
				}
			} else {
				setCollection(tc.getSelectedCollection());
			}
		}
	}

	protected void buildTreeNodes() {
		objectNodeMap.clear();
		selectObject = null;
		if (showAsSubTree) {
			GuiTree tree = (GuiTree) getGuiObject();
	    	tree.getTreeTop().removeAllChildren();
	    	tree.getTreeModel().nodeStructureChanged(tree.getTreeTop());
			if (collection!=null) {
				String cn = collection.getName().getValue();
		    	Icon icon = ZADFFactory.getIcon(cn);
		    	String nameSingle = collection.getNameSingle().getValue();
		    	TreeObject tObj = new TreeObject(icon,nameSingle,collection,collection.getToolTipText());
		    	tree.getTreeTop().setUserObject(tObj);
		    	tree.getTreeModel().nodeChanged(tree.getTreeTop());
				buildCollectionNodes(0,tree.getTreeTop());

				if ((tObj.getObject()!=null) && (tObj.getObject() instanceof MdlDataObject)) {
					objectNodeMap.put(getKeyForObject((MdlDataObject) tObj.getObject()), tree.getTreeTop());
					selectObject = (MdlDataObject) tObj.getObject();
				}

				MdlDataObject obj = GuiModel.getInstance().getObjectForContext(getContext());
				if ((obj!=null) && (objectNodeMap.containsKey(getKeyForObject(obj)))) {
					selectObject = obj;
				}
				
				tree.expandAll();
				if (selectObject!=null) {
					setSelectedObject(selectObject);
				} else {
					tree.getTree().setSelectionInterval(0,0);
				}
			} else {
				tree.getTreeTop().setUserObject("");			
				tree.getTreeModel().nodeChanged(tree.getTreeTop());
				tree.getTree().setSelectionInterval(0,0);
			}
		} else {
			GuiTree tree = (GuiTree) getGuiObject();
	    	tree.getTreeTop().removeAllChildren();
	    	tree.getTreeModel().nodeStructureChanged(tree.getTreeTop());
	    	
	    	for (DbModule dbMod: DbModel.getInstance().getModules()) {	
	    		DefaultMutableTreeNode moduleNode = null;
	    		for (DbCollection dbCol: dbMod.getCollections()) {
					if (((!showEntityModel) || ((showEntityModel) && (dbCol.getEntity().getValue()))) && 
						(dbCol.getUserLevelFetch().getValue()>=GuiController.getInstance().getSession().getUserLevel())
						) {
						if (moduleNode==null) {
							moduleNode = addNode(new TreeObject(ZADFFactory.getIcon("Module"),dbMod.getLabel().getValue(),dbMod,dbMod.getToolTipText()),tree.getTreeTop());
							objectNodeMap.put(getKeyForObject(dbMod), moduleNode);
						}
						String cn = dbCol.getName().getValue();
			        	Icon icon = ZADFFactory.getIcon(cn);
			        	String nameMulti = dbCol.getNameMulti().getValue();
				    	String toolTip = dbCol.getToolTipText();
			        	DefaultMutableTreeNode collectionNode = addNode(new TreeObject(icon,nameMulti,dbCol,toolTip),moduleNode);
						objectNodeMap.put(getKeyForObject(dbCol), collectionNode);
						buildCollectionNodes(0,collectionNode);
					}
	    		}
	    	}
	    	
			MdlDataObject obj = GuiModel.getInstance().getObjectForContext(getContext());
			if ((obj!=null) && (objectNodeMap.containsKey(getKeyForObject(obj)))) {
				selectObject = obj;
			}

			tree.expandAll();
			if (selectObject!=null) {
				setSelectedObject(selectObject);
			} else {
				tree.getTree().setSelectionInterval(0,0);
			}
		}
	}

	protected void buildCollectionNodes(int depth, DefaultMutableTreeNode parent) {
    	if (depth>=maximumDepth) {
			return;
		}

		TreeObject treeObj = (TreeObject) parent.getUserObject();
		DbCollection pDbCol = null;
		if (treeObj.getObject() instanceof DbCollection) {
			pDbCol = (DbCollection) treeObj.getObject();
		} else if (treeObj.getObject() instanceof DbCollectionProperty) {
			pDbCol = ((DbCollectionProperty) treeObj.getObject()).getColl();
		}
    	if (pDbCol!=null) {
			for (DbCollectionProperty refProp: pDbCol.getReferencedProperties()) {
				DbCollection refCol = refProp.getColl();
				if (((!showEntityModel) || ((showEntityModel) && (refProp.getEntity().getValue()))) && 
					(refCol.getUserLevelFetch().getValue()>=GuiController.getInstance().getSession().getUserLevel()) &&
					(!refProp.getName().getValue().equals(MdlObject.PROPERTY_CREATEDBY)) &&
					(!refProp.getName().getValue().equals(MdlObject.PROPERTY_CHANGEDBY))
					) {
					String cn = refCol.getName().getValue();
			    	Icon icon = ZADFFactory.getIcon(cn);
			    	String nameMulti = refProp.getEntityLabel().getValue();
			    	String toolTip = refProp.getColl().getToolTipText();
			    	String relation = "N:1";
			    	if (refProp.getPropertyClassName().equals(PrpIdRefList.class.getName())) {
			    		relation = "N:N";
			    	}
			    	if (!showEntityModel) {
			    		nameMulti = nameMulti + " (" + refProp.getColl().getNameSingle() + "." + refProp.getLabel().getValue() + " / " + relation + ")";
			    	}
			    	TreeObject object = new TreeObject(icon,nameMulti,refProp,toolTip);
					if (!branchContainsObject(object,parent)) {
						DefaultMutableTreeNode collectionNode = addNode(object,parent);
						objectNodeMap.put(getKeyForObject(refProp), collectionNode);
				    	if (!refCol.getName().getValue().equals(pDbCol.getName().getValue())) {
							buildCollectionNodes((depth + 1),collectionNode);
						}
					}
				}
			}
    	}
	}
	
	private boolean branchContainsObject(TreeObject object,DefaultMutableTreeNode parent) {
		boolean contains = false;
		if ((parent.getUserObject() instanceof TreeObject) &&
			((TreeObject) parent.getUserObject()).getObject().equals(object.getObject())
			) {
			contains = true;
		} else if ((parent.getParent()!=null) && (parent.getParent() instanceof DefaultMutableTreeNode)) {
			contains = branchContainsObject(object,(DefaultMutableTreeNode) parent.getParent());
		}
		return contains;
	}
	
	private String getContext() {
		String context = "" + showAsSubTree + Generic.SEP_STR + maximumDepth;
		if (collection!=null) {
			context = context + Generic.SEP_STR + collection.getName().getValue();
		} else {
			context = context + Generic.SEP_STR;
		}
		return context;
	}
	
	private boolean setSelectedObject(MdlDataObject o) {
		boolean selected = false;
		GuiTree tree = (GuiTree) getGuiObject();
		if (objectNodeMap.containsKey(getKeyForObject(o))) {
			DefaultMutableTreeNode node = objectNodeMap.get(getKeyForObject(o));
			tree.getTree().setSelectionPath(new TreePath(node.getPath()));
			selected = true;
		}
		return selected;
	}

	private DefaultMutableTreeNode addNode(TreeObject tObj, DefaultMutableTreeNode parentNode) {
    	DefaultMutableTreeNode tNode = new DefaultMutableTreeNode(tObj);
		GuiTree tree = (GuiTree) getGuiObject();
		tree.getTreeModel().insertNodeInto(tNode, parentNode,0);
		if ((tObj.getObject()!=null) && (tObj.getObject() instanceof MdlDataObject)) {
			objectNodeMap.put(getKeyForObject((MdlDataObject) tObj.getObject()), tNode);
			selectObject = (MdlDataObject) tObj.getObject();
		}
		return tNode;
    }
	
	private String getKeyForObject(MdlDataObject obj) {
		String key = "";
		if (obj instanceof DbCollection) {
			DbCollection c = (DbCollection) obj;
			key = c.getName().getValue();
		} else if (obj instanceof DbCollectionProperty) {
			DbCollectionProperty p = (DbCollectionProperty) obj;
			key = p.getColl().getName().getValue() + Generic.SEP_STR + p.getName().getValue();
		} else if (obj instanceof DbModule) {
			DbModule m = (DbModule) obj;
			key = m.getName().getValue() + Generic.SEP_STR + m.getLabel().getValue();
		}
		return key;
	}

	private class DefaultRenderer extends DefaultTreeCellRenderer {
		public static final long serialVersionUID = 0;
		
		private Icon defaultIcon = null;
		public DefaultRenderer() {
			this.defaultIcon = ZADFFactory.getIcon("FrameMain");
		}
		
		public Component getTreeCellRendererComponent(
	                        JTree tree,
	                        Object value,
	                        boolean sel,
	                        boolean expanded,
	                        boolean leaf,
	                        int row,
	                        boolean hasFocus) {
	        super.getTreeCellRendererComponent(
	                        tree, value, sel,
	                        expanded, leaf, row,
	                        hasFocus);
			
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	        Icon icon = null;
	        if (node.getUserObject() instanceof TreeObject) {
	        	TreeObject tObj = (TreeObject) node.getUserObject();
	        	icon = tObj.getIcon();
	        	if (tObj.getToolTipText()!=null) {
	        		setToolTipText(tObj.getToolTipText());
	        	}
	        } else if (node.getUserObject() instanceof String) {
	        	String txt = (String) node.getUserObject();
	        	if (txt.equals("")) {
	        		txt = "Empty node";
	        	} else {
	        		if (txt.equals(GuiConfig.getInstance().getGuiFactory().getApplicationName())) {
	        			txt = GuiConfig.getInstance().getGuiFactory().getToolTipText();
	        		}
	        	}
	        	setToolTipText(txt);
	        }
	        if (icon==null) {
	        	icon = defaultIcon;
	        }
        	setIcon(icon);
	        return this;
	    }
	}
	
	protected class TreeObject {
		private Icon	icon		= null;
		private String 	name		= "";
		private	Object	object		= null;
		private String	toolTipText	= null;

		public TreeObject(Icon i, String n, Object o, String tip) {
			icon = i;
			name = n;
			object = o;
			toolTipText = tip;
		}
		
		public Icon getIcon() {
			return icon;
		}

		@Override
		public String toString() {
			return name;
		}

		public Object getObject() {
			return object;
		}

		public String getToolTipText() {
			return toolTipText;
		}
	}

	
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(DbCollection collection) {
		this.collection = collection;
		buildTreeNodes();
	}

	/**
	 * @param showAsSubTree the showAsSubTree to set
	 */
	public void setShowAsSubTree(boolean showAsSubTree) {
		this.showAsSubTree = showAsSubTree;
		if (showAsSubTree) {
			maximumDepth = 1;
			if (topObject==null) {
				GuiTree tree = (GuiTree) getGuiObject();
				topObject = tree.getTreeTop().getUserObject();
			}
		} else {
			maximumDepth = 0;
			if (topObject!=null) {
				GuiTree tree = (GuiTree) getGuiObject();
				tree.getTreeTop().setUserObject(topObject);
				tree.getTreeModel().nodeChanged(tree.getTreeTop());
			}
		}
	}

	/**
	 * @return the showAsSubTree
	 */
	public boolean isShowAsSubTree() {
		return showAsSubTree;
	}

	/**
	 * @return the showEntityModel
	 */
	public boolean isShowEntityModel() {
		return showEntityModel;
	}

	/**
	 * @param showEntityModel the showEntityModel to set
	 */
	public void setShowEntityModel(boolean showEntityModel) {
		if (this.showEntityModel!=showEntityModel) {
			this.showEntityModel = showEntityModel;
			buildTreeNodes();
		}
	}

	/**
	 * @param maximumDepth the maximumDepth to set
	 */
	public void setMaximumDepth(int maximumDepth) {
		if (this.maximumDepth!=maximumDepth) {
			this.maximumDepth = maximumDepth;
			buildTreeNodes();
		}
	}
	
}
