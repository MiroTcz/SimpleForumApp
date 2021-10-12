package pl.maciejnierzwicki.simpleforumapp.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.ForumInitializer;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.ForumPropertiesForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.Role;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/admin/forumsettings")
public class AdminForumSettingsController extends BaseController {
	
	private ForumInitializer initializer;
	private Repositories repositories;
	
	@Autowired
	public AdminForumSettingsController(ForumInitializer initializer, Repositories repositories) {
		this.initializer = initializer;
		this.repositories = repositories;
	}
	
	@ModelAttribute(name = "propertiesForm")
	public ForumPropertiesForm propertiesForm() {
		return properties.toForm();
	}
	
	@GetMapping
	public String showForumSettings(@AuthenticationPrincipal User user, Model model, @ModelAttribute("propertiesForm") ForumPropertiesForm propertiesForm) {
		Iterable<Role> all_roles = repositories.getRoleRepository().findAll();
		model.addAttribute("roles", all_roles);
		
		return "admin/admin-forumsettings";
	}
	
	@PostMapping
	public String updateForumSettings(@AuthenticationPrincipal User user, Model model, @ModelAttribute("propertiesForm") @Valid ForumPropertiesForm propertiesForm, BindingResult result, Errors errors, HttpServletRequest request, HttpServletResponse response) {
		if(errors.hasErrors()) {
			return "admin/admin-forumsettings";
		}
		properties.apply(propertiesForm);
		properties.save();
		if(properties.isSetupMode()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null)	{    
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			initializer.init();
			return "redirect:/install";
		}
		Iterable<Role> all_roles = repositories.getRoleRepository().findAll();
		model.addAttribute("roles", all_roles);
		return "admin/admin-forumsettings";
	}
}