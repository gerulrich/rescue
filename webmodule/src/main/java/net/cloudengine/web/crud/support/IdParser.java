package net.cloudengine.web.crud.support;

import java.io.Serializable;

public interface IdParser<PK extends Serializable> {
	
	PK parse(String text);

}
