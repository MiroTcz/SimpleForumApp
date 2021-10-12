package pl.maciejnierzwicki.simpleforumapp.object.thread;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ThreadReplyForm {
	
	@NotBlank(message = "Treść postu nie może być pusta.")
	@Size(min = 3, message = "Treść postu musi zawierać co najmniej 3 znaki.")
	@Size(max = 3000, message = "Treść postu może zawierać co najwyżej 3000 znaków.")
	private String postContent;

}
