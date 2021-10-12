package pl.maciejnierzwicki.simpleforumapp.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.Resources;
import javax.sql.DataSource;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.simpleforumapp.object.SetupDatabaseForm;

@Configuration
@Data
@Slf4j
public class DataSourceConfig {
	
	private DatabaseType databaseType = DatabaseType.H2;
	private String databaseAddress = "localhost";
	private String databaseName = "forumapp";
	private int databasePort = 3306;
	private String databaseUsername = "";
	private String databasePassword = "";
	private Properties props = new Properties();
	private ResourceLoader resourceLoader;
	
	public enum DatabaseType {
		MYSQL,
		H2
	}
	
	@Autowired
	public DataSourceConfig(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		InputStream is = getFileStream();
		loadProperties(is);
		save();
		log.info("Using database: " + this.databaseType);
	}
	
	private InputStream getFileStream() {
		try {
			File file = ResourceUtils.getFile("database.properties");
			FileInputStream in = new FileInputStream(file);
			return in;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			Resource res = resourceLoader.getResource("classpath:database.properties");
			try {
				InputStream is = res.getInputStream();
				return is;
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	private void loadProperties(InputStream in) {
		try {
			props.load(in);
			this.databaseAddress = props.getProperty("databaseAddress");
			this.databaseName = props.getProperty("databaseName");
			if(this.databaseName == null || this.databaseName.isEmpty()) {
				this.databaseName = "forumapp";
			}
			this.databasePort = Integer.parseInt(props.getProperty("databasePort"));
			this.databaseUsername = props.getProperty("databaseUsername");
			this.databasePassword = props.getProperty("databasePassword");
			if(props.getProperty("databaseType") != null && !props.getProperty("databaseType").isEmpty()) {
				DatabaseType type = EnumUtils.getEnum(DatabaseType.class, props.getProperty("databaseType").toUpperCase());
				if(type != null) {
					this.databaseType = type;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void apply(SetupDatabaseForm form) {
		this.databaseType = form.getDbtype();
		this.databaseAddress = form.getDbaddress();
		this.databaseName = form.getDbname();
		this.databasePort = form.getDbport();
		this.databaseUsername = form.getDbusername();
		this.databasePassword = form.getDbpassword();
	}
	
	public void save() {
		props.setProperty("databaseType", this.databaseType.toString());
		props.setProperty("databaseAddress", this.databaseAddress);
		props.setProperty("databaseName", this.databaseName);
		props.setProperty("databasePort", String.valueOf(this.databasePort));
		props.setProperty("databaseUsername", this.databaseUsername);
		props.setProperty("databasePassword", this.databasePassword);
		try {
			File file = new File("");
			String path = file.getAbsolutePath() + "/database.properties";
			file = new File(path);
			if(!file.exists()) {
				file.createNewFile();
			}
			props.store(new FileOutputStream(file), null);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder<?> builder = DataSourceBuilder.create();
		
		builder.username(getDatabaseUsername());
		builder.password(getDatabasePassword());
		
		switch(getDatabaseType()) {
			case MYSQL: {
				builder.driverClassName("com.mysql.jdbc.Driver");
				builder.url("jdbc:mysql://" + getDatabaseAddress() + ":" + getDatabasePort() + "/" + getDatabaseName());
				break;
			}
			case H2:
			default: {
				builder.driverClassName("org.h2.Driver");
				builder.url("jdbc:h2:file:./" + getDatabaseName() + ";DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
				break;
			}
		}
		return builder.build();
	}
	

}
