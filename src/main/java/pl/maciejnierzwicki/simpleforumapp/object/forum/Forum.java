package pl.maciejnierzwicki.simpleforumapp.object.forum;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "forums")
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Forum {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Size(max = 40, message = "Nazwa działu nie może składać się z więcej niż 40 znaków.")
	private String name;
	
	public Forum(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
