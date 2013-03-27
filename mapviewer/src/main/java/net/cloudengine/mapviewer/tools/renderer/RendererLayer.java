package net.cloudengine.mapviewer.tools.renderer;

import java.util.HashMap;
import java.util.Map;

import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.swt.graphics.GC;

public class RendererLayer implements ICanvasLayer {

	private Map<String,Renderer> renderers = new HashMap<String, Renderer>();
	private boolean enabled = true;
	
	public void addRenderer(String name, Renderer renderer) {
		renderers.put(name, renderer);
	}
	
	public void removeRenderer(String name) {
		renderers.remove(name);
	}
	
	@Override
	public void paint(GC gc) {
		for(String name : renderers.keySet()) {
			Renderer renderer = renderers.get(name);
			renderer.paint(gc);
		}
	}

	@Override
	public void dispose() {
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
		return "ToolLayer";
	}

	@Override
	public boolean showLegend() {
		return false;
	}
	
	

}
