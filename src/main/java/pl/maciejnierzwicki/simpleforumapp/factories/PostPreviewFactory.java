package pl.maciejnierzwicki.simpleforumapp.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.post.HomePostPreview;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.post.PostPreview;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.utils.MainUtil;

@Component
public class PostPreviewFactory {
	private Repositories repositories;
	
	
	@Autowired
	public PostPreviewFactory(Repositories repositories) {
		this.repositories = repositories;
	}
	
	public PostPreview getPostPreviewFromPost(Post post) {
		PostPreview preview = new PostPreview();
		loadBase(preview, post);
		preview.setPostContent(post.getContent());
		return preview;
	}
	
	public HomePostPreview getHomePostPreviewFromPost(Post post) {
		HomePostPreview preview = new HomePostPreview();
		loadBase(preview, post);
		if(post.getContent().length() > 150) {
			preview.setPostContent(post.getContent().substring(0, 150) + "...");
		}
		else {
			preview.setPostContent(post.getContent());
		}
		
		Optional<Thread> op_thread = repositories.getThreadRepository().findById(post.getThreadid());
		if(op_thread.isPresent()) {
			Thread thread = op_thread.get();
			preview.setThreadTitle(thread.getTitle());
			preview.setThreadId(thread.getId());
			
			Optional<Forum> op_forum = repositories.getForumRepository().findById(thread.getForumid());
			
			if(op_forum.isPresent()) {
				Forum forum = op_forum.get();
				preview.setForumName(forum.getName());
			}
		}
		return preview;
	}
	
	private void loadBase(PostPreview preview, Post post) {
		preview.setPostid(post.getId());
		Optional<User> op_user = repositories.getUserRepository().findById(post.getUserid());
		
		if(op_user.isPresent()) {
			User user = op_user.get();
			preview.setPostAuthor(user.getUsername());
			preview.setPostAuthorid(user.getId());
			preview.setPostAuthorAvatarUrl(user.getAvatarUrl());
		}
		preview.setDate(MainUtil.getTextRepresentation(post.getCreationdate(), "dd.MM.yyyy HH:mm:ss"));
	}
	
	public List<HomePostPreview> getHomePreviewsfromPosts(Iterable<Post> posts) {
		List<HomePostPreview> list = new ArrayList<>();
		for(Post post : posts) {
			list.add(getHomePostPreviewFromPost(post));
		}
		return list;
	}
	
	public List<PostPreview> getPreviewsFromPosts(Iterable<Post> posts) {
		List<PostPreview> list = new ArrayList<>();
		for(Post post : posts) {
			list.add(getPostPreviewFromPost(post));
		}
		return list;
	}
}
