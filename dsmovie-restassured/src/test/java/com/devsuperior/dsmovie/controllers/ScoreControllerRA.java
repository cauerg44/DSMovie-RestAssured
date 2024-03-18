package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {
	
	private Long nonExistingId, missingMovieId;
	private String clientUsername, clientPassword, clientToken;
	
	private Map<String, Object> saveScoreInstance;
	
	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";
		
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
				
		
		saveScoreInstance = new HashMap<>();
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		nonExistingId = 100L;
		
	    saveScoreInstance.put("movieId", nonExistingId);
	    saveScoreInstance.put("score", 4);
	    JSONObject newScore = new JSONObject(saveScoreInstance);

	    given()
	    	.header("Content-type", "application/json")
	    	.header("Authorization", "Bearer " + clientToken)
	        .contentType(ContentType.JSON)
	        .accept(ContentType.JSON)
	        .body(newScore)
	    .when()
	        .put("/scores")
	    .then()
	        .statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		missingMovieId = null;
		
	    saveScoreInstance.put("movieId", missingMovieId);
	    saveScoreInstance.put("score", 4);
	    JSONObject newScore = new JSONObject(saveScoreInstance);

	    given()
	    	.header("Content-type", "application/json")
	    	.header("Authorization", "Bearer " + clientToken)
	        .contentType(ContentType.JSON)
	        .accept(ContentType.JSON)
	        .body(newScore)
	    .when()
	        .put("/scores")
	    .then()
	        .statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		saveScoreInstance.put("movieId", 1);
		saveScoreInstance.put("score", -1);
		JSONObject newScore = new JSONObject(saveScoreInstance);
		
		given()
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(422);
	}
}
