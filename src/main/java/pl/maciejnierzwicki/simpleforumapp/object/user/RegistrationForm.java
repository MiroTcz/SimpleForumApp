package pl.maciejnierzwicki.simpleforumapp.object.user;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegistrationForm extends NewUserForm {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthdate;
	
	private String country;
	
	private String city;
	
	public User toUser(PasswordEncoder encoder) {
		User user = new User(username, encoder.encode(password));
		
		if(birthdate != null) {
			user.setBirthdate(birthdate);
		}
		
		if(country != null && !country.isEmpty()) {
			user.setCountry(country);
		}
		
		if(city != null && !city.isEmpty()) {
			user.setCity(city);
		}
		
		return user;
	}
}
