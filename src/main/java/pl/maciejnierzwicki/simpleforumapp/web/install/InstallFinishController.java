package pl.maciejnierzwicki.simpleforumapp.web.install;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import pl.maciejnierzwicki.simpleforumapp.ForumInitializer;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.SetupStage;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/install/finish")
public class InstallFinishController extends BaseController {
	
	private Repositories repositories;
	
	private ForumInitializer initializer;
	private SetupProgress setupProgress;
	
	@Autowired
	public InstallFinishController(Repositories repositories, ForumInitializer initializer, SetupProgress setupProgress) {
		this.repositories = repositories;
		this.initializer = initializer;
		this.setupProgress = setupProgress;
	}
	
	@GetMapping
	public String getInstallFinishView(@AuthenticationPrincipal User user) {
		if(user == null) {
			return "redirect:/install";
		}
		if(setupProgress.getStage() != SetupStage.FINISH) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		return "install/finish";
	}
	
	@PostMapping
	public String processInstallFinish(@AuthenticationPrincipal User user, SessionStatus status, HttpServletRequest request, HttpServletResponse response) {
		if(user == null) {
			return "redirect:/install";
		}
		properties.setSetupMode(false);
		properties.save();
		initializer.createExampleData();
		status.setComplete();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null)	{    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		repositories.getUserRepository().delete(user);
		setupProgress.deleteFile();
		
		return "redirect:/login";
	}
		
	

}
