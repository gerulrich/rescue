package net.cloudengine.model.config;

import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;

public interface ATD {
	
	/**
	 * Get the next user.
	 * @param group
	 * @return
	 */
	User getNext(Group group);

}
