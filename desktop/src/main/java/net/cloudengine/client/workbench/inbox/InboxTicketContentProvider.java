package net.cloudengine.client.workbench.inbox;

import net.cloudengine.rpc.model.FolderTab;
import net.cloudengine.rpc.model.TicketViewModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class InboxTicketContentProvider implements IStructuredContentProvider, Reloadable {

	private TableViewer tv;
	private FolderSource<TicketViewModel> folderSource;
	
	public InboxTicketContentProvider(TableViewer tv, FolderSource<TicketViewModel> folderSource) {
		super();
		this.tv = tv;
		this.folderSource = folderSource;
		((TicketFolderSource)folderSource).add(this);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		FolderTab<TicketViewModel> folderTab = folderSource.getSelectedTab();
		if (folderTab != null) {
			return folderTab.getElements().toArray();
		}
		return new Object[0];
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void reload() {
		this.updateView();
	}

	private void updateView() {
		if (!this.tv.getTable().isDisposed()) {
			Display display = this.tv.getTable().getDisplay();
			display.asyncExec(new Runnable () {
				public void run () {
					InboxTicketContentProvider.this.tv.refresh();
				}
			});
		}
	}
}