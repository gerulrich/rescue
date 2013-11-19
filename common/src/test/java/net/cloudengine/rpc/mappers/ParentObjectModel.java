package net.cloudengine.rpc.mappers;

@DataObject
public class ParentObjectModel {

	private boolean enabled;
	private ChildObjectModel child;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public ChildObjectModel getChild() {
		return child;
	}
	public void setChild(ChildObjectModel child) {
		this.child = child;
	}
}
