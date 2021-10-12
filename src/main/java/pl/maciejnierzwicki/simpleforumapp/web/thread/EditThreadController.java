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

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.factories.PostPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.object.thread.EditThreadForm;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path="/editthread")
public class EditThreadController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public EditThreadController(Repositories repositories, PostPreviewFactory postPreviewFactory) {
		this.repositories = repositories;
	}
	
	@ModelAttribute(name = "editthread")
	public EditThreadForm editthread() {
		return new EditThreadForm();
	}
	
	@GetMapping
	public String getThreadView(@RequestParam("threadid") Long threadid, Model model) {
		Optional<Thread> op_thread = repositories.getThreadRepository().findById(threadid);
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			model.addAttribute("thread", thread);
		}
		return "thread/editthread";
	}
	
	@PostMapping
	public String processReply(@RequestParam("threadid") Long threadid, @AuthenticationPrincipal User user, @Valid @ModelAttribute("editthread") EditThreadForm editBody, Errors errors, Model model) {
		if(user == null) { return "redirect:/";}
		ThreadRepository threadRepo = repositories.getThreadRepository();
		Optional<Thread> op_thread = threadRepo.findById(threadid);
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			model.addAttribute("thread", thread);
			
			if(errors.hasErrors()) {
				return "editthread";
			}
			
			if(thread.getUserid().equals(user.getId())) {
				thread.setTitle(editBody.getThreadTitle());	
				threadRepo.save(thread);
			}
			return "redirect:/forums/" + thread.getForumid();
		}
		return "thread/editthread";
	}
}
