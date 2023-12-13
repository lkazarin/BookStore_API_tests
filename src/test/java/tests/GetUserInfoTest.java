package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserInfoTest extends BaseTest {

    @Test@DisplayName("Get user info")
    public void successfulGetUserInfo() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //get user information
        Response getUserInfoResponse = getUserInformation(createdUserId);
        assertEquals(createdUserId, getUserInfoResponse.jsonPath().getString("userId"), "Unexpected username");
        assertEquals(createdUserName, getUserInfoResponse.jsonPath().getString("username"), "Unexpected username");
        List<?> books = getUserInfoResponse.jsonPath().getList("books");
        assertTrue(books.isEmpty(), "The user's book collection is not empty");
    }

    @Test@DisplayName("Get user info with invalid token")
    public void getUserInfoWithInvalidToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add invalid token in header
        addAuthorizationHeader(invalidToken);

        //get user information
        getUserInformation(createdUserId, 401);
    }

    @Test@DisplayName("Get user info with empty token")
    public void getUserInfoWithEmptyToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //skip adding token in header
        //updateAuthorizationHeader("");

        //get user information
        getUserInformation(createdUserId, 401);
    }

    @Test@DisplayName("Get user info with invalid userId")
    public void getUserInfoWithInvalidUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add invalid token in header
        addAuthorizationHeader(createdUserToken);

        //get user information
        getUserInformation(invalidUserId, 401);

    }

    @Test@DisplayName("Get user info with empty userId")
    public void getUserInfoWithEmptyUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add invalid token in header
        addAuthorizationHeader(createdUserToken);

        //get user information
        getUserInformation("", 401);
    }
}