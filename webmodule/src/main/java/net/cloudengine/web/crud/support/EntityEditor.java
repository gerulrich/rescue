package net.cloudengine.web.crud.support;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.commons.Identifiable;

public class EntityEditor<E extends Identifiable<PK>, PK extends Serializable> extends PropertyEditorSupport {
	
	private Repository<E,PK> repository;
	private IdParser<PK> parser;
	
	public EntityEditor(RepositoryLocator repositoryLocator, IdParser<PK> parser, Class<E> clazz) {
		this.repository = repositoryLocator.getRepository(clazz);
		this.parser = parser;
	}
	
	@Override
	public void setAsText(String text) {
		E entity = repository.get(parser.parse(text));
		setValue(entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getAsText() {
		if (getValue() != null) {
			return ((Identifiable<Long>)getValue()).getPK().toString();
		} return "";
	}

}
