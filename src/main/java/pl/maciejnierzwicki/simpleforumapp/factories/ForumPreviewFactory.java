package pl.maciejnierzwicki.simpleforumapp.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.forum.ForumPreview;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;

@Component
public class ForumPreviewFactory {
	private PostRepository postRepo;
	private ThreadRepository threadRepo;
	
	@Autowired
	public ForumPreviewFactory(PostRepository postRepo, ThreadRepository threadRepo) {
		this.postRepo = postRepo;
		this.threadRepo = threadRepo;
	}
	
	public ForumPreview fromForum(Forum forum) {
		ForumPreview preview = new ForumPreview();
		
		preview.setForumName(forum.getName());
		preview.setForumId(forum.getId());
		
		List<Thread> threads = threadRepo.getByForumid(forum.getId());
		int threadsCount = threads.size();
		
		preview.setForumThreadsCount(threadsCount);
		
		long posts = 0;
		
		for(Thread thread : threads) {
			posts += postRepo.countByThreadid(thread.getId());
		}
		
		preview.setForumPostsCount(posts);

		return preview;
	}
	
	public List<ForumPreview> fromForums(Iterable<Forum> forums) {
		List<ForumPreview> list = new ArrayList<>();
		for(Forum forum : forums) {
			list.add(fromForum(forum));
		}
		return list;
	}
	
	

}
