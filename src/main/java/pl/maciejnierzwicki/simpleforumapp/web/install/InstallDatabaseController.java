package pl.maciejnierzwicki.simpleforumapp.web.install;


import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.simpleforumapp.ForumAppApplication;
import pl.maciejnierzwicki.simpleforumapp.object.SetupDatabaseForm;
import pl.maciejnierzwicki.simpleforumapp.object.SetupProgress;
import pl.maciejnierzwicki.simpleforumapp.object.SetupStage;
import pl.maciejnierzwicki.simpleforumapp.object.user.User;
import pl.maciejnierzwicki.simpleforumapp.security.DataSourceConfig;
import pl.maciejnierzwicki.simpleforumapp.security.DataSourceConfig.DatabaseType;
import pl.maciejnierzwicki.simpleforumapp.utils.DatabaseUtil;
import pl.maciejnierzwicki.simpleforumapp.web.BaseController;

@Controller
@RequestMapping(path = "/install/database")
public class InstallDatabaseController extends BaseController {
	
	private DataSourceConfig dataSourceConfig;
	private ThreadPoolTaskScheduler scheduler;
	private SetupProgress setupProgress;
	
	@Autowired
	public InstallDatabaseController(DataSourceConfig dataSourceConfig, ThreadPoolTaskScheduler scheduler, SetupProgress setupProgress) {
		this.dataSourceConfig = dataSourceConfig;
		this.scheduler = scheduler;
		this.setupProgress = setupProgress;
	}

	
	@ModelAttribute("setupDatabase")
	public SetupDatabaseForm setupDatabase() {
		return new SetupDatabaseForm();
	}
	

	
	@GetMapping
	public String getInstallProcessView(@AuthenticationPrincipal User user, Model model) {
		if(user == null) {
			return "redirect:/install";
		}
		if(setupProgress.getStage() != SetupStage.DATABASE_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		return "install/database";
	}
	
	@PostMapping
	public String processDatabaseSetup(@AuthenticationPrincipal User user, Model model, @ModelAttribute(name = "setupDatabase") @Valid SetupDatabaseForm form, Errors errors) {
		if(user == null) {
			return "redirect:/install";
		}
		if(errors.hasErrors()) {
			return "install/database";
		}
		if(form.getDbtype() == DatabaseType.MYSQL) {
			boolean valid_connection = DatabaseUtil.testMySQLConnection(form.getDbaddress(), form.getDbname(), form.getDbport(), form.getDbusername(), form.getDbpassword());
			if(!valid_connection) {
				model.addAttribute("connection_error", true);
				return "install/database";
			}
		}
		dataSourceConfig.apply(form);
		dataSourceConfig.save();
		setupProgress.setStage(SetupStage.FORUM_CONFIG);
		setupProgress.saveToFile();
		scheduler.schedule(() -> { ForumAppApplication.restart(); }, new Date(System.currentTimeMillis() + 500));
		return "install/database-restart";
	}
		
	

}
