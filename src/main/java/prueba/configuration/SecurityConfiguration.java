package prueba.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import prueba.filter.JWTFilter;
import prueba.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/user/authenticate",
            "/swagger-ui/index.html/user/register",
            "/swagger-ui/index.html/user/addCoin",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/user/register",
            "/user/authenticate",
    };
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JWTFilter jwtFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(SWAGGER_WHITELIST).permitAll();

        /*http.authorizeRequests()
                .antMatchers("/swagger-ui/**", "/swagger-ui.html/user/authenticate","/swagger-ui.html/user/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
         */


       /* http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(SWAGGER_WHITELIST)
                .permitAll()
                .antMatchers("/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
*/

    }
}
