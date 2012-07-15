package com.github.wolf480pl.musicsearcher;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

public class TitleAddWindow extends JFrame {
	private CheckboxTree tree;
	public TitleAddWindow(CheckboxTree tree) {
		super("Add titles");
		this.tree = tree;
		JTextArea tarea = new JTextArea("Your titles...",15,40);
		JScrollPane textView = new JScrollPane(tarea);
		textView.setPreferredSize(tarea.getPreferredSize());
		JButton okbut = new JButton(new SubmitAction(this, tarea));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		GroupLayout layout = new GroupLayout(this.getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(textView).addComponent(okbut, GroupLayout.Alignment.TRAILING));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(textView).addComponent(okbut));
		this.setLayout(layout);
		this.pack();
	}
	public class SubmitAction extends AbstractAction {
		private TitleAddWindow window;
		private JTextArea area;
		public SubmitAction(TitleAddWindow window, JTextArea textarea) {
			super("OK", null);
			this.window = window;
			this.area = textarea;
		}

		public void actionPerformed(ActionEvent e) {
			String[] tits = area.getText().split("\n");
			DefaultTreeModel model = (DefaultTreeModel) window.tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot(); 
			for (String title : tits) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(title); 
				root.add(node);
				model.nodesWereInserted(root, new int[] {root.getIndex(node)});
			}
			window.setVisible(false);
			tree.repaint();
		}
	}
}
