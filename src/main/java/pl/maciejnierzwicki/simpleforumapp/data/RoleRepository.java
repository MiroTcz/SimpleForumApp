package pl.maciejnierzwicki.simpleforumapp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.simpleforumapp.object.user.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, String>{
	

}
