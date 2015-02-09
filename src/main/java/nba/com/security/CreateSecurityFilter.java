package nba.com.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebMvcSecurity
public class CreateSecurityFilter extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;

	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.passwordEncoder(passwordEncoder())
				.usersByUsernameQuery(
						"select name, password, enabled from users where name=?")
				.authoritiesByUsernameQuery(
						"select username, role from user_roles where username=?");
	}
		
	@Override
	 public void configure(WebSecurity web) throws Exception {
	 web.ignoring()
	 .antMatchers("/images/**")
	 .antMatchers("/styles/**")
	 .antMatchers("/scripts/**");
	}
	 @Override
	 protected void configure(HttpSecurity http) throws Exception { 
		 http.formLogin()
		 .and().authorizeRequests()
		 .antMatchers("/").hasAuthority("ROLE_USER");
	 }

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
