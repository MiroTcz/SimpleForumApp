package pl.maciejnierzwicki.simpleforumapp.object;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.ResourceUtils;

import lombok.Data;

@Data
public class SetupProgress {
	
	private SetupStage stage = SetupStage.WAITING_LOGIN;
	
	public String getCurrentStageUrlPath() {
		switch((stage != null) ? stage : SetupStage.WAITING_LOGIN) {
			case WAITING_LOGIN:
			default: {
				return "/install";
			}
			case DATABASE_CONFIG: {
				return "/install/database";
			}
			case FORUM_CONFIG: {
				return "/install/forumsettings";
			}
			case ADMIN_USER_CONFIG: {
				return "/install/adminuser";
			}
			case FINISH: {
				return "/install/finish";
			}
		}
	}
	
	public void saveToFile() {
		File file;
		try {
			file = ResourceUtils.getFile("file:setup.progress");
			try {
				if(!file.exists()) { 
					file.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
				writer.write(getStage().toString());
				writer.close();
			}
			catch(IOException e) { return; }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void loadFromFile() {
		File file;
		try {
			file = ResourceUtils.getFile("file:setup.progress");
			try {
				if(!file.exists()) { 
					return;
				}
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String value = reader.readLine();
				reader.close();
				SetupStage stage = EnumUtils.getEnum(SetupStage.class, value);
				if(stage != null) { setStage(stage); }
			}
			catch(IOException e) { return; }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void deleteFile() {
		File file;
		try {
			file = ResourceUtils.getFile("file:setup.progress");
			if(!file.exists()) { 
				return;
			}
			file.delete();
		}
		catch(Exception e) {}

	}

}
