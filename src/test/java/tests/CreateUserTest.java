package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Create the user")
    public void successfulCreateUser() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        Response createUserResponse = createUser(randomUsername, passwordValue, 201);
        String actualUsername = createUserResponse.jsonPath().getString("username");
        assertEquals(createdUserName, actualUsername, "Username is not equal");

        deleteUserAfterCreatingUserTest();
    }

    @Test
    @DisplayName("Create the user with invalid username")
    public void createUserWithInvalidUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(invalidUsername, passwordValue, 400);

        deleteUserAfterCreatingUserTest();
    }

    @Test
    @DisplayName("Create the user with invalid password")
    public void createUserWithInvalidPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, invalidPassword, 400);

    }

    @Test
    @DisplayName("Create the user with empty username")
    public void createUserWithEmptyUsername() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser("", passwordValue, 400);
    }

    @Test
    @DisplayName("Create the user with empty password")
    public void createUserWithEmptyPassword() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, "", 400);
    }
}