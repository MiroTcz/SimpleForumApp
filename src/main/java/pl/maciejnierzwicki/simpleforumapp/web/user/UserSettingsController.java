package pl.maciejnierzwicki.simpleforumapp.web.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/usersettings")
public class UserSettingsController extends BaseController {
	
	private Repositories repositories;
	
	@Autowired
	public UserSettingsController(Repositories repositories) {
		this.repositories = repositories;
	}
	
	@ModelAttribute("user")
	public User user() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			Object pr = auth.getPrincipal();
			
			if(pr instanceof User) {
				return (User) pr;
			}
		}
		
		return null;
	}
	
	@GetMapping
	public String showUserSettingsView(@AuthenticationPrincipal @ModelAttribute("user") User user, Model model) {
		return "user/usersettings";
	}
	
	@PostMapping
	public String processEdit(@AuthenticationPrincipal @ModelAttribute("user") User user, Model model) {
		repositories.getUserRepository().save(user);
		
		return "user/usersettings";
	}
}
