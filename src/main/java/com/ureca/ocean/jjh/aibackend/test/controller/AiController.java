package com.ureca.ocean.jjh.aibackend.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ocean.jjh.aibackend.test.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
	
	private final TestService testService;
	
	@GetMapping("/test")
	public String test(@RequestParam(name = "message") String message) {
		return testService.test(message);
	}

}
