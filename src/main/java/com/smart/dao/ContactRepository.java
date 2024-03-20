package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

//	Pagination in Spring Boot refers to the process of dividing a large set of data into smaller, manageable chunks or pages. It is commonly used in web applications to improve performance and user experience when dealing with large data sets. Pagination allows users to navigate through different pages of data rather than loading all of it at once, which can be slow and inefficient.

//In Spring Boot, pagination is often implemented using Spring Data JPA along with Spring MVC
	
//	here i will give user id and it will make where query and will give the result
	
//	Vo sabhi contacts aayenge jinki id match karegi jo humne id provide kari hai usse jo humne @param mai provide ki hai
	@Query("from Contact as c where c.user.id =:userId")
//	here i dont want to return list  anymore i want to return page
//	pageable will have two info per page no of contacts and current page ye info fetch karke page mai save kar dega
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pePageable);
}
