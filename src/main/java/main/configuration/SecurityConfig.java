package main.configuration;

import main.service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final CustomerUserDetailService service;

    public SecurityConfig(CustomerUserDetailService service) {
        this.service = service;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.cors();
        String point1Test = "/auth";
        String point2Test = "/reg";
        String point3Test = "/adddot";
        String point4Test = "/checker";
        String point5Test = "/addDocument";
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(point1Test).permitAll()
                .antMatchers(point2Test).permitAll()
                .antMatchers(point3Test).permitAll()
                .antMatchers(point4Test).permitAll()
                .antMatchers(point5Test).permitAll()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(
                Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
