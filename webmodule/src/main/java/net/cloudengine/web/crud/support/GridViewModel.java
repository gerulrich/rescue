package net.cloudengine.web.crud.support;

import java.util.List;

import com.google.common.collect.Lists;


public class GridViewModel {

	private String titleKey;
	
	private List<ColumnViewModel> columns = Lists.newArrayList();
	private List<CrudAction> actions = Lists.newArrayList();
	
	public void addColumn(ColumnViewModel column) {
		this.columns.add(column);
	}
	
	public void addAction(CrudAction action) {
		this.actions.add(action);
	}

	public List<ColumnViewModel> getColumns() {
		return columns;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
	
	public List<CrudAction> getActions() {
		return actions;
	}

	public CrudAction getAction(int actionType) {
		for(CrudAction action : actions) {
			if(actionType==action.getAction()) {
				return action;
			}
		}
		return null;
	}
}
