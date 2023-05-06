package pl.maciejnierzwicki.simpleforumapp;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.simpleforumapp.data.ForumRepository;
import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.RoleRepository;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.data.UserRepository;
import pl.maciejnierzwicki.simpleforumapp.object.ForumProperties;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.Role;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;

@Component
public class ForumInitializer {
	
	private Repositories repositories;
	
	@Autowired
	public void setRepositories(@Lazy Repositories repositories) {
		this.repositories = repositories;
	}
	
	public Repositories getRepositories() {
		return this.repositories;
	}
	
	private ForumProperties properties;
	
	@Autowired
	public void setForumProperties(ForumProperties properties) {
		this.properties = properties;
	}
	
	public ForumProperties getForumProperties() {
		return this.properties;
	}
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Bean
	public SetupProgress setupProgress() {
		SetupProgress progress = new SetupProgress();
		progress.loadFromFile();
		return progress;
	}
	
	public void init() {
  	  
  	  createDefaultRoleIfNotExists();
  	  createAdminRoleIfNotExists();
  	  
  	  if(properties.isSetupMode()) {
  		  createSetupUser();
  	  }
	}
	
	public void createDefaultRoleIfNotExists() {
		RoleRepository roleRepo = repositories.getRoleRepository();
		if(!roleRepo.findById(properties.getDefaultUserRole()).isPresent()) {
			Role role = new Role(properties.getDefaultUserRole());
			role.setDisplayname("Użytkownik");
			roleRepo.save(role);
		}
	}
	
	public void createAdminRoleIfNotExists() {
		RoleRepository roleRepo = repositories.getRoleRepository();
		if(!roleRepo.findById("ADMIN").isPresent()) {
			Role role = new Role("ADMIN");
			role.setDisplayname("Admin");
			roleRepo.save(role);
		}
	}
	
	private void createSetupUser() {
		UserRepository userRepo = repositories.getUserRepository();
		RoleRepository roleRepo = repositories.getRoleRepository();
  	  	if(userRepo.findByUsername(properties.getSetupUsername()) == null) {
  	  	  	User setup_user = new User(properties.getSetupUsername(), encoder.encode(properties.getSetupPassword()));
  	  	  	Set<Role> setup_user_roles = setup_user.getRoles();
  	  	  	setup_user_roles.add(roleRepo.findById("ADMIN").get()); 
  	  	  	userRepo.save(setup_user);
  	  	}
  	  	
	}
	
	public void createExampleData() {
		ForumRepository forumRepo = repositories.getForumRepository();
		ThreadRepository threadRepo = repositories.getThreadRepository();
		UserRepository userRepo = repositories.getUserRepository();
		PostRepository postRepo = repositories.getPostRepository();
		RoleRepository roleRepo = repositories.getRoleRepository();
		User user = userRepo.findByUsername("example");
		if(user == null) {
			user = userRepo.save(new User("example", encoder.encode(RandomStringUtils.randomAlphanumeric(12))));
		}
		Set<Role> roles = user.getRoles();
		Optional<Role> op_default_role = roleRepo.findById(properties.getDefaultUserRole());
		if(op_default_role.isPresent()) {
			Role default_role = op_default_role.get();
			roles.add(default_role);
			userRepo.save(user);
		}
		Forum forum = forumRepo.save(new Forum("Przykładowy dział"));
		forumRepo.save(forum);
		Thread thread = new Thread("Przykładowy wątek");
		thread.setUserid(user.getId());
		thread.setForumid(forum.getId());
		threadRepo.save(thread);
		Post post = new Post("Cześć wszystkim!");
		post.setThreadId(thread.getId());
		post.setUserId(user.getId());
		postRepo.save(post);

	}

}
