package org.zalando.seaproxy.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import org.zalando.stups.oauth2.spring.server.TokenInfoResourceServerTokenServices;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableConfigurationProperties
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    private static final String PATH = "path";
    private static final String SCOPE = "scope";
    private static final String METHOD = "method";
    private static final String PERMIT = "permit";

    @Autowired
    private Oauth2Properties oauth2Properties;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

        List<String> paths = oauth2Properties.getRoutes().stream().map(route -> route.get("path")).collect(Collectors
                    .toList());

        http.authorizeRequests().antMatchers("/health").permitAll();

        http.requestMatchers().antMatchers(paths.toArray(new String[paths.size()]));

        for (Map<String, String> route : oauth2Properties.getRoutes()) {
            final String method = route.get(METHOD);
            final String path = route.get(PATH);
            final String scope = route.get(SCOPE);

            if (PERMIT.equals(scope)) {
                http.authorizeRequests().antMatchers(path).permitAll();
            } else if (null != method && !method.isEmpty()) {
                http.authorizeRequests().antMatchers(HttpMethod.valueOf(method), path).access("#oauth2.hasScope('"
                        + scope + "')");
            } else {
                http.authorizeRequests().antMatchers(path).access("#oauth2.hasScope('" + scope + "')");
            }
        }

        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices(
            final ResourceServerProperties resourceServerProperties) {

        return new TokenInfoResourceServerTokenServices(resourceServerProperties.getTokenInfoUri(), "sea-proxy");
    }
}
