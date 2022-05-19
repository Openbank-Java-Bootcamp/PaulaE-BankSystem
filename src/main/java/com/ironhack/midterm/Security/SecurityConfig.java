package com.ironhack.midterm.Security;

import com.ironhack.midterm.Filter.CustomAuthenticationFilter;
import com.ironhack.midterm.Filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAnyAuthority;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/checkings").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/savings").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/creditcards").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/admins").permitAll();
        http.authorizeRequests().antMatchers(DELETE, "/api/admins/{id}").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/thirdparties").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/thirdparties/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/checkings/{id}").hasAnyAuthority("ADMIN", "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/creditcards/{id}").hasAnyAuthority("ADMIN", "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/savings/{id}").hasAnyAuthority("ADMIN", "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/studentcheckings/{id}").hasAnyAuthority("ADMIN", "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(PATCH, "/api/studentcheckings/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/studentcheckings/{id}").hasAnyAuthority("ADMIN");;
        http.authorizeRequests().antMatchers(PATCH, "/api/creditcards/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/creditcards/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/checkings/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/checkings/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/savings/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/savings/{id}").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/accountholder/transferfunds").hasAnyAuthority( "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(PATCH, "/api/accountholder/transferfunds/thridparty").hasAnyAuthority( "ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(PATCH, "/api/thridparty/transferfund").hasAnyAuthority( "THIRD_PARTY");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
