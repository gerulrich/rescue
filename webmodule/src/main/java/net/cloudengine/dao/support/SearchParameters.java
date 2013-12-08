package net.cloudengine.dao.support;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.util.List;

public class SearchParameters implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Condition> conditions = newArrayList();
	private boolean andMode = true;

	public void addContidion(Condition condition) {
		conditions.add(checkNotNull(condition));
	}

	public void addCondition(String field, Operator operator, Object value) {
		addContidion(new Condition(field, operator, value));
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public boolean isAndMode() {
		return andMode;
	}

	public void setAndMode(boolean andMode) {
		this.andMode = andMode;
	}	
}