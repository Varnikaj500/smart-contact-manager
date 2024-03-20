//package com.smart.controller;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//
//@ControllerAdvice
//public class GlobalControllerAdvice {

//	In this GlobalControllerAdvice, we're using @ModelAttribute to add HttpServletRequest and HttpSession to the model for every controller method. This way, these objects will be available in your Thymeleaf templates.
//	Then, in your Thymeleaf template, you can access these objects directly without any errors.
	
//	
//	 @ModelAttribute
//    public void globalAttributes(Model model, HttpServletRequest request, HttpSession session) {
//	        model.addAttribute("request", request);
//	        model.addAttribute("session", session);
//	    }
//
//}
