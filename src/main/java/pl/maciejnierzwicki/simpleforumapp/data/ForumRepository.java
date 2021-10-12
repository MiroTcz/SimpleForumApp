package pl.maciejnierzwicki.simpleforumapp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.simpleforumapp.object.forum.Forum;

@Repository
public interface ForumRepository extends CrudRepository<Forum, Long> {
	
	

}
