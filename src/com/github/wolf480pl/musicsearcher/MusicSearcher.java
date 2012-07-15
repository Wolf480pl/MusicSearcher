package com.github.wolf480pl.musicsearcher;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.gargoylesoftware.htmlunit.WebClient;

public class MusicSearcher extends JFrame {

	private static final long serialVersionUID = 1L;
	private CheckboxTree tree;
	private JTextField editField;
	private JCheckBox zippyChk, ulubChk;
	private JButton applyBut;
	private JRadioButton radioS, radioE;
	private MyTableModel resultModel;

	public MusicSearcher() {
		this.initComponents();
	}

	private void initComponents() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Korzen");
		DefaultMutableTreeNode nod1 = new DefaultMutableTreeNode("Galaz");
		DefaultMutableTreeNode nod2 = new DefaultMutableTreeNode("Lisc1", false);
		nod1.add(nod2);
		top.add(nod1);
		nod1 = new DefaultMutableTreeNode("Lisc2", true);
		top.add(nod1);
		this.tree = new CheckboxTree(top);
		this.tree.addTreeSelectionListener(new EditTSListener(this));
		// tree.setRootVisible(false);
		JScrollPane treeView = new JScrollPane(this.tree);
		treeView.setPreferredSize(new Dimension(520, 250));
		ImageIcon addIcon = this.createImageIcon("add_small.png", "add");
		ImageIcon delIcon = this.createImageIcon("remove_small.png", "remove");
		JButton titleABut = new JButton(new ShowWindowAction("Add Titles",
				addIcon, new TitleAddWindow(this.tree)));
		JButton queryABut = new JButton(new ShowWindowAction("Add Query",
				addIcon, new QueryAddWindow(this.tree)));
		JButton titleDBut = new JButton(new TreeDeleteItemAction("Delete",
				delIcon, this.tree, 2));
		JButton queryDBut = new JButton(new TreeDeleteItemAction(
				"Delete Query", delIcon, this.tree, 3));
		this.editField = new JTextField(15);
		Dimension dim = this.editField.getMaximumSize();
		dim.height = 20;
		this.editField.setMaximumSize(dim);
		ButtonGroup group = new ButtonGroup();
		this.radioS = new JRadioButton("Title-dependent");
		this.radioE = new JRadioButton("Title-independent");
		this.radioS.setActionCommand("source");
		this.radioS.addActionListener(new ModeListener(this));
		this.radioS.setSelected(true);
		this.radioE.setActionCommand("effective");
		this.radioE.addActionListener(new ModeListener(this));
		group.add(this.radioS);
		group.add(this.radioE);
		this.zippyChk = new JCheckBox("zippyshare");
		this.ulubChk = new JCheckBox("ulub");
		this.applyBut = new JButton(new NodeUpdateAction("Apply", null, this));
		JButton searchBut = new JButton("Search");
		dim = searchBut.getMaximumSize();
		dim.width = 700;
		searchBut.setMaximumSize(dim);
		this.resultModel = new MyTableModel(
				new String[] { "", "Title", "Site", "Size", "Time", "kbps",
						"link", "directlink" },
				new Class[] { Boolean.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class },
				new boolean[] { true, false, false, false, false, false, false,
						false });
		JTable results = new JTable(this.resultModel);
		results.setPreferredScrollableViewportSize(new Dimension(700, 100));
		results.setFillsViewportHeight(true);
		this.resultModel.addRow(new Object[] { false, "Ala", "ma", "kota", "a",
				"kot", "ma", "Ale" });
		JScrollPane resultView = new JScrollPane(results);
		GroupLayout layout = new GroupLayout(this.getContentPane());
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout
				.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup().addComponent(titleABut)
								.addComponent(queryABut)
								.addComponent(titleDBut)
								.addComponent(queryDBut))
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(treeView)
								.addGroup(
										layout.createParallelGroup()
												.addComponent(this.editField)
												.addComponent(this.radioS)
												.addComponent(this.radioE)
												.addComponent(this.zippyChk)
												.addComponent(this.ulubChk)
												.addComponent(this.applyBut)))
				.addComponent(searchBut, GroupLayout.Alignment.CENTER)
				.addComponent(resultView));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup().addComponent(titleABut)
								.addComponent(queryABut)
								.addComponent(titleDBut)
								.addComponent(queryDBut))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(treeView)
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(this.editField)
												.addComponent(this.radioS)
												.addComponent(this.radioE)
												.addComponent(this.zippyChk)
												.addComponent(this.ulubChk)
												.addComponent(this.applyBut)))
				.addComponent(searchBut)
				.addComponent(resultView));
		this.setLayout(layout);
		// this.add(treeView);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.pack();
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MusicSearcher().setVisible(true);
			}
		});
	}

	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = this.getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void displayEdit(TreePath path, boolean tit_dep) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		switch (path.getPathCount()) {
		case 2:
			this.editField.setText(node.getUserObject().toString());
			this.editField.setEnabled(true);
			this.radioS.setSelected(true);
			this.radioS.setEnabled(false);
			this.radioE.setEnabled(false);
			this.zippyChk.setSelected(false);
			this.zippyChk.setEnabled(false);
			this.ulubChk.setSelected(false);
			this.ulubChk.setEnabled(false);
			this.applyBut.setEnabled(true);
			break;
		case 3:
			if (node.getUserObject() instanceof Query) {
				Query q = (Query) node.getUserObject();
				this.editField.setText(tit_dep ? q.getSourceQuery() : q
						.getEffectiveQuery());
				this.editField.setEnabled(true);
				(tit_dep ? this.radioS : this.radioE).setSelected(true);
				this.radioS.setEnabled(true);
				this.radioE.setEnabled(true);
				this.zippyChk.setSelected(q.isZippy());
				this.zippyChk.setEnabled(true);
				this.ulubChk.setSelected(q.isUlub());
				this.ulubChk.setEnabled(true);
				this.applyBut.setEnabled(true);
			} else {
				this.editField.setText("");
				this.editField.setEnabled(false);
				this.radioS.setSelected(true);
				this.radioS.setEnabled(false);
				this.radioE.setEnabled(false);
				this.zippyChk.setSelected(false);
				this.zippyChk.setEnabled(false);
				this.ulubChk.setSelected(false);
				this.ulubChk.setEnabled(false);
				this.applyBut.setEnabled(false);
			}
			break;
		default:
			this.editField.setText("");
			this.editField.setEnabled(false);
			this.radioS.setSelected(true);
			this.radioS.setEnabled(false);
			this.radioE.setEnabled(false);
			this.zippyChk.setSelected(false);
			this.zippyChk.setEnabled(false);
			this.ulubChk.setSelected(false);
			this.ulubChk.setEnabled(false);
			this.applyBut.setEnabled(false);
			break;
		}
	}

	public class ShowWindowAction extends AbstractAction {
		private JFrame window;

		public ShowWindowAction(String text, ImageIcon icon, JFrame window) {
			super(text, icon);
			this.window = window;
		}

		public void actionPerformed(ActionEvent e) {
			this.window.setVisible(true);
		}
	}

	public class TreeDeleteItemAction extends AbstractAction {
		private CheckboxTree tree;
		private int minlevel;

		public TreeDeleteItemAction(String text, ImageIcon icon,
				CheckboxTree tree, int minlevel) {
			super(text, icon);
			this.tree = tree;
			this.minlevel = minlevel;
		}

		public void actionPerformed(ActionEvent e) {
			TreePath[] checked = this.tree.getCheckingPaths();
			for (TreePath path : checked) {
				MutableTreeNode node = (MutableTreeNode) path
						.getLastPathComponent();
				if (path.getPathCount() >= this.minlevel) {
					this.tree.removeCheckingPath(path);
					DefaultTreeModel model = (DefaultTreeModel) this.tree
							.getModel();
					model.removeNodeFromParent(node);
				}
			}
		}
	}

	public static class NodeUpdateAction extends AbstractAction {
		private MusicSearcher window;

		public NodeUpdateAction(String text, ImageIcon icon,
				MusicSearcher window) {
			super(text, icon);
			this.window = window;
		}

		public void actionPerformed(ActionEvent e) {
			TreePath path = this.window.tree.getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			switch (path.getPathCount()) {
			case 2:
				node.setUserObject(this.window.editField.getText());
				break;
			case 3:
				if (node.getUserObject() instanceof Query) {
					Query q = (Query) node.getUserObject();
					q.setQuery(this.window.editField.getText());
					q.setZippy(this.window.zippyChk.isSelected());
					q.setUlub(this.window.ulubChk.isSelected());
				}
				break;
			}
			((DefaultTreeModel) this.window.tree.getModel()).nodeChanged(node);
			if (path.getPathCount() == 2) {
				((DefaultTreeModel) this.window.tree.getModel())
						.nodeStructureChanged(node);
			}
		}
	}

	public class EditTSListener implements TreeSelectionListener {
		private MusicSearcher window;

		public EditTSListener(MusicSearcher window) {
			this.window = window;
		}

		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = this.window.tree.getSelectionPath();
			this.window.displayEdit(path, true);
		}
	}

	public class ModeListener implements ActionListener {
		private MusicSearcher window;

		public ModeListener(MusicSearcher window) {
			this.window = window;
		}

		public void actionPerformed(ActionEvent e) {
			TreePath path = this.window.tree.getSelectionPath();
			this.window.displayEdit(path, e.getActionCommand()
					.equalsIgnoreCase("source"));
		}
	}

	public class MyTableModel extends AbstractTableModel {
		private MyColumn[] columns;
		private List<Object[]> data;

		public MyTableModel(String[] columnNames, Class<Object>[] columnTypes,
				boolean[] columnEditable) {
			int count = Math.min(
					Math.min(columnEditable.length, columnTypes.length),
					columnNames.length);
			this.columns = new MyColumn[count];
			for (int i = 0; i < count; ++i) {
				this.columns[i] = new MyColumn(columnNames[i], columnTypes[i],
						columnEditable[i]);
			}
			this.data = new ArrayList<Object[]>();
		}

		public int getRowCount() {
			return this.data.size();
		}

		public int getColumnCount() {
			return this.columns.length;
		}

		@Override
		public String getColumnName(int col) {
			return this.columns[col].name;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return this.data.get(rowIndex)[columnIndex];
		}

		@Override
		public Class getColumnClass(int col) {
			return this.columns[col].type;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return this.columns[col].editable;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			if (this.columns[col].type.isInstance(value)) {
				this.data.get(row)[col] = value;
			}
		}

		@Override
		public int findColumn(String name) {
			for (int i = 0; i < this.columns.length; ++i) {
				if (this.columns[i].name.equalsIgnoreCase(name)) {
					return i;
				}
			}
			return -1;
		}

		public void insertRow(int rowIndex, Object[] data) {
			if (data.length == this.columns.length) {
				this.data.add(rowIndex, data);
			}
		}

		public void addRow(Object[] data) {
			if (data.length == this.columns.length) {
				this.data.add(data);
			}
		}

		protected class MyColumn {
			public String name;
			public Class<Object> type;
			public boolean editable;

			public MyColumn(String name, Class type, boolean editable) {
				this.name = name;
				this.type = type;
				this.editable = editable;
			}
		}
	}
}