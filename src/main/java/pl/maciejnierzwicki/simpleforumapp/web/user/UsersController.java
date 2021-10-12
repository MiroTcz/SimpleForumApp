package pl.maciejnierzwicki.simpleforumapp.web.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.factories.UserPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.object.user.UserPreview;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/users")
public class UsersController extends BaseController {
	
	private Repositories repositories;
	private UserPreviewFactory userPreviewFactory;
	
	@Autowired
	public UsersController(Repositories repositories, UserPreviewFactory userPreviewFactory) {
		this.repositories = repositories;
		this.userPreviewFactory = userPreviewFactory;
	}
	
	@GetMapping
	public String showUsersView(Model model) {
		Iterable<User> users = repositories.getUserRepository().findAll();
		
		if(users != null && users.iterator().hasNext()) {
			model.addAttribute("users", users);
		}
		
		return "user/users";
	}
	
	@GetMapping("/{id}")
	public String showUserView(@PathVariable("id") Long user_id, Model model) {
		Optional<User> user = repositories.getUserRepository().findById(user_id);
		
		if(user.isPresent()) {
			UserPreview preview = userPreviewFactory.fromUser(user.get());
			model.addAttribute("user", preview);
		}
		
		return "user/user";
	}
}
