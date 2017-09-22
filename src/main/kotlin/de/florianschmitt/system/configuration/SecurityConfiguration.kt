package de.florianschmitt.system.configuration

import de.florianschmitt.service.authorization.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
internal class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    @Throws(Exception::class)
    fun configureGlobalSecurity(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsServiceImpl>(userDetailsService)
                .passwordEncoder(encoder())
    }

    override fun userDetailsService() = userDetailsService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // TODO: dev
        http.headers().frameOptions().disable()

        http.csrf().disable()//
                .anonymous().authorities(Roles.authorityName(Roles.REQUESTER))
                .and()//
                .authorizeRequests()
                .antMatchers("/admin/systemuser/**").hasRole(Roles.ADMIN)//
                .antMatchers("/admin/payment/**").hasAnyRole(Roles.FINANCE, Roles.ADMIN)//
                .antMatchers("/admin/**").hasRole(Roles.SYSTEMUSER)//
                .and()//a
                .httpBasic()//
                .and() //
                .sessionManagement()//
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun encoder() = BCryptPasswordEncoder()
}