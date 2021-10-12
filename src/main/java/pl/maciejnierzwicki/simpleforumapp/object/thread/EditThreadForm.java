package pl.maciejnierzwicki.simpleforumapp.object.thread;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EditThreadForm {
	
	@NotBlank(message = "Tytuł wątku nie może być pusty.")
	@Size(min = 3, message = "Tytuł wątku musi zawierać co najmniej 3 znaki.")
	private String threadTitle;

}
