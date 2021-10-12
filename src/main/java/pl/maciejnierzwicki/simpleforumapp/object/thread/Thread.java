package pl.maciejnierzwicki.simpleforumapp.object.thread;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "threads")
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Thread {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	private String title;
	
	@NotNull
	private Long userid;
	
	@NotNull
	private Long forumid;
	
	public Thread(String title) {
		this.title = title;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Long getUserid() {
		return this.userid;
	}
	
	public Long getForumid() {
		return this.forumid;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setUserid(Long id) {
		this.userid = id;
	}
	
	public void setForumid(Long forumid) {
		this.forumid = forumid;
	}
	
}
