package pl.maciejnierzwicki.simpleforumapp.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.simpleforumapp.object.thread.Thread;

@Repository
public interface ThreadRepository extends CrudRepository<Thread, Long>{

	List<Thread> getByUserid(Long userid);
	List<Thread> getByForumid(Long forumid);
}
