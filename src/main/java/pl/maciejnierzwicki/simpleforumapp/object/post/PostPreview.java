package pl.maciejnierzwicki.simpleforumapp.object.post;

import lombok.Data;

@Data
public class PostPreview {
	
	private String postAuthor;
	private String postContent;
	private String date;
	private long postAuthorid;
	private long postid;
	private String postAuthorAvatarUrl;
	
}
