package com.jayameen.zcrud.security;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Madan KN
 */
@Configuration
public class ZCrudWebSecurityConfigurerAdapter {

    @Autowired AuthExcludeUrls authExcludeUrls;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests((authorizeRequests) ->  {
                try {
                    authExcludeUrls.getExcludeUrls().forEach(url -> authorizeRequests.antMatchers(url).permitAll());
                    authorizeRequests.anyRequest().authenticated().and().oauth2ResourceServer().jwt();
                    http.csrf().disable();
                    http.cors().disable();
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            });
        return http.build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}