package com.github.wolf480pl.musicsearcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SearchThread extends Thread {
	private MusicSearcher window;
	private WebClient client;
	public int limit;
	Query[] queries;
	
	public SearchThread(MusicSearcher window, Query[] queries, int limit) {
		this.window = window;
		this.client = new WebClient(BrowserVersion.FIREFOX_3_6);
		this.limit = limit;
		this.queries = queries;
	}
	
	@Override
	public void run() {
		for (Query q : queries) {
			if (q.isZippy()) {
				q.addResults(this.searchZippy(q,
						Math.min(this.limit, q.getLimit())));
			}
			if (q.isUlub()) {
				q.addResults(this.searchUlub(q,
						Math.min(this.limit, q.getLimit())));
			}
			window.resultModel.addRows(q.getResultsAsRows());
		}
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
				HtmlPage p1 = zippyPagePreprocess(p);
				HtmlElement lrbox = p1.getElementById("lrbox");
				HtmlElement left = lrbox.getElementsByAttribute("div", "class",
						"left").get(0);
				HtmlElement sizelab = left.getElementsByAttribute("font",
						"style", "line-height:18px; font-size: 13px;").get(0);
				r.size = sizelab.asText();
				HtmlElement right = lrbox.getElementsByAttribute("div",
						"class", "right").get(0);
				HtmlElement dirlink = right.getElementsByTagName("a").get(0);
				r.directlink = r.link.replaceFirst("/v.*", "") + dirlink.getAttribute("href");
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
	
	private HtmlPage zippyPagePreprocess(HtmlPage page) {
		DomNodeList<HtmlElement> scripts = page.getElementsByTagName("script");
		Iterator<HtmlElement> I = scripts.iterator();
		while (I.hasNext()) {
			HtmlElement script = I.next();
			HtmlElement parent = (HtmlElement) script.getParentNode(); 
			if ((!parent.getTagName().equalsIgnoreCase("div") && parent.getAttribute("class").equalsIgnoreCase("right"))) {
					script.remove();
			}
		}
		WebResponse oldRes = page.getWebResponse();
		try {
			WebResponseData resData = new WebResponseData(page.asXml().getBytes(), oldRes.getStatusCode(), oldRes.getStatusMessage(), oldRes.getResponseHeaders());
			WebResponse newRes = new WebResponse(resData, oldRes.getWebRequest(), oldRes.getLoadTime());
			client.setJavaScriptEnabled(true);
			return (HtmlPage) client.loadWebResponseInto(newRes, page.getEnclosingWindow());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Result> searchUlub(Query query, int limit) {
		List<Result> results = new ArrayList<Result>();
		return results;
	}
}
