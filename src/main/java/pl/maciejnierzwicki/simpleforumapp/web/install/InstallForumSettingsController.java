package pl.maciejnierzwicki.simpleforumapp.web.install;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.object.ForumPropertiesForm;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.SetupStage;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/install/forumsettings")
public class InstallForumSettingsController extends BaseController {
	
	private SetupProgress setupProgress;
	
	@Autowired
	public InstallForumSettingsController(SetupProgress setupProgress) {
		this.setupProgress = setupProgress;
	}

	@ModelAttribute(name = "propertiesForm")
	public ForumPropertiesForm propertiesForm() {
		return properties.toForm();
	}
	
	@GetMapping
	public String getForumSettingsSetupView(@AuthenticationPrincipal User user, Model model) {
		if(user == null) {
			return "redirect:/install";
		}
		if(setupProgress.getStage() != SetupStage.FORUM_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		return "install/forumsettings";
	}
	
	@PostMapping
	public String processForumSettingsSetup(@AuthenticationPrincipal User user, Model model, @ModelAttribute(name = "propertiesForm") @Valid ForumPropertiesForm form, BindingResult result, Errors errors) {
		if(user == null) {
			return "redirect:/install";
		}
		if(errors.hasErrors()) {
			return "install/forumsettings";
		}
		properties.apply(form);
		properties.save();
		setupProgress.setStage(SetupStage.ADMIN_USER_CONFIG);
		return "redirect:/install/adminuser";
	}
		
	

}
