package com.github.wolf480pl.musicsearcher;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gargoylesoftware.htmlunit.WebClient;

public class SearchAction extends AbstractAction {
	private MusicSearcher window;
	private WebClient client;

	public SearchAction(String name, ImageIcon icon, MusicSearcher window) {
		super(name, icon);
		this.window = window;
	}

	public void actionPerformed(ActionEvent e) {
		client = new WebClient();
		
	}
	private Query[] collectQueries(CheckboxTree tree) {
		List<Query> queries = new ArrayList<Query>(); 
		for (TreePath path : tree.getCheckingPaths()) {
			if (path.getPathCount() == 3) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node.getUserObject() instanceof Query) {
					queries.add((Query) node.getUserObject());
				}
			}
		}
		return (Query[]) queries.toArray();
	}
	private List<Result> searchZippy(Query query, int limit) {
		List<Result> results = new ArrayList<Result>();
		try {
		String q = URLEncoder.encode(query.getEffectiveQuery(), "UTF-8");
		String adress = "http://www.google.pl/cse?cx=partner-pub-7156910227905046%3Aqz4zdxnajq4&cof=FORID%3A9&ie=ISO-8859-2&q="
				+ q
				+ "&sa=Szukaj&siteurl=zippysearch.pl%2F&ref=&ad=n9&num=10#gsc.tab=0&gsc.q="
				+ q
				+ "&gsc.page=1";
		URL url = new URL(adress);
		client.getPage(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
}
