package pl.maciejnierzwicki.simpleforumapp.object.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import pl.maciejnierzwicki.simpleforumapp.validation.FieldMatch;

@Data
@FieldMatch(first = "password", second = "confirmpassword", message = "Podane hasła muszą być identyczne.")
public class AdminEditUserPasswordForm {
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String password;
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String confirmpassword;

}
