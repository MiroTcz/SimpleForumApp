package pl.maciejnierzwicki.simpleforumapp.web.thread;

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

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.NewThreadForm;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/newthread")
public class NewThreadController extends BaseController {
	
	private Repositories repositories;
	
	@ModelAttribute(name = "thread")
	public NewThreadForm newThreadForm() {
		return new NewThreadForm();
	}
	
	@Autowired
	public NewThreadController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@GetMapping
	public String showNewThreadView(@AuthenticationPrincipal User user, @RequestParam(name = "forumid", defaultValue = "-1") Long forum_id, Model model) {
		Optional<Forum> op_forum = repositories.getForumRepository().findById(forum_id);
		if(op_forum.isPresent()) {
			Forum forum = op_forum.get();
			model.addAttribute("forum", forum);
		}
		return "thread/newthread";
	}
	
	@PostMapping
	public String newThread(@RequestParam(name = "forumid", defaultValue = "-1") Long forum_id, @AuthenticationPrincipal User user, @Valid @ModelAttribute("thread") NewThreadForm threadBody, Errors errors, Model model) {
		if(user == null) { return "redirect:/"; }
		Optional<Forum> op_forum = repositories.getForumRepository().findById(forum_id);
		if(op_forum.isPresent()) {
			Forum forum = op_forum.get();
			model.addAttribute("forum", forum);
			
			if(errors.hasErrors()) {
				return "thread/newthread";
			}
			
			Thread thread = new Thread(threadBody.getThreadTitle());
			thread.setUserid(user.getId());
			thread.setForumid(forum.getId());
			
			thread = repositories.getThreadRepository().save(thread);
			
			Post post = new Post(threadBody.getPostContent());
			post.setThreadId(thread.getId());
			post.setUserId(user.getId());
			
			repositories.getPostRepository().save(post);

			return "redirect:/threads/" + thread.getId();
		}
		else {
			model.addAttribute("forum_valid", false);
		}
		return "thread/newthread";
	}
}
