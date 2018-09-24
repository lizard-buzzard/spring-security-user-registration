package com.lizard.buzzard.main.config;

import com.lizard.buzzard.security.CustomRememberMeServices;
import com.lizard.buzzard.security.DaoAuthenticationProviderExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    @Qualifier("authenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    @Qualifier("authenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

//    @Autowired
//    @Qualifier("userDetailsService")
//    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("myLogoutSuccessHandler")
    private LogoutSuccessHandler myLogoutSuccessHandler;

//    @Autowired
//    private DataSource dataSource;

//    @Autowired
//    private MyJdbcTokenRepositoryImpl myJdbcTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(7);
    }

    // Beans' section
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("userDetailsService") UserDetailsService userDetailsService) {
        final DaoAuthenticationProviderExtended authProvider = new DaoAuthenticationProviderExtended();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        MyJdbcTokenRepositoryImpl repo = new MyJdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        repo.setCreateTableOnStartup(true);
        return repo;
    }

    @Autowired
    PersistentTokenRepository persistentTokenRepository;// for sake of an experiment, bound to .tokenRepository(persistentTokenRepository)

    // NOTE: uncomment two definitions below if one definition above is commented
    @Bean
    public RememberMeServices rememberMeServices(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            @Qualifier("persistentTokenRepository")PersistentTokenRepository persistentTokenRepository) {
        CustomRememberMeServices rememberMeServices = new CustomRememberMeServices("theKey", userDetailsService, persistentTokenRepository);
        return rememberMeServices;
    }
    @Autowired
    CustomRememberMeServices rememberMeServices;

    // Configuration section
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeRequests()
                .antMatchers(
                        "/login*"
                        , "/registration*"
                        , "/registrationStatus").permitAll()
                .antMatchers("/invalidSession*").anonymous()
//                .anyRequest().authenticated()
                .anyRequest().hasAuthority("READ_PRIVILEGE")
            .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/homepage.html")
                .failureUrl("/login?badcredentialerror=true")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
            .and().sessionManagement()
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .invalidSessionUrl("/invalidSession.html")
                .sessionFixation().none()                           // .newSession(), .migrateSession()
            .and().logout()
                .logoutUrl("/mylogout")                             // is not the same as default /logout; triggers logout process
                // .logoutSuccessUrl("/login?logoutSuccess=true")   // ignored in case when logoutSuccessHandler is specified
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(false)                        // is the same as default setup
                .permitAll()
            .and().rememberMe()
//                .key("uniqueAndSecret")
//                .rememberMeCookieName("example-app-remember-me")

                .tokenRepository(persistentTokenRepository)       // part of @Autowired PersistentTokenRepository persistentTokenRepository;
                .tokenValiditySeconds(24 * 60 * 60)

                .rememberMeServices(rememberMeServices).key("theKey");
        ;

        httpSecurity.csrf().disable();                              // NOTE: it's highly important !!!
//        httpSecurity.headers().frameOptions().disable();
    }

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    public SecurityConfig() {
        super();
    }

/////////////////////////////////////////////////////////////////////////////

//    private AuthenticationProvider authenticationProvider;
//
//    @Autowired
//    @Qualifier("daoAuthenticationProvider")
//    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder(StrongPasswordEncryptor passwordEncryptor){
//        PasswordEncoder passwordEncoder = new PasswordEncoder();
//        passwordEncoder.setPasswordEncryptor(passwordEncryptor);
//        return passwordEncoder;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(17);
//    }


//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
//                                                               UserDetailsService userDetailsService){
//
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return daoAuthenticationProvider;
//    }

//    @Autowired
//    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder){
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
//    }
}
