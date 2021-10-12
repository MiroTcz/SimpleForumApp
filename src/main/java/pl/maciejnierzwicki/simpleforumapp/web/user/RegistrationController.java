package pl.maciejnierzwicki.simpleforumapp.web.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.UserRepository;
import pl.maciejnierzwicki.simpleforumapp.object.user.RegistrationForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.Role;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/register")
public class RegistrationController extends BaseController {
	
	private Repositories repositories;
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute("registration")
	public RegistrationForm registration() {
		return new RegistrationForm();
	}
	
	@Autowired
	public RegistrationController(Repositories repositories, PasswordEncoder passwordEncoder) {
		this.repositories = repositories;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping
	public String showRegistration(@ModelAttribute("registration") RegistrationForm form, Model model) {
		return "user/register";
	}
	
	@PostMapping
	public String processRegistration(@Valid @ModelAttribute("registration") RegistrationForm form, Errors errors, Model model) {
		if(errors.hasErrors() || !properties.isRegistrationsEnabled()) {
			return "user/register";
		}
		UserRepository userRepo = repositories.getUserRepository();
		
		User user = userRepo.findByUsername(form.getUsername());

		if(user != null) {
			model.addAttribute("userexists", true);
			return "user/register";
		}
		
		user = form.toUser(passwordEncoder);
		
		Optional<Role> op_default_role = repositories.getRoleRepository().findById(properties.getDefaultUserRole());
		
		if(op_default_role.isPresent()) {
			Role role = op_default_role.get();
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			user.setRoles(roles);
		}
		
		userRepo.save(user);
		return "redirect:/login";
	}
}
