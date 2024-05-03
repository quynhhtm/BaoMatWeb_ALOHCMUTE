package hcmute.alohcmute.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import hcmute.alohcmute.filter.ContentTypeOptionsFilter;
import hcmute.alohcmute.security.UserDetailsServiceImpl;
import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSpringSecurity {
    
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) 
			throws Exception { 
		final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
		configurers.add(new GlobalAuthenticationConfigurerAdapter() { 
				@Override 
				public void configure(AuthenticationManagerBuilder auth) 
						throws Exception { 
				}
			}
		);
		return authConfig.getAuthenticationManager();
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//    	        .csrf(csrf -> csrf.disable())
    	        .authorizeHttpRequests((authorize) -> authorize
    	        				.requestMatchers("/404").permitAll()
    	        				.requestMatchers("/forgotpassword/**").permitAll()
    	        				.requestMatchers("/register/**").permitAll()
    	        				.requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
    	        				.requestMatchers("/admin/**").hasAuthority("ADMIN")
    	        				.anyRequest().authenticated()
    	        )
    	        .formLogin(
    	                form -> form
    	                        .loginPage("/login").loginProcessingUrl("/login")
    	                        .usernameParameter("username").passwordParameter("password")
    	                        .defaultSuccessUrl("/user/newfeed", true)
    	                        .permitAll()
    	        )
    	        .logout(
    	                logout -> logout
    	                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    	                        .permitAll()
    	        )
    	        .exceptionHandling(
    	        		except -> except
    	        				.accessDeniedPage("/403")
    	        )
    	        .build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	return (web) -> web.ignoring().requestMatchers("/css/**", "/images/**", "/js/**");
    }
    
    @Bean
    public FilterRegistrationBean<Filter> contentTypeOptionsFilterRegistrationBean() {
    	FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
    	registrationBean.setFilter(new ContentTypeOptionsFilter());
    	registrationBean.addUrlPatterns("/*"); // Áp dụng filter cho tất cả các URL
    	registrationBean.setName("contentTypeOptionsFilter");
    	registrationBean.setOrder(1); // Xác định thứ tự ưu tiên của filter
    	return registrationBean;
    }


}