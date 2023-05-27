package com.devthalys.inventorycontrolsystem.config;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                    .and()
                .authorizeRequests()
                .antMatchers("/products/save")
                    .hasRole("MANAGER")
                .antMatchers("/products/update/**")
                    .hasRole("MANAGER")
                .antMatchers("/stock/movementRegister")
                    .hasRole("MANAGER")
                .anyRequest().authenticated()
                    .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser("gerente")
                    .password(passwordEncoder().encode("123"))
                    .roles("MANAGER")
                        .and()
                    .withUser("operador")
                    .password(passwordEncoder().encode("456"))
                    .roles("OPERATOR");
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