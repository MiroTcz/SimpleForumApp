package pl.maciejnierzwicki.simpleforumapp.web.thread;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.factories.PostPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.post.PostPreview;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.thread.ThreadReplyForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path="/threads")
public class ThreadController extends BaseController {
	
	private Repositories repositories;
	private PostPreviewFactory postPreviewFactory;
	
	@ModelAttribute(name = "post")
	public ThreadReplyForm post() {
		return new ThreadReplyForm();
	}
	
	@Autowired
	public ThreadController(Repositories repositories, PostPreviewFactory postPreviewFactory) {
		this.repositories = repositories;
		this.postPreviewFactory = postPreviewFactory;
	}
	
	@GetMapping(path = "/{id}")
	public String getThreadView(@PathVariable("id") Long threadid, Model model) {
		fillModelWithForumAndThreadInfo(model, threadid);
		fillModelWithPosts(model, threadid);
		return "thread/thread";
	}
	
	@PostMapping(path = "/{id}/reply")
	public String processReply(@PathVariable("id") Long threadid, @AuthenticationPrincipal User user, @Valid @ModelAttribute("post") ThreadReplyForm replyBody, Errors errors, Model model) {
		if(user == null) { return "redirect:/threads/" + threadid; }
		if(errors.hasErrors()) {
			fillModelWithForumAndThreadInfo(model, threadid);
			fillModelWithPosts(model, threadid);
			return "/thread/thread";
		}
		PostRepository postRepo = repositories.getPostRepository();
		Post post = new Post(replyBody.getPostContent());
		post.setThreadId(threadid);
		post.setUserId(user.getId());
		postRepo.save(post);
		return "redirect:/threads/" + threadid;
	}
	
	private void fillModelWithForumAndThreadInfo(Model model, long threadid) {
		Optional<Thread> op_thread = repositories.getThreadRepository().findById(threadid);
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			model.addAttribute("thread", thread);
			
			Optional<Forum> op_forum = repositories.getForumRepository().findById(thread.getForumid());
			
			if(op_forum.isPresent()) {
				Forum forum = op_forum.get();
				model.addAttribute("forum", forum);
			}
		}
	}
	
	private void fillModelWithPosts(Model model, long threadid) {
		List<Post> posts = repositories.getPostRepository().getByThreadid(threadid);
		if(!posts.isEmpty()) {
			List<PostPreview> posts_preview = postPreviewFactory.getPreviewsFromPosts(posts);
			model.addAttribute("posts", posts_preview);
		}
	}
}
