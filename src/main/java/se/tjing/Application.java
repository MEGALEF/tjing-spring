package se.tjing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;

import se.tjing.signin.SimpleSignInAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJpaRepositories
// @EnableSocial
@PropertySource("classpath:application.properties")
public class Application {

	public static void main(String[] args) {
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}
		System.setProperty("server.port", webPort);

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new SimpleSignInAdapter(new HttpSessionRequestCache());
	}

}
