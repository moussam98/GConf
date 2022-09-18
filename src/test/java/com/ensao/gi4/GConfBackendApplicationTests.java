package com.ensao.gi4;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.ensao.gi4.controller.UserController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GConfBackendApplicationTests {

	@LocalServerPort
	private int port;
	@Autowired
	private UserController personController;
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	void contextLoads() {
		assertThat(testRestTemplate.getForObject("http://localhost:" + port + "/api/v1/person/show", String.class)).isNotBlank(); 
		assertThat(testRestTemplate.getForObject("http://localhost:" + port + "/api/v1/person/show", String.class)).contains("Hello world we're testing application context"); 
	}

}
