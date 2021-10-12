package pl.maciejnierzwicki.simpleforumapp.object.forum;

import lombok.Data;

@Data
public class ForumPreview {
	
	private long forumId;
	private String forumName;
	private long forumThreadsCount;
	private long forumPostsCount;

}
