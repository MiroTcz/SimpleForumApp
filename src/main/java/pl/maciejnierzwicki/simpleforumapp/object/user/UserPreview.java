package pl.maciejnierzwicki.simpleforumapp.object.user;

import java.util.Set;

import lombok.Data;

@Data
public class UserPreview {
	
	private long userid;
	private String username;
	private int age;
	private String birthdate;
	private String country;
	private String city;
	private String avatarUrl;
	private Set<Role> roles;

}
