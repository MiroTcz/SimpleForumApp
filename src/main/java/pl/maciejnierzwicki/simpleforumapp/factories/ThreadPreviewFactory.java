package pl.maciejnierzwicki.simpleforumapp.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.UserRepository;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.thread.ThreadPreview;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;

@Component
public class ThreadPreviewFactory {
	private PostRepository postRepo;
	private UserRepository userRepo;
	
	
	@Autowired
	public ThreadPreviewFactory( PostRepository postRepo, UserRepository userRepo) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
	}
	
	public ThreadPreview fromThread(Thread thread) {
		ThreadPreview preview = new ThreadPreview();
		
		preview.setThreadTitle(thread.getTitle());
		preview.setThreadId(thread.getId());
		
		long user_id = thread.getUserid();
		
		Optional<User> op_user = userRepo.findById(user_id);
		if(op_user.isPresent()) {
			User user = op_user.get();
			preview.setThreadAuthor(user.getUsername());
			preview.setThreadAuthorId(user_id);
		}
		
		List<Post> posts = postRepo.getByThreadid(thread.getId());
		
		preview.setThreadPostsCount(posts.size());

		return preview;
	}
	
	public List<ThreadPreview> fromThreads(Iterable<Thread> threads) {
		List<ThreadPreview> list = new ArrayList<>();
		for(Thread thread : threads) {
			list.add(fromThread(thread));
		}
		return list;
	}
}
