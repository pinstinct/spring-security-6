package dev.limhm.security6;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class AccountDto {

  private String username;
  private String password;
  private Collection<GrantedAuthority> authorities;

  public AccountDto(String username, String password, Collection<GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }
}
