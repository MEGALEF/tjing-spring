package se.tjing;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.apache.commons.dbcp.BasicDataSource;

import se.tjing.restcontrollers.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext.xml");
		
		SpringApplication.run(Application.class, args);
	}	
	
	@Bean
	BasicDataSource dataSource() throws URISyntaxException{
		URI dbUri = new URI("postgres://postgres:root@localhost:5432/myapp");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
	}
	
//	@Bean
//	LocalEntityManagerFactoryBean entityManagerFactory(){
//		LocalEntityManagerFactoryBean factory = new LocalEntityManagerFactoryBean();
//		factory.setPersistenceUnitName("persistenceUnit");
//		factory.
//	}
	
}
