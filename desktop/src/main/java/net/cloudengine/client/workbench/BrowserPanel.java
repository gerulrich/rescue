package net.cloudengine.client.workbench;
import net.cloudengine.rpc.controller.ticket.InboxController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

// Mailing list browser panel
public class BrowserPanel extends Composite {

	private Label subjectLabel;
	private Label label_2;
	private Label addressLabel;
	private Composite tagPane;
	private Composite browserPane;
	private Browser browser;

	public BrowserPanel(Composite parent) {
		super(parent, SWT.NONE);
		tagPane = new Composite(this, SWT.BORDER);

		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		this.setLayout(layout);

		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.heightHint = 53;
		tagPane.setLayoutData(gd);
		RowLayout rl = new RowLayout();
		rl.wrap = true;
		rl.pack = true;
		rl.spacing = 3;
		tagPane.setLayout(rl);

		final Label label = new Label(tagPane, SWT.NONE | SWT.LEFT);
		label.setText("From:");
		// label.setBounds(10, 10, 45, 15);

		addressLabel = new Label(tagPane, SWT.NONE | SWT.LEFT);
		addressLabel.setBounds(61, 10, 120, 20);

		final Label label_1 = new Label(tagPane, SWT.NONE | SWT.LEFT);
		label_1.setText("Date:");
		// label_1.setBounds(204, 10, 28, 15);

		label_2 = new Label(tagPane, SWT.NONE);
		label_2.setBounds(238, 10, 78, 15);

		final Label label_3 = new Label(tagPane, SWT.NONE | SWT.LEFT);
		label_3.setText("Subject: ccc");
		label_3.setBounds(10, 31, 45, 15);

		subjectLabel = new Label(tagPane, SWT.NONE | SWT.LEFT);
		subjectLabel.setBounds(61, 31, 138, 15);
		this.browserPane = new Composite(this, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		browserPane.setLayout(gridLayout);

		final GridData gd_browserPane = new GridData(SWT.FILL, SWT.FILL, true, true);
		browserPane.setLayoutData(gd_browserPane);

		browser = new Browser(browserPane, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.setTicket(null);
	}

	// Specific e-mail messages into the list
	public void setTicket(Object mail) {
//		if (mail != null) {
//			subjectLabel.setText(mail.getTitle() + "     ,");
//			addressLabel.setText(mail.getSender() + "     ,");
//			label_2.setText(mail.getDate() + "     ,");
//			browser.setText(mail.getMainPartHTML());
//		} else {
//			System.err.println("mail == null ? " + this);
//		}
	}

}