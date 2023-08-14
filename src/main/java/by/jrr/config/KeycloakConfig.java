//package by.jrr.config;
//
//import by.jrr.constant.Endpoint;
//import org.keycloak.adapters.KeycloakConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//import org.springframework.web.cors.CorsConfiguration;
//
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
//public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {
//
//    @Bean
//    public KeycloakConfigResolver KeycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        var grantedAuthorityMapper = new SimpleAuthorityMapper();
//        grantedAuthorityMapper.setPrefix("ROLE_");
//        grantedAuthorityMapper.setConvertToUpperCase(true);
//
//        var keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
//
//        auth.authenticationProvider(keycloakAuthenticationProvider);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http
//                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//                .and()
//                .authorizeRequests()
//                .antMatchers(Endpoint.HOME).permitAll()
//                .antMatchers(Endpoint.COURSE+"/*").permitAll()
//
//                .antMatchers(Endpoint.REDIRECT+"/**").permitAll()
//
//                .antMatchers(Endpoint.COURSE_LIST).permitAll()
//                .antMatchers(Endpoint.TOPIC_LIST).permitAll()
//                .antMatchers(Endpoint.LECTURE_LIST).permitAll()
//                .antMatchers(Endpoint.PRACTICE_LIST).permitAll()
//                .antMatchers(Endpoint.Q_AND_A_LIST).permitAll()
//                .antMatchers(Endpoint.PROJECT_LIST).permitAll()
//                .antMatchers(Endpoint.ISSUE_LIST).permitAll()
//                .antMatchers(Endpoint.DOMAIN_LIST).permitAll()
//                .antMatchers(Endpoint.SUBJECT_LIST).permitAll()
//                .antMatchers(Endpoint.REVIEW_REQUEST_LIST).permitAll()
//                .antMatchers(Endpoint.REGISTER_USER_AND_ENROLL_TO_STREAM).permitAll()
//                .antMatchers(Endpoint.REGISTER_USER).permitAll()
//                .antMatchers(Endpoint.PROFILE_LIST).permitAll()
////
//                .antMatchers(Endpoint.TOPIC+"/*").permitAll()
//                .antMatchers(Endpoint.TOPIC_LIST).permitAll()
//                .antMatchers("/api/registerForm/validate/email/*").permitAll() // TODO: 23/06/20 make it psfs
//                .antMatchers("/auth/validate/user").permitAll() // TODO: 23/06/20 make it psfs
//                .antMatchers("/api/registerForm/validate/firstAndLastName/*").permitAll() // TODO: 23/06/20 make it psfs
//                .antMatchers("/api/registerForm/validate/phone/*").permitAll() // TODO: 23/06/20 make it psfs
//                .antMatchers(Endpoint.PROFILE_CARD+"/*").permitAll()
//                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                .antMatchers("/crm/**").hasAnyRole("ADMIN", "SALES")
//                .antMatchers("/"+Endpoint.REGISTER_USER_ADMIN_REST_ERRORS+"/**").hasAuthority("ROLE_ADMIN")
//                .antMatchers("/login").authenticated()
//                .anyRequest()
//                .authenticated();
//    }
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring()
//                .antMatchers(
//                        "/resources/**",
//                        "/static/**",
//                        "/css/**",
//                        "/js/**",
//                        "/img/**",
//                        "/dist/**",
//                        "/plugins/**",
//                        "/my/**",
//                        "/h2-console/**"
//                );
//    }
//
//}
//
////disable csrf  https://coderoad.ru/51952221/Spring-Boot-Keycloak-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-%D1%82%D0%BE%D0%BB%D1%8C%D0%BA%D0%BE-%D0%B4%D0%BB%D1%8F-%D0%BC%D0%B5%D1%82%D0%BE%D0%B4%D0%B0-get
