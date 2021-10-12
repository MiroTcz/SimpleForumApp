package pl.maciejnierzwicki.simpleforumapp.object.user;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AdminEditUserForm {
	
	@NotBlank(message = "Nazwa użytkownika nie może być pusta.")
	@Size(min = 4, message = "Nazwa użytkownika musi składać się z co najmniej 4 znaków.")
	private String username;
	
	@Size(min = 1, message = "Użytkownik musi należeć do co najmniej jednej grupy.")
	private List<String> activeRoles;

}
