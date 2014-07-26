package net.cloudengine.web.crud.support;

public class ColumnViewModel {

	private String titleKey;
	private String propertyPath;
	
	public ColumnViewModel(String titleKey, String propertyPath) {
		super();
		this.titleKey = titleKey;
		this.propertyPath = propertyPath;
	}
	
	public String getTitleKey() {
		return titleKey;
	}
	
	public String getPropertyPath() {
		return propertyPath;
	}

}
