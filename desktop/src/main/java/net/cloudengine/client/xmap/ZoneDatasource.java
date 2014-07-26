package net.cloudengine.client.xmap;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.client.ui.AnnotatedCallbackResolver;
import net.cloudengine.client.ui.Callback;
import net.cloudengine.client.ui.JobUtils;
import net.cloudengine.client.ui.PostCallback;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Datasource;
import net.cloudengine.rpc.controller.geo.GeoController;
import net.cloudengine.rpc.controller.geo.ZoneModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class ZoneDatasource implements Datasource<ZoneItem> {

	private static final Logger logger = LoggerFactory.getLogger(ZoneDatasource.class);
	private List<ZoneItem> items = new ArrayList<ZoneItem>();
	
	public ZoneDatasource(GeoController controller, String type) {
		super();
		Callback callback = new AnnotatedCallbackResolver(this, "loadZones");
		JobUtils.execAsync(controller, callback).getZoneByType(type);
	}
	
	@PostCallback(name="loadZones")
	public void loadZones(List<ZoneModel> result) {
		WKTReader reader = new WKTReader();
		for(ZoneModel zone : result) {
			try {
				Geometry geom = reader.read(zone.getGeometry());
				ZoneItem item = new ZoneItem(zone, geom);
				items.add(item);				
			} catch (ParseException e) {
				logger.error("Error al parsear la geometria", e);
			}
		}
	}
	
	@Override
	public void init(MapWidgetContext context) {

	}

	@Override
	public List<ZoneItem> getItems() {
		return items;
	}

}
