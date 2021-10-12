package pl.maciejnierzwicki.simpleforumapp.web.install;


import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.object.SetupLoginForm;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.SetupStage;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/install")
public class InstallController extends BaseController {
	
	
    @Resource(name="authenticationManager")
    private AuthenticationManager authManager;
    
    private SetupProgress setupProgress;
	
	@Autowired
	public InstallController(AuthenticationManager authManager, SetupProgress setupProgress) {
		this.authManager = authManager;
		this.setupProgress = setupProgress;
	}
	
	@ModelAttribute("setupLogin")
	public SetupLoginForm setupLogin() {
		return new SetupLoginForm();
	}
	
	@GetMapping
	public String getInstallView(Model model, @AuthenticationPrincipal User user) {
		if(setupProgress.getStage() != SetupStage.WAITING_LOGIN && user != null) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("setupprogress", setupProgress);
		return "install/install";
	}
	
	@PostMapping
	public String processLogin(Model model, @ModelAttribute(name = "setupLogin") SetupLoginForm form, HttpServletRequest req) {
		UsernamePasswordAuthenticationToken authReq
		 = new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword());
		Authentication auth;
		try {
			auth = authManager.authenticate(authReq);
		}
		catch(BadCredentialsException e) {
			return "redirect:/install?error";
		}
		if(!auth.isAuthenticated() || !auth.getName().equals(properties.getSetupUsername())) { return "redirect:/install?error"; }
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);
	    HttpSession session = req.getSession(true);
	    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
	    
	    if(setupProgress.getStage() == SetupStage.WAITING_LOGIN) {
	    	setupProgress.setStage(SetupStage.DATABASE_CONFIG);
	    }
		
		return "redirect:/install/database";
	}
		
	

}
