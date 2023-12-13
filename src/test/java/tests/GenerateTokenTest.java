package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenerateTokenTest extends BaseTest {

    @Test@DisplayName("Generate token")
    public void successfulGenerateToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        Response generateTokenResponse = generateToken(createdUserName, passwordValue, 200);
        createdUserToken = getCreatedUserTokenFromResponse(generateTokenResponse);
        generateTokenResponse.then()
                .body("token", notNullValue())
                .body("expires", notNullValue())
                .body("status", equalTo("Success"))
                .body("result", equalTo("User authorized successfully."));

        String token = generateTokenResponse.jsonPath().getString("token");
        assertNotNull(token, "Token is null or empty");
        String expires = generateTokenResponse.jsonPath().getString("expires");
        assertNotNull(expires, "Expires field is null or empty");
    }

    @Test@DisplayName("Generate token with invalid username")
    public void GenerateTokenWithInvalidUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(invalidUsername, passwordValue, 400);
    }

    @Test@DisplayName("Generate token with invalid password")
    public void GenerateTokenWithInvalidPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, invalidPassword, 400);
    }

    @Test@DisplayName("Generate token with invalid username and password")
    public void GenerateTokenWithInvalidUsernameAndPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(invalidUsername, invalidPassword, 400);
    }

    @Test@DisplayName("Generate token with empty username")
    public void GenerateTokenWithEmptyUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken("", passwordValue, 400);
    }

    @Test@DisplayName("Generate token with empty password")
    public void GenerateTokenWithEmptyPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, "", 400);
    }
}