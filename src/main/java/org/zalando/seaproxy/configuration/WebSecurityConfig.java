package org.zalando.seaproxy.configuration;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.cloud.security.oauth2.resource.ResourceServerProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import org.zalando.stups.oauth2.spring.server.TokenInfoResourceServerTokenServices;

@Configuration
@EnableWebSecurity
@EnableOAuth2Resource
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.scope:uid}")
    private String scope;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().authorizeRequests()
            .antMatchers("/status.info").permitAll().and().authorizeRequests().antMatchers("**")
            .access("#oauth2.hasScope('" + scope + "')").anyRequest().authenticated();
    }

    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices(
            final ResourceServerProperties resourceServerProperties) {

        return new TokenInfoResourceServerTokenServices(resourceServerProperties.getTokenInfoUri(), "sea-proxy");
    }
}
