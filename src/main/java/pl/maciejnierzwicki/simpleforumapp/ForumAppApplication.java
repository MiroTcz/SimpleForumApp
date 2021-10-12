package pl.maciejnierzwicki.simpleforumapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@SpringBootApplication
public class ForumAppApplication {
	
	/*
	@Autowired
	@Value("${forum.database-file-path:classpath:database.properties}")
	private String databaseFilePath;
	
	@Bean
	public String databaseFilePath() {
		return databaseFilePath;
	}
	*/
	
	private static ConfigurableApplicationContext context;
	
	private ForumInitializer initializer;
	
	@Autowired
	public void setForumInitializer(@Lazy ForumInitializer initializer) {
		this.initializer = initializer;
	}
	
	public ForumInitializer getForumInitializer() {
		return initializer;
	}
	
	@Bean
	public ThreadPoolTaskScheduler getScheduler(){
	    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	    return scheduler;
	}
	
	
	public static void main(String[] args) {
		context = SpringApplication.run(ForumAppApplication.class, args);
	}
	
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(ForumAppApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
	
	@InitBinder
	public void initDateBinder(WebDataBinder binder) { 
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	
	 @Bean
	  public CommandLineRunner dataLoader() {
	    return new CommandLineRunner() {
	      @Override
	      public void run(String... args) throws Exception {
	    	  initializer.init();
	      }
	    };
	  }

}
