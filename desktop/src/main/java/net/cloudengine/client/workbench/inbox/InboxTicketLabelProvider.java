package net.cloudengine.client.workbench.inbox;

import net.cloudengine.rpc.model.TicketViewModel;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * {@link LabelProvider} que representa los ticket en una tabla para la bandeja de entrada.
 * @author German Ulrich
 *
 */
public class InboxTicketLabelProvider extends LabelProvider implements ITableLabelProvider {

	
	public InboxTicketLabelProvider() {

	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		TicketViewModel ticket = (TicketViewModel) element;
		switch (columnIndex) {
			case 0: return ticket.getTicketId();
			case 1: return ticket.getCreationDate();
			case 2: return ticket.getAssignedUser();
			case 3: return ticket.getCurrentState();
			case 4: return ticket.getCreatorUser();
		}
		return null;
	}
	

}