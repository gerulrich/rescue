package net.cloudengine.dao.jpa;

import java.util.Collection;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.resource.Resource;

public interface ResourceRepository extends Repository<Resource, Long> {
	
	/**
	 * Retorna todos los recursos cuya version es mayor
	 * a la especificada. Su proposito es obtener los recursos
	 * que se modificaron desde la ultima consulta.
	 * @param version
	 * @return
	 */
	Collection<Resource> getResourceWithGreaterVersion(long version);
	
}
