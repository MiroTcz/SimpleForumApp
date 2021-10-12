package pl.maciejnierzwicki.simpleforumapp.web.admin;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.simpleforumapp.data.ForumRepository;
import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.forum.NewForumForm;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/admin/forumcategories")
public class AdminForumCategoriesController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public AdminForumCategoriesController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@ModelAttribute(name = "newForum")
	public NewForumForm newForum() {
		return new NewForumForm();
	}
	
	@GetMapping
	public String showForumCategoriesSettings(@AuthenticationPrincipal User user, Model model) {
		Iterable<Forum> forums = repositories.getForumRepository().findAll();
		model.addAttribute("forums", forums);
		return "admin/admin-forumcategories";
	}
	
	@PostMapping
	public String processForumCategoriesSettingsNewForum(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newForum") @Valid NewForumForm form, Errors errors) {
		ForumRepository forumRepo = repositories.getForumRepository();
		if(!errors.hasErrors()) {
			Forum forum = new Forum(form.getName());
			forumRepo.save(forum);
		}
		
		Iterable<Forum> forums = forumRepo.findAll();
		
		model.addAttribute("forums", forums);
		
		if(!errors.hasErrors()) {
			return "redirect:/admin/forumcategories";
		}
		return "admin/admin-forumcategories";
	}
	
	@GetMapping("/edit")
	public String showForumCategoriesEditForm(@AuthenticationPrincipal User user, Model model, @RequestParam("forumid") Long forum_id) {
		fillModelWithForumData(model, forum_id);
		return "admin/admin-forumcategories-edit";
	}
	
	@PostMapping("/edit")
	public String showForumCategoriesEditForm(@AuthenticationPrincipal User user, Model model, @RequestParam("forumid") Long forum_id, @ModelAttribute("forum") @Valid Forum forum, Errors errors) {
		if(errors.hasErrors()) {
			model.addAttribute("forum", forum);
			return "admin/admin-forumcategories-edit";
		}
		ForumRepository forumRepo = repositories.getForumRepository();
		
		Optional<Forum> op_forum = forumRepo.findById(forum_id);
		
		if(op_forum.isPresent()) {
			Forum edited_forum = op_forum.get();
			edited_forum.setName(forum.getName());
			forumRepo.save(edited_forum);
			model.addAttribute("forum", edited_forum);
		}
		
		else {
			model.addAttribute("forum", forum);
		}
		
		
		
		return "admin/admin-forumcategories-edit";
	}
	
	@GetMapping("/delete")
	public String showForumCategoriesDeleteForm(@AuthenticationPrincipal User user, Model model, @RequestParam("forumid") Long forum_id) {
		fillModelWithForumData(model, forum_id);
		return "admin/admin-forumcategories-delete";
	}
	
	@PostMapping("/delete")
	public String processForumCategoriesDeleteForm(@AuthenticationPrincipal User user, Model model, @RequestParam("forumid") Long forum_id) {
		ForumRepository forumRepo = repositories.getForumRepository();
		ThreadRepository threadRepo = repositories.getThreadRepository();
		PostRepository postRepo = repositories.getPostRepository();
		Optional<Forum> op_forum = forumRepo.findById(forum_id);
		
		if(op_forum.isPresent()) {
			Forum forum = op_forum.get();
			
			List<Thread> threads = threadRepo.getByForumid(forum.getId());
			
			for(Thread thread : threads) {
				List<Post> posts = postRepo.getByThreadid(thread.getId());
				for(Post post : posts) {
					postRepo.delete(post);
				}
				threadRepo.delete(thread);
			}
			forumRepo.delete(forum);
		}

		return "admin/admin-forumcategories-delete";
	}
	
	private void fillModelWithForumData(Model model, long forum_id) {
		Optional<Forum> op_forum = repositories.getForumRepository().findById(forum_id);
		if(op_forum.isPresent()) {
			Forum forum = op_forum.get();
			model.addAttribute("forum", forum);
		}
	}

}
