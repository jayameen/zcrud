package com.jayameen.zcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Madan KN
 */
@Configuration
public class ZCrudWebSecurityConfigurerAdapter {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests((authorizeRequests) -> authorizeRequests
                .antMatchers("/").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/oauth2/**/**").permitAll()
                .antMatchers("/login/**/**/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/**/*.html").permitAll()
                .anyRequest().authenticated()
            ).csrf(Customizer.withDefaults());
            http.oauth2ResourceServer((oauth2) -> oauth2.opaqueToken());
        return http.build();
    }
    /*
    AuthenticationManagerResolver<HttpServletRequest> customAuthenticationManager() {
        LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers = new LinkedHashMap<>();

        // USE JWT tokens (locally validated) to validate HEAD, GET, and OPTIONS requests
        List<String> readMethod = Arrays.asList("HEAD", "GET", "OPTIONS");
        RequestMatcher readMethodRequestMatcher = request -> readMethod.contains(request.getMethod());
        authenticationManagers.put(readMethodRequestMatcher, jwt());

        // all other requests will use opaque tokens (remotely validated)
        RequestMatchingAuthenticationManagerResolver authenticationManagerResolver
                = new RequestMatchingAuthenticationManagerResolver(authenticationManagers);

        // Use opaque tokens (remotely validated) for all other requests
        authenticationManagerResolver.setDefaultAuthenticationManager(opaque());
        return authenticationManagerResolver;
    }

    // Mimic the default configuration for JWT validation.
    AuthenticationManager jwt() {

        // This is basically the default jwt logic
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
        return authenticationProvider::authenticate;
    }

    // Mimic the default configuration for opaque token validation
    AuthenticationManager opaque() {
        // The default opaque token logic
        OpaqueTokenIntrospector introspectionClient = new NimbusOpaqueTokenIntrospector(
                introspectorUri,
                clientID,
                clientSecret);
        return new OpaqueTokenAuthenticationProvider(introspectionClient)::authenticate;
    }
    */

}