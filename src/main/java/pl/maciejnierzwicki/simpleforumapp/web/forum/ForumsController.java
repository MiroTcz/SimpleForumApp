package pl.maciejnierzwicki.simpleforumapp.web.forum;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.factories.ForumPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.factories.ThreadPreviewFactory;
import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;
import pl.maciejnierzwicki.simpleforumapp.object.forum.ForumPreview;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.thread.ThreadPreview;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/forums")
public class ForumsController extends BaseController {
	
	private Repositories repositories;
	private ThreadPreviewFactory threadPreviewFactory;
	private ForumPreviewFactory forumPreviewFactory;
	
	@Autowired
	public ForumsController(Repositories repositories, ThreadPreviewFactory threadPreviewFactory, ForumPreviewFactory forumPreviewFactory) {
		this.repositories = repositories;
		this.threadPreviewFactory = threadPreviewFactory;
		this.forumPreviewFactory = forumPreviewFactory;
	}
	
	@GetMapping
	public String getForumsView(Model model) {
		Iterable<Forum> forums = repositories.getForumRepository().findAll();
		
		if(forums != null && forums.iterator().hasNext()) {
			List<ForumPreview> forums_preview = forumPreviewFactory.fromForums(forums);
			if(!forums_preview.isEmpty()) {
				model.addAttribute("forums", forums_preview);
			}
		}
		
		return "forum/forums";
	}
	
	@GetMapping(path = "/{id}")
	public String getForumView(@PathVariable("id") Long forumid, Model model) {
		Optional<Forum> op_forum = repositories.getForumRepository().findById(forumid);
		if(op_forum.isPresent()) {
			Forum forum = op_forum.get();
			model.addAttribute("forum", forum);
			
			List<Thread> threads = repositories.getThreadRepository().getByForumid(forum.getId());
			
			if(threads != null && threads.iterator().hasNext()) {
				List<ThreadPreview> threads_preview = threadPreviewFactory.fromThreads(threads);
				model.addAttribute("threads", threads_preview);
			}
		}
		return "forum/forum";
	}
	

}
