package net.cloudengine.model.auth

import org.springframework.security.acls.domain.AbstractPermission
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission

class AppPermission extends AbstractPermission {
        
	
	//	Permission READ = new BasePermission(1 << 0, 'R'); // 1
	//	Permission WRITE = new BasePermission(1 << 1, 'W'); // 2
	//	Permission CREATE = new BasePermission(1 << 2, 'C'); // 4
	//	Permission DELETE = new BasePermission(1 << 3, 'D'); // 8
	//	Permission ADMINISTRATION = new BasePermission(1 << 4, 'A'); // 16
	
	// FIXME cambiar permisos.
    public static final Permission DISPATCH = new AppPermission(1 << 5, 'I'.charAt(0)) // 32
    public static final Permission WRITE = new AppPermission(1 << 6, 'G'.charAt(0)) // 64

    protected AppPermission(int mask) {
       super(mask);
    }

    protected AppPermission(int mask, char code) {
        super(mask, code);
    }
}