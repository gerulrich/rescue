package net.cloudengine.client.workbench.inbox;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.cloudengine.client.ui.AnnotatedCallbackResolver;
import net.cloudengine.client.ui.JobUtils;
import net.cloudengine.client.ui.PostCallback;
import net.cloudengine.rpc.controller.ticket.InboxController;
import net.cloudengine.rpc.model.Folder;
import net.cloudengine.rpc.model.FolderTab;
import net.cloudengine.rpc.model.TicketViewModel;


public class TicketFolderSource implements FolderSource<TicketViewModel> {

	private Folder<TicketViewModel> folder;
	private FolderTab<TicketViewModel> folderTab;
	private InboxController controller;
	private List<Reloadable> observers = new ArrayList<Reloadable>();
	private boolean fullyTnitialized = false;
	
	public TicketFolderSource(InboxController controller) {
		super();
		this.controller = controller;
		this.init(true);
	}
	
	private void init(boolean quick) {
		if (quick) {
			JobUtils.execAsync(controller, new AnnotatedCallbackResolver(this, "setup")).getQuick();
		} else {
			this.fullyTnitialized = true;
			JobUtils.execAsync(controller, new AnnotatedCallbackResolver(this, "setup")).getQuick();
		}
	}
	
	@PostCallback(name="setup")
	public void setup(Folder<TicketViewModel> folder) {
		update(folder);
		if(!fullyTnitialized) {
			this.init(false);
		} else {
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						Folder<TicketViewModel> folder = controller.getQuick();
						update(folder);
					} catch (Throwable t) {
						//logger.error("Failed to get folder from server, t);"
					}
				}
			}, 10, 30, TimeUnit.SECONDS);
		}
	}
	
	private void update(Folder<TicketViewModel> folder) {
		this.folder = folder;
		if (folderTab != null) {
			String tabName = folderTab.getName(); 
			this.setSelectedTab(tabName);
		}
		
		this.notityObservers();
	}

	@Override
	public String[] getTabNames() {
		if (folder == null) {
			return new String[0];
		}
		String names[] = new String[folder.getTabs().size()];
		for(int i = 0; i < names.length; i++) {
			names[i] = folder.getTabs().get(i).getName();
		}
		return names;
	}

	@Override
	public Folder<TicketViewModel> getFolder() {
		return folder;
	}

	@Override
	public FolderTab<TicketViewModel> getSelectedTab() {
		if (this.folderTab == null) {
			String names[] = this.getTabNames();
			if (names.length > 0) {
				this.setSelectedTab(names[0]);
			}
		}
		return this.folderTab;
	}

	@Override
	public void setSelectedTab(String name) {
		for(FolderTab<TicketViewModel> tab : folder.getTabs()) {
			if (tab.getName().equalsIgnoreCase(name)) {
				folderTab = tab;
				break;
			}
		}
		this.notityObservers();
	}
	
	public void add(Reloadable reloadable) {
		this.observers.add(reloadable);
	}
	
	private void notityObservers() {
		for (Reloadable reloadable : this.observers) {
			reloadable.reload();
		}
	}	
}