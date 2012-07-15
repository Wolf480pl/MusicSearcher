package com.github.wolf480pl.musicsearcher;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Query {
	private String query;
	private DefaultMutableTreeNode title;
	private boolean zippy, ulub;
	public Query(DefaultMutableTreeNode title, String query, boolean zippy, boolean ulub) {
		this.title = title;
		this.query = query;
		this.zippy = zippy;
		this.ulub = ulub;
	}
	public String getSourceQuery() {
		return query;
	}
	public String getEffectiveQuery() {
		String tit = (String) title.getUserObject();
		return query.replaceAll("%", tit);
	}
	@Override
	public String toString() {
		return getEffectiveQuery() + (zippy ? " [Z]" : "") + (ulub ? " [U]" : "");
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
}
