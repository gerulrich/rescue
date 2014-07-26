package net.cloudengine.rpc.controller.ticket;

import net.cloudengine.rpc.model.Folder;
import net.cloudengine.rpc.model.TicketViewModel;

public interface InboxController {
	
	Folder<TicketViewModel> getQuick();
	Folder<TicketViewModel> getFull();

}
