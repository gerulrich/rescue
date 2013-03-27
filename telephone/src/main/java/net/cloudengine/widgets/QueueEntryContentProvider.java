package net.cloudengine.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.Queue;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class QueueEntryContentProvider implements ITreeContentProvider {

	private Map<String, Queue> queues = new TreeMap<String, Queue>();
	private Map<String, List<QEntry>> entries = new TreeMap<String, List<QEntry>>();

	private TreeViewer tree;
	private EventProvider provider;
	private EventListener listener;

	public QueueEntryContentProvider(TreeViewer tree, EventProvider provider) {
		this.tree = tree;
		this.provider = provider;
		this.listener = new EventListenerAdapter() {
			
			@Override
			public void queueEntryRemoved(String queue, QEntry entry) {
				entries.get(queue).remove(entry);
				updateView();
			}
			
			@Override
			public void queueEntryAdded(String queue, QEntry entry) {
				if (entries.get(queue) == null) {
					entries.put(queue, new ArrayList<QEntry>());
				}
				entries.get(queue).add(entry);
				updateView();
			}
			
			@Override
			public void queueAdded(Queue queue) {
				queues.put(queue.getName(), queue);
				updateView();
			}
			
			@Override
			public void onDisconnect() {
				queues.clear();
				entries.clear();
				updateView();
			}			
		};
		this.provider.addListener(listener);
	}
	
	private void updateView() {
		if (!this.tree.getTree().isDisposed()) {
			Display display = this.tree.getTree().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					QueueEntryContentProvider.this.tree.refresh();
					QueueEntryContentProvider.this.tree.expandAll();
				}
			});
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return toArray(queues);
	}

	@Override
	public void dispose() {
		this.provider.removeListener(listener);
		this.queues.clear();
		this.entries.clear();
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		
	}
	
	private Object[] toArray(Map<String, Queue>  map) {
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
		if (element instanceof Queue) {
			Queue q = (Queue) element;
			if (entries.get(q.getName()) != null) {
				return entries.get(q.getName()).toArray();
			}			
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof QEntry) {
			QEntry entry = (QEntry) element;
			return queues.get(entry.getQueue());
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Queue) {
			Queue q = (Queue) element;
			return entries.get(q.getName()) != null && entries.get(q.getName()).size() > 0;
		}
		return false;
	}
}