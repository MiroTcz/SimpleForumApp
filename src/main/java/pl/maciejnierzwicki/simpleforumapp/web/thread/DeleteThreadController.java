package pl.maciejnierzwicki.simpleforumapp.web.thread;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/deletethread")
public class DeleteThreadController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public DeleteThreadController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@GetMapping
	public String showDeletePostView(@RequestParam(name = "threadid", defaultValue = "-1") Long thread_id, Model model) {
		Optional<Thread> op_thread = repositories.getThreadRepository().findById(thread_id);
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			model.addAttribute("thread", thread);
		}
		
		return "thread/deletethread";
	}
	
	@PostMapping
	public String processDelete(@RequestParam(name = "threadid", defaultValue = "-1") Long thread_id, @AuthenticationPrincipal User user, Model model) {
		if(user == null) { return "redirect:/"; }
		PostRepository postRepo = repositories.getPostRepository();
		ThreadRepository threadRepo = repositories.getThreadRepository();
		Optional<Thread> op_thread = threadRepo.findById(thread_id);
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			
			if(thread.getUserid().equals(user.getId())) {
				List<Post> posts = postRepo.getByThreadid(thread_id);
				posts.forEach(post -> postRepo.delete(post));
				threadRepo.delete(thread);
				model.addAttribute("deleted", true);
			}
		}
		
		return "thread/deletethread";
	}
}
