package by.jrr.auth.configuration;

import by.jrr.auth.service.MyUserDetailsService;
import by.jrr.constant.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";

        http.
                authorizeRequests()
                .antMatchers(loginPage).permitAll()
                .antMatchers(Endpoint.HOME).permitAll()
                .antMatchers(Endpoint.COURSE+"/*").permitAll()
                .antMatchers(Endpoint.COURSE_LIST).permitAll()
                .antMatchers(Endpoint.TOPIC_LIST).permitAll()
                .antMatchers(Endpoint.LECTURE_LIST).permitAll()
                .antMatchers(Endpoint.PRACTICE_LIST).permitAll()
                .antMatchers(Endpoint.Q_AND_A_LIST).permitAll()
                .antMatchers(Endpoint.PROJECT_LIST).permitAll()
                .antMatchers(Endpoint.ISSUE_LIST).permitAll()
                .antMatchers(Endpoint.DOMAIN_LIST).permitAll()
                .antMatchers(Endpoint.SUBJECT_LIST).permitAll()
                .antMatchers(Endpoint.REVIEW_REQUEST_LIST).permitAll()
                .antMatchers(Endpoint.REGISTER_USER_AND_ENROLL_TO_STREAM).permitAll()
                .antMatchers(Endpoint.REGISTER_USER).permitAll()
                .antMatchers(Endpoint.PROFILE_LIST).permitAll()
                .antMatchers(Endpoint.PROFILE_CARD+"/*").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage)
//                .loginPage("/")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true)
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        http.headers().frameOptions().disable(); //this is need to open H2 database console
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/dist/**", "/plugins/**");
    }

}

