package pl.maciejnierzwicki.simpleforumapp.object.thread;

import lombok.Data;

@Data
public class ThreadPreview {
	
	private long threadId;
	private String threadTitle;
	private String threadAuthor;
	private long threadAuthorId;
	private int threadPostsCount;

}
