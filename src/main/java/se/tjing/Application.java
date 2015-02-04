package se.tjing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.social.config.annotation.EnableSocial;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJpaRepositories
@EnableSocial
@PropertySource("classpath:application.properties")
public class Application {

	public static void main(String[] args) {
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}
		System.setProperty("server.port", webPort);

		// ApplicationContext ctx = new ClassPathXmlApplicationContext(
		// "classpath:META-INF/spring/applicationContext.xml");

		SpringApplication.run(Application.class, args);
	}

	@Bean
	DataSource dataSource() {
		String username = "";
		String password = "";
		String dbUrl = "";
		try {
			URI dbUri = new URI(System.getenv("DATABASE_URL"));

			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
			dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
					+ dbUri.getPort() + dbUri.getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl(dbUrl);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);

		return basicDataSource;
	}

	// <bean
	// class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"
	// id="entityManagerFactory">
	// <property name="packagesToScan" value="se.tjing.user" />
	// <property name="persistenceUnitName" value="persistenceUnit"/>
	// <property name="dataSource" ref="dataSource"/>
	// </bean>

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPersistenceUnitName("persistenceUnit");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPackagesToScan("se.tjing.user");
		factory.setDataSource(dataSource());
		Properties props = new Properties();

		factory.setJpaProperties(props);
		return factory;
	}

	// <bean class="org.springframework.orm.jpa.JpaTransactionManager"
	// id="transactionManager">
	// <property name="entityManagerFactory" ref="entityManagerFactory"/>
	// </bean>

	@Bean
	JpaTransactionManager transactionManager() {
		JpaTransactionManager mgr = new JpaTransactionManager();
		mgr.setEntityManagerFactory(entityManagerFactory().getObject());
		return mgr;
	}
}
