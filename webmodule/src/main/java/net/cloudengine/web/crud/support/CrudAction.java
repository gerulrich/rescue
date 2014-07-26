package net.cloudengine.web.crud.support;

public class CrudAction {
	
	public static final int ADD = 1;
	public static final int EDIT = 2;
	public static final int DELETE = 3;
	public static final int VIEW = 4;
	public static final int LIST = 5;
	public static final int SECUTIRY = 6;
	public static final int CUSTOM = 7;
	
	private int action;
	private String url;
	private String messageKey;
	private String icon;
	
	public CrudAction(int action, String url, String messageKey) {
		super();
		this.action = action;
		this.url = url;
		this.messageKey = messageKey;
		switch(action) {
			case EDIT: icon = "edit.png"; break;
			case DELETE: icon = "delete.png"; break;
			case VIEW: icon = "view_doc.png"; break;
			case SECUTIRY: icon = "password.png"; break;
			case CUSTOM: icon = "gears.png"; break;			
		}
	}
	
	public int getAction() {
		return action;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getMessageKey() {
		return messageKey;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public boolean isGeneric() {
		return action == ADD || action == LIST;
	}
}