package com.geoc.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    public void configure(HttpSecurity  http) throws Exception{
    	http.httpBasic();
    	http.authorizeRequests()
        .antMatchers("/inbox","/question/add").authenticated()
        .anyRequest().permitAll();
    	http.csrf().disable();
    }
}
