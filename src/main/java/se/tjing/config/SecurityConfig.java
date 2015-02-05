package se.tjing.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApplicationContext context;

	@Inject
	private DataSource dataSource;

	@Autowired
	public void registerAuthentication(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(
						"select username, password, true from person where username = ?")
				.authoritiesByUsernameQuery(
						"select username, 'ROLE_USER' from person where username = ?")
				.passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.css", "/**/*.png", "/**/*.gif",
				"/**/*.jpg");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/signin/authenticate")
				.failureUrl("/signin?param.error=bad_credentials")
				.and()
				.logout()
				.logoutUrl("/signout")
				.deleteCookies("JSESSIONID")
				.and()
				.authorizeRequests()
				.antMatchers("/admin/**", "/favicon.ico", "/resources/**",
						"/auth/**", "/signin/**", "/signup/**",
						"/disconnect/facebook").permitAll().antMatchers("/**")
				.authenticated().and().rememberMe();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

}
