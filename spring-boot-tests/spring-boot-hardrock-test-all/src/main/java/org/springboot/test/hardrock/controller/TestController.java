package org.springboot.test.hardrock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("health");
	}
}
