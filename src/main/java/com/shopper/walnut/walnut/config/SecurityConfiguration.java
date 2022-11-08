package com.shopper.walnut.walnut.config;

import com.shopper.walnut.walnut.service.BrandSignUpService;
import com.shopper.walnut.walnut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BrandSignUpService brandService;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/Users/chandle/Downloads/walnut/src/main/resources/static/brand/**");

        super.configure(web);
    }
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers(
                        "/"
                        , "/user/register"
                        , "/user/email-auth"
                        , "/user/find-password"
                        , "/user/register/write"
                        , "/brand/register"
                        , "/brand/register.do"
                        , "/category/**"
                        , "/item/info"
                )
                .permitAll();
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN");
        http.authorizeRequests()
                .antMatchers("/brand/main/**")
                .hasAuthority("ROLE_BRAND");
        http.formLogin()
                .loginPage("/user/login")
                .failureHandler(getFailureHandler())
                .successHandler(new LogAuthenticationSuccess())
                .permitAll();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                .accessDeniedPage("/common/error");


        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getPasswordEncoder());
        super.configure(auth);
    }
}
