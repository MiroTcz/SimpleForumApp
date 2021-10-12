package pl.maciejnierzwicki.simpleforumapp.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.simpleforumapp.security.DataSourceConfig.DatabaseType;

@Component
@Data
public class ForumProperties {
	
	private String forumTitle = "Simple Forum App";
	
	private int latestPostsCount = 5;
	
	private int maxPostLength = 3000;
	
	private boolean registrationsEnabled = true;
	private boolean setupMode = true;
	private String setupUsername = "Setup";
	private String setupPassword = null;
	private Properties props = new Properties();
	private String defaultUserRole = "USER";
	private ResourceLoader resourceLoader;
	

	@Autowired
	public ForumProperties(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		InputStream is = getFileStream();
		loadProperties(is);
		save();
	}
	
	private InputStream getFileStream() {
		try {
			File file = ResourceUtils.getFile("forum.properties");
			FileInputStream in = new FileInputStream(file);
			return in;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			Resource res = resourceLoader.getResource("classpath:forum.properties");
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
			this.forumTitle = props.getProperty("forumTitle");
			this.latestPostsCount = Integer.parseInt(props.getProperty("latestPostsCount"));
			this.maxPostLength = Integer.parseInt(props.getProperty("maxPostLength"));
			this.registrationsEnabled = Boolean.parseBoolean(props.getProperty("registrationsEnabled"));
			this.setupMode = Boolean.parseBoolean(props.getProperty("setupMode"));
			this.setupUsername = props.getProperty("setupUsername");
			if(this.setupUsername == null || this.setupUsername.isEmpty()) {
				this.setupUsername = "Setup";
				props.setProperty("setupUsername", this.setupUsername);
			}
			this.setupPassword = props.getProperty("setupPassword");
			if(this.setupPassword == null || this.setupPassword.isEmpty()) {
				this.setupPassword = RandomStringUtils.randomAlphanumeric(12);
				props.setProperty("setupPassword", this.setupPassword);
			}
			
			this.defaultUserRole = props.getProperty("defaultUserRole");
			if(this.defaultUserRole == null || this.defaultUserRole.isEmpty()) {
				this.defaultUserRole = "USER";
			}
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void apply(ForumPropertiesForm form) {
		this.forumTitle = form.getForumTitle();
		this.latestPostsCount = form.getLatestPostsCount();
		this.maxPostLength = form.getMaxPostLength();
		this.registrationsEnabled = form.isRegistrationsEnabled();
		this.setupMode = form.isSetupMode();
		this.defaultUserRole = form.getDefaultUserRole();
	}
	
	public ForumPropertiesForm toForm() {
		ForumPropertiesForm form = new ForumPropertiesForm();
		form.setForumTitle(getForumTitle());
		form.setLatestPostsCount(getLatestPostsCount());
		form.setMaxPostLength(getMaxPostLength());
		form.setRegistrationsEnabled(isRegistrationsEnabled());
		form.setSetupMode(isSetupMode());
		form.setDefaultUserRole(getDefaultUserRole());
		return form;
	}
	
	public void save() {
		props.setProperty("forumTitle", this.forumTitle);
		props.setProperty("latestPostsCount", String.valueOf(this.latestPostsCount));
		props.setProperty("maxPostLength", String.valueOf(this.maxPostLength));
		props.setProperty("registrationsEnabled", String.valueOf(this.registrationsEnabled));
		props.setProperty("setupMode", String.valueOf(this.setupMode));
		props.setProperty("setupUsername", this.setupUsername);
		props.setProperty("setupPassword", this.setupPassword);
		props.setProperty("defaultUserRole", this.defaultUserRole);
		try {
			File file = new File("");
			String path = file.getAbsolutePath() + "/forum.properties";
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
	
	
}
