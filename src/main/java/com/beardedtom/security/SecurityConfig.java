package com.beardedtom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * Defines query from application.properties
     */
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        /**
         * BCryptPasswordEncoder, works with Spring Security 5
         */
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    /**
     * Basic database authentication(with jdbc driver), properties
     */
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers(HttpMethod.POST, "/panel/sensors/sensor/**").permitAll()
                .antMatchers(HttpMethod.GET, "/panel/sensors/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").anyRequest().authenticated()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN").anyRequest().authenticated()
                .antMatchers("/panel").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage("/home").defaultSuccessUrl("/panel?loginSuccessful=true").failureUrl("/?loginFailure=true")
                .usernameParameter("userEmail")
                .passwordParameter("userPassword")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home?logoutSuccessful=true").deleteCookies("JSESSIONID").and()
                .exceptionHandling().accessDeniedPage("/access_denied");
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
