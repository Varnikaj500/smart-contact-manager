package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


// this controller will handle all the request. If the request is handled perfectly than we will get a view i.e; html page 

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
//	The response i am sending i want to get that
	@ResponseBody
	public String test()
	{
		
//		Creating object of user to set its properties
		User user = new User();
		user.setName("Varnika Jain");
		user.setEmail("varni@gmail.com");
		
//		To save our user obj in user repo
		userRepository.save(user);
		return "Working";
	}
	
	@GetMapping("/")
	public String home(Model model)
	{
//		to use this in our template home.html pass this in th:text
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/sign-up")
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Contact Manager");
//		sending blank user object in model so that whatever we are sending from their will come here
		model.addAttribute("user", new User());
		return "signup";
	}
	
//	this handler id for registering user
	
//	With help of this model all the fields which are matching with form and user object all the values will come in this user
//	For checkbox i am using request param
    @RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement" , defaultValue = "false")boolean agreement, Model model, HttpSession session)
	{
    	
    	try {
    		
//    		If i have not done the agreement we will throw the exception which will go into the catch block and will add user and will add the message in session and show the signup page
    	
    	if(!agreement)
    	{
    		System.out.println("You have not agreed to terms and conditions");
   		throw new Exception("You have not agreed to terms and conditions");
    	}
    	
    	if(result1.hasErrors())
    	{
    		System.out.println("Error"+result1.toString());
//    		jo bhi data hai vo form mai vapas aa jaya
    		model.addAttribute("user",user);
    		return "signup";
    	}
    	
//    	And if everything has been done properly and the agreement is done as well it will set the user, enable it,set the image print it and save it in the end and then naya user add karega that is sab fields blank aayegi and at last success message show hoga along with signup page
    	user.setRole("ROLE_USER");
    	user.setEnabled(true);
    	user.setImageUrl("default.png");
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("Agreement" +agreement);
		System.out.println("User"+user);
		
		User result = this.userRepository.save(user);
//		Jo data aaya hai user mai vahi data vapas chala jaayega 
		model.addAttribute("user", new User());
//		session.setAttribute("message", new Message("Successfully Registered","alert-success"));
//		session.removeAttribute("message");
		session.setAttribute("message", new Message("Successfully Registered","alert-success"));
//		session.removeAttribute("message");

		return "signup";
    	}
    	catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
//		session.removeAttribute("message");

			return "signup";
    	}
	}
    
//    handler for custom login
//    To send any data i can use model
    @GetMapping("/signin")
    public String customLogin(Model model)
    {
    	model.addAttribute("title","Login Page");
    	return "login";
    }
}
