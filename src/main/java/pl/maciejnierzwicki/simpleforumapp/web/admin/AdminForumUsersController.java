package pl.maciejnierzwicki.simpleforumapp.web.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.simpleforumapp.data.PostRepository;
import pl.maciejnierzwicki.simpleforumapp.data.Repositories;
import pl.maciejnierzwicki.simpleforumapp.data.RoleRepository;
import pl.maciejnierzwicki.simpleforumapp.data.ThreadRepository;
import pl.maciejnierzwicki.simpleforumapp.data.UserRepository;
import pl.maciejnierzwicki.simpleforumapp.object.post.Post;
import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;
import pl.maciejnierzwicki.simpleforumapp.object.user.AdminEditUserForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.AdminEditUserPasswordForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.NewUserForm;
import pl.maciejnierzwicki.simpleforumapp.object.user.Role;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping("/admin/forumusers")
public class AdminForumUsersController extends BaseController {
	
	private Repositories repositories;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public AdminForumUsersController(Repositories repositories, PasswordEncoder passwordEncoder) {
		this.repositories = repositories;
		this.passwordEncoder = passwordEncoder;
	}
	
	@ModelAttribute(name = "newUser")
	public NewUserForm newUser() {
		return new NewUserForm();
	}
	
	@ModelAttribute(name = "editUser")
	public AdminEditUserForm editUser() {
		return new AdminEditUserForm();
	}
	
	@ModelAttribute(name = "editUserPassword")
	public AdminEditUserPasswordForm editUserPassword() {
		return new AdminEditUserPasswordForm();
	}
	
	@GetMapping
	public String showForumUsersSettings(@AuthenticationPrincipal User user, Model model) {
		Iterable<User> users = repositories.getUserRepository().findAll();
		model.addAttribute("users", users);
		
		return "admin/admin-forumusers";
	}
	
	@PostMapping
	public String processForumUsersNewUser(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newUser") @Valid NewUserForm form, Errors errors) {
		UserRepository userRepo = repositories.getUserRepository();
		if(!errors.hasErrors()) {
			User new_user = form.toUser(passwordEncoder);
			if(userRepo.findByUsername(new_user.getUsername()) == null) {
				Optional<Role> op_default_role = repositories.getRoleRepository().findById("USER");
				
				if(op_default_role.isPresent()) {
					Role role = op_default_role.get();
					Set<Role> roles = new HashSet<>();
					roles.add(role);
					new_user.setRoles(roles);
				}
				userRepo.save(new_user);
			}
			
			else { model.addAttribute("exists", true); }
		}
		
		if(userRepo.findByUsername(form.getUsername()) != null) {
			model.addAttribute("exists", true);
		}
		
		Iterable<User> users = userRepo.findAll();
		
		model.addAttribute("users", users);
		
		if(!errors.hasErrors()) {
			return "redirect:/admin/forumusers";
		}
		return "admin/admin-forumusers";
	}
	
	@GetMapping("/edit")
	public String showForumUsersEditForm(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		model.addAttribute("userid", user_id);
		Optional<User> op_user = repositories.getUserRepository().findById(user_id);
		
		if(op_user.isPresent()) {
			model.addAttribute("editUser", op_user.get().toAdminEditUserForm());
			model.addAttribute("editUserPassword", editUserPassword());
		}
		
		Iterable<Role> all_roles = repositories.getRoleRepository().findAll();
		model.addAttribute("all_roles", all_roles);
		
		return "admin/admin-forumusers-edit";
	}
	
	@PostMapping("/edit")
	public String processForumUsersEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editUser") @Valid AdminEditUserForm form, BindingResult result, Errors errors, @RequestParam("userid") Long user_id) {
		fillModelWithUserAndAllRolesData(model, user_id);
		if(errors.hasErrors()) {
			return "admin/admin-forumusers-edit";
		}
		UserRepository userRepo = repositories.getUserRepository();
		Optional<User> op_user = userRepo.findById(user_id);
		
		if(op_user.isPresent()) {
			User edited_user = op_user.get();
			edited_user.setUsername(form.getUsername());
			RoleRepository roleRepo = repositories.getRoleRepository();
			Set<Role> roles = new HashSet<>();
			for(String id : form.getActiveRoles()) {
				Optional<Role> role = roleRepo.findById(id);
				if(role.isPresent()) {
					roles.add(role.get());
				}
			}
			edited_user.setRoles(roles);
			userRepo.save(edited_user);
			model.addAttribute("editUser", edited_user.toAdminEditUserForm());
		}
		
		else {
			model.addAttribute("editUser", form);
		}
		
		return "admin/admin-forumusers-edit";
	}
	
	@PostMapping("/edit/password")
	public String processForumUsersEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editUserPassword") @Valid AdminEditUserPasswordForm form, BindingResult result, Errors errors, @RequestParam("userid") Long user_id) {
		fillModelWithUserAndAllRolesData(model, user_id);
		UserRepository userRepo = repositories.getUserRepository();
		if(errors.hasErrors()) {
			Optional<User> op_user = userRepo.findById(user_id);
			if(op_user.isPresent()) {
				model.addAttribute("editUser", op_user.get().toAdminEditUserForm());
			}
			return "admin/admin-forumusers-edit";
		}
		
		Optional<User> op_user = userRepo.findById(user_id);
		
		if(op_user.isPresent()) {
			User edited_user = op_user.get();
			edited_user.setPassword(passwordEncoder.encode(form.getPassword()));
			userRepo.save(edited_user);
			model.addAttribute("editUser", edited_user.toAdminEditUserForm());
		}
		
		return "admin/admin-forumusers-edit";
	}
	
	@GetMapping("/delete")
	public String showForumUsersDeleteForm(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		
		Optional<User> op_user = repositories.getUserRepository().findById(user_id);
		
		if(op_user.isPresent()) {
			User selected_user = op_user.get();
			model.addAttribute("user", selected_user);
		}
		
		return "admin/admin-forumusers-delete";
	}
	
	@PostMapping("/delete")
	public String processForumUsersDeleteForm(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		UserRepository userRepo = repositories.getUserRepository();
		PostRepository postRepo = repositories.getPostRepository();
		ThreadRepository threadRepo = repositories.getThreadRepository();
		Optional<User> op_user = userRepo.findById(user_id);
		
		if(op_user.isPresent()) {
			User selected_user = op_user.get();
			
			if(!selected_user.getId().equals(user.getId())) {
				
				List<Post> posts = postRepo.getByUserid(selected_user.getId());
				for(Post post : posts) {
					postRepo.delete(post);
				}
				
				List<Thread> threads = threadRepo.getByUserid(selected_user.getId());
				for(Thread thread : threads) {
					threadRepo.delete(thread);
				}
				
				userRepo.delete(selected_user);	
			}
		}

		return "admin/admin-forumusers-delete";
	}
	
	private void fillModelWithUserAndAllRolesData(Model model, long user_id) {
		model.addAttribute("userid", user_id);
		Iterable<Role> all_roles = repositories.getRoleRepository().findAll();
		model.addAttribute("all_roles", all_roles);
	}

}
