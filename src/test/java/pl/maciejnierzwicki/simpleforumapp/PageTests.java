package pl.maciejnierzwicki.simpleforumapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.object.ForumProperties;

@SpringBootTest
@AutoConfigureMockMvc
public class PageTests {
	
	private MockMvc mockMvc;
	
	private PostRepository postRepo;
	
	private ForumProperties properties;
	
	@Autowired
	public PageTests(MockMvc mockMvc, PostRepository postRepo, ForumProperties properties) {
		this.properties = properties;
		this.mockMvc = mockMvc;
		this.postRepo = postRepo;
		properties.setSetupMode(false);
	}
	
	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"));
	} 
	
	@Test
	public void testLoginPage() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("user/login"));
	}
	
	@Test
	public void testUsersPage() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(view().name("user/users"));
	}
	
	@Test
	public void testSingleUserPage() throws Exception {
		mockMvc.perform(get("/users/1")).andExpect(status().isOk()).andExpect(view().name("user/user"));
	}
	
	@Test
	public void testUserSettingsPage() throws Exception {
		mockMvc.perform(get("/usersettings")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/usersettings")).andExpect(status().isOk()).andExpect(view().name("user/usersettings"));
	}
	
	@Test
	public void testRegistrationPage() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("user/register"));
	}
	
	@Test
	public void testForumsPage() throws Exception {
		mockMvc.perform(get("/forums")).andExpect(status().isOk()).andExpect(view().name("forum/forums"));
	}
	
	@Test
	public void testSingleForumPage() throws Exception {
		mockMvc.perform(get("/forums/1")).andExpect(status().isOk()).andExpect(view().name("forum/forum"));
	}
	
	@Test
	public void testThreadPage() throws Exception {
		mockMvc.perform(get("/threads/1")).andExpect(status().isOk()).andExpect(view().name("thread/thread"));
	}
	
	@Test
	public void testNewThreadPage() throws Exception {
		mockMvc.perform(get("/newthread")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/newthread")).andExpect(status().isOk()).andExpect(view().name("thread/newthread"));
	}
	
	@Test
	public void testEditThreadPage() throws Exception {
		mockMvc.perform(get("/editthread")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/editthread")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testDeleteThreadPage() throws Exception {
		mockMvc.perform(get("/deletethread")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/deletethread")).andExpect(status().isOk()).andExpect(view().name("thread/deletethread"));
	}
	
	@Test
	public void testEditPostPage() throws Exception {
		mockMvc.perform(get("/editpost")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/editpost")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testDeletePostPage() throws Exception {
		mockMvc.perform(get("/deletepost")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/deletepost")).andExpect(status().isOk()).andExpect(view().name("post/deletepost"));
	}
	
	@Test
	public void testAdminMainPage() throws Exception {
		mockMvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/admin")).andExpect(status().isOk()).andExpect(view().name("admin/admin"));
	}
	
	@Test
	public void testAdminForumSettingsPage() throws Exception {
		mockMvc.perform(get("/admin/forumsettings")).andExpect(status().is3xxRedirection());
		//mockMvc.perform(get("/admin/forumsettings")).andExpect(status().isOk()).andExpect(view().name("admin/admin-forumsettings"));
	}
	
	@Test
	public void testAdminForumCategoriesPages() throws Exception {
		mockMvc.perform(get("/admin/forumcategories")).andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/admin/forumcategories/edit")).andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/admin/forumcategories/delete")).andExpect(status().is3xxRedirection());
		
		/*
		mockMvc.perform(get("/admin/forumcategories")).andExpect(status().isOk()).andExpect(view().name("admin/admin-forumcategories"));
		mockMvc.perform(get("/admin/forumcategories/edit")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/admin/forumcategories/delete")).andExpect(status().isBadRequest());
		*/
	}
	
	@Test
	public void testAdminForumUsersPages() throws Exception {
		mockMvc.perform(get("/admin/forumusers")).andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/admin/forumusers/edit")).andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/admin/forumusers/delete")).andExpect(status().is3xxRedirection());
		
		/*
		mockMvc.perform(get("/admin/forumusers")).andExpect(status().isOk()).andExpect(view().name("admin/admin-forumusers"));
		mockMvc.perform(get("/admin/forumusers/edit")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/admin/forumusers/delete")).andExpect(status().isBadRequest());
		*/
	}
	

}
