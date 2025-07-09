package com.ureca.ocean.jjh.aibackend.test.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {
	
	private final ChatClient chatClient;
	
	public String test(String message) {
		String response = chatClient.prompt()
				.user(message)
				.call()
				.content();
		return response;
	}
}
