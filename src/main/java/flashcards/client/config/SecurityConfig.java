package flashcards.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http    .authorizeRequests()
                .antMatchers("/users", "/users/**").hasRole("ADMIN")
                .antMatchers("/register/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/authenticateUser").defaultSuccessUrl("/").permitAll()
                .and()
                .logout((logout) -> logout.logoutSuccessUrl("/login?logout").permitAll())
                //.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").deleteCookies("remove").invalidateHttpSession(true)
                //.and()
                .exceptionHandling().accessDeniedPage("/access-denied");


        return http.build();
    }

}
