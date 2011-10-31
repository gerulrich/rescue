package net.cloudengine.service.acl;

import java.util.List;
import java.util.Map;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.ChildrenExistException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

public class MongoAclServiceImpl implements MutableAclService {

	@Override
	public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity) {
		return null;
	}

	@Override
	public Acl readAclById(ObjectIdentity object) throws NotFoundException {
		return null;
	}

	@Override
	public Acl readAclById(ObjectIdentity object, List<Sid> sids) throws NotFoundException {
		return null;
	}

	@Override
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects) throws NotFoundException {
		return null;
	}

	@Override
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableAcl createAcl(ObjectIdentity objectIdentity)
			throws AlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren)
			throws ChildrenExistException {
		// TODO Auto-generated method stub

	}

	@Override
	public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
