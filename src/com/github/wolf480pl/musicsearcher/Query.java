package com.github.wolf480pl.musicsearcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class Query {
	private String query;
	private DefaultMutableTreeNode title;
	private boolean zippy, ulub;
	private List<Result> results;
	private int limit;

	public Query(DefaultMutableTreeNode title, String query, boolean zippy,
			boolean ulub) {
		this.title = title;
		this.query = query;
		this.zippy = zippy;
		this.ulub = ulub;
		this.results = new ArrayList<Result>();
		this.limit = 10;
	}

	public String getSourceQuery() {
		return this.query;
	}

	public String getEffectiveQuery() {
		String tit = (String) this.title.getUserObject();
		return this.query.replaceAll("%", tit);
	}

	@Override
	public String toString() {
		return this.getEffectiveQuery() + (this.zippy ? " [Z]" : "")
				+ (this.ulub ? " [U]" : "");
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isZippy() {
		return this.zippy;
	}

	public boolean isUlub() {
		return this.ulub;
	}

	public void setZippy(boolean flag) {
		this.zippy = flag;
	}

	public void setUlub(boolean flag) {
		this.ulub = flag;
	}

	public TreeNode getTitleNode() {
		return this.title;
	}

	public String getTitleString() {
		return (String) this.title.getUserObject();
	}

	public void setTitle(DefaultMutableTreeNode node) {
		this.title = node;
	}

	public void addResult(Result result) {
		this.results.add(result);
	}

	public void addResults(Result[] results) {
		this.results.addAll(Arrays.asList(results));
	}

	public void addResults(Collection<Result> results) {
		this.results.addAll(results);
	}

	public Result[] getResults() {
		return this.results.toArray(new Result[0]);
	}
	
	public List<Object[]> getResultsAsRows() {
		Iterator<Result> I = results.iterator();
		List<Object[]> ret = new ArrayList<Object[]>();
		while (I.hasNext()) {
			ret.add(I.next().toRow());
		}
		return ret;
	}

	public void removeResult(int index) {
		this.results.remove(index);
	}

	public void removeResult(Result result) {
		this.results.remove(result);
	}

	public void removeAllResults() {
		this.results = new ArrayList<Result>();
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
