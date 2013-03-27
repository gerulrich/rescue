package net.cloudengine.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class QueueMemberContentProvider implements ITreeContentProvider {

	private Map<String, Queue> queues = new TreeMap<String, Queue>();
	private Map<String, List<QMember>> members = new TreeMap<String, List<QMember>>();

	private TreeViewer tree;
	private EventProvider provider;
	private EventListener listener;

	public QueueMemberContentProvider(TreeViewer tree, EventProvider provider) {
		this.tree = tree;
		this.provider = provider;
		this.listener = new EventListenerAdapter() {
			
			@Override
			public void queueMemberRemoved(String queue, QMember member) {
				members.get(queue).remove(member);
				updateView();
			}
			
			@Override
			public void queueMemberAdded(String queue, QMember member) {
				if (members.get(queue) == null) {
					members.put(queue, new ArrayList<QMember>());
				}
				if (!members.get(queue).contains(member)) {
					members.get(queue).add(member);
				}
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
				members.clear();
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
					QueueMemberContentProvider.this.tree.refresh();
					QueueMemberContentProvider.this.tree.expandAll();
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
		this.members.clear();
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
			if (members.get(q.getName()) != null) {
				return members.get(q.getName()).toArray();
			}			
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof QMember) {
			QMember member = (QMember) element;
			return queues.get(member.getQueue());
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Queue) {
			Queue q = (Queue) element;
			return members.get(q.getName()) != null && members.get(q.getName()).size() > 0;
		}
		return false;
	}
}