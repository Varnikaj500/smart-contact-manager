package com.smart.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Path;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

// All the views or pages I want user to access i will put in user handler only
@Controller
@RequestMapping("/user")
public class UserController {

//	to add the user in all the handlers present in this class
//	with help of principal we can fetch username i.e., email i.e, unique identifier of user
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
//		I want to send data of that person whom i want to login		
	String userName =	principal.getName();
	System.out.println("USERNAME"+userName);
//	Get the user using userName(email)
	User user =  userRepository.getUserByUserName(userName);
	System.out.println("USER"+user);
	model.addAttribute("user",user);

	}
	{
		
	}
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/index")
//	with help of principal we can fetch username i.e., email i.e, unique identifier of user
	public String dashboard(Model model,Principal principal )
	{

//		normal ke andar meri file hai user dashboard	
		model.addAttribute("title", "User Dashboard");
	return "normal/user_dashboard";
	}
	
//	open add form handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title" , "Add Contact");
//		Adding blank obj of contact
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
//	processing add contact form
	@PostMapping("/process-contact")
//	With help of principal fetch the details of user
//	In contact all the details are coming except file . To save the file i am going to use request param and pass profile image. To store the image in a variable i am going to use multipart file
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		
		try {
//		Will get the name of person who is logged in
		String name= principal.getName();
	User user =this.userRepository.getUserByUserName(name);
//	For bidirectional mapping user ko contact dena tha and contact to user
	contact.setUser(user);
//	User will have one list in which i am adding my contact
		user.getContacts().add(contact);
//		In the above two lines contact ko user dena hai and user ke contact list mai 
		
//		Processing and uploading file
		if (file.isEmpty())
		{
//			If the file is empty then try our message
			System.out.println("File is empty");
//			If file is empty than in contact contact.jpg will come in database table
//			When i am manually adding a photo i am going to keep in img folder but when i am uploading it it will go in target img folder 
			contact.setImage("contact.jpg");
		}
		else {
//			upload the file to folder and update the name of contact
			contact.setImage(file.getOriginalFilename());
//			i need a path where i want to upload for that i will use class path resource
		File savefile= new ClassPathResource("static/img").getFile();
//		Path to image i will pass this path in target
	java.nio.file.Path path	= Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//		Source se sab file uthayega or destination par copy kar dega. file.getinputstream=jaha se data read karna hai. target=jaha data write karna hai
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Image is uploaded");
		}
		
		this.userRepository.save(user);
//		for this i require two string i.e., to print values
		System.out.println("DATA"+contact);
		System.out.println("Added to the data base");
		
//		success message
//		In this key value pair in key i am going to put message. In helper we have message class in which we have content and type which can tell weather it is error or success message.Therefore value mai i am going to create object of message class
		session.setAttribute("message", new Message("Your contact is added","success"));
		
		}catch(Exception e) {
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			
//			error message
			session.setAttribute("message", new Message("Your contact is added","danger"));
		}
//		returning the same view as above because after successful registration i want to return to add maybe any new contact
		return "normal/add_contact_form";
	}
	
//	show contacts handler
//	Per page i want to show only 5 contacts
//	current page =0[page]
//	First we need on which page our user is and for this we need path variable
//	At the time of get user will provide the page which will go into path variable page
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title","Show User Contacts");
//		Contact ki list ko bhejni hai for that sabse pehele user nikalna hai
//		To do this i am going to fetch the users with help of principal
//		String userName = principal.getName();
//		to get username we will use userrepo to get username
//		User user = this.userRepository.getUserByUserName(userName);
//		List<Contact> contacts = user.getContacts();
		
		
//		Using contact repo
//		Here if i write this.contactrepo.findall=It will give me all the contacts present in database but i only want contacts of that user who is logged in. For that we need custom method
//		with help of principal i am first going to fetch user id
	String userName	= principal.getName();
//	with help of userrepo i can fetch entire user . And with this user i can fetch userid
	User user = this.userRepository.getUserByUserName(userName);
	
//	pageable is parent in which i am storing page request
//	It has two info currentPage-page, Contact per page - 5
//	page request will help us to create object of pageable
	Pageable pageable = PageRequest.of(page, 5);
	
//	in the contacts all contacts are saved
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);
// With help of these three attributes i can show my contacts current page and total pages on view
		//		to send the contacts we have fetched in form of list
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
//		to get total no of pages
		m.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_contacts";
	}
	
	
//	Showing particular contact details
//	i need dynamic cid i..e, with help of path variable i am going to do it i.e., passing the id dynamically to receive this id i am using path variable
	@GetMapping("/{cId}/contact")
//	with help of cid i am going to fetch contact details and then going to send it in view i..e, contact_detail. With help of principal i can tell konsa banda login hai
	public String showConntactDetail(@PathVariable("cId") Integer cId, Model model,Principal principal)
	{
		System.out.println("CID"+cId);
	Optional<Contact> contactOptional = this.contactRepository.findById(cId);
	Contact contact = contactOptional.get();
	
//	to apply check i want to see which user is logged in
  String userName= principal.getName();
//  with help of name i can find who is the user. With help of user.getId i can even fetch user id
     User user = this.userRepository.getUserByUserName(userName);
	
	
//	Now i have to again send this contact into view for that i am going to use model. In this model i will keep my contact
//     I can find contact ka konsa user hai
//     the person who is logged in is equal to jo humne contact nikala hai uske user  ki id se
//     contact ke user ki id or jo bnda login hai uski id same ho to to he hum contact bhejenge
     
     if (user.getId()==contact.getUser().getId()) 
     {
		model.addAttribute("contact", contact);
		model.addAttribute("title",contact.getName());
	 }
		return "normal/contact_detail";
	}
	
//	delete contact handler
//	Message can be set with help of http session
//	The url i am providing here is a relative url because i am alredy providing user above
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId,HttpSession session,Principal principal)
	{
	Contact contact= this.contactRepository.findById(cId).get();
     
//     it is required to unlink the user from contact because we have cascaded and it is not able to delete
//     contact.setUser(null);
     
//     remove image from img name we will getfrom contact.getImage
     
//     check so that no other person can delete anyone else's contact
//     this.contactRepository.delete(contact);
	
	User user = this.userRepository.getUserByUserName(principal.getName());
//	when the contact i want to delete matches with any of the contacts of that user then only it will delete 
	user.getContacts().remove(contact);
	this.userRepository.save(user);
	
     session.setAttribute("message",new Message("Contact deleted successfully","success"));
     return "redirect:/user/show-contacts/0";
	}
	
//	open update form handler
//	If i use get anyone can access by url without even clicking on button. If i use post i can't access it by url i can only access it by button. if i try to directly get through url it will show error because it will go as get request 
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m)
	{
		m.addAttribute("title", "Update Contact");
	Contact contact	= this.contactRepository.findById(cid).get();
	m.addAttribute("contact", contact);
		return "normal/update_form";
	}
	
//	update contact handler
//	for image i am going to use multipart. Our new image will come into this file of multipartFile
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage")MultipartFile file, Model m, HttpSession session, Principal principal)
	{
//		First i am going to check that weater user has selected new image if yes than i have to rewrite it in new destination
		try {
//			old contact detail to get old image name
			Contact oldcontactDetail = this.contactRepository.findById(contact.getcId()).get();
			
			if (!file.isEmpty())
			{
//				delete old photo from folder
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile,oldcontactDetail.getImage());
				file1.delete();
				
//				update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				java.nio.file.Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
				
			}else {
				contact.setImage(oldcontactDetail.getImage());
			}
			
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your contact is updated", "success"));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Contact Name"+contact.getName());
		return"redirect:/user/"+contact.getcId()+"/contact";
	}
	
	
//	Your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}
	

	
}

