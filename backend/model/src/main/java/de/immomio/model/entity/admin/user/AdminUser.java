package de.immomio.model.entity.admin.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.immomio.data.base.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user", schema = "administration")
public class AdminUser extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = -5119815590592853171L;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    private Date lastLogin;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 1, max = 255)
    private String password;

    private boolean locked;

    private boolean expired;

    private boolean enabled;

    private AdminUserType type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Transient
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
