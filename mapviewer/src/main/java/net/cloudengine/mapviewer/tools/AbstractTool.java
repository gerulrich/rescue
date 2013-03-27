package net.cloudengine.mapviewer.tools;

import org.eclipse.jface.resource.ImageDescriptor;

import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.MapWidgetContext;

public abstract class AbstractTool {
	
	private MapWidgetContext context;
	private boolean enabled;

	public AbstractTool(MapWidgetContext context, boolean enabled) {
		super();
		this.context = context;
		this.enabled = enabled;
	}
	
	public MapWidgetContext getContext() {
		return context;
	}

	public void setContext(MapWidgetContext context) {
		this.context = context;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	protected MapWidget getMap() {
		return context.getMap();
	}
	
	public abstract ImageDescriptor getIcon();
	public abstract void activate();
	public abstract void deactivate();
//	public abstract void registerListeners(MapWidget widgets);

}
