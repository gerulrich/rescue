package net.cloudengine.client.xmap;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cloudengine.client.ui.AnnotatedCallbackResolver;
import net.cloudengine.client.ui.JobUtils;
import net.cloudengine.client.ui.PostCallback;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.util.MapUtil;
import net.cloudengine.rpc.controller.resource.ResourceController;
import net.cloudengine.rpc.model.resource.ResourceTypeModel;
import net.sf.swtgraph.layeredcanvas.AbstractLayer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.google.inject.Injector;


public class ResourceLayer extends AbstractLayer {

	private ConcurrentMap<Long, Image> images = new ConcurrentHashMap<Long, Image>();
	int difx = 0;
	int dify = 0;
	
	public ResourceLayer(MapWidgetContext context, Injector injector) {
		super("Recursos", context, true);
		ResourceController controller = injector.getInstance(ResourceController.class);
		JobUtils.execAsync(controller, new AnnotatedCallbackResolver(this, "loadTypes")).getTypes();
	}
	
	@PostCallback(name="loadTypes")
	public void loadTypes(List<ResourceTypeModel> types) {
		for(ResourceTypeModel type : types) {
			Image image = new Image(Display.getCurrent(), new ByteArrayInputStream(type.getImage()));
			images.put(type.getId(), image);
		}
	}
	
	public void dispose() {
		for(Long id : images.keySet()) {
			Image image = images.remove(id);
			image.dispose();
		}
	}

	public void paint(GC gc) {
		
		Device device = Display.getCurrent();
		Color green = new Color (device, 93, 219, 49);

//		int difx = MapWidget.lon2position(-58.51210, map.getZoom()) -map.mapPosition.x;
//		int dify = MapWidget.lat2position(-34.52596, map.getZoom()) -map.mapPosition.y;
		
		difx = MapUtil.lon2position(-58.36798, getMap().getZoom()) -getMap().mapPosition.x;
		dify = MapUtil.lat2position(-34.61761, getMap().getZoom()) -getMap().mapPosition.y;
		
		Color currentBc = gc.getBackground();
		Color currentFc = gc.getForeground();
		
//		if (selected) {
//			gc.setAntialias(SWT.ON);
//			gc.setLineStyle(1);
//			gc.setLineWidth(2);
//			gc.setForeground(green);
//			gc.setBackground(green);
//		
//			gc.drawArc(difx-15, dify-8, 30, 16, 0, 360);
//		
//			int alpha = gc.getAlpha();
//			gc.setAlpha(50);
//			gc.fillArc(difx-15, dify-8, 30, 16, 0, 360);
//			gc.setAlpha(alpha);
//		}		
		
		Image image = images.get(1L);
		if (image != null)
			gc.drawImage(image, 0, 0, image.getImageData().width, image.getImageData().height, difx-image.getImageData().width/4+2, dify-image.getImageData().height+2, image.getImageData().width, image.getImageData().height);
//		gc.drawPoint(difx, dify);
		
		gc.setBackground(currentBc);
		gc.setForeground(currentFc);
		green.dispose();
	}

//	@Override
//	public void selectObjects(int x, int y) {
//		if (difx >= x-10 && difx <= x+10 && dify >=y-10 && dify <= y+10) {
//			selected = true;
//		} else {
//			selected = false;
//		}
//		System.out.println("x="+x+", y="+y);
//		System.out.println("objeto x="+difx+", y="+dify);
//	}

//	@Override
//	public Collection<ISelectable> getObjectsSelected() {
		// TODO Auto-generated method stub
//		return null;
//	}
}
