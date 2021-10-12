package pl.maciejnierzwicki.simpleforumapp.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	
	@GetMapping
	public String showLoginPage() {
		return "user/login";
	}

}
