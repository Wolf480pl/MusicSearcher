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
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SearchAction extends AbstractAction {
	private MusicSearcher window;
	private WebClient client;
	public int limit;

	public SearchAction(String name, ImageIcon icon, MusicSearcher window) {
		super(name, icon);
		this.window = window;
		this.limit = 1;
	}

	public void actionPerformed(ActionEvent e) {
		this.client = new WebClient();
		Query[] queries = this.collectQueries(this.window.tree);
		for (Query q : queries) {
			if (q.isZippy()) {
				q.addResults(this.searchZippy(q,
						Math.min(this.limit, q.getLimit())));
			}
			if (q.isUlub()) {
				q.addResults(this.searchUlub(q,
						Math.min(this.limit, q.getLimit())));
			}
		}
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

	private List<Result> searchZippy(Query query, int limit) {
		List<Result> results = new ArrayList<Result>();
		try {
			String q = URLEncoder.encode(query.getEffectiveQuery(), "UTF-8");
			String adress = "http://www.google.pl/cse?cx=partner-pub-7156910227905046%3Aqz4zdxnajq4&cof=FORID%3A9&ie=ISO-8859-2&q="
					+ q
					+ "&sa=Szukaj&siteurl=zippysearch.pl%2F&ref=&ad=n9&num="
					+ limit
					+ "#gsc.tab=0&gsc.q="
					+ q
					+ "&gsc.page=1";
			URL url = new URL(adress);
			HtmlPage page = this.client.getPage(url);
			HtmlElement div_cse = page.getElementById("cse");
			HtmlElement div_results = div_cse.getElementsByAttribute("div",
					"class", "gsc-results gsc-webResult").get(0);
			List<HtmlElement> resultTables = div_results
					.getElementsByAttribute("table", "class",
							"gsc-table-result");
			for (HtmlElement tab : resultTables) {
				HtmlElement link = tab.getElementsByAttribute("a", "class",
						"gs-title").get(0);
				Result r = new Result(query);
				r.link = link.getAttribute("href");
				r.title = link.asText();
				this.client.setJavaScriptEnabled(false);
				HtmlPage p = this.client.getPage(r.link);
				this.client.setJavaScriptEnabled(true);
				HtmlPage p1 = (HtmlPage) this.client.loadWebResponseInto(
						p.getWebResponse(),
						p.getEnclosingWindow());
				// this.client.getJavaScriptEngine()
				// .registerWindowAndMaybeStartEventLoop(
				// p1.getEnclosingWindow());
				HtmlElement lrbox = p1.getElementById("lrbox");
				HtmlElement left = lrbox.getElementsByAttribute("div", "class",
						"left").get(0);
				HtmlElement sizelab = left.getElementsByAttribute("font",
						"style", "line-height:18px; font-size: 13px;").get(0);
				r.size = sizelab.asText();
				HtmlElement right = lrbox.getElementsByAttribute("div",
						"class", "right").get(0);
				HtmlElement dirlink = right.getElementsByTagName("a").get(0);
				r.directlink = dirlink.getAttribute("href");
				results.add(r);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	private List<Result> searchUlub(Query query, int limit) {
		List<Result> results = new ArrayList<Result>();
		return results;
	}
}
