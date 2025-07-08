package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {
    @GetMapping("")
    public String Main() {
    	return "main"; 
    }
    
    @GetMapping("/login")
    public String login() {
    	return "login";
    }
    
    @GetMapping("/signup")
    public String join() {
    	return "join";
    }
    
    @GetMapping("/user/edit") 
    public String user_edit() {
    	return "user_edit";
    }
    
    @GetMapping("/user/delete")
    public String user_delete() {
    	return "user_deletion";
    }
    
    @GetMapping("/user/find")
    public String user_find() {
    	return "user_find";
    }
}
