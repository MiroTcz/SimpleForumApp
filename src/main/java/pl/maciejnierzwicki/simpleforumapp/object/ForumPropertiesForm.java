package pl.maciejnierzwicki.simpleforumapp.object;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ForumPropertiesForm {
	
	@NotBlank(message = "Tytuł forum nie może składać się wyłącznie z białych spacji.")
	@Size(max = 40, message = "Tytuł forum może składać się z maksymalnie 40 znaków.")
	private String forumTitle = "Simple Forum App";
	
	@Min(value = 1, message = "Wartość nie może być mniejsza od 1.")
	@Max(value = 15, message = "Wartość nie może być większa od 15.")
	private int latestPostsCount = 5;
	
	@Min(value = 5, message = "Wartość nie może być mniejsza od 5.")
	@Max(value = Integer.MAX_VALUE, message = "Wartość nie może być większa od " + Integer.MAX_VALUE)
	private int maxPostLength = 5;
	
	@NotBlank(message = "Wartość domyślnej grupy nie może być pusta.")
	private String defaultUserRole = "USER";
	
	private boolean registrationsEnabled = true;
	private boolean setupMode = true;
	


}
