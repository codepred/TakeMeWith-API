package codepred.user.model;

import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {
  ROLE_NONE,
  ROLE_ADMIN,
  ROLE_DRIVER,
  ROLE_PASSENGER;

  public String getAuthority() {
    return name();
  }

}
