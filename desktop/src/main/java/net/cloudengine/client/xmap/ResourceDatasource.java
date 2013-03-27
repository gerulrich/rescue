package net.cloudengine.client.xmap;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Datasource;
import net.cloudengine.rpc.controller.resource.ResourceController;
import net.cloudengine.rpc.model.resource.ResourceModel;

public class ResourceDatasource implements Datasource<ResourceItem> {

	private ConcurrentMap<Long, ResourceItem> items = new ConcurrentHashMap<Long,ResourceItem>();
	private ResourceController controller;
	private MapWidgetContext context;
	private Timer timer;
	private TimerTask task;
	private long version = 0;
	
	public ResourceDatasource(ResourceController controller) {
		this.controller = controller;
		
		task = new TimerTask() 
	     { 
	         public void run()  
	         { 
	        	 try {
	        		 update();
	        	 } catch (Exception e) {
	        		 e.printStackTrace();
	        	 }
	         } 
	     };

	     timer = new Timer(); 
	     timer.scheduleAtFixedRate(task, 5000, 10000);
		
	}
	
	private void update() {
		boolean hasUpdate = false;
		List<ResourceModel> resources = this.controller.getAll(version);
		for(ResourceModel resource : resources) {
			ResourceItem item = items.get(resource.getId());
			if (item == null) {
				item = new ResourceItem();
				item.setId(resource.getId());
				items.put(resource.getId(), item);
			}
			item.setLat(resource.getLat());
			item.setLon(resource.getLon());
			item.setName(resource.getName());
			item.setType(resource.getType());
			item.setTypeDescription(resource.getTypeName());
			version = Math.max(version, resource.getVersion());
			hasUpdate = true;
		}
		if (hasUpdate && context != null) {
			context.getMap().repaintMap();
		}
	}
	
	@Override
	public void init(MapWidgetContext context) {
		this.context = context;
	}
	
	@Override
	public List<ResourceItem> getItems() {
		return new LinkedList<ResourceItem>(items.values());
	}
}

