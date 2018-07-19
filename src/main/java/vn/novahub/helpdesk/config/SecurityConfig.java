package vn.novahub.helpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import vn.novahub.helpdesk.filter.TokenAuthenticationFilterAfter;
import vn.novahub.helpdesk.filter.TokenAuthenticationFilterBefore;
import vn.novahub.helpdesk.service.MyUserDetailService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
                            jsr250Enabled = true,
                            prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService userDetailService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        TokenAuthenticationFilterBefore tokenAuthenticationFilterBefore = new TokenAuthenticationFilterBefore();
        TokenAuthenticationFilterAfter tokenAuthenticationFilterAfter = new TokenAuthenticationFilterAfter();

        httpSecurity.addFilterBefore(tokenAuthenticationFilterBefore, BasicAuthenticationFilter.class);
        httpSecurity.addFilterAfter(tokenAuthenticationFilterAfter, BasicAuthenticationFilter.class);

        httpSecurity.sessionManagement().disable();

        httpSecurity.authorizeRequests()
                        .antMatchers("/api**", "/login", "/api/login", "/login-google", "/register", "/user/update").permitAll()
                    .and()
                        .exceptionHandling().accessDeniedPage("/403")
                    .and()
                        .csrf().disable();

    }
}
