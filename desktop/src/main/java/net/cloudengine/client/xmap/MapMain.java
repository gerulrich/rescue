package net.cloudengine.client.xmap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.app.MapApplication;
import net.cloudengine.client.main.GuiceModule;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.DebugTileLayer;
import net.cloudengine.mapviewer.layers.LayerDSBased;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.cloudengine.mapviewer.tiles.TileServerType;
import net.cloudengine.mapviewer.tools.renderer.RendererLayer;
import net.cloudengine.mapviewer.tools.selection.Cast;
import net.cloudengine.rpc.controller.auth.SigninService;
import net.cloudengine.rpc.controller.geo.ZoneController;
import net.cloudengine.rpc.controller.resource.ResourceController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.caucho.hessian.client.MyHessianProxyFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MapMain extends MapApplication {
	
	private String baseUrl;
	private Injector injector;
	
	public MapMain(Injector injector, String baseUrl) {
		super(toMap(injector, baseUrl));
	}
	
	private static Map<String,Object> toMap(Injector injector, String baseUrl) {
		Map<String,Object> properties = new TreeMap<String, Object>();
		properties.put("injector", injector);
		properties.put("baseUrl", baseUrl);
		return properties;
	}
	
	@Override
	public void init(Map<String, Object> props) {
		this.baseUrl = props.get("baseUrl").toString();
		this.injector = Cast.as(Injector.class, props.get("injector"));
	}

	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(GuiceModule.BASE);
		SigninService signin = injector.getInstance(SigninService.class);
		String token = signin.login("admin@admin.com", "admin");
		MyHessianProxyFactory.setSessionId(token);
		
		MapApplication app = new MapMain(injector, "http://localhost:8080/webmodule/tiles");
		app.setBlockOnOpen(true);
	    // Open the main window
	    app.open();
	    // Dispose the display
	    Display.getCurrent().dispose();
	    System.exit(0);
	}
	
	@Override
	public void create() {
		setShellStyle(SWT.TITLE | SWT.RESIZE);
		super.create();
	}

	@Override
	protected void configureMap(MapWidgetContext context) {
		super.configureMap(context);
		
		context.addTileServer(new TileServer("Google Maps", baseUrl, 18, TileServerType.GOOGLEMAPS), true);
		context.addTileServer(new TileServer("OSM", baseUrl, 18, TileServerType.OPENSTREET), true);
		context.addTileServer(new TileServer("Google Maps (sat)", baseUrl, 18, TileServerType.GOOGLESAT), true);

		context.addLayer(new DebugTileLayer(context));

		// Agrego los layers de zona existentes.
		ZoneController zoneController = injector.getInstance(ZoneController.class);
		List<String> zoneTypes = zoneController.getZoneTypes();
		for(String zoneType : zoneTypes) {
			ZoneDatasource zd = new ZoneDatasource(zoneController, zoneType);
			ZoneSymbolizer zs = new ZoneSymbolizer();
			context.addLayer(new ZoneLayer(zoneType, context, zd, zs, false));
		}
		
		// Agrego el layer de recursos.
		ResourceController controller = injector.getInstance(ResourceController.class);
		ResourceDatasource rd = new ResourceDatasource(controller);
		ResourceSymbolizer rs = new ResourceSymbolizer(controller);
		context.addLayer(new LayerDSBased<ResourceItem>("Recursos", context, rd, rs, true));
		
		context.addLayer(new RendererLayer());
	}
	
	

}
