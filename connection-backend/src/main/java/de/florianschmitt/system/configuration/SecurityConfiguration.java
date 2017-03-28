package de.florianschmitt.system.configuration;

import de.florianschmitt.service.authorization.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: dev
        http.headers().frameOptions().disable();

        http.csrf().disable()//
                .anonymous().authorities(Roles.INSTANCE.authorityName(Roles.INSTANCE.getREQUESTER()))
                .and()//
                .authorizeRequests()
                .antMatchers("/admin/systemuser/**").hasRole(Roles.INSTANCE.getADMIN())//
                .antMatchers("/admin/payment/**").hasAnyRole(Roles.INSTANCE.getFINANCE(), Roles.INSTANCE.getADMIN())//
                .antMatchers("/admin/**").hasRole(Roles.INSTANCE.getSYSTEMUSER())//
                .and()//a
                .httpBasic()//
                .and() //
                .sessionManagement()//
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}