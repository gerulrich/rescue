package net.cloudengine.mapviewer.layers;

public interface Item {
	
	Long getId();
	String getName();
	double getLon();
	double getLat();

	boolean allowSelection();
	boolean isSelected();
	void selectItem();
	void unselectItem();
	
}
