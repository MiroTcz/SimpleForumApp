package pl.maciejnierzwicki.simpleforumapp.web.post;

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
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/deletepost")
public class DeletePostController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public DeletePostController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@GetMapping
	public String showDeletePostView(@RequestParam(name = "postid", defaultValue = "-1") Long post_id, @AuthenticationPrincipal User user, Model model) {
		if(user == null) { return "redirect:/"; }
		Optional<Post> op_post = repositories.getPostRepository().findById(post_id);
		if(op_post.isPresent()) {
			Post post = op_post.get();
			if(!post.getUserid().equals(user.getId())) { return "redirect:/"; }
			model.addAttribute("post", post);
			fillModelWithThreadData(model, post);
		}
		
		return "post/deletepost";
	}
	
	@PostMapping
	public String processDelete(@RequestParam(name = "postid", defaultValue = "-1") Long post_id, @AuthenticationPrincipal User user, Model model) {
		if(user == null) { return "redirect:/"; }
		PostRepository postRepo = repositories.getPostRepository();
		Optional<Post> op_post = postRepo.findById(post_id);
		if(op_post.isPresent()) {
			Post post = op_post.get();
			fillModelWithThreadData(model, post);
			
			if(post.getUserid().equals(user.getId())) {
				postRepo.delete(post);
				model.addAttribute("deleted", true);	
			}
		}
		return "post/deletepost";
	}
	
	
	private void fillModelWithThreadData(Model model, Post post) {
		if(post == null) { return; }
		Optional<Thread> op_thread = repositories.getThreadRepository().findById(post.getThreadid());
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			model.addAttribute("thread", thread);
		}
	}
}
