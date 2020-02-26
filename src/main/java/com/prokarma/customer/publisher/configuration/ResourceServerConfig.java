package com.prokarma.customer.publisher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.prokarma.customer.publisher.security.ApiErrorWebResponseExceptionTranslator;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private static final String RESOURCE_ID = "resource_id";

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.authenticationEntryPoint(authenticationEntryPoint()).resourceId(RESOURCE_ID)
        .stateless(false);
  }

  @Bean
  public WebResponseExceptionTranslator<?> exceptionTranslator() {
    return new ApiErrorWebResponseExceptionTranslator();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    final OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
    entryPoint.setExceptionTranslator(exceptionTranslator());
    return entryPoint;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.anonymous().disable().authorizeRequests().antMatchers("/customers**")
        .access("hasRole('ADMIN')").and().exceptionHandling()
        .accessDeniedHandler(new OAuth2AccessDeniedHandler());
  }
}
