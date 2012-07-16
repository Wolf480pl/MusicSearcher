package com.github.wolf480pl.musicsearcher;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SearchAction extends AbstractAction {
	private MusicSearcher window;
	private WebClient client;
	public int limit;
	SearchThread thread;

	public SearchAction(String name, ImageIcon icon, MusicSearcher window) {
		super(name, icon);
		this.window = window;
		this.limit = 1;
	}

	public void actionPerformed(ActionEvent e) {
		Query[] queries = this.collectQueries(this.window.tree);
		thread = new SearchThread(window, queries, limit);
		thread.start();
	}

	private Query[] collectQueries(CheckboxTree tree) {
		List<Query> queries = new ArrayList<Query>();
		for (TreePath path : tree.getCheckingPaths()) {
			if (path.getPathCount() == 3) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				if (node.getUserObject() instanceof Query) {
					queries.add((Query) node.getUserObject());
				}
			}
		}
		return queries.toArray(new Query[0]);
	}
}
