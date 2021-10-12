package pl.maciejnierzwicki.simpleforumapp.web.post;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.ThreadReplyForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path="/editpost")
public class EditPostController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public EditPostController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@ModelAttribute(name = "editpost")
	public ThreadReplyForm editpost() {
		return new ThreadReplyForm();
	}
	
	@GetMapping
	public String getPostView(@RequestParam("postid") Long postid, @AuthenticationPrincipal User user, Model model) {
		if(user == null) { return "redirect:/"; }
		Optional<Post> op_post = repositories.getPostRepository().findById(postid);
		if(op_post.isPresent()) {
			Post post = op_post.get();
			model.addAttribute("post", post);
		}
		return "post/editpost";
	}
	
	@PostMapping
	public String processReply(@RequestParam("postid") Long postid, @AuthenticationPrincipal User user, @Valid @ModelAttribute("editpost") ThreadReplyForm replyBody, Errors errors, Model model) {
		PostRepository postRepo = repositories.getPostRepository();
		
		Optional<Post> op_post = postRepo.findById(postid);
		if(op_post.isPresent()) {
			Post post = op_post.get();
			model.addAttribute("post", post);
			
			if(errors.hasErrors()) {
				return "post/editpost";
			}
			
			if(post.getUserid().equals(user.getId())) {
				post.setContent(replyBody.getPostContent());
				postRepo.save(post);
			}
			
			return "redirect:/threads/" + post.getThreadid();
		}
		return "post/editpost";
	}

}
