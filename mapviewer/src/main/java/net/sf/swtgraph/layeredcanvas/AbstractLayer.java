package net.sf.swtgraph.layeredcanvas;

import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.MapWidgetContext;


public abstract class AbstractLayer implements ICanvasLayer {

	private String name;
	private boolean enabled = true;
	private MapWidgetContext context;
	
	public AbstractLayer(String name, MapWidgetContext context, boolean enabled) {
		super();
		this.name = name;
		this.context = context;
		this.enabled = enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getName() {
		return name;
	}
	
	protected MapWidget getMap() {
		return context.getMap();
	}
	
	protected MapWidgetContext getContext() {
		return context;
	}

	@Override
	public boolean showLegend() {
		return true;
	}
	

}
