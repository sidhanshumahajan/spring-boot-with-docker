package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1")
public class DemoController {

	// private static final String VALUE = "hello";

	@GetMapping("/")
	public String helloWorld() {
		return "Hello World";
	}
}
