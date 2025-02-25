package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1")
public class DemoController {

	
	@GetMapping("/")
	public String helloWorld() {
		return "Hello World";
	}
}
