package com.devthalys.inventorycontrolsystem.config;

import com.devthalys.inventorycontrolsystem.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                    .and()
                .authorizeRequests()
                .antMatchers("/users/save").hasRole("CEO")
                .antMatchers("/users/update/**").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/users/delete/**").hasRole("CEO")
                .antMatchers("/products/save").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/products/update/**").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/products/delete/**").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/inventory/save").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/inventory/update/**").hasAnyRole("CEO", "MANAGER")
                .antMatchers("/inventory/delete/**").hasAnyRole("CEO", "MANAGER")
                .anyRequest().authenticated()
                    .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser("ceo")
                    .password(passwordEncoder().encode("123"))
                    .roles("CEO");

            auth
                    .userDetailsService(userService)
                    .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}