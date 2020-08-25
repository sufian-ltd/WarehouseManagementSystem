package com.warehouse.Controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.warehouse.constant.WarehouseConstant;
import com.warehouse.entity.User;
import com.warehouse.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home(Authentication authentication, Model model) {
		if (authentication != null) {
			String role = "GUEST";
			for (GrantedAuthority authority : authentication.getAuthorities())
				role = authority.getAuthority();
			if (role.equals(WarehouseConstant.ROLE_OWNER)) {
				return "redirect:/owners/dashboard";
			} else if (role.equals(WarehouseConstant.ROLE_USER)) {
				return "redirect:/users/dashboard";
			} else {
				return "redirect:/login";
			}
		}
		return "index";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("/registration")
	public String showSignupPage(Model theModel) {
		theModel.addAttribute("user", new User());
		return "registration";
	}

	@PostMapping("/registration")
	public String createUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "You entered invalid data..!!");
		} else {
			User userExists = userService.findByUsername(user.getUsername());
			if (userExists != null) {
				model.addAttribute("msg", "This username already exist..!!");
			} else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userService.save(user);
				model.addAttribute("msg", "User Registration Successfull..!!");
				model.addAttribute("user", new User());
			}
		}
		return "registration";
	}

	@GetMapping("/profile")
	public String profile(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute(user);
		return "profile";
	}

	@PostMapping("/profile")
	public String profile(Principal principal, @ModelAttribute("user") User newUser, Model model) {
		User user = userService.findByUsername(principal.getName());
		newUser.setRole(user.getRole());
		newUser.setId(user.getId());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		userService.save(newUser);
		model.addAttribute(newUser);
		model.addAttribute("msg", "Profile Update Successfull..!!!!!!");
		return "profile";
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "access-denied";
	}
}
