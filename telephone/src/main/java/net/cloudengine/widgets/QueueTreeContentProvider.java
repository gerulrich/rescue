package net.cloudengine.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;
import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.pbx.CTIQueue;
import net.cloudengine.pbx.CTIQueueEntry;
import net.cloudengine.pbx.PBXMonitor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class QueueTreeContentProvider implements ITreeContentProvider {

	private Map<String, CTIQueue> queues = new TreeMap<String, CTIQueue>();
	private Map<String, List<CTIQueueEntry>> entries = new TreeMap<String, List<CTIQueueEntry>>();

	private TreeViewer tree;
	private PBXMonitor monitor;

	public QueueTreeContentProvider(TreeViewer tree, PBXMonitor monitor, CallsMonitor callsMonitor) {
		this.tree = tree;
		this.monitor = monitor;
		for (CTIQueue queue : this.monitor.getQueues()) {
			queues.put(queue.getNumber(), queue);
		}
		callsMonitor.addListener(new CallListener() {
			
			@Override
			public void onQueueCall(Call call, String queue) {
				CTIQueueEntry entry = new CTIQueueEntry();
				entry.setDate(call.creationDate());
				entry.setNumber(call.getCallerId());
				entry.setQueue(queue);
				List<CTIQueueEntry> list = entries.get(queue);
				if (list == null) {
					list = new ArrayList<CTIQueueEntry>();
					entries.put(queue, list);
				}
				list.add(entry);
				updateView();
			}
			
			@Override
			public void onDequeueCall(Call call, String queue) {
				List<CTIQueueEntry> list = entries.get(queue);
				CTIQueueEntry searched = null;
				if (list != null) {
					for(CTIQueueEntry entry : list) {
						if (entry.getNumber().equals(call.getCallerId())) {
							searched = entry;
							break;
						}
					}
					list.remove(searched);
					updateView();
				}
			}
			
			@Override
			public void onNewCall(Call call) {}
			
			@Override
			public void onChangeState(Call call) {}
			
			@Override
			public void onCallFinish(Call call) { }
		});
	}
	
	private void updateView() {
		for (final CTIQueue q : this.queues.values()) {
			Display display = this.tree.getTree().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					QueueTreeContentProvider.this.tree.refresh(q);
				}
			});
		}
		this.tree.expandAll();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return toArray(queues);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
	}
	
	private Object[] toArray(Map<String,CTIQueue>  map) {
		
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
		if (element instanceof CTIQueue) {
			CTIQueue q = (CTIQueue) element;
			if (entries.get(q.getNumber()) != null) {
				return entries.get(q.getNumber()).toArray();
			}			
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof CTIQueueEntry) {
			CTIQueueEntry entry = (CTIQueueEntry) element;
			return queues.get(entry.getQueue());
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof CTIQueue) {
			CTIQueue q = (CTIQueue) element;
			return entries.get(q.getNumber()) != null && entries.get(q.getNumber()).size() > 0;
		}
		return false;
	}

}
