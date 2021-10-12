package pl.maciejnierzwicki.simpleforumapp.object.post;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Post {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long threadid;
	
	private Long userid;
	
	private String content;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate;
	
	public Post(String content) {
		this.content = content;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public Long getThreadid() {
		return this.threadid;
	}
	
	public Long getUserid() {
		return this.userid;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public Date getCreationdate() {
		return this.creationDate;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setThreadId(long id) {
		this.threadid = id;
	}
	
	public void setUserId(long id) {
		this.userid = id;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@PrePersist
	public void setCreationdate() {
		this.creationDate = new Date();
	}

}
