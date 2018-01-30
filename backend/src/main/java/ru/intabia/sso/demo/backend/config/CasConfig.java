package ru.intabia.sso.demo.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Enable Spring Security OAuth 2.0.
 */
@Slf4j
@EnableOAuth2Sso
@Profile("cas-sso")
@Configuration
public class CasConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.debug("CAS SSO is configured...");
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/backend*", "/swagger-ui.html", "/webjars/**")
                .hasRole("USER")
                .anyRequest()
                .permitAll();
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
        return map -> {
            log.debug("Extract CAS Authorities... {}", map);
            if (map != null && map.containsKey("role")) {
                String role = (String) map.get("role");
                if ("user".equals(role)) {
                    return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
                }
            }
            throw new BadCredentialsException("Not in available role");
        };
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }
}
