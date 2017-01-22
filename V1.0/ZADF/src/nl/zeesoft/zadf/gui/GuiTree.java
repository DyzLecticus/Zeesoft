package nl.zeesoft.zadf.gui;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import nl.zeesoft.zadf.controller.GuiController;

public class GuiTree extends GuiPanelObject {
	private JScrollPane				scrollPanel	= null;

	private DefaultTreeModel		treeModel	= null;
	private JTree					tree		= null;
	private DefaultMutableTreeNode	treeTop		= null;

	public GuiTree(String name, int row, int column, Object root) {
		super(name, row, column);
		treeTop = new DefaultMutableTreeNode(root);
		treeModel = new DefaultTreeModel(treeTop);
		tree = new JTree(treeTop);
		
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setModel(treeModel);
        
        scrollPanel = new JScrollPane(tree);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(10);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(scrollPanel);
	        tree.addTreeSelectionListener(GuiController.getInstance());
		}
	}

	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject r = super.getGuiObjectForSourceComponent(source);
		if (source==tree) {
			r = this;
		}
		return r;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tree.setEnabled(enabled);
	}

	public void expandAll() {
		int row = 0;
		while (row < tree.getRowCount()) {
			tree.expandRow(row);
			row++;
		}
	}

	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * @return the treeTop
	 */
	public DefaultMutableTreeNode getTreeTop() {
		return treeTop;
	}

	/**
	 * @return the treeModel
	 */
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * @return the scrollPanel
	 */
	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}
}
