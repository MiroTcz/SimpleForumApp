package pl.maciejnierzwicki.simpleforumapp.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUtil {
	
	public static boolean testMySQLConnection(String address, String dbname, int port, String username, String password) {
		Configuration config = new Configuration();
		config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		config.setProperty("hibernate.connection.url", "jdbc:mysql://" + address + ":" + port + "/" + dbname);
		config.setProperty("hibernate.connection.username", username);
		config.setProperty("hibernate.connection.password", password);
		
		SessionFactory factory;
		try {
			factory = config.buildSessionFactory();
		}
		catch(Exception e) {
			return false;
		}
		Session session = factory.openSession();
		
		return session.isOpen();
	}
}
