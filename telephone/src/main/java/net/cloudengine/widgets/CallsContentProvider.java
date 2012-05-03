package net.cloudengine.widgets;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;
import net.cloudengine.cti.CallsMonitor;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.google.inject.Inject;

public class CallsContentProvider implements IStructuredContentProvider {

	private List<Call> calls = new ArrayList<Call>();
	private TableViewer tv;
	private CallsMonitor monitor;
	
	@Inject
	public CallsContentProvider(CallsMonitor monitor, TableViewer tv) {
		this.tv = tv;
		this.monitor = monitor;
		this.monitor.addListener(new CallListener() {
			
			@Override
			public void onNewCall(Call call) {
				calls.add(call);
				updateView();
			}
			
			@Override
			public void onCallFinish(Call call) {
				calls.remove(call);
				updateView();
			}

			@Override
			public void onChangeState(Call call) {
				updateView();				
			}

			@Override
			public void onQueueCall(Call call, String queue) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDequeueCall(Call call, String queue) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public Object[] getElements(Object inputElement) {
		return calls.toArray();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
	
	private void updateView() {
		Display display = this.tv.getTable().getDisplay();
		display.syncExec (new Runnable () {
			public void run () {
				CallsContentProvider.this.tv.refresh();
			}
		});
	}
	
}