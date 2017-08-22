package tz.co.nezatech.dev.surveysubmission.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from tbl_user where username=?")
				.authoritiesByUsernameQuery(
						"select u.username, r.name as role from tbl_role r left join tbl_user u on r.id=u.role_id where username=?")
				.passwordEncoder(passwordEncoder);
		;
	}

	@Bean
	public static DefaultRolesPrefixPostProcessor defaultRolesPrefixPostProcessor() {
		return new DefaultRolesPrefixPostProcessor();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated().and().csrf().disable()
					.httpBasic();
			http.antMatcher("/survey/**").authorizeRequests().anyRequest().authenticated().and().csrf().disable()
			.httpBasic();
			
		}
	}

	@Configuration
	public static class FormLoginWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**").permitAll().anyRequest()
					.authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll().and()
					.logout().permitAll().and().headers().frameOptions().sameOrigin();
		}
	}
}