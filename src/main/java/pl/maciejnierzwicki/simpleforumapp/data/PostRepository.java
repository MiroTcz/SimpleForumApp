package pl.maciejnierzwicki.simpleforumapp.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import pl.maciejnierzwicki.simpleforumapp.object.post.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
	
	List<Post> getByUserid(Long userid);
	List<Post> getByThreadid(Long threadid);
	Long countByThreadid(Long threadid);
	List<Post> findAllByOrderByIdDesc(Pageable pageable);
}