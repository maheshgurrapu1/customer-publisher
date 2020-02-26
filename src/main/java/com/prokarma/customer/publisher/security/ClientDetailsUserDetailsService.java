package com.prokarma.customer.publisher.security;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsUserDetailsService implements UserDetailsService {

  private final ClientDetailsService clientDetailsService;

  public ClientDetailsUserDetailsService(ClientDetailsService clientDetailsService) {
    this.clientDetailsService = clientDetailsService;
  }

  public UserDetails loadUserByUsername(String username) {
    ClientDetails clientDetails;
    try {
      clientDetails = clientDetailsService.loadClientByClientId(username);
    } catch (NoSuchClientException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
    String clientSecret = clientDetails.getClientSecret();
    return new User(username, clientSecret, getAuthority());
  }

  private List<SimpleGrantedAuthority> getAuthority() {
    return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }
}
