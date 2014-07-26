package net.cloudengine.client.workbench.inbox;

import net.cloudengine.rpc.model.Folder;
import net.cloudengine.rpc.model.FolderTab;

public interface FolderSource<T> {
	
	String[] getTabNames();
	Folder<T> getFolder();
	FolderTab<T> getSelectedTab();
	void setSelectedTab(String name);

}