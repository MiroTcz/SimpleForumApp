package pl.maciejnierzwicki.simpleforumapp.object.post;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HomePostPreview extends PostPreview {
	
	private String threadTitle;
	private Long threadId;
	private String forumName;
	
}
