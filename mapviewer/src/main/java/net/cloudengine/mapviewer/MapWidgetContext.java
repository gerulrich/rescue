package net.cloudengine.mapviewer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import net.cloudengine.beans.GenericPropertyChangeEvent;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.cloudengine.mapviewer.tools.AbstractTool;
import net.cloudengine.mapviewer.tools.selection.Group;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

public class MapWidgetContext {
	
	private List<TileServer> baseTileServers = new ArrayList<TileServer>();
    private List<TileServer> overlayTileServers = new ArrayList<TileServer>();
    private TileServer currentTileServer = null;
    private List<ICanvasLayer> layers = new ArrayList<ICanvasLayer>();
    private List<AbstractTool> tools = new ArrayList<AbstractTool>();
    
    
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<Group> groups = new ArrayList<Group>();
    
    private MapWidget map;
    
    public MapWidget getMap() {
		return map;
	}

	public void setMap(MapWidget map) {
		this.map = map;
	}

	public void addTileServer(TileServer tileServer, boolean base) {
    	if (base) {
    		baseTileServers.add(tileServer);
    		if (currentTileServer == null) {
    			currentTileServer = tileServer;
    		}
    	} else {
    		overlayTileServers.add(tileServer);
    	}
    }
	
	public void addTool(AbstractTool tool) {
		this.tools.add(tool);
	}
    
    public List<TileServer> getBaseTileServers() {
    	return baseTileServers;
    }
    
    public void addLayer(ICanvasLayer layer) {
    	this.layers.add(layer);
    }
    
    public List<ICanvasLayer> getlayers() {
    	return layers;
    }

	public List<AbstractTool> getTools() {
		return tools;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		List<Group> oldValue = this.groups;
		this.groups = groups;
		PropertyChangeEvent event = new GenericPropertyChangeEvent<List<Group>>(this,
		                                                     "groups",
		                                                     oldValue,
		                                                     this.groups);
		pcs.firePropertyChange(event);
//		pcs.firePropertyChange("groups", oldValue, this.groups);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
    
    
}
