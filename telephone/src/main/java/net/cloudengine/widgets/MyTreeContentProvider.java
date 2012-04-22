package net.cloudengine.widgets;

import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.pbx.Group;
import net.cloudengine.pbx.PBXMonitor;
import net.cloudengine.pbx.PhoneExt;
import net.cloudengine.pbx.PhoneStatusListener;
import net.cloudengine.pbx.Status;
import net.cloudengine.pbx.asterisk.AsteriskPBXMonitor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class MyTreeContentProvider implements ITreeContentProvider {

	private Map<String, PhoneExt> conectedPhones = new TreeMap<String, PhoneExt>();
	private Map<String, PhoneExt> disconectedPhones = new TreeMap<String, PhoneExt>();

	private TreeViewer tree;
	private PBXMonitor monitor;

	public MyTreeContentProvider(TreeViewer tree, PBXMonitor monitor) {
		this.tree = tree;
		this.monitor = monitor;

		for (PhoneExt phone : monitor.getAllPhoneExt()) {
			if (phone.getStatus().equals(Status.UNAVAILABLE)) {
				disconectedPhones.put(phone.getNumber(), phone);
			} else {
				conectedPhones.put(phone.getNumber(), phone);
			}
		}

		this.monitor.addListener(new PhoneStatusListener() {
			@Override
			public void onStatusChange(String extension, Status newStatus) {

				boolean available = conectedPhones.containsKey(extension);

				boolean cambio_de_grupo = (Status.UNAVAILABLE.equals(newStatus) && available)
						|| (!Status.UNAVAILABLE.equals(newStatus) && !available);

				if (cambio_de_grupo) {

					if (available) {
						PhoneExt phone = conectedPhones.get(extension);
						phone.setStatus(newStatus);
						conectedPhones.remove(extension);
						disconectedPhones.put(extension, phone);
						updateView();
					} else {
						PhoneExt phone = disconectedPhones.get(extension);
						phone.setStatus(newStatus);
						disconectedPhones.remove(extension);
						conectedPhones.put(extension, phone);
						updateView();
					}
					
				} else {
					
					if (available) {
						PhoneExt phone = conectedPhones.get(extension);
						phone.setStatus(newStatus);
						updateView();
					} else {
						PhoneExt phone = disconectedPhones.get(extension);
						phone.setStatus(newStatus);
						updateView();
					}
				}
			}
		});
	}
	
	private void updateView() {
		for (final Group g : this.monitor.getGroups()) {
			Display display = this.tree.getTree().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					MyTreeContentProvider.this.tree.refresh(g);
				}
			});
		}
		this.tree.expandAll();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return monitor.getGroups().toArray();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
	}
	
	private Object[] toArray(Map<String,PhoneExt>  map) {
		
		Object o[] = new Object[map.size()];
		int i = 0;
		for (String key : map.keySet()) {
			o[i] = map.get(key);
			i++;
		}
				
		return o;

	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof Group) {
			Group g = (Group) element;
			if (Status.UNAVAILABLE.equals(g.getStatus())) {
				return toArray(disconectedPhones);
			} else {
				return toArray(conectedPhones);
			}
		}
		throw new IllegalArgumentException("Solo los grupos pueden tener sub elementos");
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof PhoneExt) {
			PhoneExt phone = (PhoneExt) element;
			if (phone.getStatus().equals(Status.UNAVAILABLE)) {
				return AsteriskPBXMonitor.UNAVAILABLE;
			} else {
				return AsteriskPBXMonitor.AVAILABLE;
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Group)
			return true;
		return false;
	}

}
