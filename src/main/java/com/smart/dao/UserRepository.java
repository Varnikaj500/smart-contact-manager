package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	
//	Creating a method that will return a user using a username i.e., email
//	Going to write a query where i want to fetch dynamic email. For that i am using param . param and query email should match. Going to take variable also string email
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);
}
// Whenever i am going to call getUserByUserName i need to pass an email string vo chali jaayegi query wali email mai or usse hume username mill jaayega
