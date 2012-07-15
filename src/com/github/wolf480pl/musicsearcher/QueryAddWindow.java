package com.github.wolf480pl.musicsearcher;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class QueryAddWindow extends JFrame {
	private JTextField tfield;
	private JCheckBox zippyChk, ulubChk;
	private CheckboxTree tree;

	public QueryAddWindow(CheckboxTree tree) {
		super("Add Query");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.tree = tree;
		tfield = new JTextField(40);
		zippyChk = new JCheckBox("zippyshare");
		ulubChk = new JCheckBox("ulub");
		JButton butok = new JButton(new SubmitAction(this));
		GroupLayout layout = new GroupLayout(this.getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(tfield).addComponent(zippyChk).addComponent(ulubChk).addComponent(butok));
		layout.setVerticalGroup(layout.createParallelGroup().addComponent(tfield).addComponent(zippyChk).addComponent(ulubChk).addComponent(butok));
		this.setLayout(layout);
		this.pack();
	}
	public class SubmitAction extends AbstractAction {
		private QueryAddWindow window;
		public SubmitAction(QueryAddWindow window) {
			super("OK", null);
			this.window = window;
		}
		public void actionPerformed(ActionEvent e) {
			TreePath[] checked = window.tree.getCheckingPaths();
			String query = window.tfield.getText();
			boolean zippy = window.zippyChk.isSelected();
			boolean ulub = window.ulubChk.isSelected();
			for (TreePath path : checked) {
				if (path.getPathCount() == 2) {
					DefaultMutableTreeNode title = (DefaultMutableTreeNode) path.getLastPathComponent();
					Query q = new Query(title, query, zippy, ulub);
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(q);
					title.add(node);
					((DefaultTreeModel) window.tree.getModel()).nodesWereInserted(title, new int[] {title.getIndex(node)});
				}
			}
			window.setVisible(false);
		}
	}
}
