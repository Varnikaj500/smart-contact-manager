package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

// In this we are going to declare the url
//	Spring will make obj of this bean and we can call it anywhere
//	This method is used to create and configure a bean that provides the UserDetailsService. The UserDetailsService interface is a core Spring Security interface that provides a contract for implementing user retrieval functionality, typically used for authentication and authorization purposes.
	
	@Bean
	public UserDetailsService getUserDetailsService()
	{
//		This line creates a new instance of the UserDetailsServiceImpl class, which likely implements the UserDetailsService interface. This service class would typically contain the logic to retrieve user details from a data source, such as a database, LDAP server, or any other authentication provider.
		return new UserDetailsServiceImpl();
	}
	
//	Going to declare bean of BcryptPasswordEncoder
//	This method is used to create and configure a bean that provides a BCryptPasswordEncoder. The BCryptPasswordEncoder is a password encoder provided by Spring Security for encoding passwords using the BCrypt hashing algorithm.
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
//		This line creates a new instance of the BCryptPasswordEncoder class. This encoder class generates hash representations of passwords using BCrypt, which is a strong hashing algorithm designed to be computationally intensive and resistant to brute-force attacks.
		return new BCryptPasswordEncoder();
	}
	
//	This method is used to create and configure a DaoAuthenticationProvider bean. DaoAuthenticationProvider is a class provided by Spring Security that implements the AuthenticationProvider interface. It's responsible for performing authentication based on user details obtained from a data access object (DAO).
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
//		Inside the method, you instantiate a DaoAuthenticationProvider object and configure it:
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		: This line sets the UserDetailsService for the authentication provider. It provides the service responsible for loading user details during authentication.
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
//		This line sets the password encoder for the authentication provider. You are using the passwordEncoder() bean, which returns an instance of BCryptPasswordEncoder (or any other implementation of PasswordEncoder), to configure the password encoder used for password verification during authentication.
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
//	To get these methods go to source and use override and implement method
//	Methods override karke sab configurations define karni hai.
//	Ek method chaeye jiske andar mil jaye authentication manager builder
//	Configure method
//  
//	Auth...builder ko i need to provide authentication provider. We need to tell builder what type of authentication provider i am using eg database etc here i am using database only
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	
	
//	We need to provide route as well i.e, konsa route kon use kar sakta hai
//	With help of this http i can tell don't protect all the routes just protect the ones i am telling you to protect i.e.., /admin vahi use kar paayega jiske pass role hoga admin ka
//	Admin se start hone wale url ko vo he access kar payega jinka role admin hai
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers ("/**").permitAll().and().formLogin()
//		Here i am telling that my login page is present at signin
		.loginPage("/signin")
//		telling where i am going to submit my url
		.loginProcessingUrl("/dologin")
//		After successful login . It will go to user/index page
		.defaultSuccessUrl("/user/index")
//		If any error it will go to this url
//		.failureUrl("/login-fail")
		.and().csrf().disable();
		
//		Here i have told spring security that my login page is signin . So now i am going to get my customized page
	}

	
	
}
