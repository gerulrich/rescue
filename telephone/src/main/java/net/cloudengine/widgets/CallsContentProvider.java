package net.cloudengine.widgets;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.Call;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class CallsContentProvider implements IStructuredContentProvider {

	private List<Call> calls = new ArrayList<Call>();
	private TableViewer tv;
	private EventProvider provider;
	private EventListener listener;
	
	public CallsContentProvider(EventProvider provider, TableViewer tv) {
		this.tv = tv;
		this.provider = provider;
		this.listener = new EventListenerAdapter() {
			
			@Override
			public void onDisconnect() { 
				calls.clear();
				updateView();
			}
			
			@Override
			public void newCall(Call call) {
				calls.add(call);
				updateView();
			}
			
			@Override
			public void changeCall(Call call) {
				if (call.isInQueue()) {
					calls.remove(call);
				}
				updateView();
			}

			@Override
			public void hangupCall(Call call) {
				calls.remove(call);
				updateView();				
			}
		};
		this.provider.addListener(listener);
	}
	
	public Object[] getElements(Object inputElement) {
		return calls.toArray();
	}

	public void dispose() {
		this.provider.removeListener(listener);
		calls.clear();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}
	
	private void updateView() {
		if (!this.tv.getTable().isDisposed()) {
			Display display = this.tv.getTable().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					CallsContentProvider.this.tv.refresh();
				}
			});
		}
	}
}