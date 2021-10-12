package pl.maciejnierzwicki.simpleforumapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import pl.maciejnierzwicki.simpleforumapp.object.ForumProperties;

@Controller
public class BaseController {
	
	@Autowired
	protected ForumProperties properties;
	
	@ModelAttribute("properties")
	public ForumProperties properties() {
		return properties;
	}

}
