package net.cloudengine.rpc.mappers;

public class ParentObject {
	
	private boolean enabled;
	private ChildObject child;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public ChildObject getChild() {
		return child;
	}
	public void setChild(ChildObject child) {
		this.child = child;
	}

}
