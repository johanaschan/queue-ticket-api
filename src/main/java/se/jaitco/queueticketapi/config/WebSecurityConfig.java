package se.jaitco.queueticketapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.jaitco.queueticketapi.filter.JwtAuthenticationTokenFilter;
import se.jaitco.queueticketapi.filter.UnauthorizedEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login/").permitAll()
                .antMatchers("/tickets-websocket/**").permitAll()
                .antMatchers("/topic/*").permitAll()
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}