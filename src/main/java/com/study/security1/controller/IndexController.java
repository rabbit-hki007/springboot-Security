package com.study.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.security1.model.User;
import com.study.security1.repository.UserRepository;


@Controller
public class IndexController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping({ "", "/" })
	public String index() {
		return "index";
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {		
		return "user";
	
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	
	}
	
	@PostMapping("/joinProc")
	public String joinProc(User user) {
        System.out.println("회원가입 진행 : " + user);
        String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER");
		userRepository.save(user);
		return "redirect:/loginForm";	
	}
	
	// ROLE_ADMIN 권한만 접속 가능한 링크 - 한개의 권한만 접속 가능하도록 함
	@Secured("ROLE_ADMIN") //SecurityConfig에서 @EnableGlobalMethodSecurity(securedEnabled = true) 로 설정했을때 가능 
	@GetMapping("/info")
	public @ResponseBody  String info() {
		return "개인정보";
	}
	
	// ROLE_MANAGER  ROLE_ADMIN 여러개의 권한이 접속 가능하도록 함
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //SecurityConfig에서@EnableGlobalMethodSecurity(prePostEnabled = true) 로 설정했을때 가능 
	@GetMapping("/data")
	public @ResponseBody  String data() {
		return "데이타 정보";
	}

}
