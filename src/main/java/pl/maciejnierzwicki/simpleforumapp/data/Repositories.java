package pl.maciejnierzwicki.simpleforumapp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Repositories {
	
	private final ForumRepository forumRepository;
	private final PostRepository postRepository;
	private final RoleRepository roleRepository;
	private final ThreadRepository threadRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public Repositories(ForumRepository forumRepository, PostRepository postRepository, 
			RoleRepository roleRepository, ThreadRepository threadRepository, UserRepository userRepository) {
		this.forumRepository = forumRepository;
		this.postRepository = postRepository;
		this.roleRepository = roleRepository;
		this.threadRepository = threadRepository;
		this.userRepository = userRepository;
	}

}
