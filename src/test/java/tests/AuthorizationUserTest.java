package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationUserTest extends BaseTest{

    @Test@DisplayName("Check user authorization")
    public void successfulAuthorizationUser() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //check authorization
        Response authorizedUserResponse = checkAuthorization(createdUserName, passwordValue);
        assertTrue(authorizedUserResponse.getBody().as(Boolean.class), "The response body is not 'true'");
    }

    @Test@DisplayName("Check user authorization without generating token")
    public void authorizationUserWithoutGeneratingToken() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //skip generating token
        //generateToken(createdUserName, passwordValue);

        //check authorization
        checkAuthorization(createdUserName, passwordValue, 400);
    }


    @Test@DisplayName("Check user authorization with invalid username")
    public void authorizationUserWithInvalidUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //check authorization
        checkAuthorization(invalidUsername, passwordValue, 404);
    }

    @Test@DisplayName("Check user authorization with invalid password")
    public void authorizationUserWithInvalidPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //check authorization
        checkAuthorization(createdUserName, invalidPassword, 404);
    }

    @Test@DisplayName("Check user authorization with empty username")
    public void authorizationUserWithEmptyUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //check authorization
        checkAuthorization("", passwordValue, 400);
    }

    @Test@DisplayName("Check user authorization with empty password")
    public void authorizationUserWithEmptyPassword() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //check authorization
        checkAuthorization(createdUserName, "", 400);
    }

}