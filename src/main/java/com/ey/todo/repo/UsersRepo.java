package com.ey.todo.repo;

import java.util.List;

import com.ey.todo.dao.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsersRepo extends CrudRepository<Users,Integer>
{
	@Query(value ="select * from users where username= :username and password=:password ", nativeQuery=true)
	List<Users> findByUserName( @Param("username")String username, @Param("password")String password);
	
}
