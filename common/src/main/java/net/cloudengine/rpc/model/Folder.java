package net.cloudengine.rpc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Folder<T> implements Serializable {

	private static final long serialVersionUID = -8723714176211801798L;
	
	private List<FolderTab<T>> tabs = new ArrayList<FolderTab<T>>();

	public List<FolderTab<T>> getTabs() {
		return tabs;
	}

	public void setTabs(List<FolderTab<T>> tabs) {
		this.tabs = tabs;
	}
}
