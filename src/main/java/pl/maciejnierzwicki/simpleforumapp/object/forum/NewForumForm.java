package pl.maciejnierzwicki.simpleforumapp.object.forum;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewForumForm {
	
	@Size(min = 3, message = "Nazwa działu musi składać się z co najmniej 3 znaków.")
	@Size(max = 40, message = "Nazwa działu nie może składać się z więcej niż 40 znaków.")
	private String name;
	
}
