package pl.maciejnierzwicki.simpleforumapp.web.install;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.UserRepository;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.SetupStage;
import pl.maciejnierzwicki.simpleforumapp.object.user.RegistrationForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.Role;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/install/adminuser")
public class InstallAdminAccountController extends BaseController {
	
	private Repositories repositories;
	private PasswordEncoder passwordEncoder;
	private SetupProgress setupProgress;
	
	public InstallAdminAccountController(Repositories repositories, PasswordEncoder passwordEncoder, SetupProgress setupProgress) {
		this.repositories = repositories;
		this.passwordEncoder = passwordEncoder;
		this.setupProgress = setupProgress;
	}
	
	@ModelAttribute("registration")
	public RegistrationForm registration() {
		return new RegistrationForm();
	}
	
	@GetMapping
	public String getAdminAccountSetupView(@AuthenticationPrincipal User user, @ModelAttribute("registration") RegistrationForm form, Model model) {
		if(user == null) {
			return "redirect:/install";
		}
		if(setupProgress.getStage() != SetupStage.ADMIN_USER_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		return "install/adminuser";
	}
	
	@PostMapping
	public String processAdminAccountSetup(@AuthenticationPrincipal User user, Model model, @Valid @ModelAttribute("registration") RegistrationForm form, BindingResult result, Errors errors) {
		if(user == null) {
			return "redirect:/install";
		}
		if(errors.hasErrors()) {
			return "install/adminuser";
		}
		UserRepository userRepo = repositories.getUserRepository();
		
		User admin_user = userRepo.findByUsername(form.getUsername());
		if(admin_user != null) {
			model.addAttribute("userexists", true);
			return "install/adminuser";
		}
		admin_user = form.toUser(passwordEncoder);
		
		Optional<Role> op_admin_role = repositories.getRoleRepository().findById("ADMIN");
		if(op_admin_role.isPresent()) {
			Role role = op_admin_role.get();
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			admin_user.setRoles(roles);
		}
		userRepo.save(admin_user);
		setupProgress.setStage(SetupStage.FINISH);
		return "redirect:/install/finish";
	}
		
	

}
