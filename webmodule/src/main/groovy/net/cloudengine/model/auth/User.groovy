
package net.cloudengine.model.auth

import javax.validation.constraints.NotNull

import net.cloudengine.validation.Email

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document(collection="user")
class User implements UserDetails {
	
	@Id ObjectId id;
	
	@NotNull
	@NotEmpty
	String displayName
	
	@NotEmpty
	@Email
	String username
	
	String password
	
	@NotEmpty
	String roles;
	
	@Transient
	List<Permission> permissions;

	Account account = new Account();
	
	@DBRef Group group;

	boolean hasPermission(String name) {
		return permissions.find { Permission p ->
			return p.getName().equals(name);
		}
	}
	
	@Override
	Collection<GrantedAuthority> getAuthorities() {
		return permissions;
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