package pl.maciejnierzwicki.simpleforumapp.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.factories.PostPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.factories.ThreadPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.object.post.HomePostPreview;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;

@Controller
@RequestMapping(path = "/")
public class HomeController extends BaseController {
	
	private Repositories repositories;
	private PostPreviewFactory postPreviewFactory;
	
	@Autowired
	public HomeController(Repositories repositories, ThreadPreviewFactory threadPreviewFactory, PostPreviewFactory postPreviewFactory) {
		this.repositories = repositories;
		this.postPreviewFactory = postPreviewFactory;
	}
	
	@GetMapping
	public String getHomeView(Model model) {
		List<Post> posts = repositories.getPostRepository().findAllByOrderByIdDesc(PageRequest.of(0, properties.getLatestPostsCount()));
		List<HomePostPreview> previews = postPreviewFactory.getHomePreviewsfromPosts(posts);
		model.addAttribute("previews", previews);
		return "home";
	}
	

}
