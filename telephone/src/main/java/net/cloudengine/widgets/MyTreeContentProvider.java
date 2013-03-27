package net.cloudengine.widgets;

import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.Status;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class MyTreeContentProvider implements ITreeContentProvider {

	private static final String CONECTADOS = "Conectados";
	private static final String DESCONECTADOS = "Desconectados";
	
	private Map<String, Extension> connected = new TreeMap<String, Extension>();
	private Map<String, Extension> disconnected = new TreeMap<String, Extension>();

	private TreeViewer tree;
	private EventProvider provider;
	private EventListener listener;
	
	public MyTreeContentProvider(TreeViewer tree, EventProvider provider) {
		this.tree = tree;
		this.provider = provider;
		this.listener = new EventListenerAdapter() {
			
			@Override
			public void extensionChanged(Extension extension) {
				connected.remove(extension.getNumber());
				disconnected.remove(extension.getNumber());
				if (Status.UNAVAILABLE.equals(extension.getStatus())) {
					disconnected.put(extension.getNumber(), extension);
				} else {
					connected.put(extension.getNumber(), extension);
				}
				updateView();
			}
			
			@Override
			public void extensionAdded(Extension extension) {
				Status status = extension.getStatus();
				if (Status.UNAVAILABLE.equals(status)) {
					disconnected.put(extension.getNumber(), extension);
				} else {
					connected.put(extension.getNumber(), extension);
				}
				updateView();
			}
			
			@Override
			public void onDisconnect() {
				connected.clear();
				disconnected.clear();
				updateView();
			}			
		};
		this.provider.addListener(listener);
	}
	
	private void updateView() {
		Display display = this.tree.getTree().getDisplay();
		display.syncExec (new Runnable () {
			public void run () {
				MyTreeContentProvider.this.tree.refresh();
				MyTreeContentProvider.this.tree.expandAll();
			}
		});
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return new String[] { CONECTADOS, DESCONECTADOS };
	}

	@Override
	public void dispose() {
		this.provider.removeListener(listener);
		this.connected.clear();
		this.disconnected.clear();
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) { 
		
	}
	
	private Object[] toArray(Map<String,Extension>  map) {
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
		if (element instanceof String) {
			String group = (String)element;
			if (CONECTADOS.equals(group)) {
				return toArray(connected);
			} else {
				return toArray(disconnected);
			}
		}
		throw new IllegalArgumentException("Solo los grupos pueden tener sub elementos");
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Extension) {
			Extension ext = (Extension) element;
			if (Status.UNAVAILABLE.equals(ext.getStatus())) {
				return DESCONECTADOS;
			} else {
				return CONECTADOS;
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof String)
			return true;
		return false;
	}

}
