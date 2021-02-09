package com.example.signing;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SigningIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	static final String KEY_CONTROLLER_PREFIX = "/key";
	static final String SIGN_CONTROLLER_PREFIX = "/sign";
	static final String SERVER_SCHEME = "http://localhost:8080";

	@Test
	public void keyFlow() {
		assertEquals(0, getKeys().length);
		String key = generateKey();
		assertEquals(1, getKeys().length);
		deleteKey(key);
		assertEquals(0, getKeys().length);
	}

	@Test
	public void validateResourceNotFound(){
		deleteKey("fakeKey", HttpStatus.NOT_FOUND);
	}

	private Boolean verify(String key, String data, String signature){
		ResponseEntity<Boolean> entity = this.restTemplate.exchange(
				RequestEntity.get(uri(SIGN_CONTROLLER_PREFIX+"?key="+key+"&data="+data+"&signature="+signature))
						.header(HttpHeaders.ORIGIN, SERVER_SCHEME).build(),
				Boolean.class);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		return entity.getBody();
	}

	private String sign(String key, String data){
		ResponseEntity<String> entity = this.restTemplate.exchange(
				RequestEntity.post(uri(SIGN_CONTROLLER_PREFIX+"?key="+key+"&data="+data))
						.header(HttpHeaders.ORIGIN, SERVER_SCHEME).build(),
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		return entity.getBody();
	}

	private String deleteKey(String key, HttpStatus exceptedStatus){
		ResponseEntity<String> entity = this.restTemplate.exchange(
				RequestEntity.delete(uri(KEY_CONTROLLER_PREFIX+"?key="+key))
						.header(HttpHeaders.ORIGIN, SERVER_SCHEME).build(),
				String.class);
		assertEquals(exceptedStatus, entity.getStatusCode());
		return entity.getBody();
	}

	private String deleteKey(String key){
		return deleteKey(key, HttpStatus.OK);
	}

	private String generateKey(){
		ResponseEntity<String> entity = this.restTemplate.exchange(
				RequestEntity.post(uri(KEY_CONTROLLER_PREFIX))
						.header(HttpHeaders.ORIGIN, SERVER_SCHEME).build(),
				String.class);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		return entity.getBody();
	}

	private String[] getKeys(){
		ResponseEntity<String[]> entity = this.restTemplate.exchange(
				RequestEntity.get(uri(KEY_CONTROLLER_PREFIX))
						.header(HttpHeaders.ORIGIN, SERVER_SCHEME).build(),
				String[].class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		return entity.getBody();
	}

	private URI uri(String path) {
		return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
	}
}
