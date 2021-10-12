package pl.maciejnierzwicki.simpleforumapp.web.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/admin")
public class AdminController extends BaseController {
	
	@GetMapping
	public String showAdminPanel(@AuthenticationPrincipal User user) {
		return "admin/admin";
	}
}
