package pl.maciejnierzwicki.simpleforumapp.factories;

import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.object.user.UserPreview;
import pl.maciejnierzwicki.simpleforumapp.utils.MainUtil;

@Component
public class UserPreviewFactory {
	
	public UserPreview fromUser(User user) {
		UserPreview preview = new UserPreview();
		
		preview.setUserid(user.getId());
		preview.setUsername(user.getUsername());
		preview.setBirthdate(MainUtil.getTextRepresentation(user.getBirthdate(), "dd.MM.yyyy"));
		preview.setAge(MainUtil.getFullYears(user.getBirthdate()));
		preview.setAvatarUrl(user.getAvatarUrl());
		if(user.getCountry() != null && !user.getCountry().isBlank()) {
			preview.setCountry(user.getCountry().trim());
		}
		if(user.getCity() != null && !user.getCity().isBlank()) {
			preview.setCity(user.getCity().trim());
		}
		preview.setRoles(user.getRoles());
		
		return preview;
	}
}
