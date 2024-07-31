package dev.limhm.security6;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AccountDto accountDto = new AccountDto("user", "{noop}1111",
        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    return new CustomUserDetails(accountDto);
  }
}
