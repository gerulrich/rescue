
package net.cloudengine.model.auth

import java.util.ArrayList
import java.util.List

import org.bson.types.ObjectId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id

@Entity(value="user", noClassnameStored=true)
class User implements UserDetails {
	
	@Id
	ObjectId id;
	String displayName
	String username
	String password

	Account account = new Account();
	Collection<Role> roles;
		
	@Override
	Collection<GrantedAuthority> getAuthorities() {
		// FIXME sacar los roles de algun lado
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority('ROLE_USER'));
		authorities.add(new SimpleGrantedAuthority('ROLE_ADMIN'));
		return authorities;
	}
	
	@Override
	String toString() {
		
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");
	
		result.append( this.getClass().getName() );
		result.append( " Object {" );
		result.append(newLine);
	
		this.properties.each {
			result.append(newLine+it.getKey()+": "+it.getValue());
		}
		result.append("}");
		return result.toString();
	}

	def addRole(Role role) {
		this.roles.add(role);
	}
	
	def removeRole(Role role) {
		this.roles.remove(role);
	}

	@Override
	boolean isAccountNonExpired() {
		return !account.isAccountExpired();
	}

	@Override
	boolean isAccountNonLocked() {
		return !account.isLocked();
	}

	@Override
	boolean isCredentialsNonExpired() {
		return !account.isCredentialsExpired();
	}

	@Override
	boolean isEnabled() {
		return account.isEnabled();
	}
}