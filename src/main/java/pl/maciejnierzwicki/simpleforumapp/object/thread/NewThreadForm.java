package pl.maciejnierzwicki.simpleforumapp.object.thread;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewThreadForm {
	
	@NotBlank(message = "Tytuł wątku nie może być pusty.")
	@Size(min = 3, message = "Tytuł wątku musi zawierać co najmniej 3 znaki.")
	private String threadTitle;
	
	@NotBlank(message = "Treść postu nie może być pusta.")
	@Size(min = 3, message = "Treść postu musi zawierać co najmniej 3 znaki.")
	@Size(max = 1000, message = "Treść postu może zawierać co najwyżej 1000 znaków.")
	private String postContent;

}
