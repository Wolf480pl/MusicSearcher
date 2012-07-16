package com.github.wolf480pl.musicsearcher;

public class Result {
	public Query query;
	public String title;
	public String size;
	public String link;
	public String directlink;
	public int site;

	public final static int ZIPPYSHARE = 1;
	public final static int ULUB = 2;

	public Result() {
	}

	public Result(Query query) {
		this.query = query;
	}

	public String getSiteTag() {
		switch (this.site) {
		case ZIPPYSHARE:
			return "[Z]";
		case ULUB:
			return "[U]";
		}
		return "";
	}

	public static String makeLink(String url) {
		return "<a href='" + url + "'>" + url + "</a>";
	}

	public Object[] toRow() {
		return new Object[] { new Boolean(false), this.query.getTitleString(),
				this.query.getEffectiveQuery(), this.title, this.getSiteTag(),
				this.size, makeLink(this.link), makeLink(this.directlink) };
	}
}
